package com.buddhadata.sandbox.neo4j.baseball.relationship;

/**
 * Enumerated transaction types expected from Retrosheet transactions
 */
public enum TransactionType {
    A  ("assigned from one team to another without compensation"),
    C  ("conditional deal"),
    Cr ("returned to original team after conditional deal"),
    D  ("rule 5 draft pick"),
    Da ("amateur draft pick"),
    Df ("first year draft pick"),
    Dm ("minor league draft pick"),
    Dn ("selected in amateur draft but did not sign"),
    Dr ("returned to original team after draft selection"),
    Ds ("special draft pick"),
    Dv ("amateur draft pick voided"),
    F  ("free agent signing"),
    Fa ("amateur free agent signing"),
    Fb ("amateur free agent 'bonus baby' signing under the 1953-57 rule requiring player to stay on ML roster"),
    Fc ("free agent compensation pick"),
    Fg ("free agent granted"),
    Fo ("free agent signing with first ML team"),
    Fv ("free agent signing voided"),
    Hb ("went on the bereavement list"),
    Hbr("came off the bereavement list"),
    Hd ("declared ineligible"),
    Hdr("reinistated from the ineligible list"),
    Hf ("demoted to the minor league"),
    Hfr("promoted from the minor league"),
    Hh ("held out"),
    Hhr("ended hold out"),
    Hi ("went on the disabled list"),
    Hir("came off the disabled list"),
    Hm ("went into military service"),
    Hmr("returned from military service"),
    Hs ("suspended"),
    Hsr("reinstated after a suspension"),
    Hu ("unavailable but not on DL"),
    Hur("returned from being unavailable"),
    Hv ("voluntarity retired"),
    Hvr("unretired"),
    J  ("jumped teams"),
    Jr ("returned to original team after jumping"),
    L  ("loaned to another team"),
    Lr ("returned to original team after loan"),
    M  ("obtained rights when entering into working agreement with minor league team"),
    Mr ("rights returned when working agreement with minor league team ended"),
    P  ("purchase"),
    Pr ("returned to original team after purchase"),
    Pv ("purchase voided"),
    R  ("released"),
    T  ("traded from one team to another team"),
    Tn ("traded but refused to report"),
    Tp ("added to trade (usually because one of the original players refused to report or retired)"),
    Tr ("returned to original team after trade"),
    Tv ("trade voided"),
    U  ("unknown (could have been two separate transactions)"),
    V  ("player purchased or assigned to team from league"),
    Vg ("player assigned to league control"),
    W  ("waiver pick"),
    Wr ("returned to original team after waiver pick"),
    Wf ("first year waiver pick"),
    Wv ("waiver pick voided"),
    X  ("expansion draft"),
    Xe ("premium phase of expansion draft"),
    Xm ("either the 1960 AL minor league expansion draft or the premium phase of the 1961 NL draft"),
    Xp ("added as expansion pick at a later date"),
    Xr ("returned to original team after expansion draft"),
    Z  ("voluntarily retired"),
    Zr ("returned from voluntarily retired list")
    ;

    /**
     * Description
     */
    private final String desc;

    /**
     * Construction
     * @param desc
     */
    private TransactionType(String desc) {
        this.desc = desc;
    }
}
