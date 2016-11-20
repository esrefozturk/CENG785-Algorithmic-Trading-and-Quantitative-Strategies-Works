import javax.swing.table.AbstractTableModel;
import java.util.HashMap;

/**
 * Created by esrefozturk on 20/11/2016.
 */
public class OrderTableModel extends AbstractTableModel {

    public HashMap<Integer, Order> orders = new HashMap<Integer, Order>();

    public OrderTableModel(HashMap<Integer,Order> aOrders){
        orders = aOrders;
    }

    public String[] headers = new String[]{"Symbol", "Quantity", "Side"};

    public int getRowCount() {
        return orders.size();
    }

    public int getColumnCount() {
        return headers.length;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex){
            case 0: return orders.get(rowIndex-1).side;
        }
        return null;
    }
}
