package com.ocbc.oms.app.model;

import lombok.Data;

/**
 * @author hzy
 * @since 2021-11-22
 */
@Data
public class CcyPairs {

    private String ccyPair;
    private String dealtCcy;
    private String contraCcy;

    public CcyPairs(String ccyPair, String dealtCcy, String contraCcy) {
        this.ccyPair = ccyPair;
        this.dealtCcy = dealtCcy;
        this.contraCcy = contraCcy;
    }
}
