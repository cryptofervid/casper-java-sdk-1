package com.casper.sdk.service.serialization.cltypes;

import com.casper.sdk.types.*;
import org.apache.commons.lang3.ArrayUtils;

import java.math.BigInteger;

/**
 * Builder to help with programmatic conversion of value types.
 */
public class CLValueBuilder {

    private static final TypesFactory TYPES_FACTORY = new TypesFactory();

    public static CLValue bool(final Object value) {
        return buildCLValue(CLType.BOOL, value);
    }

    public static CLValue byteArray(final Object value) {
        byte[] bytes = TYPES_FACTORY.getInstance(CLType.BYTE_ARRAY).serialize(value);
        // U32 at front of array is length
        return new CLValue(bytes, new CLByteArrayInfo(getArrayLength(bytes)), value);
    }

    public static CLValue i32(final Object value) {
        return buildCLValue(CLType.I32, value);
    }

    public static CLValue i64(final Object value) {
        return buildCLValue(CLType.I64, value);
    }

    public static CLValue string(final Object value) {
        return buildCLValue(CLType.STRING, value);
    }

    public static CLValue u32(final Object value) {
        return buildCLValue(CLType.U32, value);
    }

    public static CLValue u64(final Object value) {
        return buildCLValue(CLType.U64, value);
    }

    public static CLValue u256(final Object value) {
        return buildCLValue(CLType.U32, value);
    }

    public static CLValue u512(final Object value) {
        return buildCLValue(CLType.U512, value);
    }

    public static CLValue accountKey(final byte[] value) {
        return createKey(value, CLKeyInfo.KeyType.ACCOUNT_ID);
    }

    public static CLKeyValue hashKey(final byte[] value) {
        return createKey(value, CLKeyInfo.KeyType.HASH_ID);
    }

    public static CLKeyValue uRefKey(final byte[] value) {
        return createKey(value, CLKeyInfo.KeyType.UREF_ID);
    }

    private static CLKeyValue createKey(byte[] value, CLKeyInfo.KeyType keyType) {
        return new CLKeyValue(value, keyType, null);
    }

    private static CLValue buildCLValue(final CLType type, final Object value) {
        return new CLValue(TYPES_FACTORY.getInstance(type).serialize(value), type, value);
    }

    private static int getArrayLength(final byte[] bytes) {
        // U32 at front of array is length of byte array in
        if (bytes != null && bytes.length > 3) {
            final byte[] u32Len = new byte[4];
            System.arraycopy(bytes, 0, u32Len, 0, 4);
            // Switch from LE to BE byte order, allows Java to convert array to a number
            ArrayUtils.reverse(u32Len);
            return new BigInteger(u32Len).intValue();
        } else {
            return 0;
        }
    }
}
