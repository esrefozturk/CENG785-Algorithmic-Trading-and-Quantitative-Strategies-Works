import quickfix.*;
import quickfix.MessageFactory;
import quickfix.field.*;
import quickfix.fix42.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;
import java.util.TimeZone;

/**
 * Created by esrefozturk on 05/11/2016.
 */
public class OEMSRunner {

    public Application oems;
    public SessionSettings oemsSettings;
    public MessageFactory oemsMessageFactory;
    public FileLogFactory oemsFileLogFactory;
    public FileStoreFactory oemsFileStoreFactory;
    public SocketInitiator oemsSocketInitiator;

    public Session session;


    public OEMSRunner(String settingsFileName) throws Exception {
        oems = new OEMSApplication("OEMS");
        oemsSettings = new SessionSettings(settingsFileName);
        oemsMessageFactory = new DefaultMessageFactory();
        oemsFileLogFactory = new FileLogFactory(oemsSettings);
        oemsFileStoreFactory = new FileStoreFactory(oemsSettings);
        oemsSocketInitiator = new SocketInitiator(oems, oemsFileStoreFactory, oemsSettings, oemsFileLogFactory, oemsMessageFactory);
    }

    public static void main(String[] argz) throws Exception {

        HashMap<String, Order> orders = new HashMap<String, Order>();

        TimeZone.setDefault(TimeZone.getTimeZone("Turkey"));

        OEMSRunner oemsRunner = new OEMSRunner("OEMSSettings.txt");

        oemsRunner.start();

        oemsRunner.session.logon();


        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }


        String prompt = "FIXME> ";
        String line;
        String[] args;
        Symbol aSymbol;
        MarketDepth aDepth;
        Side aSide;
        TimeInForce aTimeInForce;
        OrderQty aOrderQty;

        while (true) {
            System.out.print(prompt);
            Scanner reader = new Scanner(System.in);
            line = reader.nextLine();
            args = line.split(" ");
            if (args[0].equals("exit")) {
                System.out.println("BYE");
                break;
            } else if (args[0].equals("help")) {
                System.out.println("TopOfBook <Symbol>");
                System.out.println("DepthOfBook <Symbol> <Depth>");
                System.out.println("Order <Type> <Symbol> <Side> <TimeInForce> <Quantity> <Price>");
                System.out.println("Status <Id> <Symbol> <Side>");
                System.out.println("Cancel <OrigId> <Id> <Symbol> <Side>");
                System.out.println("Replace <Type> <OrigId> <Id> <Symbol> <Side>");
            } else if (args[0].equals("TopOfBook")) {
                if (args.length < 3) {
                    System.out.println("TopOfBook <Symbol>");
                }
                aSymbol = new Symbol(args[1]);
                aDepth = new MarketDepth(1);
                oemsRunner.sendMarketDataRequest(aSymbol, aDepth);
            } else if (args[0].equals("DepthOfBook")) {
                if (args.length < 3) {
                    System.out.println("TopOfBook <Symbol>");
                    continue;
                }
                aSymbol = new Symbol(args[1]);
                aDepth = new MarketDepth(Integer.parseInt(args[2]));
                oemsRunner.sendMarketDataRequest(aSymbol, aDepth);
            } else if (args[0].equals("Order")) {

                try {
                    Order order = new Order(args[1], args[2], args[3], args[4], Double.parseDouble(args[5]), Double.parseDouble(args[6]));
                    oemsRunner.sendNewOrderSingle(order.ordType, order.symbol, order.side, order.orderQty, order.timeInForce, order.price);

                    orders.put(order.clOrdID.getValue().toString(), order);
                } catch (Exception e) {
                    System.out.println("Order <Type> <Symbol> <Side> <TimeInForce> <Quantity> <Price>");
                }
                // Order MARKET IBM SELL GOOD_TILL_DATE 1 1


            } else if (args[0].equals("List")) {
                for (int i = 0; i < orders.size(); i++) {
                    Order order = orders.get((new Integer(i + 1)).toString());
                    System.out.println(order.clOrdID.getValue() + " " + order.symbol.getValue() + " " + order.side.getValue());
                }
            } else if (args[0].equals("TWAP")) {
                try {
                    Order order = new Order(args[1], args[2], args[3], args[4], Double.parseDouble(args[5]), Double.parseDouble(args[6]));
                    oemsRunner.sendNewOrderSingle(order.ordType, order.symbol, order.side, order.orderQty, order.timeInForce, order.price);

                    orders.put(order.clOrdID.getValue().toString(), order);
                    oemsRunner.sendTWAPOrder(orders, order.ordType, order.symbol, order.side, order.orderQty, order.timeInForce, order.price, Long.parseLong(args[7]), Integer.parseInt(args[8]));
                } catch (Exception e) {
                    System.out.println("Order <Type> <Symbol> <Side> <TimeInForce> <Quantity> <Price> <Interval> <Count>");
                }
            }


        }


        oemsRunner.session.logout();
        oemsRunner.stop();


    }

    public void sendMarketDataRequest(Symbol aSymbol, MarketDepth aDepth) {
        this.session.send(this.createMarketDataRequest(aSymbol, aDepth));
    }

    public void sendNewOrderSingle(OrdType aOrdType, Symbol aSymbol, Side aSide, OrderQty aOrderQty, TimeInForce aTimeInForce, Price aPrice) {

        this.session.send(this.createNewOrderSingle(aOrdType, aSymbol, aSide, aOrderQty, aTimeInForce, aPrice));
    }

    public void sendOrderStatusRequest(ClOrdID aClOrdID, Symbol aSymbol, Side aSide) {
        this.session.send(this.createOrderStatusRequest(aClOrdID, aSymbol, aSide));
    }

    public void sendOrderCancelRequest(OrigClOrdID aOrigClOrdID, ClOrdID aClOrdID, Symbol aSymbol, Side aSide) {
        this.session.send(this.createOrderCancelRequest(aOrigClOrdID, aClOrdID, aSymbol, aSide));
    }

    public void sendOrderCancelReplaceRequest(OrigClOrdID aOrigClOrdID, ClOrdID aClOrdID, Symbol aSymbol, Side aSide) {
        this.session.send(this.createOrderCancelReplaceRequest(aOrigClOrdID, aClOrdID, aSymbol, aSide));
    }


    public void start() throws ConfigError {
        oemsSocketInitiator.start();
        SessionID sessionId = oemsSocketInitiator.getSessions().get(0);
        session = Session.lookupSession(sessionId);

    }

    public void stop() {
        System.out.println("OEMS Runner: stop");

        oemsSocketInitiator.stop();
    }

    public MarketDataRequest createMarketDataRequest(Symbol aSymbol, MarketDepth aDepth) {
        MarketDataRequest message = new MarketDataRequest();

        MDReqID mdReqID = new MDReqID("id");
        SubscriptionRequestType subscriptionRequestType = new SubscriptionRequestType(SubscriptionRequestType.SNAPSHOT);
        MarketDepth marketDepth = aDepth;

        MarketDataRequest.NoMDEntryTypes noMDEntryTypes = new MarketDataRequest.NoMDEntryTypes();
        MDEntryType mdEntryType = new MDEntryType(MDEntryType.BID);
        Symbol symbol = aSymbol;
        MarketDataRequest.NoRelatedSym noRelatedSym = new MarketDataRequest.NoRelatedSym();
        noRelatedSym.set(symbol);

        noMDEntryTypes.set(mdEntryType);

        message.set(mdReqID);
        message.set(subscriptionRequestType);
        message.set(marketDepth);
        message.addGroup(noMDEntryTypes);
        message.addGroup(noRelatedSym);

        return message;


    }

    public NewOrderSingle createNewOrderSingle(OrdType aOrdType, Symbol aSymbol, Side aSide, OrderQty aOrderQty, TimeInForce aTimeInForce, Price aPrice) {
        NewOrderSingle message = new NewOrderSingle();

        ClOrdID fClOrdID = new ClOrdID("OEMS-Order0001-30.10.2015-13:59:00");
        HandlInst fHandlInst = new HandlInst(HandlInst.AUTOMATED_EXECUTION_ORDER_PUBLIC);
        Symbol fSymbol = aSymbol;
        Side fSide = aSide;
        TransactTime fTransactTime = new TransactTime(new Date());
        OrderQty fOrderQty = aOrderQty;
        OrdType fOrdType = aOrdType;
        Price fPrice = aPrice;
        StopPx fStopPx = new StopPx(3.50);
        TimeInForce fTimeInForce = aTimeInForce;


        message.set(fClOrdID);
        message.set(fHandlInst);
        message.set(fSymbol);
        message.set(fSide);
        message.set(fTransactTime);
        message.set(fOrderQty);
        message.set(fOrdType);
        message.set(fPrice);
        message.set(fStopPx);
        message.set(fTimeInForce);
        return message;
    }

    public OrderStatusRequest createOrderStatusRequest(ClOrdID aClOrdID, Symbol aSymbol, Side aSide) {
        OrderStatusRequest message = new OrderStatusRequest();

        ClOrdID clOrdID = aClOrdID;
        Symbol symbol = aSymbol;
        Side side = aSide;

        message.set(clOrdID);
        message.set(symbol);
        message.set(side);

        return message;
    }

    public OrderCancelRequest createOrderCancelRequest(OrigClOrdID aOrigClOrdID, ClOrdID aClOrdID, Symbol aSymbol, Side aSide) {
        OrderCancelRequest message = new OrderCancelRequest();

        OrigClOrdID origClOrdID = aOrigClOrdID;
        ClOrdID clOrdID = aClOrdID;
        Symbol symbol = aSymbol;
        Side side = aSide;
        TransactTime transactTime = new TransactTime(new Date());

        message.set(origClOrdID);
        message.set(clOrdID);
        message.set(symbol);
        message.set(side);
        message.set(transactTime);


        return message;
    }

    public OrderCancelReplaceRequest createOrderCancelReplaceRequest(OrigClOrdID aOrigClOrdID, ClOrdID aClOrdID, Symbol aSymbol, Side aSide) {

        OrderCancelReplaceRequest message = new OrderCancelReplaceRequest();

        OrigClOrdID origClOrdID = aOrigClOrdID;
        ClOrdID clOrdID = aClOrdID;
        HandlInst handlInst = new HandlInst(HandlInst.AUTOMATED_EXECUTION_ORDER_PUBLIC);
        Symbol symbol = aSymbol;
        Side side = aSide;
        TransactTime transactTime = new TransactTime(new Date());
        OrdType ordType = new OrdType(OrdType.LIMIT);

        message.set(origClOrdID);
        message.set(clOrdID);
        message.set(handlInst);
        message.set(symbol);
        message.set(side);
        message.set(transactTime);
        message.set(ordType);


        return message;
    }

    public void sendTWAPOrder(HashMap<String, Order> orders, final OrdType aOrdType, final Symbol aSymbol, final Side aSide, final OrderQty aOrderQty, final TimeInForce aTimeInForce, Price aPrice, final long interval, final int count) {
        for (int i = 0; i < count; i++) {
            final int j = i;
            new Thread(new Runnable() {
                public void run() {
                    try {
                        Thread.sleep((j + 1) * interval * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Order order = new Order(aOrdType, aSymbol, aSide, aTimeInForce, aOrderQty, aPrice);
                    sendNewOrderSingle(order.ordType, order.symbol, order.side, order.orderQty, order.timeInForce, order.price);

                    orders.put(order.clOrdID.getValue().toString(), order);

                }
            }).start();
        }
    }
}
