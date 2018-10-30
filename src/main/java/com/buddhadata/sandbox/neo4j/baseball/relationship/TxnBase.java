package com.buddhadata.sandbox.neo4j.baseball.relationship;

import java.util.Date;

public class TxnBase {

    /**
     * Neo4j Primary Key
     */
    private Long id;

    private int retrosheetId;

    private Date transactionDate;

    protected TxnBase() {
    }

    protected TxnBase (Date transactionDate) {
        this (0, transactionDate);
    }

    protected TxnBase(int retrosheetId,
                      Date transactionDate) {
        this.retrosheetId = retrosheetId;
        this.transactionDate = transactionDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getRetrosheetId() {
        return retrosheetId;
    }

    public void setRetrosheetId(int retrosheetId) {
        this.retrosheetId = retrosheetId;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TxnBase that = (TxnBase) o;

        return transactionDate != null ? transactionDate.equals(that.transactionDate) : that.transactionDate == null;

    }

    @Override
    public int hashCode() {
        return transactionDate != null ? transactionDate.hashCode() : 0;
    }
}
