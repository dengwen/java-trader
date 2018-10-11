package trader.service.trade;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.google.gson.JsonObject;

import trader.common.exchangeable.Exchangeable;
import trader.common.util.JsonUtil;

public class OrderImpl implements Order {

    protected Exchangeable exchangeable;
    protected String ref;
    protected OrderDirection direction;
    protected int volume;
    protected long limitPrice;
    protected OrderPriceType priceType;
    protected OrderOffsetFlag offsetFlag;
    protected String sysId;
    protected volatile OrderState state;
    protected volatile OrderSubmitState submitState;
    protected String failReason;
    protected long[] hostTimes = new long[OrderTime.values().length];
    protected long[] serverTimes = new long[OrderTime.values().length];
    protected List<Transaction> transactions = new ArrayList<>();
    private Properties attrs = new Properties();

    public OrderImpl(Exchangeable e, String ref, OrderPriceType priceType, OrderOffsetFlag offsetFlag, long limitPrice, int volume)
    {
        exchangeable = e;
        this.ref = ref;
        this.priceType = priceType;
        this.offsetFlag = offsetFlag;
        this.limitPrice = limitPrice;
        this.volume = volume;
        state = OrderState.Unknown;
    }

    @Override
    public Exchangeable exchangeable() {
        return exchangeable;
    }

    @Override
    public String getRef() {
        return ref;
    }

    @Override
    public String getSysId() {
        return sysId;
    }

    @Override
    public OrderDirection getDirection() {
        return direction;
    }

    @Override
    public OrderPriceType getPriceType() {
        return priceType;
    }

    @Override
    public OrderOffsetFlag getOffsetFlags() {
        return offsetFlag;
    }

    @Override
    public long getLimitPrice() {
        return limitPrice;
    }

    @Override
    public int getVolume() {
        return volume;
    }

    @Override
    public OrderState getState() {
        return state;
    }

    @Override
    public OrderSubmitState getSubmitState() {
        return submitState;
    }

    @Override
    public long getHostTime(OrderTime time) {
        return hostTimes[time.ordinal()];
    }

    @Override
    public long getServerTime(OrderTime time) {
        return serverTimes[time.ordinal()];
    }

    @Override
    public String getAttr(String attr) {
        return attrs.getProperty(attr);
    }

    @Override
    public void setAttr(String attr, String value) {
        if ( value==null ) {
            attrs.remove(attr);
        }else {
            attrs.setProperty(attr, value);
        }
    }

    @Override
    public List<Transaction> getTransactions() {
        return transactions;
    }

    @Override
    public JsonObject toJsonObject() {
        JsonObject json = new JsonObject();
        json.addProperty("exchangeable", exchangeable.id());
        json.addProperty("ref", ref);
        json.addProperty("direction", direction.name());
        json.addProperty("volume", volume);
        json.addProperty("limitPrice", limitPrice);
        json.addProperty("priceType", priceType.name());
        json.addProperty("offsetFlag", offsetFlag.name());
        json.addProperty("state", state.name());
        json.addProperty("submitState", submitState.name());
        if ( failReason!=null ) {
            json.addProperty("failReason", failReason);
        }
        JsonObject hostTimesJson = new JsonObject();
        JsonObject serverTimesJson = new JsonObject();
        for(OrderTime timeType:OrderTime.values()) {
            long hostTime = hostTimes[timeType.ordinal()];
            long serverTime = serverTimes[timeType.ordinal()];
            if ( hostTime!=0 ) {
                hostTimesJson.addProperty(timeType.name(), hostTime);
            }
            if ( serverTime!=0 ) {
                serverTimesJson.addProperty(timeType.name(), serverTime);
            }
        }
        json.add("hostTimes", hostTimesJson);
        json.add("serverTimes", hostTimesJson);
        if ( !attrs.isEmpty() ) {
            json.add("attrs", JsonUtil.props2json(attrs));
        }
        return json;
    }

    public OrderState setState(OrderState state) {
        OrderState lastState = this.state;
        this.state = state;
        return lastState;
    }

    public void setSubmitState(OrderSubmitState submitState) {
        this.submitState = submitState;
    }

    public void setSysId(String sysId) {
        this.sysId = sysId;
    }

    public void setTime(OrderTime time, long hostTime, long serverTime) {
        hostTimes[time.ordinal()] = hostTime;
        serverTimes[time.ordinal()] = serverTime;
    }

    public void setFailReason(String failReason) {
        this.failReason = failReason;
    }

    public void attachTransaction(Transaction txn) {
        transactions.add(txn);
    }
}
