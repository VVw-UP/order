package com.ocbc.oms.app.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ocbc.oms.app.model.TTrade;
import com.ocbc.oms.app.model.dto.TradeDto;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TTradeMapper extends BaseMapper<TTrade> {

    /**
     * select trade info with order type
     *
     * @return
     */
    @Select("select " +
            "tt.id as id, " +
            "tt.order_id as orderId, " +
            "tt.price as price, " +
            "tt.trade_status_id as tradeStatusId, " +
            "tt.direction_id as directionId, " +
            "tor.order_type_id as orderTypeId, " +
            "tcp.ccy_pair as ccyPair " +
            "from t_trade tt " +
            "join t_order tor on tt.order_id = tor.id " +
            "join t_currency_pair tcp on tt.ccy_pair_id = tcp.id " +
            "where tt.trade_status_id = 4")
    List<TradeDto> selectTrades();
}
