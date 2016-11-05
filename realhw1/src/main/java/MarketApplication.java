/**
 * Created by esrefozturk on 05/11/2016.
 */


import quickfix.*;
import quickfix.field.*;
import quickfix.fix42.MessageCracker;


public class MarketApplication extends MessageCracker implements Application {

    public String name;


    public MarketApplication(String aName) {
        name = aName;

        System.out.println(name + " : constructor");
    }


    public void onCreate(SessionID sessionId) {
        System.out.println(name + " : onCreate");
        System.out.println("  SessionID : " + sessionId);
    }


    public void onLogon(SessionID sessionId) {
        System.out.println(name + " : onLogon");
        System.out.println("  SessionID : " + sessionId);
    }


    public void onLogout(SessionID sessionId) {
        System.out.println(name + " : onLogout");
        System.out.println("  SessionID : " + sessionId);
    }


    public void toAdmin(Message message, SessionID sessionId) {
        System.out.println(name + " : toAdmin");
        System.out.println("  SessionID : " + sessionId);
        System.out.println("  Message   : " + message);
    }


    public void fromAdmin(Message message, SessionID sessionId) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon {
        System.out.println(name + " : fromAdmin");
        System.out.println("  SessionID : " + sessionId);
        System.out.println("  Message   : " + message);
    }


    public void toApp(Message message, SessionID sessionId) throws DoNotSend {
        System.out.println(name + " : toApp");
        System.out.println("  SessionID : " + sessionId);
        System.out.println("  Message   : " + message);
    }


    public void fromApp(Message message, SessionID sessionId) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
        System.out.println(name + " : fromApp");
        System.out.println("  SessionID : " + sessionId);
        System.out.println("  Message   : " + message);

        crack(message, sessionId);
    }


    public void onMessage(quickfix.fix42.MarketDataRequest message, SessionID sessionId) {
        System.out.println(name + " : MarketDataRequest");
    }


    public void onMessage(quickfix.fix42.NewOrderSingle message, SessionID sessionId) throws FieldNotFound {

        System.out.println(name + " : NewOrderSingle");


        ClOrdID fClOrdID = new ClOrdID();
        Symbol fSymbol = new Symbol();
        Side fSide = new Side();
        TransactTime fTransactTime = new TransactTime();
        OrderQty fOrderQty = new OrderQty();
        OrdType fOrdType = new OrdType();
        Price fPrice = new Price();
        StopPx fStopPx = new StopPx();
        TimeInForce fTimeInForce = new TimeInForce();

        message.get(fClOrdID);
        message.get(fSymbol);
        message.get(fSide);
        message.get(fTransactTime);
        message.get(fOrderQty);
        message.get(fOrdType);
        message.get(fPrice);
        message.get(fStopPx);
        message.get(fTimeInForce);


        System.out.println(fClOrdID.toString());
        System.out.println(fSymbol.toString());
        System.out.println(fSide.toString());
        System.out.println(fTransactTime.toString());
        System.out.println(fOrderQty.toString());
        System.out.println(fOrdType.toString());
        System.out.println(fPrice.toString());
        System.out.println(fStopPx.toString());
        System.out.println(fTimeInForce.toString());

    }


    public void onMessage(quickfix.fix42.OrderStatusRequest message, SessionID sessionId) {
        System.out.println(name + " : OrderStatusRequest");
    }


    public void onMessage(quickfix.fix42.OrderCancelRequest message, SessionID sessionId) {
        System.out.println(name + " : OrderCancelRequest");
    }


    public void onMessage(quickfix.fix42.OrderCancelReplaceRequest message, SessionID sessionId) {
        System.out.println(name + " : OrderCancelReplaceRequest");
    }

}

