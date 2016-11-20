import quickfix.field.Side;

/**
 * Created by esrefozturk on 20/11/2016.
 */


public class Order {
    public static int CURRENT_ID = 1;

    public int id;
    public Side side;

    public Order(Side aSide){
        id = CURRENT_ID++;
        side = aSide;
    }
}
