package com.buddhadata.sandbox.neo4j.baseball.relationship;

import java.util.Date;

/**
 * Created by scsosna on 7/19/18.
 */
public class TxnBase {

    private Date transactionDate;

    protected TxnBase() {
    }

    protected TxnBase(Date transactionDate) {
        this.transactionDate = transactionDate;
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
