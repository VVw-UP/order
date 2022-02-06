package com.ocbc.oms.app.model.dto;

import com.ocbc.oms.app.model.Spread;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpreadVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String ccy;
    private Integer userId;
    private List<Spread> values;
}
