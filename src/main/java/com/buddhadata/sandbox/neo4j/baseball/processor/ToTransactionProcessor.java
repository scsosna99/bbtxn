package com.buddhadata.sandbox.neo4j.baseball.processor;

import com.buddhadata.sandbox.neo4j.baseball.node.Player;
import com.buddhadata.sandbox.neo4j.baseball.node.Team;
import com.buddhadata.sandbox.neo4j.baseball.relationship.*;
import org.neo4j.ogm.session.Session;

import java.lang.reflect.Constructor;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by scsosna on 10/27/18.
 */
public class ToTransactionProcessor extends TransactionProcessor {

    public static final ToTransactionProcessor instance = new ToTransactionProcessor();

    private static final Map<TransactionType, Constructor[]> constructors = loadConstructors();

    private ToTransactionProcessor() {};

    public void process (Session session,
                         TransactionType transactionType,
                         String[] fields) {

        try {
            //  Get the constructors for this transaction type.
            Constructor[] c = constructors.get(transactionType);
            if (c != null && c.length == 1) {

                //  Find player
                Player player = getPlayer(session, fields[TXN_FIELD_PLAYER]);
                if (player != null) {

                    //  Find team
                    Team toTeam = getTeam(session, fields[TXN_FIELD_TO_TEAM], fields[TXN_FIELD_TO_LEAGUE]);

                    //  Parse the transaction id and date fields
                    int txnId = Integer.valueOf(fields[TXN_FIELD_ID]);
                    LocalDate txnDate = parseTxnDate(fields[TXN_FIELD_DATE_PRIMARY]);

                    //  Create the transactions for the single constructor.
                    session.save(c[0].newInstance(transactionType, txnId, player, toTeam, txnDate));
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
    private static Map<TransactionType, Constructor[]> loadConstructors() {

        Map<TransactionType, Constructor[]> toReturn = new HashMap<>();

        toReturn.put (TransactionType.Da, new Constructor[] {getConstructor(DraftedByTxn.class)});
        toReturn.put (TransactionType.F, new Constructor[] {getConstructor(SignedTxn.class)});
        toReturn.put (TransactionType.Fa, new Constructor[] {getConstructor(SignedTxn.class)});
        toReturn.put (TransactionType.Fb, new Constructor[] {getConstructor(SignedTxn.class)});
        toReturn.put (TransactionType.Fo, new Constructor[] {getConstructor(SignedTxn.class)});
        toReturn.put (TransactionType.Zr, new Constructor[] {getConstructor(ReturnedToTxn.class)});

        return toReturn;
    }
}
