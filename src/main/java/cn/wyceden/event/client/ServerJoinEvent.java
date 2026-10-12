package cn.wyceden.event.client;

import cn.wyceden.event.Event;

public class ServerJoinEvent extends Event {

    private final String address;

    public ServerJoinEvent(final String address) {
        this.address = address;
    }

    public String getAddress() {
        return this.address;
    }
}
