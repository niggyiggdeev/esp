package net.eaglercraft.protocol;

import io.netty.channel.Channel;

public final class VersionOverride {

    private VersionOverride() {}

    public static void inspect(Channel channel, Object packet) {
        HandshakeState state = channel.attr(
                io.netty.util.AttributeKey.valueOf("eagler:state")
        ).get();

        if (state == HandshakeState.NEGOTIATED) {
            channel.attr(
                    io.netty.util.AttributeKey.valueOf("eagler:state")
            ).set(HandshakeState.COMPLETE);
        }
    }
}
