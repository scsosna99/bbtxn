package com.buddhadata.sandbox.neo4j.baseball.processor;

import com.buddhadata.sandbox.neo4j.baseball.node.Player;
import com.buddhadata.sandbox.neo4j.baseball.node.Team;
import com.buddhadata.sandbox.neo4j.baseball.relationship.DraftedByTxn;
import com.buddhadata.sandbox.neo4j.baseball.relationship.ReleasedTxn;
import com.buddhadata.sandbox.neo4j.baseball.relationship.ReturnedToTxn;
import com.buddhadata.sandbox.neo4j.baseball.relationship.SignedTxn;
import org.neo4j.ogm.session.Session;

import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by scsosna on 10/27/18.
 */
public class ToTransactionProcessor extends TransactionProcessor {

    public static final ToTransactionProcessor instance = new ToTransactionProcessor();

    private static final Map<String,Constructor[]> constructors = loadConstructors();

    private ToTransactionProcessor() {};

    public void process (Session session,
                         String[] fields) {

        try {
            //  Get the constructors for this transaction type.
            Constructor[] c = constructors.get(fields[TXN_FIELD_TYPE]);
            if (c != null && c.length == 1) {

                //  Find player
                Player player = getPlayer(session, fields[TXN_FIELD_PLAYER]);
                if (player != null) {

                    //  Find team
                    Team toTeam = getTeam(session, fields[TXN_FIELD_TO_TEAM], fields[TXN_FIELD_TO_LEAGUE]);

                    //  Parse the transaction id and date fields
                    int txnId = Integer.valueOf(fields[TXN_FIELD_ID]);
                    Date txnDate = parseTxnDate(fields[TXN_FIELD_DATE_PRIMARY]);

                    //  Create the transactions for the single constructor.
                    session.save(c[0].newInstance(txnId, player, toTeam, txnDate));
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
    private static Map<String,Constructor[]> loadConstructors() {

        Map<String, Constructor[]> toReturn = new HashMap<>();

        toReturn.put ("Da", new Constructor[] {getConstructor(DraftedByTxn.class)});
        toReturn.put ("F", new Constructor[] {getConstructor(SignedTxn.class)});
        toReturn.put ("Fa", new Constructor[] {getConstructor(SignedTxn.class)});
        toReturn.put ("Fb", new Constructor[] {getConstructor(SignedTxn.class)});
        toReturn.put ("Fo", new Constructor[] {getConstructor(SignedTxn.class)});
        toReturn.put ("Zr", new Constructor[] {getConstructor(ReturnedToTxn.class)});

        return toReturn;
    }
}
