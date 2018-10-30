package com.buddhadata.sandbox.neo4j.baseball.processor;

import com.buddhadata.sandbox.neo4j.baseball.node.Player;
import com.buddhadata.sandbox.neo4j.baseball.node.Team;
import com.buddhadata.sandbox.neo4j.baseball.relationship.*;
import org.neo4j.ogm.session.Session;

import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by scsosna on 10/27/18.
 */
public class FromToTransactionProcessor extends TransactionProcessor {

    public static final FromToTransactionProcessor instance = new FromToTransactionProcessor();

    private static final Map<String,Constructor[]> constructors = loadConstructors();

    private FromToTransactionProcessor() {};

    public void process (Session session,
                         String[] fields) {

        try {
            //  Get the constructors for this transaction type.
            Constructor[] c = constructors.get(fields[TXN_FIELD_TYPE]);
            if (c != null && c.length == 2) {

                //  Find player
                Player player = getPlayer(session, fields[TXN_FIELD_PLAYER]);
                if (player != null) {

                    //  Find teams
                    Team fromTeam = getTeam(session, fields[TXN_FIELD_FROM_TEAM], fields[TXN_FIELD_FROM_LEAGUE]);
                    Team toTeam = getTeam(session, fields[TXN_FIELD_TO_TEAM], fields[TXN_FIELD_TO_LEAGUE]);

                    //  Parse the transaction id and date fields
                    int txnId = Integer.valueOf(fields[TXN_FIELD_ID]);
                    Date txnDate = parseTxnDate(fields[TXN_FIELD_DATE_PRIMARY]);

                    //  Create the transactions for all constructors available.
                    if (fromTeam != null) session.save(c[0].newInstance(txnId, player, fromTeam, txnDate));
                    if (toTeam != null) session.save(c[1].newInstance(txnId, player, toTeam, txnDate));
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

        toReturn.put ("A", new Constructor[] {getConstructor(AssignedFromTxn.class), getConstructor(AssignedToTxn.class)});
        toReturn.put ("C", new Constructor[] {getConstructor(ConditionalFromTxn.class), getConstructor(ConditionalToTxn.class)});
        toReturn.put ("Cr", new Constructor[] {getConstructor(ReturnedFromTxn.class), getConstructor(ReturnedToTxn.class)});
        toReturn.put ("D", new Constructor[] {getConstructor(DraftedFromTxn.class), getConstructor(DraftedToTxn.class)});
        toReturn.put ("Df", new Constructor[] {getConstructor(DraftedFromTxn.class), getConstructor(DraftedToTxn.class)});
        toReturn.put ("Dm", new Constructor[] {getConstructor(DraftedFromTxn.class), getConstructor(DraftedToTxn.class)});
        toReturn.put ("Dr", new Constructor[] {getConstructor(ReturnedFromTxn.class), getConstructor(ReturnedToTxn.class)});
        toReturn.put ("Ds", new Constructor[] {getConstructor(DraftedFromTxn.class), getConstructor(DraftedToTxn.class)});
        toReturn.put ("Fc", new Constructor[] {getConstructor(CompensatedFromTxn.class), getConstructor(CompensatedToTxn.class)});
        toReturn.put ("J", new Constructor[] {getConstructor(JumpedFromTxn.class), getConstructor(JumpedToTxn.class)});
        toReturn.put ("Jr", new Constructor[] {getConstructor(ReturnedFromTxn.class), getConstructor(ReturnedToTxn.class)});
        toReturn.put ("L", new Constructor[] {getConstructor(LoanedFromTxn.class), getConstructor(LoanedToTxn.class)});
        toReturn.put ("Lr", new Constructor[] {getConstructor(ReturnedFromTxn.class), getConstructor(ReturnedToTxn.class)});
        toReturn.put ("M", new Constructor[] {getConstructor(RightsFromTxn.class), getConstructor(RightsToTxn.class)});
        toReturn.put ("Mr", new Constructor[] {getConstructor(ReturnedFromTxn.class), getConstructor(ReturnedToTxn.class)});
        toReturn.put ("P", new Constructor[] {getConstructor(PurchasedFromTxn.class), getConstructor(PurchasedToTxn.class)});
        toReturn.put ("Pr", new Constructor[] {getConstructor(ReturnedFromTxn.class), getConstructor(ReturnedToTxn.class)});
        toReturn.put ("Pv", new Constructor[] {getConstructor(VoidedFromTxn.class), getConstructor(VoidedToTxn.class)});
        toReturn.put ("T", new Constructor[] {getConstructor(TradedFromTxn.class), getConstructor(TradedToTxn.class)});
        toReturn.put ("Tn", new Constructor[] {getConstructor(ReturnedFromTxn.class), getConstructor(ReturnedToTxn.class)});
        toReturn.put ("Tp", new Constructor[] {getConstructor(TradedFromTxn.class), getConstructor(TradedToTxn.class)});
        toReturn.put ("Tr", new Constructor[] {getConstructor(ReturnedFromTxn.class), getConstructor(ReturnedToTxn.class)});
        toReturn.put ("Tv", new Constructor[] {getConstructor(VoidedFromTxn.class), getConstructor(VoidedToTxn.class)});
        toReturn.put ("U", new Constructor[] {getConstructor(UnknownFromTxn.class), getConstructor(UnknownFromTxn.class)});
        toReturn.put ("W", new Constructor[] {getConstructor(WaiverFromTxn.class), getConstructor(WaiverFromTxn.class)});
        toReturn.put ("Wf", new Constructor[] {getConstructor(WaiverFromTxn.class), getConstructor(WaiverFromTxn.class)});
        toReturn.put ("Wr", new Constructor[] {getConstructor(ReturnedFromTxn.class), getConstructor(ReturnedToTxn.class)});
        toReturn.put ("Wv", new Constructor[] {getConstructor(VoidedFromTxn.class), getConstructor(VoidedToTxn.class)});
        toReturn.put ("X", new Constructor[] {getConstructor(DraftedFromTxn.class), getConstructor(DraftedToTxn.class)});
        toReturn.put ("Xe", new Constructor[] {getConstructor(DraftedFromTxn.class), getConstructor(DraftedToTxn.class)});
        toReturn.put ("Xm", new Constructor[] {getConstructor(DraftedFromTxn.class), getConstructor(DraftedToTxn.class)});
        toReturn.put ("Xp", new Constructor[] {getConstructor(DraftedFromTxn.class), getConstructor(DraftedToTxn.class)});
        toReturn.put ("Xr", new Constructor[] {getConstructor(ReturnedFromTxn.class), getConstructor(ReturnedToTxn.class)});

        return toReturn;
    }
}
