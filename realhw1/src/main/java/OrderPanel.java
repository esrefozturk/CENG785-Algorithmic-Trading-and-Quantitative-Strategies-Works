import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

/**
 * Created by esrefozturk on 20/11/2016.
 */
public class OrderPanel extends JPanel {
    public OrderPanel(HashMap<Integer,Order> orders)
    {
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1;
        constraints.weighty = 1;

        add(new JScrollPane(new OrderTable(orders)), constraints);
    }
}
