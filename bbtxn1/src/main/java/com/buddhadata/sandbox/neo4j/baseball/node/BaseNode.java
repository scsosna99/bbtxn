package com.buddhadata.sandbox.neo4j.baseball.node;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class BaseNode {
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
}
