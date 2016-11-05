import quickfix.*;

/**
 * Created by esrefozturk on 05/11/2016.
 */
public class OEMSApplication implements Application {

    public String name;

    public OEMSApplication(String aName) {
        name = aName;
    }


    public void onCreate(SessionID sessionId) {

    }

    public void onLogon(SessionID sessionId) {

    }

    public void onLogout(SessionID sessionId) {

    }

    public void toAdmin(Message message, SessionID sessionId) {

    }

    public void fromAdmin(Message message, SessionID sessionId) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon {

    }

    public void toApp(Message message, SessionID sessionId) throws DoNotSend {

    }

    public void fromApp(Message message, SessionID sessionId) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {

    }
}
