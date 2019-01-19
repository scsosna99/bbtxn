package com.buddhadata.sandbox.neo4j.baseball.relationship;

import com.buddhadata.sandbox.neo4j.baseball.node.Player;

import java.time.LocalDateTime;

public class TxnBase {

    /**
     * Neo4j Primary Key
     */
    private Long id;

    /**
     * Unique identifier of transaction in retrosheet raw data
     */
    private int txnRetrosheetId;

    /**
     * Date on which transaction occurred
     */
    private LocalDateTime transactionDate;

    /**
     * Identifies the player involved in the transaction
     */
    private String playerRetrosheetId;

    /**
     * Type of transaction
     */
    private TransactionType transactionType;

    protected TxnBase() {}

    /**
     * Constructor
     * @param transactionType type of transaction
     * @param txnRetrosheetId unique identifier in retrosheet raw data
     * @param transactionDate date on which transaction occurred
     */
    protected TxnBase(TransactionType transactionType,
                      int txnRetrosheetId,
                      Player player,
                      LocalDateTime transactionDate) {
        this.transactionType = transactionType;
        this.txnRetrosheetId = txnRetrosheetId;
        this.playerRetrosheetId = player.getRetrosheetId();
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
    public int getTxnRetrosheetId() {
        return txnRetrosheetId;
    }

    /**
     * setter
     * @param txnRetrosheetId unique identifier in retrosheet raw data
     */
    public void setTxnRetrosheetId(int txnRetrosheetId) {
        this.txnRetrosheetId = txnRetrosheetId;
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
     * @return player involved in the transaction
     */
    public String getPlayerRetrosheetId() {
        return playerRetrosheetId;
    }

    /**
     * setter
     * @param playerRetrosheetId player involved in the transaction
     */
    public void setPlayerRetrosheetId(String playerRetrosheetId) {
        this.playerRetrosheetId = playerRetrosheetId;
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

        if (txnRetrosheetId != txnBase.txnRetrosheetId) return false;
        return transactionType == txnBase.transactionType;

    }

    @Override
    public int hashCode() {
        int result = txnRetrosheetId;
        result = 31 * result + (transactionType != null ? transactionType.hashCode() : 0);
        return result;
    }
}
