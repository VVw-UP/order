package com.ocbc.oms.app.api;

import com.ocbc.oms.app.model.CcyPairs;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author mqt
 * @version 1.0
 * @date 2021/11/5 12:38
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllCcyPairsResponse {
    private List<CcyPairs> result;
}
