import quickfix.*;
import quickfix.MessageFactory;
import quickfix.field.*;
import quickfix.fix42.*;

import java.util.*;

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


    public OEMSRunner(String settingsFileName,
                      ArrayList<MarketDataSnapshotFullRefresh> marketDataSnapshotFullRefreshes,
                      ArrayList<ExecutionReport> executionReports,
                      ArrayList<DontKnowTrade> dontKnowTrades,
                      ArrayList<OrderCancelReject> orderCancelRejects) throws Exception {
        oems = new OEMSApplication("OEMS", marketDataSnapshotFullRefreshes, executionReports, dontKnowTrades, orderCancelRejects);
        oemsSettings = new SessionSettings(settingsFileName);
        oemsMessageFactory = new DefaultMessageFactory();
        oemsFileLogFactory = new FileLogFactory(oemsSettings);
        oemsFileStoreFactory = new FileStoreFactory(oemsSettings);
        oemsSocketInitiator = new SocketInitiator(oems, oemsFileStoreFactory, oemsSettings, oemsFileLogFactory, oemsMessageFactory);
    }

    public static void main(String[] argz) throws Exception {

        HashMap<String, Order> orders = new HashMap<String, Order>();
        ArrayList<MarketDataSnapshotFullRefresh> marketDataSnapshotFullRefreshes = new ArrayList<MarketDataSnapshotFullRefresh>();
        ArrayList<ExecutionReport> executionReports = new ArrayList<ExecutionReport>();
        ArrayList<DontKnowTrade> dontKnowTrades = new ArrayList<DontKnowTrade>();
        ArrayList<OrderCancelReject> orderCancelRejects = new ArrayList<OrderCancelReject>();

        TimeZone.setDefault(TimeZone.getTimeZone("Turkey"));

        OEMSRunner oemsRunner = new OEMSRunner("OEMSSettings.txt", marketDataSnapshotFullRefreshes, executionReports, dontKnowTrades, orderCancelRejects);

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
            if (args[0].equals("Exit")) {
                System.out.println("BYE");
                break;
            } else if (args[0].equals("help")) {
                System.out.println("Commands:");
                System.out.println("\t\t\tTopOfBook <Symbol>");
                System.out.println("\t\t\tDepthOfBook <Symbol> <Depth>");
                System.out.println("\t\t\tOrder <Type> <Symbol> <Side> <TimeInForce> <Quantity> <Price>");
                System.out.println("\t\t\tStatus <Id>");
                System.out.println("\t\t\tCancel <OrigId> <Symbol> <Side>");
                System.out.println("\t\t\tReplace <OrigId> <Type> <Symbol> <Side> <TimeInForce> <Quantity> <Price>");
                System.out.println("\t\t\tTWAP <Type> <Symbol> <Side> <TimeInForce> <Quantity> <Price> <Interval> <Count>");
                System.out.println("\t\t\tIncomings");
                System.out.println("\t\t\tExit");


            } else if (args[0].equals("TopOfBook")) {
                try {
                    aSymbol = new Symbol(args[1]);
                    aDepth = new MarketDepth(1);
                    oemsRunner.sendMarketDataRequest(aSymbol, aDepth);
                } catch (Exception e) {
                    System.out.println("Invalid request");
                }

            } else if (args[0].equals("DepthOfBook")) {
                try {
                    aSymbol = new Symbol(args[1]);
                    aDepth = new MarketDepth(Integer.parseInt(args[2]));
                    oemsRunner.sendMarketDataRequest(aSymbol, aDepth);
                } catch (Exception e) {
                    System.out.println("Invalid request");
                }

            } else if (args[0].equals("Order")) {

                try {
                    Order order = new Order(args[1], args[2], args[3], args[4], Double.parseDouble(args[5]), Double.parseDouble(args[6]));
                    oemsRunner.sendNewOrderSingle(order);
                    orders.put(order.clOrdID.getValue().toString(), order);
                } catch (Exception e) {
                    System.out.println("Invalid request");
                }


            } else if (args[0].equals("List")) {
                for (int i = 0; i < orders.size(); i++) {
                    Order order = orders.get((new Integer(i + 1)).toString());
                    System.out.print("Id: " + order.clOrdID.getValue() + ",");
                    if (order.cancelID != null) {
                        System.out.print("Cancel/Replace ID: " + order.cancelID.getValue() + ",");
                    } else {
                        if (order.ordType.getValue() == OrdType.MARKET) {
                            System.out.print("Type: MARKET,");
                        } else if (order.ordType.getValue() == OrdType.LIMIT) {
                            System.out.print("Type: LIMIT,");
                        }

                        System.out.print("Symbol: " + order.symbol.getValue() + ",");

                        if (order.side.getValue() == Side.SELL) {
                            System.out.print("Side: SELL,");
                        } else if (order.side.getValue() == Side.BUY) {
                            System.out.print("Side: BUY,");
                        }

                        if (order.timeInForce.getValue() == TimeInForce.DAY) {
                            System.out.print("Time In Force: DAY,");
                        } else if (order.timeInForce.getValue() == TimeInForce.GOOD_TILL_DATE) {
                            System.out.print("Time In Force: GOOD_TILL_DATE,");
                        } else if (order.timeInForce.getValue() == TimeInForce.FILL_OR_KILL) {
                            System.out.print("Time In Force: FILL_OR_KILL,");
                        } else if (order.timeInForce.getValue() == TimeInForce.IMMEDIATE_OR_CANCEL) {
                            System.out.print("Time In Force: IMMEDIATE_OR_CANCEL,");
                        }


                        System.out.print("Quantity: " + order.orderQty.getValue() + ",");
                        System.out.print("Price: " + order.price.getValue() + ",");
                    }
                    System.out.println();

                }
            } else if (args[0].equals("TWAP")) {
                try {
                    Order order = new Order(args[1], args[2], args[3], args[4], Double.parseDouble(args[5]), Double.parseDouble(args[6]));
                    oemsRunner.sendNewOrderSingle(order);
                    orders.put(order.clOrdID.getValue().toString(), order);
                    oemsRunner.sendTWAPOrder(orders, order, Long.parseLong(args[7]), Integer.parseInt(args[8]) - 1);
                } catch (Exception e) {
                    System.out.println("Invalid request");
                }
            } else if (args[0].equals("Status")) {
                try {
                    Order order = orders.get(args[1]);
                    oemsRunner.sendOrderStatusRequest(order);
                } catch (Exception e) {
                    System.out.println("Invalid request");
                }
            } else if (args[0].equals("Cancel")) {
                try {
                    Order origOrder = orders.get(args[1]);
                    Order order = new Order(args[1]);
                    order.symbol = origOrder.symbol;
                    order.side = origOrder.side;
                    oemsRunner.sendOrderCancelRequest(order);
                    orders.put(order.clOrdID.getValue().toString(), order);
                } catch (Exception e) {

                    System.out.println("Invalid request");
                }
            } else if (args[0].equals("Modify")) {
                try {
                    Order order = new Order(args[2], args[3], args[4], args[5], Double.parseDouble(args[6]), Double.parseDouble(args[7]));
                    order.cancelID = new OrigClOrdID(args[1]);
                    oemsRunner.sendOrderCancelReplaceRequest(order);
                    orders.put(order.clOrdID.getValue().toString(), order);
                } catch (Exception e) {
                    System.out.println("Invalid request");
                }
            } else if (args[0].equals("Incomings")) {

                while (marketDataSnapshotFullRefreshes.size() > 0) {
                    MarketDataSnapshotFullRefresh marketDataSnapshotFullRefresh = marketDataSnapshotFullRefreshes.get(0);

                    NoMDEntries noMDEntries = new NoMDEntries();
                    marketDataSnapshotFullRefresh.get(noMDEntries);
                    for (int i = 1; i <= noMDEntries.getValue(); i++) {
                        MarketDataSnapshotFullRefresh.NoMDEntries group = new MarketDataSnapshotFullRefresh.NoMDEntries();
                        MDEntryType mdEntryType = new MDEntryType();
                        MDEntryPx mdEntryPx = new MDEntryPx();
                        marketDataSnapshotFullRefresh.getGroup(i, group);
                        group.get(mdEntryType);
                        group.get(mdEntryPx);
                        if (mdEntryType.getValue() == MDEntryType.BID) {
                            System.out.println("MarketDataSnapshotFullRefresh => Type=BID,Price=" + mdEntryPx.getValue());
                        } else if (mdEntryType.getValue() == MDEntryType.OFFER) {
                            System.out.println("MarketDataSnapshotFullRefresh => Type=OFFER,Price=" + mdEntryPx.getValue());
                        }

                    }


                    marketDataSnapshotFullRefreshes.remove(0);
                }

                while (executionReports.size() > 0) {
                    ExecutionReport executionReport = executionReports.get(0);

                    if (executionReport.getOrdStatus().getValue() == OrdStatus.STOPPED) {
                        System.out.println("OrderCancelReject => Id=" + executionReport.getClOrdID().getValue() + ",Status=STOPPED");
                    } else if (executionReport.getOrdStatus().getValue() == OrdStatus.CANCELED) {
                        System.out.println("OrderCancelReject => Id=" + executionReport.getClOrdID().getValue() + ",Status=CANCELLED");
                    } else if (executionReport.getOrdStatus().getValue() == OrdStatus.FILLED) {
                        System.out.println("OrderCancelReject => Id=" + executionReport.getClOrdID().getValue() + ",Status=FILLED");
                    } else {
                        System.out.println("OrderCancelReject => Id=" + executionReport.getClOrdID().getValue() + ",Status=" + executionReport.getOrdStatus().getValue());
                    }
                    executionReports.remove(0);
                }

                while (dontKnowTrades.size() > 0) {
                    DontKnowTrade dontKnowTrade = dontKnowTrades.get(0);
                    if (dontKnowTrade.getDKReason().getValue() == DKReason.NO_MATCHING_ORDER) {
                        System.out.println("DontKnowTrade: => Id=" + dontKnowTrade.getOrderID() + ",Status=NO_MATCHING_ORDER");
                    } else if (dontKnowTrade.getDKReason().getValue() == DKReason.UNKNOWN_SYMBOL) {
                        System.out.println("DontKnowTrade: => Id=" + dontKnowTrade.getOrderID() + ",Status=UNKNOWN_SYMBOL");
                    } else {
                        System.out.println("DontKnowTrade: => Id=" + dontKnowTrade.getOrderID().getValue() + ",Status=" + dontKnowTrade.getDKReason().getValue());
                    }


                    dontKnowTrades.remove(0);
                }

                while (orderCancelRejects.size() > 0) {
                    OrderCancelReject orderCancelReject = orderCancelRejects.get(0);
                    if (orderCancelReject.getOrdStatus().getValue() == OrdStatus.STOPPED) {
                        System.out.println("OrderCancelReject => Id=" + orderCancelReject.getClOrdID().getValue() + ",Status=STOPPED");
                    } else if (orderCancelReject.getOrdStatus().getValue() == OrdStatus.CANCELED) {
                        System.out.println("OrderCancelReject => Id=" + orderCancelReject.getClOrdID().getValue() + ",Status=CANCELLED");
                    } else {
                        System.out.println("OrderCancelReject => Id=" + orderCancelReject.getClOrdID().getValue() + ",Status=" + orderCancelReject.getOrdStatus().getValue());
                    }

                    orderCancelRejects.remove(0);
                }

            }


        }


        oemsRunner.session.logout();
        oemsRunner.stop();


    }

    public void sendMarketDataRequest(Symbol aSymbol, MarketDepth aDepth) {
        this.session.send(this.createMarketDataRequest(aSymbol, aDepth));
    }

    public void sendNewOrderSingle(Order order) {

        this.session.send(this.createNewOrderSingle(order));
    }

    public void sendOrderStatusRequest(Order order) {
        this.session.send(this.createOrderStatusRequest(order));
    }

    public void sendOrderCancelRequest(Order order) {
        this.session.send(this.createOrderCancelRequest(order));
    }

    public void sendOrderCancelReplaceRequest(Order order) {
        this.session.send(this.createOrderCancelReplaceRequest(order));
    }


    public void start() throws ConfigError {
        oemsSocketInitiator.start();
        SessionID sessionId = oemsSocketInitiator.getSessions().get(0);
        session = Session.lookupSession(sessionId);

    }

    public void stop() {

        oemsSocketInitiator.stop();
    }

    public MarketDataRequest createMarketDataRequest(Symbol aSymbol, MarketDepth aDepth) {
        MarketDataRequest message = new MarketDataRequest();

        MDReqID mdReqID = new MDReqID("id");
        SubscriptionRequestType subscriptionRequestType = new SubscriptionRequestType(SubscriptionRequestType.SNAPSHOT);
        MarketDepth marketDepth = aDepth;

        MarketDataRequest.NoMDEntryTypes noMDEntryTypesBID = new MarketDataRequest.NoMDEntryTypes();
        MarketDataRequest.NoMDEntryTypes noMDEntryTypesOFFER = new MarketDataRequest.NoMDEntryTypes();

        MDEntryType mdEntryTypeBID = new MDEntryType(MDEntryType.BID);
        MDEntryType mdEntryTypeOFFER = new MDEntryType(MDEntryType.OFFER);


        Symbol symbol = aSymbol;
        MarketDataRequest.NoRelatedSym noRelatedSym = new MarketDataRequest.NoRelatedSym();
        noRelatedSym.set(symbol);

        noMDEntryTypesBID.set(mdEntryTypeBID);
        noMDEntryTypesOFFER.set(mdEntryTypeOFFER);


        message.set(mdReqID);
        message.set(subscriptionRequestType);
        message.set(marketDepth);
        message.addGroup(noMDEntryTypesBID);
        message.addGroup(noMDEntryTypesOFFER);
        message.addGroup(noRelatedSym);

        return message;


    }

    public NewOrderSingle createNewOrderSingle(Order order) {
        NewOrderSingle message = new NewOrderSingle();

        ClOrdID fClOrdID = order.clOrdID;
        HandlInst fHandlInst = new HandlInst(HandlInst.AUTOMATED_EXECUTION_ORDER_PUBLIC);
        Symbol symbol = order.symbol;
        Side side = order.side;
        TransactTime transactTime = new TransactTime(new Date());
        OrderQty orderQty = order.orderQty;
        OrdType ordType = order.ordType;
        Price price = order.price;
        StopPx stopPx = new StopPx(3.50);
        TimeInForce timeInForce = order.timeInForce;


        message.set(fClOrdID);
        message.set(fHandlInst);
        message.set(symbol);
        message.set(side);
        message.set(transactTime);
        message.set(orderQty);
        message.set(ordType);
        message.set(price);
        message.set(stopPx);
        message.set(timeInForce);
        return message;
    }

    public OrderStatusRequest createOrderStatusRequest(Order order) {
        OrderStatusRequest message = new OrderStatusRequest();

        ClOrdID clOrdID = order.clOrdID;
        Symbol symbol = order.symbol;
        Side side = order.side;

        message.set(clOrdID);
        message.set(symbol);
        message.set(side);

        return message;
    }

    public OrderCancelRequest createOrderCancelRequest(Order order) {
        OrderCancelRequest message = new OrderCancelRequest();

        OrigClOrdID origClOrdID = order.cancelID;
        ClOrdID clOrdID = order.clOrdID;
        Symbol symbol = order.symbol;
        Side side = order.side;
        TransactTime transactTime = new TransactTime(new Date());

        message.set(origClOrdID);
        message.set(clOrdID);
        message.set(symbol);
        message.set(side);
        message.set(transactTime);


        return message;
    }

    public OrderCancelReplaceRequest createOrderCancelReplaceRequest(Order order) {

        OrderCancelReplaceRequest message = new OrderCancelReplaceRequest();

        OrigClOrdID origClOrdID = order.cancelID;
        ClOrdID clOrdID = order.clOrdID;
        HandlInst handlInst = new HandlInst(HandlInst.AUTOMATED_EXECUTION_ORDER_PUBLIC);
        Symbol symbol = order.symbol;
        Side side = order.side;
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

    public void sendTWAPOrder(HashMap<String, Order> orders, Order order, final long interval, final int count) {
        for (int i = 0; i < count; i++) {
            final int j = i;
            new Thread(new Runnable() {
                public void run() {
                    try {
                        Thread.sleep((j + 1) * interval * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Order newOrder = new Order(order.ordType, order.symbol, order.side, order.timeInForce, order.orderQty, order.price);
                    sendNewOrderSingle(newOrder);

                    orders.put(newOrder.clOrdID.getValue().toString(), newOrder);

                }
            }).start();
        }
    }
}
