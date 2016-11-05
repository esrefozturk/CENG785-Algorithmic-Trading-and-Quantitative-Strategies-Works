import quickfix.*;

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

    public static void main(String[] args) throws Exception {
        TimeZone.setDefault(TimeZone.getTimeZone("Turkey"));

        OEMSRunner oemsRunner = new OEMSRunner("OEMSSettings.txt");

        oemsRunner.start();

        oemsRunner.session.logon();

        oemsRunner.session.logout();

        oemsRunner.stop();
    }
}
