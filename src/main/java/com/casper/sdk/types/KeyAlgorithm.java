package com.casper.sdk.types;

import java.util.HashMap;
import java.util.Map;

public enum KeyAlgorithm {

    ED25519(1),
    SECP256K1(2);

    private static final Map<Integer, KeyAlgorithm> map = new HashMap<>();

    static {
        for (KeyAlgorithm key : KeyAlgorithm.values()) {
            map.put(key.value, key);
        }
    }

    private final int value;

    KeyAlgorithm(int value) {
        this.value = value;
    }

    public static KeyAlgorithm valueOf(int pageType) {
        return map.get(pageType);
    }

    public static KeyAlgorithm fromId(char id) {
        if (id == 1 || id == '1') {
            return ED25519;
        } else if (id == 2 || id == '2') {
            return SECP256K1;
        }
        throw new IllegalArgumentException("Unknown algorithm ID " + id);
    }

    public int getValue() {
        return value;
    }
}

