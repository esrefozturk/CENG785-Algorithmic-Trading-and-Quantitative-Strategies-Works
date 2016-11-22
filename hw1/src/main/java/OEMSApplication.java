import quickfix.*;
import quickfix.Message;
import quickfix.fix42.*;
import quickfix.fix42.MessageCracker;

import java.util.ArrayList;

/**
 * Created by esrefozturk on 05/11/2016.
 */
public class OEMSApplication extends MessageCracker implements Application {

    public String name;
    ArrayList<MarketDataSnapshotFullRefresh> marketDataSnapshotFullRefreshes = null;
    ArrayList<ExecutionReport> executionReports = null;
    ArrayList<DontKnowTrade> dontKnowTrades = null;
    ArrayList<OrderCancelReject> orderCancelRejects = null;

    public OEMSApplication(String aName,
                           ArrayList<MarketDataSnapshotFullRefresh> marketDataSnapshotFullRefreshes,
                           ArrayList<ExecutionReport> executionReports,
                           ArrayList<DontKnowTrade> dontKnowTrades,
                           ArrayList<OrderCancelReject> orderCancelRejects) {
        name = aName;
        this.marketDataSnapshotFullRefreshes = marketDataSnapshotFullRefreshes;
        this.executionReports = executionReports;
        this.dontKnowTrades = dontKnowTrades;
        this.orderCancelRejects = orderCancelRejects;


        //System.out.println(name + " : constructor");
    }


    public void onCreate(SessionID sessionId) {
        //System.out.println(name + " : onCreate");
        //System.out.println("  SessionID : " + sessionId);
    }


    public void onLogon(SessionID sessionId) {
        //System.out.println(name + " : onLogon");
        //System.out.println("  SessionID : " + sessionId);
    }

    public void onLogout(SessionID sessionId) {
        //System.out.println(name + " : onLogout");
        //System.out.println("  SessionID : " + sessionId);
    }

    public void toAdmin(Message message, SessionID sessionId) {
        //System.out.println(name + " : toAdmin");
        //System.out.println("  SessionID : " + sessionId);
        //System.out.println("  Message   : " + message);
    }


    public void fromAdmin(Message message, SessionID sessionId) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon {
        //System.out.println(name + " : fromAdmin");
        //System.out.println("  SessionID : " + sessionId);
        //System.out.println("  Message   : " + message);
    }


    public void toApp(Message message, SessionID sessionId) throws DoNotSend {
        //System.out.println(name + " : toApp");
        //System.out.println("  SessionID : " + sessionId);
        //System.out.println("  Message   : " + message);
    }

    public void fromApp(Message message, SessionID sessionId) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
        //System.out.println(name + " : fromApp");
        //System.out.println("  SessionID : " + sessionId);
        //System.out.println("  Message   : " + message);
        crack(message, sessionId);
    }

    public void onMessage(MarketDataSnapshotFullRefresh marketDataSnapshotFullRefresh, SessionID sessionId) throws FieldNotFound {
        this.marketDataSnapshotFullRefreshes.add(marketDataSnapshotFullRefresh);

    }

    public void onMessage(ExecutionReport executionReport, SessionID sessionId) throws FieldNotFound {
        this.executionReports.add(executionReport);

    }

    public void onMessage(DontKnowTrade dontKnowTrade, SessionID sessionId) throws FieldNotFound {
        this.dontKnowTrades.add(dontKnowTrade);

    }

    public void onMessage(OrderCancelReject orderCancelReject, SessionID sessionId) throws FieldNotFound {
        this.orderCancelRejects.add(orderCancelReject);

    }


}

