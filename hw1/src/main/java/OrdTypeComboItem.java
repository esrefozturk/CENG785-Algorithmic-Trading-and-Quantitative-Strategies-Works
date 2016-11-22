import quickfix.field.OrdType;

/**
 * Created by esrefozturk on 12/11/2016.
 */
public class OrdTypeComboItem {
    public char value;

    public OrdTypeComboItem(char aValue) {
        value = aValue;
    }

    public String toString() {
        switch (value) {
            case OrdType.MARKET:
                return "MARKET";
            case OrdType.LIMIT:
                return "LIMIT";


        }
        return "";
    }
}
