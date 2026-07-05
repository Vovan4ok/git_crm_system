package org.volodymyrzganiaiko.gym.crm.system.utils;

public class ValueValidator {
    private ValueValidator() {}

    public static void requireNotNull(Object value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException(fieldName + " must not be empty");
        }
    }

    public static void requireNotBlank(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " must not be blank");
        }
    }
}
