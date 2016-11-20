import javax.swing.*;
import java.util.HashMap;

/**
 * Created by esrefozturk on 20/11/2016.
 */
public class OrderTable extends JTable {
    public OrderTable(HashMap<Integer,Order> orders){
        super(new OrderTableModel(orders));
    }
}
