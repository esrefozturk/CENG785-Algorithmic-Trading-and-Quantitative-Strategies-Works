import quickfix.*;

/**
 * Created by esrefozturk on 05/11/2016.
 */
public class OEMSApplication implements Application {

    public String name;


    public OEMSApplication(String aName) {
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
    }

}

