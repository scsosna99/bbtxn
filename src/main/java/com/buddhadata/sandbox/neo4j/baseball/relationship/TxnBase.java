package com.buddhadata.sandbox.neo4j.baseball.relationship;

import java.time.LocalDateTime;

public class TxnBase {

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
