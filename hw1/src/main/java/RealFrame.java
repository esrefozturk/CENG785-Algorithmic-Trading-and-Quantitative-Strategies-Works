import quickfix.field.OrdType;
import quickfix.field.Side;
import quickfix.field.TimeInForce;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * Created by esrefozturk on 12/11/2016.
 */
public class RealFrame extends JFrame {

    public RealFrame(OEMSRunner aoemsRunner) {
        final OEMSRunner oemsRunner = aoemsRunner;
        setSize(1280, 960);
        setLayout(new FlowLayout());


        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        final JComboBox<SideComboItem> sideJComboBox = new JComboBox<SideComboItem>();
        sideJComboBox.addItem(new SideComboItem(Side.BUY));
        sideJComboBox.addItem(new SideComboItem(Side.SELL));

        final JComboBox<TimeInForceComboItem> timeInForceComboBox = new JComboBox<TimeInForceComboItem>();
        timeInForceComboBox.addItem(new TimeInForceComboItem(TimeInForce.DAY));
        timeInForceComboBox.addItem(new TimeInForceComboItem(TimeInForce.GOOD_TILL_DATE));
        timeInForceComboBox.addItem(new TimeInForceComboItem(TimeInForce.FILL_OR_KILL));
        timeInForceComboBox.addItem(new TimeInForceComboItem(TimeInForce.IMMEDIATE_OR_CANCEL));

        final JComboBox<OrdTypeComboItem> ordTypeComboBox = new JComboBox<OrdTypeComboItem>();
        ordTypeComboBox.addItem(new OrdTypeComboItem(OrdType.MARKET));
        ordTypeComboBox.addItem(new OrdTypeComboItem(OrdType.LIMIT));


        final JTextField symbolTextField = new JTextField("Symbol");
        symbolTextField.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                symbolTextField.setText("");
            }

            public void focusLost(FocusEvent e) {

            }
        });

        final JTextField priceTextField = new JTextField("Price");
        symbolTextField.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                symbolTextField.setText("");
            }

            public void focusLost(FocusEvent e) {

            }
        });


        JButton marketDataRequestButton = new JButton("Market Data Request");
        marketDataRequestButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                //oemsRunner.sendMarketDataRequest();

                char side = ((SideComboItem) sideJComboBox.getSelectedItem()).value;
                char timeInForce = ((TimeInForceComboItem) timeInForceComboBox.getSelectedItem()).value;
                char ordType = ((OrdTypeComboItem) ordTypeComboBox.getSelectedItem()).value;

                System.out.println(side + " " + timeInForce + " " + ordType);


            }
        });


 /*
        JButton orderStatusRequestButton = new JButton("Market Data Request");
        orderStatusRequestButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                //oemsRunner.sendOrderStatusRequest();
            }
        });



        JButton orderCancelRequestButton = new JButton("Market Data Request");
        orderCancelRequestButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                //oemsRunner.sendOrderCancelRequest();
            }
        });

        JButton orderCancelReplaceRequestButton = new JButton("Market Data Request");
        orderCancelReplaceRequestButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                //oemsRunner.sendOrderCancelReplaceRequest();
            }
        });
 */


        JButton newOrderSingleButton = new JButton("Order");
        newOrderSingleButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                //oemsRunner.sendNewOrderSingle();
            }
        });


        //add(marketDataRequestButton);

        //add(orderStatusRequestButton);
        //add(orderCancelRequestButton);
        //add(orderCancelReplaceRequestButton);

        add(sideJComboBox);
        add(timeInForceComboBox);
        add(ordTypeComboBox);

        add(symbolTextField);

        add(newOrderSingleButton);


        setVisible(true);
    }


}
