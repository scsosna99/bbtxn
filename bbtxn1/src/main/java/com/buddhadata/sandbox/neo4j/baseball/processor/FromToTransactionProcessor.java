package com.buddhadata.sandbox.neo4j.baseball.processor;

import com.buddhadata.sandbox.neo4j.baseball.node.Player;
import com.buddhadata.sandbox.neo4j.baseball.node.Team;
import com.buddhadata.sandbox.neo4j.baseball.relationship.*;
import org.neo4j.ogm.session.Session;

import java.lang.reflect.Constructor;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by scsosna on 10/27/18.
 */
public class FromToTransactionProcessor extends TransactionProcessor {

    public static final FromToTransactionProcessor instance = new FromToTransactionProcessor();

    private static final Map<TransactionType, Constructor[]> constructors = loadConstructors();

    private FromToTransactionProcessor() {};

    public void process (Session session,
                         TransactionType transactionType,
                         String[] fields) {

        try {
            //  Get the constructors for this transaction type.
            Constructor[] c = constructors.get(transactionType);
            if (c != null && c.length == 2) {

                //  Find player
                Player player = getPlayer(session, fields[TXN_PLAYER]);
                if (player != null) {

                    //  Find teams
                    Team fromTeam = getTeam(session, fields[TXN_FROM_TEAM], fields[TXN_FROM_LEAGUE]);
                    Team toTeam = getTeam(session, fields[TXN_TO_TEAM], fields[TXN_TO_LEAGUE]);

                    //  Parse the transaction id and date fields
                    int txnId = Integer.valueOf(fields[TXN_RETROSHEET_ID]);
                    LocalDateTime txnDate = parseTxnDate(fields[TXN_DATE_PRIMARY]);

                    //  Create the transactions for all constructors available.
                    if (fromTeam != null) session.save(c[0].newInstance(transactionType, txnId, player, fromTeam, txnDate));
                    if (toTeam != null) session.save(c[1].newInstance(transactionType, txnId, player, toTeam, txnDate));
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

        toReturn.put (TransactionType.A, new Constructor[] {getConstructor(AssignedFromTxn.class), getConstructor(AssignedToTxn.class)});
        toReturn.put (TransactionType.C, new Constructor[] {getConstructor(ConditionalFromTxn.class), getConstructor(ConditionalToTxn.class)});
        toReturn.put (TransactionType.Cr, new Constructor[] {getConstructor(ReturnedFromTxn.class), getConstructor(ReturnedToTxn.class)});
        toReturn.put (TransactionType.D, new Constructor[] {getConstructor(DraftedFromTxn.class), getConstructor(DraftedToTxn.class)});
        toReturn.put (TransactionType.Df, new Constructor[] {getConstructor(DraftedFromTxn.class), getConstructor(DraftedToTxn.class)});
        toReturn.put (TransactionType.Dm, new Constructor[] {getConstructor(DraftedFromTxn.class), getConstructor(DraftedToTxn.class)});
        toReturn.put (TransactionType.Dr, new Constructor[] {getConstructor(ReturnedFromTxn.class), getConstructor(ReturnedToTxn.class)});
        toReturn.put (TransactionType.Ds, new Constructor[] {getConstructor(DraftedFromTxn.class), getConstructor(DraftedToTxn.class)});
        toReturn.put (TransactionType.Fc, new Constructor[] {getConstructor(CompensatedFromTxn.class), getConstructor(CompensatedToTxn.class)});
        toReturn.put (TransactionType.J, new Constructor[] {getConstructor(JumpedFromTxn.class), getConstructor(JumpedToTxn.class)});
        toReturn.put (TransactionType.Jr, new Constructor[] {getConstructor(ReturnedFromTxn.class), getConstructor(ReturnedToTxn.class)});
        toReturn.put (TransactionType.L, new Constructor[] {getConstructor(LoanedFromTxn.class), getConstructor(LoanedToTxn.class)});
        toReturn.put (TransactionType.Lr, new Constructor[] {getConstructor(ReturnedFromTxn.class), getConstructor(ReturnedToTxn.class)});
        toReturn.put (TransactionType.M, new Constructor[] {getConstructor(RightsFromTxn.class), getConstructor(RightsToTxn.class)});
        toReturn.put (TransactionType.Mr, new Constructor[] {getConstructor(ReturnedFromTxn.class), getConstructor(ReturnedToTxn.class)});
        toReturn.put (TransactionType.P, new Constructor[] {getConstructor(PurchasedFromTxn.class), getConstructor(PurchasedToTxn.class)});
        toReturn.put (TransactionType.Pr, new Constructor[] {getConstructor(ReturnedFromTxn.class), getConstructor(ReturnedToTxn.class)});
        toReturn.put (TransactionType.Pv, new Constructor[] {getConstructor(VoidedFromTxn.class), getConstructor(VoidedToTxn.class)});
        toReturn.put (TransactionType.T, new Constructor[] {getConstructor(TradedFromTxn.class), getConstructor(TradedToTxn.class)});
        toReturn.put (TransactionType.Tn, new Constructor[] {getConstructor(ReturnedFromTxn.class), getConstructor(ReturnedToTxn.class)});
        toReturn.put (TransactionType.Tp, new Constructor[] {getConstructor(TradedFromTxn.class), getConstructor(TradedToTxn.class)});
        toReturn.put (TransactionType.Tr, new Constructor[] {getConstructor(ReturnedFromTxn.class), getConstructor(ReturnedToTxn.class)});
        toReturn.put (TransactionType.Tv, new Constructor[] {getConstructor(VoidedFromTxn.class), getConstructor(VoidedToTxn.class)});
        toReturn.put (TransactionType.U, new Constructor[] {getConstructor(UnknownFromTxn.class), getConstructor(UnknownFromTxn.class)});
        toReturn.put (TransactionType.W, new Constructor[] {getConstructor(WaiverFromTxn.class), getConstructor(WaiverFromTxn.class)});
        toReturn.put (TransactionType.Wf, new Constructor[] {getConstructor(WaiverFromTxn.class), getConstructor(WaiverFromTxn.class)});
        toReturn.put (TransactionType.Wr, new Constructor[] {getConstructor(ReturnedFromTxn.class), getConstructor(ReturnedToTxn.class)});
        toReturn.put (TransactionType.Wv, new Constructor[] {getConstructor(VoidedFromTxn.class), getConstructor(VoidedToTxn.class)});
        toReturn.put (TransactionType.X, new Constructor[] {getConstructor(DraftedFromTxn.class), getConstructor(DraftedToTxn.class)});
        toReturn.put (TransactionType.Xe, new Constructor[] {getConstructor(DraftedFromTxn.class), getConstructor(DraftedToTxn.class)});
        toReturn.put (TransactionType.Xm, new Constructor[] {getConstructor(DraftedFromTxn.class), getConstructor(DraftedToTxn.class)});
        toReturn.put (TransactionType.Xp, new Constructor[] {getConstructor(DraftedFromTxn.class), getConstructor(DraftedToTxn.class)});
        toReturn.put (TransactionType.Xr, new Constructor[] {getConstructor(ReturnedFromTxn.class), getConstructor(ReturnedToTxn.class)});

        return toReturn;
    }
}
