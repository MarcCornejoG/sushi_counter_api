package com.sushiapi.SushiApi.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SushiType {
    NIGIRI("nigiri"),
    MAKI("maki"),
    URAMAKI("uramaki"),
    SASHIMI("sashimi");

    private final String value;

    SushiType(String value) {
        this.value = value;
    }
    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static SushiType fromValue(String value) {
        if (value == null) return null;

        System.out.println("=== ENUM DEBUG ===");
        System.out.println("Received value: '" + value + "'");
        System.out.println("Length: " + value.length());

        String cleanValue = value.trim().toLowerCase();
        System.out.println("Clean value: '" + cleanValue + "'");
        for (SushiType type : SushiType.values()) {
            System.out.println("Comparing with: '" + type.value + "'");

            if (type.value.equals(cleanValue)) {
                System.out.println("MATCH FOUND!");

                return type;
            }
        }
        System.out.println("NO MATCH FOUND!");

        throw new IllegalArgumentException("Unknown SushiType: '" + value + "'");
    }
}
