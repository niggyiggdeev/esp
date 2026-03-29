package net.eaglercraft.protocol;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;

public class EaglerServerProtocol extends ChannelInitializer<Channel> {

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        if (pipeline.get("packet_handler") != null) {
            pipeline.addBefore("packet_handler",
                    "eagler_protocol",
                    new UpstreamBridge());
        } else {
            pipeline.addLast("eagler_protocol", new UpstreamBridge());
        }
    }
}
