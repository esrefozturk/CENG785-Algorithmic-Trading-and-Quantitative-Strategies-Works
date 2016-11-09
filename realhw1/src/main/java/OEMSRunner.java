import quickfix.*;
import quickfix.MessageFactory;
import quickfix.field.*;
import quickfix.fix42.*;

import java.util.Date;
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

    public void start() throws ConfigError {
        oemsSocketInitiator.start();
        SessionID sessionId = oemsSocketInitiator.getSessions().get(0);
        session = Session.lookupSession(sessionId);

    }

    public void stop() {
        System.out.println("OEMS Runner: stop");

        oemsSocketInitiator.stop();
    }


    public MarketDataRequest createMarketDataRequest() {
        MarketDataRequest message = new MarketDataRequest();

        MDReqID mdReqID = new MDReqID("id");
        SubscriptionRequestType subscriptionRequestType = new SubscriptionRequestType(SubscriptionRequestType.SNAPSHOT);
        MarketDepth marketDepth = new MarketDepth(0);

        MarketDataRequest.NoMDEntryTypes noMDEntryTypes = new MarketDataRequest.NoMDEntryTypes();
        MDEntryType mdEntryType = new MDEntryType(MDEntryType.BID);
        Symbol symbol = new Symbol("IBM");
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

    public NewOrderSingle createNewOrderSingle() {
        NewOrderSingle message = new NewOrderSingle();

        ClOrdID fClOrdID = new ClOrdID("OEMS-Order0001-30.10.2015-13:59:00");
        HandlInst fHandlInst = new HandlInst(HandlInst.AUTOMATED_EXECUTION_ORDER_PUBLIC);
        Symbol fSymbol = new Symbol("IBM");
        Side fSide = new Side(Side.BUY);
        TransactTime fTransactTime = new TransactTime(new Date());
        OrderQty fOrderQty = new OrderQty(500.0);
        OrdType fOrdType = new OrdType(OrdType.LIMIT);
        Price fPrice = new Price(3.95);
        StopPx fStopPx = new StopPx(3.50);
        TimeInForce fTimeInForce = new TimeInForce(TimeInForce.DAY);


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

    public OrderStatusRequest createOrderStatusRequest() {
        OrderStatusRequest message = new OrderStatusRequest();

        ClOrdID clOrdID = new ClOrdID("esref");
        Symbol symbol = new Symbol("esref");
        Side side = new Side(Side.BUY);

        message.set(clOrdID);
        message.set(symbol);
        message.set(side);

        return message;
    }

    public OrderCancelRequest createOrderCancelRequest() {
        OrderCancelRequest message = new OrderCancelRequest();

        OrigClOrdID origClOrdID = new OrigClOrdID("esref");
        ClOrdID clOrdID = new ClOrdID("esref");
        Symbol symbol = new Symbol("esref");
        Side side = new Side(Side.BUY);
        TransactTime transactTime = new TransactTime(new Date());

        message.set(origClOrdID);
        message.set(clOrdID);
        message.set(symbol);
        message.set(side);
        message.set(transactTime);


        return message;
    }

    public OrderCancelReplaceRequest createOrderCancelReplaceRequest() {

        OrderCancelReplaceRequest message = new OrderCancelReplaceRequest();

        OrigClOrdID origClOrdID = new OrigClOrdID("esref");
        ClOrdID clOrdID = new ClOrdID("esref");
        HandlInst handlInst = new HandlInst(HandlInst.AUTOMATED_EXECUTION_ORDER_PUBLIC);
        Symbol symbol = new Symbol("esref");
        Side side = new Side(Side.BUY);
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


    public static void main(String[] args) throws Exception {
        TimeZone.setDefault(TimeZone.getTimeZone("Turkey"));

        OEMSRunner oemsRunner = new OEMSRunner("OEMSSettings.txt");

        oemsRunner.start();

        oemsRunner.session.logon();
        System.out.println("hebele1");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }
        oemsRunner.session.send(oemsRunner.createOrderCancelReplaceRequest());

        System.out.println("hebele2");


    }
}
