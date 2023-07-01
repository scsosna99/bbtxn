package com.buddhadata.sandbox.neo4j.baseball.relationship;

import com.buddhadata.sandbox.neo4j.baseball.processor.FromToTransactionProcessor;
import com.buddhadata.sandbox.neo4j.baseball.processor.FromTransactionProcessor;
import com.buddhadata.sandbox.neo4j.baseball.processor.ToTransactionProcessor;
import org.neo4j.ogm.session.Session;

import java.time.LocalDateTime;

public class TxnBase {

    /**
     * Field position for the transactions' raw data, separated by commas
     */
    public static int TXN_APPROXIMATE = 2;
    public static int TXN_APPROXIMATE_SECONDARY = 4;
    public static int TXN_DATE_PRIMARY = 0;
    public static int TXN_DATE_SECONDARY = 3;
    public static int TXN_DRAFT_PICK = 14;
    public static int TXN_DRAFT_RND = 13;
    public static int TXN_DRAFT_TYPE = 12;
    public static int TXN_FROM_LEAGUE = 9;
    public static int TXN_FROM_TEAM = 8;
    public static int TXN_INFO = 15;
    public static int TXN_PLAYER = 6;
    public static int TXN_RETROSHEET_ID = 5;
    public static int TXN_TIME = 1;
    public static int TXN_TO_LEAGUE = 11;
    public static int TXN_TO_TEAM = 10;
    public static int TXN_TYPE = 7;

    /**
     * Neo4j Primary Key
     */
    private Long id;

    /**
     * Unique identifier of transaction in retrosheet raw data
     */
    private int retrosheetId;

    /**
     * Date on which transaction occurred
     */
    private LocalDateTime transactionDate;

    /**
     * Type of transaction
     */
    private TransactionType transactionType;

    protected TxnBase() {}

    /**
     * Constructor
     * @param transactionType type of transaction
     * @param retrosheetId unique identifier in retrosheet raw data
     * @param transactionDate date on which transaction occurred
     */
    protected TxnBase(TransactionType transactionType,
                      int retrosheetId,
                      LocalDateTime
                              transactionDate) {
        this.transactionType = transactionType;
        this.retrosheetId = retrosheetId;
        this.transactionDate = transactionDate;
    }

    public static void fromRaw (Session session, String line) {
        String[] fields = line.split(",", -1);
        unquoteFields(fields);

        //  Determine what transaction type we're dealing with.
        String txnType = fields[TXN_TYPE];
        if (txnType != null && !txnType.isEmpty()) {
            TransactionType type = TransactionType.valueOf(fields[TXN_TYPE]);
            switch (type) {
                case A:  // assigned from one team to another without compensation
                case C:  // conditional deal
                case Cr: // returned to original team after conditional deal
                case D:  // rule 5 draft pick
                case Df: // first year draft pick
                case Dm: // minor league draft pick
                case Dr: // returned to original team after draft selection
                case Ds: // special draft pick
                case Fc: // free agent compensation pick
                case J:  // jumped teams
                case Jr: // returned to original team after jumping
                case L:  // loaned to another team
                case Lr: // returned to original team after loan
                case M:  // obtained rights when entering into working agreement with minor league team
                case Mr: // rights returned when working agreement with minor league team ended
                case P:  // purchase
                case Pr: // returned to original team after purchase
                case Pv: // purchase voided
                case T:  // traded from one team to another team
                case Tn: // traded but refused to report
                case Tp: // added to trade (usually because one of the original players refused to report or retired)
                case Tr: // returned to original team after trade
                case Tv: // trade voided
                case U:  // unknown (could have been two separate transactions)
                case Wr: // returned to original team after waiver pick
                case W:  // waiver pick
                case Wf: // first year waiver pick
                case Wv: // waiver pick voided
                case X:  // expansion draft
                case Xe: // premium phase of expansion draft
                case Xm: // either the 1960 AL minor league expansion draft or the premium phase of the 1961 NL draft
                case Xp: // added as expansion pick at a later date
                case Xr: // returned to original team after expansion draft
                    FromToTransactionProcessor.instance.process(session, type, fields);
                    break;

                case Fg: // free agent granted
                case Fv: // free agent signing voided
                case R:  // released
                case Z:  // voluntarily retired
                    FromTransactionProcessor.instance.process(session, type, fields);
                    break;

                case Da: // amateur draft pick
                case F:  // free agent signing
                case Fa: // amateur free agent signing
                case Fb: // amateur free agent "bonus baby" signing under the 1953-57 rule requiring player to stay on ML roster
                case Fo: // free agent signing with first ML team
                case Zr: // returned from voluntarily retired list
                    ToTransactionProcessor.instance.process(session, type, fields);
                    break;

                //  Intentionally not processing
                case Dn: // selected in amateur draft but did not sign
                case Dv: // amateur draft pick voided
                    break;

                //  Don't expect any transactions of these types
                case Hb:  // went on the bereavement list
                case Hbr: // came off the bereavement list
                case Hd:  // declared ineligible
                case Hdr: // reinistated from the ineligible list
                case Hf:  // demoted to the minor league
                case Hfr: // promoted from the minor league
                case Hh:  // held out
                case Hhr: // ended hold out
                case Hi:  // went on the disabled list
                case Hir: // came off the disabled list
                case Hm:  // went into military service
                case Hmr: // returned from military service
                case Hs:  // suspended
                case Hsr: // reinstated after a suspension
                case Hu:  // unavailable but not on DL
                case Hur: // returned from being unavailable
                case Hv:  // voluntarity retired
                case Hvr: // unretired
                case Vg: // player assigned to league control
                case V:  // player purchased or assigned to team from league
                    System.out.println("Unexpected transaction type: " + type);
                    break;

                default:
                    System.out.println("Unknown transaction type: " + type);
            }
        }

    }

    /**
     * Remove all double-quotes from fields.
     * @param fields array of fields from parsed raw data.
     */
    protected static void unquoteFields (String[] fields) {

        for (int i = 0; i < fields.length; i++) {
            String tmp = fields[i];
            if (tmp != null && !tmp.isEmpty() && tmp.startsWith("\"")) {
                fields[i] = tmp.replace("\"", "").trim();
            } else {
                fields[i] = tmp.trim();
            }
        }
    }

    /**
     * getter
     * @return Neo4J primary key
     */
    public Long getId() {
        return id;
    }

    /**
     * setter
     * @param id Neo4J primary key
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * getter
     * @return unique identifier in retrosheet raw data
     */
    public int getRetrosheetId() {
        return retrosheetId;
    }

    /**
     * setter
     * @param retrosheetId unique identifier in retrosheet raw data
     */
    public void setRetrosheetId(int retrosheetId) {
        this.retrosheetId = retrosheetId;
    }

    /**
     * getter
     * @return date on which transaction occurred
     */
    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    /**
     * setter
     * @param transactionDate date on which transaction occurred
     */
    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    /**
     * getter
     * @return type of transaction
     */
    public TransactionType getTransactionType() {
        return transactionType;
    }

    /**
     * setter
     * @param transactionType type of transaction
     */
    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TxnBase txnBase = (TxnBase) o;

        if (retrosheetId != txnBase.retrosheetId) return false;
        return transactionType == txnBase.transactionType;

    }

    @Override
    public int hashCode() {
        int result = retrosheetId;
        result = 31 * result + (transactionType != null ? transactionType.hashCode() : 0);
        return result;
    }
}
