package com.ocbc.oms.app.mspatsorderapi;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ocbc.oms.app.api.*;
import com.ocbc.oms.app.dbservice.CurrencyPairService;
import com.ocbc.oms.app.managers.*;
import com.ocbc.oms.app.model.CustomerSegment;
import com.ocbc.oms.app.model.Spread;
import com.ocbc.oms.app.model.TExpiryTime;
import com.ocbc.oms.app.model.TraderSpread;
import com.ocbc.oms.app.model.dto.*;
import com.ocbc.oms.app.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureWebMvc
@Slf4j
public class MsPatsOrderApiApplicationTests {

    @MockBean
    private DbOMSManager dbOMSManager;
    @MockBean
    private ClientUserChannelCounterpartyEntityManager clientUserChannelCounterpartyManager;
    @MockBean
    private CurrencyManager currencyManager;
    @MockBean
    private DirectionWatchTypeManager directionTimeInForceWatchTypeManager;
    @MockBean
    private OrderProductTypeManager orderProductTypeManager;
    @MockBean
    private TradeManager tradeManager;
    @MockBean
    private CfsManager cfsManager;
    @MockBean
    private TimeManager timeManager;

    @MockBean
    private CustomerSegmentMapper customerSegmentMapper;

    @MockBean
    private TraderSpreadMapper traderSpreadMapper;

    @MockBean
    private SpreadMapper spreadMapper;

    @MockBean
    private TEntityMapper tEntityMapper;

    @MockBean
    private GlobalDictMapper globalDictMapper;

    @MockBean
    private CurrencyPairService currencyPairService;

    @MockBean
    private TExpiryTimeMapper tExpiryTimeMapper;

    @MockBean
    private TOrderTypeMapper tOrderTypeMapper;

    @Autowired
    private OrderController orderController;

    @Autowired
    private CustomerSegmentController customerSegmentController;

    @Autowired
    private TraderSpreadController traderSpreadController;

    @Autowired
    private SpreadController spreadController;

    @Autowired
    private GlobalDictController globalDictController;

    @Autowired
    private ExpiryController expiryController;

    @Test
    public void contextLoads() {
        log.info("OrderController -> {}", orderController);
        assertThat(orderController).isNotNull();
    }

    @Test
    public void findCustomerSegment() {
        assertThat(customerSegmentController.findCustomerSegment(2)).isEmpty();
    }

    @Test
    public void insertCustomerSegment() {
        Mockito.when(customerSegmentMapper.updateById(new CustomerSegment())).thenReturn(1);
        assertThat(customerSegmentController.insertCustomerSegment(new CustomerSegmentDto())).isFalse();
        //log.info("CustomerSegmentController insert ->{}", o);
    }

    @Test
    public void updateCustomerSegment() {
        Mockito.when(customerSegmentMapper.updateById(new CustomerSegment())).thenReturn(1);
        Boolean o = customerSegmentController.updateCustomerSegment(new CustomerSegmentDto());
        assertThat(o).isTrue();
        //log.info("CustomerSegmentController update ->{}", o);
    }

    @Test
    public void deleteCustomerSegment() {
        Mockito.when(customerSegmentMapper.deleteById(1)).thenReturn(1);
        assertThat(customerSegmentController.deleteCustomerSegment(1, 2)).isTrue();
        //log.info("CustomerSegmentController delete ->{}", customerSegmentController.deleteCustomerSegment(1, 2));
    }


    @Test
    public void findEntity() {
        Mockito.when(tEntityMapper.findEntity()).thenReturn(new ArrayList<>());
        assertThat(customerSegmentController.findEntity()).isEmpty();
        //log.info("CustomerSegmentController findEntity ->{}", customerSegmentController.findEntity());
    }

    @Test
    public void findTraderSpread() {
        Mockito.when(traderSpreadMapper.selectList(new QueryWrapper<>())).thenReturn(new ArrayList<>());
        assertThat(traderSpreadController.findTraderSpread(2)).isEmpty();
        //log.info("TraderSpread find ->{}", traderSpreadController.findTraderSpread(2));
    }

    @Test
    public void insertTraderSpread() {
        List<TraderSpread> list = new ArrayList<>();
        list.add(new TraderSpread());
        Mockito.when(traderSpreadMapper.selectList(new QueryWrapper<>())).thenReturn(list);
        Mockito.when(traderSpreadMapper.insert(new TraderSpread())).thenReturn(1);
        assertThat(traderSpreadController.insertTraderSpread(new TraderSpreadDto())).isNotNull();
        //log.info("TraderSpread insert ->{}", traderSpreadController.insertTraderSpread(new TraderSpreadDto()));
    }

    @Test
    public void updateTraderSpread() {
        Mockito.when(traderSpreadMapper.updateById(new TraderSpread())).thenReturn(1);
        assertThat(traderSpreadController.updateTraderSpread(new TraderSpreadDto())).isNotNull();
        //log.info("TraderSpread update ->{}", traderSpreadController.updateTraderSpread(new TraderSpreadDto()));
    }

    @Test
    public void deleteTraderSpread() {
        Mockito.when(traderSpreadMapper.deleteById(1)).thenReturn(1);
        assertThat(traderSpreadController.deleteTraderSpread(1, 2)).isNotNull();
        //log.info("TraderSpread delete ->{}", traderSpreadController.deleteTraderSpread(1, 2));
    }

    @Test
    public void findSpread() {
        Mockito.when(spreadMapper.selectList(new QueryWrapper<>())).thenReturn(new ArrayList<>());
        assertThat(spreadController.findSpread(1)).isEmpty();
        //log.info("Spread find ->{}", spreadController.findSpread(1));
    }

    @Test
    public void insertSpread(){
        SpreadVo spreadVo = new SpreadVo();
        spreadVo.setCcy("USD/CNY");
        spreadVo.setUserId(2);
        ArrayList<Spread> spreads = new ArrayList<>();
        Spread spread = new Spread();
        spread.setSpreadFrom(1);
        spreads.add(spread);
        spread.setUserId(2);
        spreadVo.setValues(spreads);
        Mockito.when(spreadMapper.insert(spread)).thenReturn(1);
        assertThat(spreadController.insertSpread(spreadVo)).isNotNull();
    }

    @Test
    public void updateSpread(){
        SpreadVo spreadVo = new SpreadVo();
        spreadVo.setCcy("USD/CNY");
        spreadVo.setUserId(2);
        ArrayList<Spread> spreads = new ArrayList<>();
        Spread spread = new Spread();
        spread.setSpreadFrom(1);
        spreads.add(spread);
        spread.setUserId(2);
        spreadVo.setValues(spreads);
        Mockito.when(spreadMapper.updateById(spread)).thenReturn(1);
        assertThat(spreadController.updateSpread(spreadVo)).isNotNull();
    }

    @Test
    public void deleteSpread(){
        Mockito.when(spreadMapper.delete(new QueryWrapper<>())).thenReturn(1);
        assertThat(spreadController.deleteSpread(new SpreadDelDto())).isNotNull();
    }

    @Test
    public void findSegmentType(){
        assertThat(globalDictController.findSegmentType()).isEmpty();
    }

    @Test
    public void findMurexBookingType(){
        assertThat(globalDictController.findMurexBookingType()).isEmpty();
    }

    @Test
    public void findCcyPair(){
        assertThat(globalDictController.findCcyPair()).isEmpty();
    }

    @Test
    public void findCcyPairs(){
        assertThat(globalDictController.findCcyPairs()).isEmpty();
    }

    @Test
    public void insertExpiryTime(){
        TExpiryTimeVo expiryTimeVo = new TExpiryTimeVo();
        expiryTimeVo.setDays(1);
        expiryTimeVo.setHours(23);
        expiryTimeVo.setMinutes(59);
        expiryTimeVo.setOrderTypeId(1);
        expiryTimeVo.setOrderTypeName("limit");
        TExpiryTime expiryTime = new TExpiryTime();
        BeanUtils.copyProperties(expiryTimeVo,expiryTime);
        Mockito.when(tExpiryTimeMapper.insert(expiryTime)).thenReturn(1);
        assertThat(expiryController.insertExpiryTime(expiryTimeVo)).isNotNull();
    }

    @Test
    public void findExpiryTimes(){
        Mockito.when(tOrderTypeMapper.selectList(new QueryWrapper<>())).thenReturn(new ArrayList<>());
        Mockito.when(tExpiryTimeMapper.selectList(new QueryWrapper<>())).thenReturn(new ArrayList<>());
        assertThat(expiryController.findExpiryTimes());
    }

}
