package net.eaglercraft.protocol.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;

/**
 * Server-side protocol bridge for Eaglercraft 1.8.x connections.
 *
 * This module installs the upstream negotiation layer before the
 * Minecraft packet serializer in order to inspect handshake state
 * and attach connection metadata.
 *
 * Compatible with Netty 4.x (1.8 server runtime).
 */
public class EaglerServerProtocol extends ChannelInitializer<Channel> {

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        /*
         * Insert upstream protocol inspector before vanilla packet handling.
         * This ensures handshake frames are available for inspection
         * before login state transition.
         */
        pipeline.addBefore("packet_handler",
                "eagler_upstream",
                new EaglerUpstreamHandler());
    }
}
