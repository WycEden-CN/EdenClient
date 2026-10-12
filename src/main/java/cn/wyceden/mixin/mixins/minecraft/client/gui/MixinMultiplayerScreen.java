package cn.wyceden.mixin.mixins.minecraft.client.gui;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import cn.wyceden.event.EventBus;
import cn.wyceden.event.client.ServerJoinEvent;

import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.network.ServerInfo;

@Mixin(MultiplayerScreen.class)
public class MixinMultiplayerScreen {

    @Inject(method = "connect(Lnet/minecraft/client/network/ServerInfo;)V", at = @At("HEAD"), cancellable = true)
    private void onConnect(ServerInfo server, CallbackInfo ci) {
        EventBus.getInstance().post(new ServerJoinEvent(server.address));
    }
}
