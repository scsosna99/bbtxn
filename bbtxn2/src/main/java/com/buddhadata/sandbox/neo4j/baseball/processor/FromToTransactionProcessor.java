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
public class FromToTransactionProcessor extends TransactionProcessor {

    public static final FromToTransactionProcessor instance = new FromToTransactionProcessor();

    private static final Map<TransactionType, Constructor> constructors = loadConstructors();

    private FromToTransactionProcessor() {};

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

                    //  Find teams
                    Team fromTeam = getTeam(session, fields[TXN_FIELD_FROM_TEAM], fields[TXN_FIELD_FROM_LEAGUE]);
                    Team toTeam = getTeam(session, fields[TXN_FIELD_TO_TEAM], fields[TXN_FIELD_TO_LEAGUE]);

                    //  Parse the transaction id and date fields
                    int txnId = Integer.valueOf(fields[TXN_FIELD_ID]);
                    LocalDateTime txnDate = parseTxnDate(fields[TXN_FIELD_DATE_PRIMARY]);

                    //  Create the transactions for all constructors available.
                    if (fromTeam != null && toTeam != null) {
                        session.save(c.newInstance(transactionType, txnId, player, fromTeam, toTeam, txnDate));
                    } else {
                        System.out.println ("Missing team: type=" + transactionType + ", from=" + fromTeam + ", to=" + toTeam);
                    }
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
        Class[] params = new Class[] {TransactionType.class, int.class, Player.class, Team.class, Team.class, LocalDateTime.class};

        Map<TransactionType, Constructor> toReturn = new HashMap<>();
        toReturn.put (TransactionType.A, getConstructor(AssignedTxn.class, params));
        toReturn.put (TransactionType.C, getConstructor(ConditionalTxn.class, params));
        toReturn.put (TransactionType.Cr, getConstructor(ReturnedTxn.class, params));
        toReturn.put (TransactionType.D, getConstructor(DraftedTxn.class, params));
        toReturn.put (TransactionType.Df, getConstructor(DraftedTxn.class, params));
        toReturn.put (TransactionType.Dm, getConstructor(DraftedTxn.class, params));
        toReturn.put (TransactionType.Dr, getConstructor(ReturnedTxn.class, params));
        toReturn.put (TransactionType.Ds, getConstructor(DraftedTxn.class, params));
        toReturn.put (TransactionType.Fc, getConstructor(CompensatedTxn.class, params));
        toReturn.put (TransactionType.J, getConstructor(JumpedTxn.class, params));
        toReturn.put (TransactionType.Jr, getConstructor(ReturnedTxn.class, params));
        toReturn.put (TransactionType.L, getConstructor(LoanedTxn.class, params));
        toReturn.put (TransactionType.Lr, getConstructor(ReturnedTxn.class, params));
        toReturn.put (TransactionType.M, getConstructor(RightsTxn.class, params));
        toReturn.put (TransactionType.Mr, getConstructor(ReturnedTxn.class, params));
        toReturn.put (TransactionType.P, getConstructor(PurchasedTxn.class, params));
        toReturn.put (TransactionType.Pr, getConstructor(ReturnedTxn.class, params));
        toReturn.put (TransactionType.Pv, getConstructor(VoidedTxn.class, params));
        toReturn.put (TransactionType.T, getConstructor(TradedTxn.class, params));
        toReturn.put (TransactionType.Tn, getConstructor(ReturnedTxn.class, params));
        toReturn.put (TransactionType.Tp, getConstructor(TradedTxn.class, params));
        toReturn.put (TransactionType.Tr, getConstructor(ReturnedTxn.class, params));
        toReturn.put (TransactionType.Tv, getConstructor(VoidedTxn.class, params));
        toReturn.put (TransactionType.U, getConstructor(UnknownTxn.class, params));
        toReturn.put (TransactionType.W, getConstructor(WaiverPickTxn.class, params));
        toReturn.put (TransactionType.Wf, getConstructor(WaiverPickTxn.class, params));
        toReturn.put (TransactionType.Wr, getConstructor(ReturnedTxn.class, params));
        toReturn.put (TransactionType.Wv, getConstructor(VoidedTxn.class, params));
        toReturn.put (TransactionType.X, getConstructor(DraftedTxn.class, params));
        toReturn.put (TransactionType.Xe, getConstructor(DraftedTxn.class, params));
        toReturn.put (TransactionType.Xm, getConstructor(DraftedTxn.class, params));
        toReturn.put (TransactionType.Xp, getConstructor(DraftedTxn.class, params));
        toReturn.put (TransactionType.Xr, getConstructor(ReturnedTxn.class, params));

        return toReturn;
    }
}
