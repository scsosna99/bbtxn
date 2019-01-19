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
public class FromTransactionProcessor extends TransactionProcessor {

    /**
     * Singleton instance of the processor
     */
    public static final FromTransactionProcessor instance = new FromTransactionProcessor();

    /**
     * The constructors specific to this transaction type
     */
    private static final Map<TransactionType, Constructor> constructors = loadConstructors();

    /**
     * Private constructor to prevent additional instances.
     */
    private FromTransactionProcessor() {};

    /**
     * Does the actual processing
     * @param session Neo4J database session
     * @param transactionType the specific type of transaction being processed
     * @param fields the individual fields of the transaction
     * @param unassigned the singleton Unassigned node which is used everywhere
     */
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
                    Team fromTeam = getTeam(session, fields[TXN_FIELD_FROM_TEAM], fields[TXN_FIELD_FROM_LEAGUE]);

                    //  Parse the transaction id and date fields
                    int txnId = Integer.valueOf(fields[TXN_FIELD_ID]);
                    LocalDateTime txnDate = parseTxnDate(fields[TXN_FIELD_DATE_PRIMARY]);

                    //  Create the transactions for the single constructor.
                    session.save(c.newInstance(transactionType, txnId, player, fromTeam, unassigned, txnDate));
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
        Class[] params = new Class[] {TransactionType.class, int.class, Player.class, Team.class, Unassigned.class, LocalDateTime.class};

        Map<TransactionType, Constructor> toReturn = new HashMap<>();
        toReturn.put (TransactionType.Fg, getConstructor(GrantedFreeAgencyTxn.class, params));
        toReturn.put (TransactionType.Fv, getConstructor(FreeAgentVoidedTxn.class, params));
        toReturn.put (TransactionType.R, getConstructor(ReleasedTxn.class, params));
        toReturn.put (TransactionType.Z, getConstructor(RetiredTxn.class, params));

        return toReturn;
    }
}
