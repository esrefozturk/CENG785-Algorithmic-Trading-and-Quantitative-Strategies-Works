import quickfix.field.*;

/**
 * Created by esrefozturk on 20/11/2016.
 */
public class Order {
    public static int CURRENT_ID = 1;
    public ClOrdID clOrdID = null;
    public OrdType ordType = null;
    public Symbol symbol = null;
    public Side side = null;
    public OrderQty orderQty = null;
    public TimeInForce timeInForce = null;
    public Price price = null;
    public OrigClOrdID cancelID = null;


    public Order(OrdType ordType, Symbol symbol, Side side, TimeInForce timeInForce, OrderQty orderQty, Price price) {
        this.clOrdID = new ClOrdID((new Integer(CURRENT_ID++)).toString());
        this.ordType = ordType;
        this.symbol = symbol;
        this.side = side;
        this.orderQty = orderQty;
        this.timeInForce = timeInForce;
        this.price = price;
    }

    public Order(String ordType, String symbol, String side, String timeInForce, double orderQty, double price) throws Exception {

        if (ordType.equals("MARKET")) {
            this.ordType = new OrdType(OrdType.MARKET);
        } else if (ordType.equals("LIMIT")) {
            this.ordType = new OrdType(OrdType.LIMIT);
        } else {
            throw (new Exception());
        }

        if (side.equals("BUY")) {
            this.side = new Side(Side.BUY);
        } else if (side.equals("SELL")) {
            this.side = new Side(Side.SELL);
        } else {
            throw (new Exception());
        }

        if (timeInForce.equals("DAY")) {
            this.timeInForce = new TimeInForce(TimeInForce.DAY);
        } else if (timeInForce.equals("GOOD_TILL_DATE")) {
            this.timeInForce = new TimeInForce(TimeInForce.GOOD_TILL_DATE);
        } else if (timeInForce.equals("FILL_OR_KILL")) {
            this.timeInForce = new TimeInForce(TimeInForce.FILL_OR_KILL);
        } else if (timeInForce.equals("IMMEDIATE_OR_CANCEL")) {
            this.timeInForce = new TimeInForce(TimeInForce.IMMEDIATE_OR_CANCEL);
        } else {
            throw (new Exception());
        }

        this.symbol = new Symbol(symbol);
        this.orderQty = new OrderQty(orderQty);
        this.price = new Price(price);

        this.clOrdID = new ClOrdID((new Integer(CURRENT_ID++)).toString());
    }

    public Order(String cancelID) {
        this.cancelID = new OrigClOrdID(cancelID);
        this.clOrdID = new ClOrdID((new Integer(CURRENT_ID++)).toString());
    }

}
