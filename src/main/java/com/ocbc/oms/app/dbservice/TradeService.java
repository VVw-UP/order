package com.ocbc.oms.app.dbservice;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ocbc.oms.app.api.CFSOrderRequest;
import com.ocbc.oms.app.consts.DataBaseConst;
import com.ocbc.oms.app.error.api.APIErrorConstant;
import com.ocbc.oms.app.error.api.TradeDateValueDateCalculationException;
import com.ocbc.oms.app.model.*;
import com.ocbc.oms.app.model.dto.TradeDateDto;
import com.ocbc.oms.app.model.dto.TradeDto;
import com.ocbc.oms.app.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.ocbc.oms.app.consts.CommonConstants.*;

@Service
@Slf4j
public class TradeService {

    @Autowired
    private SpreadMapper spreadMapper;

    @Autowired
    private TraderSpreadMapper traderSpreadMapper;

    @Autowired
    private THolidayMapper tHolidayMapper;

    @Autowired
    private TCurrencyMapper tCurrencyMapper;

    @Autowired
    private final TTradeMapper tTradeMapper;

    @Autowired
    private TTradeDateHistoryMapper tTradeDateHistoryMapper;

    @Value("${valuedate.special.ccypair:USD/CAD,USD/TRY,USD/PHP,USD/RUB,CAD/RUB,CAD/TRY,CAD/PHP,RUB/TRY,RUB/PHP,PHP/TRY,RUB/CAD,TRY/CAD,PHP/CAD,TRY/RUB,PHP/RUB,TRY/PHP}")
    private String specialSpotCcyPair;

    @Value("${currency.default.nzd.time}")
    private String nzdDefaultTime;

    @Value("${currency.default.ny.time}")
    private String nyDefaultTime;

    @Value("${currency.default.timezone}")
    private String defaultTimeZone;

    @Value("${currency.default.local.timezone}")
    private String defaultLocalTimeZone;

    public TradeService(TTradeMapper tTradeMapper) {
        this.tTradeMapper = tTradeMapper;
    }

    public int addTrade(TTrade trade) {
        return tTradeMapper.insert(trade);
    }

    public List<TTrade> getTradeByOrderId(Long orderId) {
        QueryWrapper<TTrade> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", orderId);
        return tTradeMapper.selectList(queryWrapper);
    }

    public int updateTrade(TTrade trade) {
        return tTradeMapper.updateById(trade);
    }

    public List<TTrade> findAllTrade() {
        return tTradeMapper.selectList(null);
    }

    @Transactional(rollbackFor = Exception.class)
    public int updateTrades(Long id) {
        UpdateWrapper<TTrade> wrapper = new UpdateWrapper<>();
        wrapper.clear();
        wrapper.eq(DataBaseConst.ID, id);
        wrapper.eq(DataBaseConst.T_TRADE_TRADE_STATUS_ID, 4);
        return tTradeMapper.update(new TTrade(11), wrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    public int fillTrades(Long id, Integer status) {
        UpdateWrapper<TTrade> wrapper = new UpdateWrapper<>();
        wrapper.clear();
        wrapper.eq(DataBaseConst.ID, id);
        wrapper.eq(DataBaseConst.T_TRADE_TRADE_STATUS_ID, 4);
        return tTradeMapper.update(new TTrade(status, LocalDateTime.now()), wrapper);
    }

    public List<TTrade> findByTradeStatusId() {
        QueryWrapper<TTrade> wrapper = new QueryWrapper<>();
        wrapper.eq(DataBaseConst.T_TRADE_TRADE_STATUS_ID, 4);
        return tTradeMapper.selectList(wrapper);
    }

    public List<TradeDto> getTradeWithOrderType() {
        return tTradeMapper.selectTrades();
    }

    public void calculatePrice(BigDecimal price, TTrade trade, String sideType, Integer minorUnits) {
        BigDecimal num = new BigDecimal("1");
        BigDecimal eventualPrice = new BigDecimal("0");
        switch (sideType) {
            case DIRECTION_BUY:
                eventualPrice = num.divide(price, minorUnits, BigDecimal.ROUND_FLOOR);
                break;
            case DIRECTION_SELL:
                eventualPrice = num.divide(price, minorUnits, BigDecimal.ROUND_CEILING);
                break;
        }
        trade.setWatchPrice(eventualPrice);
    }

    public TradeDateDto getTradeDateAndValueDate(String ccyPair) {
        TradeDateDto tradeDateDto = null;
        LocalDateTime tradeDate = LocalDateTime.now();
        String[] ccys = ccyPair.split("[/]");
        try {
            String ccy1 = ccys[0];
            String ccy2 = ccys[1];

            TCurrencyDataDto currencyDto1 = getCurrencyFromCache(ccy1, tradeDate);
            TCurrencyDataDto currencyDto2 = getCurrencyFromCache(ccy2, tradeDate);

            tradeDate = currencyDto1.getTradeDate().isAfter(currencyDto2.getTradeDate()) ? currencyDto1.getTradeDate() : currencyDto2.getTradeDate();

            // LocalDateTime to date
            ZoneId zoneId = ZoneId.systemDefault();
            Date date = Date.from(tradeDate.atZone(zoneId).toInstant());

            Map<String, List<Date>> holidayMap = createHolidayMap(Arrays.asList(ccys), date);
            Date valueDate = calcValueDate(currencyDto1, currencyDto2, holidayMap, date, ccyPair);
            log.debug("tradeDate:{}，valueDate:{}", date, valueDate);
            tradeDateDto = new TradeDateDto(date2LocalDateTime(date), date2LocalDateTime(valueDate));
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new TradeDateValueDateCalculationException(APIErrorConstant.TradeDateValueDateCalculationExceptionMessage, APIErrorConstant.TradeDateValueDateCalculationExceptionCode);
        }
        return tradeDateDto;
    }

    private TCurrencyDataDto getCurrencyFromCache(String ccyCode, LocalDateTime nowTime) {
        QueryWrapper<TCurrency> queryWrapper= new QueryWrapper<>();
        queryWrapper.eq("code",ccyCode);
        TCurrency currencyByCode = tCurrencyMapper.selectOne(queryWrapper);
        // deal with roll time
        String realRollTime = dealWithRollTime(ccyCode, currencyByCode.getRollTime(), currencyByCode.getTimeZone());

        // calculate tradeDate
        LocalDateTime tradeDate = calcTradeDate(realRollTime, nowTime);

        // insert DB
        TTradeDateHistory tTradeDateHistory = new TTradeDateHistory();
        tTradeDateHistory.setLastModifyTimestamp(localDateTime2Date(nowTime));
        Date date = localDateTime2Date(tradeDate);
        tTradeDateHistory.setCurrenctTradeDate(date);
        tTradeDateHistory.setCurrencyId(new BigDecimal(currencyByCode.getId()));
        try{
            tTradeDateHistoryMapper.insert(tTradeDateHistory);
        }catch (Exception e){
            log.debug("exist tradeDate history record,update modifyTime,{}{}", ccyCode, date);
            TTradeDateHistory tTradeDateHistory1 = new TTradeDateHistory();
            tTradeDateHistory1.setLastModifyTimestamp(localDateTime2Date(nowTime));
            UpdateWrapper<TTradeDateHistory> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("currency_id",currencyByCode.getId());
            updateWrapper.eq("currenct_trade_date",date);
            tTradeDateHistoryMapper.update(tTradeDateHistory1,updateWrapper);
        }

        // insert cache
        TCurrencyDataDto dataDto = new TCurrencyDataDto(ccyCode, tradeDate, nowTime, currencyByCode.getRollTime(), new Long(currencyByCode.getSpotDays()), currencyByCode.getTimeZone());
        return dataDto;
    }
    /**
     * LocalDateTime转换为Date
     *
     * @param localDateTime
     */
    public Date localDateTime2Date(LocalDateTime localDateTime) {
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDateTime.atZone(zoneId);
        return Date.from(zdt.toInstant());
    }
    private Map<String, List<Date>> createHolidayMap(List<String> ccyList, Date tradeDate) {
        ArrayList<String> list = new ArrayList<>(ccyList);
        list.add(CURRENCY_USD);
        QueryWrapper<THoliday> queryWrapper =new QueryWrapper<>();
        queryWrapper.in("ccy",list);
        queryWrapper.gt("holiday_date",tradeDate);
        List<THoliday> tHolidays = tHolidayMapper.selectList(queryWrapper);
        Map<String, List<Date>> holidayMap = new HashMap<>();
        for (THoliday tHoliday : tHolidays) {
            if (holidayMap.containsKey(tHoliday.getCcy())) {
                holidayMap.get(tHoliday.getCcy()).add(tHoliday.getHolidayDate());
            } else {
                List<Date> dates = new ArrayList<>();
                dates.add(tHoliday.getHolidayDate());
                holidayMap.put(tHoliday.getCcy(), dates);
            }
        }
        return holidayMap;
    }
    private Date calcValueDate(TCurrencyDataDto firstCcy, TCurrencyDataDto secondCcy,
                               Map<String, List<Date>> holidayMap, Date tradeDate, String ccyPiar) {
        Long spotDays1 = firstCcy.getSpotDays();
        Long spotDays2 = secondCcy.getSpotDays();
        if (StringUtils.isNotBlank(ccyPiar) && specialSpotCcyPair.contains(ccyPiar)) {
            spotDays1 = 1L;
            spotDays2 = 1L;
        }
        Date firstValueDate = getNextValueDate(firstCcy.getCurrency(), holidayMap, tradeDate, spotDays1);
        Date secondValueDate = getNextValueDate(secondCcy.getCurrency(), holidayMap, tradeDate, spotDays2);
        Date valueDate = getRealValueDate(firstCcy, secondCcy, holidayMap, firstValueDate, secondValueDate, 0);
        List<Date> usdHolidayList = holidayMap.get(CURRENCY_USD);
        while (usdHolidayList != null && usdHolidayList.contains(valueDate)) {
            firstValueDate = getNextValueDate(firstCcy.getCurrency(), holidayMap, firstValueDate, 1);
            secondValueDate = getNextValueDate(secondCcy.getCurrency(), holidayMap, secondValueDate, 1);
            valueDate = getRealValueDate(firstCcy, secondCcy, holidayMap, firstValueDate, secondValueDate, 0);
        }
        return valueDate;
    }
    private Date getNextValueDate(String ccyCode, Map<String, List<Date>> holidayMap, Date tradeDate, long days) {
        if (days < 1) {
            log.warn("Currency :{} spot days param invalid,use default 2");
            days = 2;
        }
        Date valueDate = refreshDate(tradeDate);
        //当货币对包含USD且为T+2时，USD不跳过T+1的holiday， 当包含USD且为T+1时，则跳过
        boolean isFirst = false;
        for (int i = 0; i < days; i++) {
            isFirst = (i == 0) && (days > 1);
            valueDate = getNextValueDate(ccyCode, valueDate, holidayMap, isFirst);
        }
        return valueDate;
    }
    /**
     * 时区 时间转换方法:将传入的时间（可能为其他时区）转化成目标时区对应的时间
     *
     * @param sourceTime 时间格式必须为：yyyy-MM-dd HH:mm:ss
     * @param sourceId   入参的时间的时区id 比如：+08:00
     * @param targetId   要转换成目标时区id 比如：+09:00
     * @param reFormat   返回格式 默认：yyyy-MM-dd HH:mm:ss
     * @return string 转化时区后的时间
     */
    public static String timeConvert(String sourceTime, String sourceId,
                                     String targetId, String reFormat) {
        //校验入参是否合法
        if (StringUtils.isBlank(sourceTime) || StringUtils.isBlank(sourceId) ||
                StringUtils.isBlank(targetId)) {
            log.error("timeConvert : in parameter error");
            return null;
        }

        if (StringUtils.isBlank(reFormat)) {
            reFormat = YYYY_MM_DD_HH_MM_SS;
        }

        //校验 时间格式必须为：yyyy-MM-dd HH:mm:ss
        String reg = "^[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}$";
        if (!sourceTime.matches(reg)) {
            log.error("check time format exception");
            return null;
        }

        try {
            //时间格式
            SimpleDateFormat df = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
            //根据入参原时区id，获取对应的timezone对象
            TimeZone sourceTimeZone = TimeZone.getTimeZone(sourceId);
            //设置SimpleDateFormat时区为原时区（否则是本地默认时区），目的:用来将字符串sourceTime转化成原时区对应的date对象
            df.setTimeZone(sourceTimeZone);
            //将字符串sourceTime转化成原时区对应的date对象
            java.util.Date sourceDate = df.parse(sourceTime);

            //开始转化时区：根据目标时区id设置目标TimeZone
            TimeZone targetTimeZone = TimeZone.getTimeZone(targetId);
            //设置SimpleDateFormat时区为目标时区（否则是本地默认时区），目的:用来将字符串sourceTime转化成目标时区对应的date对象
            df.setTimeZone(targetTimeZone);
            //得到目标时间字符串
            String targetTime = df.format(sourceDate);
            SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
            java.util.Date date = sdf.parse(targetTime);
            sdf = new SimpleDateFormat(reFormat);
            return sdf.format(date);
        } catch (ParseException e) {
            log.error("parse sourceTime to targetTime error,{}", e.getMessage());
            return null;
        }
    }
    /**
     * Date转LocalDate
     *
     * @param date
     */
    public static LocalDateTime date2LocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
    /**
     * date类型时分秒清零
     * @param date
     * @return
     */
    public Date refreshDate(Date date){
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date);
        cal1.set(Calendar.HOUR_OF_DAY, 0);
        cal1.set(Calendar.MINUTE, 0);
        cal1.set(Calendar.SECOND, 0);
        cal1.set(Calendar.MILLISECOND, 0);
        return cal1.getTime();
    }
    private Date getNextValueDate(String ccy, Date cDate, Map<String, List<Date>> map, boolean firstDay) {
        Date valueDate = addDate(cDate, 1);
        LocalDateTime vDate = date2LocalDateTime(valueDate);
        int weekDay = vDate.getDayOfWeek().getValue();
        if (weekDay == 6 || weekDay == 7) {
            return getNextValueDate(ccy, valueDate, map, firstDay);
        }
        if (CURRENCY_USD.equals(ccy) && firstDay) {
            return valueDate;
        }

        if (map.containsKey(ccy)) {
            List<Date> dateList = map.get(ccy);
            if (dateList.contains(valueDate)) {
                return getNextValueDate(ccy, valueDate, map, firstDay);
            } else {
                return valueDate;
            }
        }
        return valueDate;
    }
    private Date addDate(Date cDate, int adds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(cDate);
        calendar.add(Calendar.DATE, adds);
        //跳过周六周日
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            calendar.add(Calendar.DATE, 2);
        } else if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            calendar.add(Calendar.DATE, 1);
        }
        cDate = calendar.getTime();
        return cDate;
    }
    /**
     * @param firstCcy
     * @param secondCcy
     * @param holidayMap
     * @param firstValueDate
     * @param secondValueDate
     * @param count           参数，因为是一个递归调用，为了防止出现异常导致死循环，超过次数则失败
     * @return
     */
    private Date getRealValueDate(TCurrencyDataDto firstCcy, TCurrencyDataDto secondCcy,
                                  Map<String, List<Date>> holidayMap,
                                  Date firstValueDate, Date secondValueDate,
                                  int count) {
        count++;
        if (count > 1000) {
            log.warn("Can't find real valuedate for loop {} times", count);
            return null;
        }
        int cmpRslt = firstValueDate.compareTo(secondValueDate);
        if (cmpRslt == 0) {
            return firstValueDate;
        } else if (cmpRslt < 0) {
            firstValueDate = getNextValueDate(firstCcy.getCurrency(), firstValueDate, holidayMap, false);
            return getRealValueDate(firstCcy, secondCcy, holidayMap, firstValueDate, secondValueDate, count);
        } else {
            secondValueDate = getNextValueDate(secondCcy.getCurrency(), secondValueDate, holidayMap, false);
            return getRealValueDate(firstCcy, secondCcy, holidayMap, firstValueDate, secondValueDate, count);
        }
    }
    public String dealWithRollTime(String ccyCode, String rollTime, String timeZone) {
        if (CURRENCY_NZD.equals(ccyCode)) {
            rollTime = StringUtils.isBlank(rollTime) ? nzdDefaultTime : rollTime;
        } else {
            rollTime = StringUtils.isBlank(rollTime) ? nyDefaultTime : rollTime;
        }

        timeZone = StringUtils.isBlank(timeZone) ? defaultTimeZone : timeZone;

        SimpleDateFormat sf = new SimpleDateFormat(YYYY_MM_DD);
        String sourceTime = sf.format(new Date()) + " " + rollTime;
        String timeConvert = timeConvert(sourceTime, timeZone, defaultLocalTimeZone, YYYY_MM_DD_HH_MM_SS);

        Date date = null;
        try {
            date = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS).parse(timeConvert);
        } catch (ParseException e) {
            log.error("convertTime parse error,Currency:{}{}", ccyCode, e.getMessage());
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(HH_MM_SS);
        return sdf.format(date);
    }
    private LocalDateTime calcTradeDate(String rollTime, LocalDateTime nowTime) {
        LocalDateTime tradeDate;
        SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD);
        String dealRollTime = sdf.format(new Date()) + " " + rollTime;

        DateTimeFormatter df = DateTimeFormatter.ofPattern(YYYY_MM_DD_HH_MM_SS);
        LocalDateTime ldt = LocalDateTime.parse(dealRollTime, df);

        tradeDate = nowTime.isAfter(ldt) ? nowTime : nowTime.minusDays(1);

        //  pass Saturday and Sunday
        if (tradeDate.getDayOfWeek().getValue() == DayOfWeek.SATURDAY.getValue()) {
            tradeDate = tradeDate.plusDays(2);
        } else if (tradeDate.getDayOfWeek().getValue() == DayOfWeek.SUNDAY.getValue()) {
            tradeDate = tradeDate.plusDays(1);
        }

        return tradeDate;
    }

    public BigDecimal calculateLimitPrice(Integer minorUnits, String ccyPair, BigDecimal dealtAmount, BigDecimal basePrice, String side) {
        //Get the traderSpread
        BigDecimal traderSpread = new BigDecimal("0");
        QueryWrapper<TraderSpread> traderSpreadQueryWrapper = new QueryWrapper<>();
        traderSpreadQueryWrapper.eq("ccy_par", ccyPair);

        if (traderSpreadMapper.selectOne(traderSpreadQueryWrapper) != null && traderSpreadMapper.selectOne(traderSpreadQueryWrapper).getSpread() != null) {
            traderSpread = traderSpreadMapper.selectOne(traderSpreadQueryWrapper).getSpread();
        }

        //Get the spread by DealtAmount
        BigDecimal sumSpreadTraderSpread;

        QueryWrapper<Spread> spreadQueryWrapper = new QueryWrapper<>();
        spreadQueryWrapper.eq("ccy_par", ccyPair);
        spreadQueryWrapper.lt("spread_from", dealtAmount);
        spreadQueryWrapper.ge("spread_to", dealtAmount);
        List<Spread> selectList = spreadMapper.selectList(spreadQueryWrapper);

        if (CollectionUtils.isEmpty(selectList)) {
            QueryWrapper<Spread> wrapper = new QueryWrapper<>();
            wrapper.eq("ccy_par", ccyPair);
            wrapper.orderByDesc("spread_to");
            selectList = spreadMapper.selectList(wrapper);
        }

        BigDecimal spreadPoint = CollectionUtils.isEmpty(selectList) ? new BigDecimal("0") : selectList.get(0).getSpreadPoint();

        sumSpreadTraderSpread = traderSpread.add(spreadPoint);
        BigDecimal price = sumSpreadTraderSpread.multiply(BigDecimal.valueOf(Math.pow(10, -minorUnits)));

        if (DIRECTION_SELL.equals(side)) {
            basePrice = basePrice.add(price);
        } else if (DIRECTION_BUY.equals(side)) {
            basePrice = basePrice.subtract(price);
        }
        return basePrice;
    }
}
