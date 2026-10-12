package cn.wyceden.event.client;

import cn.wyceden.event.Event;

import net.minecraft.network.packet.Packet;

public class ReceivePacketEvent extends Event {

	private final Packet<?> packet;

	public ReceivePacketEvent(Packet<?> packet) {
		this.packet = packet;
	}

	public Packet<?> getPacket() {
		return packet;
	}
}
