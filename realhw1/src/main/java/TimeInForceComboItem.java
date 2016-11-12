import quickfix.field.TimeInForce;

/**
 * Created by esrefozturk on 12/11/2016.
 */
public class TimeInForceComboItem {
    public char value;

    public TimeInForceComboItem(char aValue) {
        value = aValue;
    }

    public String toString() {
        switch (value) {
            case TimeInForce.DAY:
                return "DAY";
            case TimeInForce.GOOD_TILL_DATE:
                return "GOOD TILL DATE";
            case TimeInForce.FILL_OR_KILL:
                return "FILL OR KILL";
            case TimeInForce.IMMEDIATE_OR_CANCEL:
                return "IMMEDIATE OR CANCEL";


        }
        return "";
    }
}
