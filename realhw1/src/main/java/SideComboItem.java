import quickfix.field.Side;

/**
 * Created by esrefozturk on 12/11/2016.
 */
public class SideComboItem {

    public char value;

    public SideComboItem(char aValue) {
        value = aValue;
    }

    public String toString() {
        switch (value) {
            case Side.SELL:
                return "SELL";
            case Side.BUY:
                return "BUY";


        }
        return "";
    }
}
