package com.buddhadata.sandbox.neo4j.baseball.processor;

import com.buddhadata.sandbox.neo4j.baseball.node.Player;
import com.buddhadata.sandbox.neo4j.baseball.node.Team;
import com.buddhadata.sandbox.neo4j.baseball.node.Unassigned;
import com.buddhadata.sandbox.neo4j.baseball.relationship.*;
import org.neo4j.ogm.session.Session;

import java.lang.reflect.Constructor;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by scsosna on 10/27/18.
 */
public class ToTransactionProcessor extends TransactionProcessor {

    public static final ToTransactionProcessor instance = new ToTransactionProcessor();

    private static final Map<TransactionType, Constructor> constructors = loadConstructors();

    private ToTransactionProcessor() {};

    public void process (Session session,
                         TransactionType transactionType,
                         String[] fields,
                         Unassigned unassigned) {

        try {
            //  Get the constructors for this transaction type.
            Constructor c = constructors.get(transactionType);
            if (c != null) {

                //  Find player
                Player player = getPlayer(session, fields[TXN_FIELD_PLAYER]);
                if (player != null) {

                    //  Find team
                    Team toTeam = getTeam(session, fields[TXN_FIELD_TO_TEAM], fields[TXN_FIELD_TO_LEAGUE]);

                    //  Parse the transaction id and date fields
                    int txnId = Integer.valueOf(fields[TXN_FIELD_ID]);
                    LocalDateTime txnDate = parseTxnDate(fields[TXN_FIELD_DATE_PRIMARY]);

                    //  Create the transactions for the single constructor.
                    session.save(c.newInstance(transactionType, txnId, player, unassigned, toTeam, txnDate));
                }
            } else {
                System.out.println ("Inappropriate number of constructors.");
            }
        } catch (Exception e) {
            System.out.println("Exception creating transaction: " + e);
        }
    }

    /**
     * Load the map of constructor(s) appropriate for the specific transaction type.
     * @return
     */
    private static Map<TransactionType, Constructor> loadConstructors() {

        //  The parameters passed to any FromTxn relationship constructor
        Class[] params = new Class[] {TransactionType.class, int.class, Player.class, Unassigned.class, Team.class, LocalDateTime.class};

        Map<TransactionType, Constructor> toReturn = new HashMap<>();
        toReturn.put (TransactionType.Da, getConstructor(DraftedByTxn.class, params));
        toReturn.put (TransactionType.F, getConstructor(SignedTxn.class, params));
        toReturn.put (TransactionType.Fa, getConstructor(SignedTxn.class, params));
        toReturn.put (TransactionType.Fb, getConstructor(SignedTxn.class, params));
        toReturn.put (TransactionType.Fo, getConstructor(SignedTxn.class, params));
        toReturn.put (TransactionType.Zr, getConstructor(UnretiredTxn.class, params));

        return toReturn;
    }
}
