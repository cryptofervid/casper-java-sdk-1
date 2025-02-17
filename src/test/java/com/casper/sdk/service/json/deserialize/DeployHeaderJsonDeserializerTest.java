package com.casper.sdk.service.json.deserialize;

import com.casper.sdk.types.DeployHeader;
import com.casper.sdk.types.Digest;
import com.casper.sdk.types.KeyAlgorithm;
import com.casper.sdk.types.PublicKey;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.List;

import static com.casper.sdk.service.serialization.util.ByteUtils.decodeHex;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItems;

/**
 * Tests the {@link DeployHeader} can be parsed from JSON
 */
class DeployHeaderJsonDeserializerTest {

    private static final String JSON =
            "{\n" +
            "    \"account\": \"017f747b67bd3fe63c2a736739dfe40156d622347346e70f68f51c178a75ce5537\",\n" +
            "    \"timestamp\": \"2021-05-04T14:20:35.104Z\",\n" +
            "    \"ttl\": \"30m\",\n" +
            "    \"body_hash\": \"f2e0782bba4a0a9663cafc7d707fd4a74421bc5bfef4e368b7e8f38dfab87db8\",\n" +
            "    \"dependencies\": [\n" +
            "      \"0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f\",\n" +
            "      \"1010101010101010101010101010101010101010101010101010101010101010\"\n" +
            "    ],\n" +
            "    \"chain_name\": \"mainnet\"\n" +
            "}";

    private DeployHeader deployHeader;

    @BeforeEach
    void setUp() throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        deployHeader = mapper.reader().readValue(JSON, DeployHeader.class);
    }

    @Test
    void testParseDeployHeaderAccountFromJson() {
        assertThat(deployHeader, is(notNullValue()));
        assertThat(deployHeader.getAccount(), is(notNullValue(PublicKey.class)));
        assertThat(PublicKey.toHex(deployHeader.getAccount().getBytes()), is("7f747b67bd3fe63c2a736739dfe40156d622347346e70f68f51c178a75ce5537"));
        assertThat(deployHeader.getAccount().getKeyAlgorithm(), is(KeyAlgorithm.ED25519));
    }

    @Test
    void testParseDeployHeaderTimestampFromJson() {
        final OffsetDateTime offsetDateTime = OffsetDateTime.parse("2021-05-04T14:20:35.104Z");
        final long epochMilli = offsetDateTime.toInstant().toEpochMilli();
        assertThat(deployHeader.getTimestamp(), is(epochMilli));
    }

    @Test
    void testParseDeployHeaderTtlFromJson() {
        assertThat(deployHeader.getTtl(), is(1800000L));
    }

    @Test
    void testParseDeployHeaderBodyHashFromJson() {
        assertThat(deployHeader.getBodyHash(), is(notNullValue(Digest.class)));
        assertThat(deployHeader.getBodyHash().getHash(), is(decodeHex("f2e0782bba4a0a9663cafc7d707fd4a74421bc5bfef4e368b7e8f38dfab87db8")));
    }

    @Test
    void testParseDeployHeaderChainNameFromJson() {
        assertThat(deployHeader.getChainName(), is(notNullValue(String.class)));
        assertThat(deployHeader.getChainName(), is("mainnet"));
    }

    @Test
    void testParseDeployHeaderDependenciesFromJson() {
        assertThat(deployHeader.getDependencies(), is(notNullValue(List.class)));
        assertThat(deployHeader.getDependencies().size(), is(2));
        assertThat(
                deployHeader.getDependencies(),
                hasItems(
                        new Digest("0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f"),
                        new Digest("1010101010101010101010101010101010101010101010101010101010101010")
                )
        );
    }
}
