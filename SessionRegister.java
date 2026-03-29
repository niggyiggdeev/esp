package net.eaglercraft.protocol;

import io.netty.channel.Channel;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class SessionRegistry {

    private static final Set<Channel> ACTIVE =
            ConcurrentHashMap.newKeySet();

    private SessionRegistry() {}

    public static void register(Channel channel) {
        ACTIVE.add(channel);
    }

    public static void unregister(Channel channel) {
        ACTIVE.remove(channel);
    }

    public static int size() {
        return ACTIVE.size();
    }
}
