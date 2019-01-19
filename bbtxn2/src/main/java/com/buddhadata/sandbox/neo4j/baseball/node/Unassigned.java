package com.buddhadata.sandbox.neo4j.baseball.node;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;

/**
 * Domain Object for the baseball team
 */
@NodeEntity
public class Unassigned {

    /**
     * Internal Neo4J id of the node
     */
    @Id
    @GeneratedValue
    private Long id;
    /**
     * Constructor
     */
    public Unassigned() {
        return;
    }
}
