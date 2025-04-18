package pl.aml.bk.clidbdemo.domain.database.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enum representing the allowed operation types in the system.
 * These values are based on the operations defined in data.sql.
 */
@RequiredArgsConstructor
@Getter
public enum OperationType {
    DEPOSIT("Deposit"),
    WITHDRAWAL("Withdrawal"),
    TRANSFER("Transfer"),
    PURCHASE("Purchase"),
    REFUND("Refund"),
    TOP_UP("Top-Up"),
    PAYMENT("Payment");

    private final String value;


    /**
     * Converts a string value to the corresponding enum value.
     *
     * @param value the string value to convert
     * @return the corresponding enum value, or null if no match is found
     */
    public static OperationType fromValue(String value) {
        for (OperationType type : OperationType.values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown operation type: " + value);
    }
}
