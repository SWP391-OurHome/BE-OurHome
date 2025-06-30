package com.javaweb.utils;

public class FormatUtils {
    public <T> void updateIfPresent(PropertyConsumer<T> setter, String value) {
        if (value != null && !value.isEmpty()) {
            setter.accept((T) value);
        }
    }

    public void updateNumericIfPresent(PropertyNumericConsumer setter, String value) {
        if (value != null && !value.isEmpty()) {
            try {
                setter.accept(Integer.parseInt(value));
            } catch (NumberFormatException e) {
                // Ignore invalid numeric input and retain old value
            }
        }
    }

    public void updateBooleanIfPresent(PropertyBooleanConsumer setter, String value) {
        if (value != null && !value.isEmpty()) {
            setter.accept(Boolean.parseBoolean(value));
        }
    }

    @FunctionalInterface
    private interface PropertyConsumer<T> {
        void accept(T value);
    }

    @FunctionalInterface
    private interface PropertyNumericConsumer {
        void accept(Integer value);
    }

    @FunctionalInterface
    private interface PropertyBooleanConsumer {
        void accept(Boolean value);
    }
}
