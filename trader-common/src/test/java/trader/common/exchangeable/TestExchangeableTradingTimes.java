package trader.common.exchangeable;

import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;

import org.junit.Test;

import trader.common.util.DateUtil;

public class TestExchangeableTradingTimes {

    @Test
    public void test_ru() {
        Exchangeable ru1901 = Exchangeable.fromString("ru1901");
        assertTrue(ru1901.exchange()==Exchange.SHFE);
        ExchangeableTradingTimes tradingTimes = ru1901.exchange().getTradingTimes(ru1901, DateUtil.str2localdate("20181203"));
        assertTrue(tradingTimes!=null);

        LocalDateTime time0 = DateUtil.str2localdatetime("20181130 21:01:01");
        assertTrue(tradingTimes.getTimeStage(time0)==MarketTimeStage.MarketOpen);
        assertTrue(tradingTimes.getSegmentType(time0)==MarketType.Night);
        assertTrue(tradingTimes.getTradingTimeInSegment(time0, MarketType.Night)==61*1000);

        LocalDateTime time1 = DateUtil.str2localdatetime("20181203 14:01:01");
        assertTrue(tradingTimes.getSegmentType(time1)==MarketType.Day);

        LocalDateTime time2 = DateUtil.str2localdatetime("20181203 15:01:01");
        assertTrue(tradingTimes.getSegmentType(time2)==null );

        LocalDateTime time4 = DateUtil.str2localdatetime("20181203 09:01:01");
        assertTrue(tradingTimes.getTradingTimeInSegment(time4, MarketType.Day)==61*1000);
    }

}