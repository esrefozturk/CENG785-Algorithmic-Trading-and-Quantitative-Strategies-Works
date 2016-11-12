import quickfix.field.OrdType;
import quickfix.field.Side;
import quickfix.field.TimeInForce;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by esrefozturk on 12/11/2016.
 */
public class RealFrame extends JFrame {

    public RealFrame(OEMSRunner aoemsRunner) {
        final OEMSRunner oemsRunner = aoemsRunner;
        setSize(600, 400);
        setLayout(new FlowLayout());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        JButton marketDataRequestButton = new JButton("Market Data Request");
        marketDataRequestButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                oemsRunner.sendMarketDataRequest();
            }
        });

        JButton newOrderSingleButton = new JButton("Market Data Request");
        newOrderSingleButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                oemsRunner.sendNewOrderSingle();
            }
        });

        JButton orderStatusRequestButton = new JButton("Market Data Request");
        orderStatusRequestButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                oemsRunner.sendOrderStatusRequest();
            }
        });

        JButton orderCancelRequestButton = new JButton("Market Data Request");
        orderCancelRequestButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                oemsRunner.sendOrderCancelRequest();
            }
        });

        JButton orderCancelReplaceRequestButton = new JButton("Market Data Request");
        orderCancelReplaceRequestButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                oemsRunner.sendOrderCancelReplaceRequest();
            }
        });

        JComboBox<SideComboItem> sideJComboBox = new JComboBox<SideComboItem>();
        sideJComboBox.addItem(new SideComboItem(Side.BUY));
        sideJComboBox.addItem(new SideComboItem(Side.SELL));

        JComboBox<TimeInForceComboItem> timeInForceComboBox = new JComboBox<TimeInForceComboItem>();
        timeInForceComboBox.addItem(new TimeInForceComboItem(TimeInForce.DAY));
        timeInForceComboBox.addItem(new TimeInForceComboItem(TimeInForce.GOOD_TILL_DATE));
        timeInForceComboBox.addItem(new TimeInForceComboItem(TimeInForce.FILL_OR_KILL));
        timeInForceComboBox.addItem(new TimeInForceComboItem(TimeInForce.IMMEDIATE_OR_CANCEL));

        JComboBox<OrdTypeComboItem> ordTypeComboBox = new JComboBox<OrdTypeComboItem>();
        ordTypeComboBox.addItem(new OrdTypeComboItem(OrdType.MARKET));
        ordTypeComboBox.addItem(new OrdTypeComboItem(OrdType.LIMIT));


        add(marketDataRequestButton);
        add(newOrderSingleButton);
        add(orderStatusRequestButton);
        add(orderCancelRequestButton);
        add(orderCancelReplaceRequestButton);

        add(sideJComboBox);
        add(timeInForceComboBox);
        add(ordTypeComboBox);


        setVisible(true);
    }


}
