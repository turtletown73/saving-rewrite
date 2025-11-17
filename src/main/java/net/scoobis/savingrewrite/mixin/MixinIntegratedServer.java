package net.scoobis.savingrewrite.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.client.server.IntegratedServer;
import net.scoobis.savingrewrite.SavingrewriteMod;

@Mixin(IntegratedServer.class)
public class MixinIntegratedServer {
    @Redirect(method = "tickServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/server/IntegratedServer;saveEverything(ZZZ)Z"))
    public boolean savingrewrite_tickServer(IntegratedServer instance, boolean b1, boolean b2, boolean b3) {
        SavingrewriteMod.LOGGER.info("Avoided save on game pause.");
        return false;
    }
}
