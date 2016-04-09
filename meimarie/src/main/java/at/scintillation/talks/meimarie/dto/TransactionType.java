package at.scintillation.talks.meimarie.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author <a href="mailto:alexander.rosemann@scintillation.at">Alexander Rosemann</a>
 * @since 1.0.0
 */
public enum TransactionType {

    CREDIT_CARD("CC"),
    WIRE_TRANSFER("WT"),
    CASH("CA");

    private String type;

    TransactionType(String method) {
        type = method;
    }

    @JsonValue
    public String getType() {
        return type;
    }

    private static Map<String, TransactionType> namesMap = new HashMap<>(4);

    static {
        namesMap.put(CREDIT_CARD.getType(), CREDIT_CARD);
        namesMap.put(WIRE_TRANSFER.getType(), WIRE_TRANSFER);
        namesMap.put(CASH.getType(), CASH);
    }

    @JsonCreator
    public static TransactionType forValue(String value) {
        return namesMap.get(value);
    }
}
