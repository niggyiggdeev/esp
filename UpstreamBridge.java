package net.eaglercraft.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.util.AttributeKey;

import java.util.UUID;

public class UpstreamBridge extends ChannelDuplexHandler {

    private static final AttributeKey<String> SESSION =
            AttributeKey.valueOf("eagler:session");

    private static final AttributeKey<HandshakeState> STATE =
            AttributeKey.valueOf("eagler:state");

    private static final AttributeKey<Boolean> FLAG =
            AttributeKey.valueOf("eagler:flag");

    private static final byte[] SIGNATURE = new byte[]{
            0x45, 0x41, 0x47, 0x4C
    };

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.channel().attr(SESSION).set(UUID.randomUUID().toString().replace("-", ""));
        ctx.channel().attr(STATE).set(HandshakeState.INIT);
        ctx.channel().attr(FLAG).set(false);
        SessionRegistry.register(ctx.channel());
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        SessionRegistry.unregister(ctx.channel());
        super.channelInactive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof ByteBuf) {
            ByteBuf buf = (ByteBuf) msg;

            if (scan(buf)) {
                ctx.channel().attr(FLAG).set(true);
                ctx.channel().attr(STATE).set(HandshakeState.NEGOTIATED);
                CipherNegotiator.derive(ctx.channel());
            }
        }

        super.channelRead(ctx, msg);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        VersionOverride.inspect(ctx.channel(), msg);
        super.write(ctx, msg, promise);
    }

    private boolean scan(ByteBuf buf) {
        int readable = buf.readableBytes();

        for (int i = 0; i <= readable - SIGNATURE.length; i++) {
            boolean match = true;
            for (int j = 0; j < SIGNATURE.length; j++) {
                if (buf.getByte(i + j) != SIGNATURE[j]) {
                    match = false;
                    break;
                }
            }
            if (match) return true;
        }

        return false;
    }
}
