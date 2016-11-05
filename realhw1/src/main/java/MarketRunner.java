/**
 * Created by esrefozturk on 05/11/2016.
 */

import quickfix.*;

import java.util.TimeZone;


public class MarketRunner {


    public Application market;
    public SessionSettings marketSettings;
    public MessageFactory marketMessageFactory;
    public FileLogFactory marketFileLogFactory;
    public FileStoreFactory marketFileStoreFactory;
    public ThreadedSocketAcceptor marketSocketAcceptor;


    public MarketRunner(String settingsFileName) throws Exception {

        System.out.println("Market Runner: constructor");


        market = new MarketApplication("MARKET");
        marketSettings = new SessionSettings(settingsFileName);
        marketMessageFactory = new DefaultMessageFactory();
        marketFileLogFactory = new FileLogFactory(marketSettings);
        marketFileStoreFactory = new FileStoreFactory(marketSettings);
        marketSocketAcceptor = new ThreadedSocketAcceptor(market, marketFileStoreFactory, marketSettings, marketFileLogFactory, marketMessageFactory);

    }

    public static void main(String[] args) throws Exception {
        TimeZone.setDefault(TimeZone.getTimeZone("Turkey"));

        MarketRunner marketRunner = new MarketRunner("MarketSettings.txt");

        marketRunner.start();
    }

    public void start() throws ConfigError {
        System.out.println("Market Runner: start");

        marketSocketAcceptor.start();
    }

    public void stop() {
        System.out.println("Market Runner: stop");

        marketSocketAcceptor.stop();
    }

}

