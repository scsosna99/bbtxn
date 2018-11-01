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
public class FromTransactionProcessor extends TransactionProcessor {

    public static final FromTransactionProcessor instance = new FromTransactionProcessor();

    private static final Map<TransactionType, Constructor[]> constructors = loadConstructors();

    private FromTransactionProcessor() {};

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
                    Team fromTeam = getTeam(session, fields[TXN_FIELD_FROM_TEAM], fields[TXN_FIELD_FROM_LEAGUE]);

                    //  Parse the transaction id and date fields
                    int txnId = Integer.valueOf(fields[TXN_FIELD_ID]);
                    LocalDate txnDate = parseTxnDate(fields[TXN_FIELD_DATE_PRIMARY]);

                    //  Create the transactions for the single constructor.
                    session.save(c[0].newInstance(transactionType, txnId, player, fromTeam, txnDate));
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

        toReturn.put (TransactionType.Fg, new Constructor[] {getConstructor(GrantedFreeAgencyTxn.class)});
        toReturn.put (TransactionType.Fv, new Constructor[] {getConstructor(VoidedFromTxn.class)});
        toReturn.put (TransactionType.R, new Constructor[] {getConstructor(ReleasedTxn.class)});
        toReturn.put (TransactionType.Z, new Constructor[] {getConstructor(RetiredTxn.class)});

        return toReturn;
    }
}
