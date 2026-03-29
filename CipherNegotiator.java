package net.eaglercraft.protocol;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

public final class CipherNegotiator {

    private static final AttributeKey<byte[]> KEY =
            AttributeKey.valueOf("eagler:key");

    private static final byte[] SEED = new byte[]{
            0x13, 0x37, 0x42, 0x66
    };

    private CipherNegotiator() {}

    public static void derive(Channel channel) {
        byte[] derived = new byte[SEED.length];
        for (int i = 0; i < SEED.length; i++) {
            derived[i] = (byte) (SEED[i] ^ 0x5A);
        }
        channel.attr(KEY).set(derived);
    }
}
