package net.scoobis.savingrewrite.mixin;

import java.io.IOException;
import java.util.Map;

import net.minecraft.Util;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.profiling.metrics.profiling.MetricsRecorder;
import net.minecraft.world.level.Level;
import org.objectweb.asm.Opcodes;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.scoobis.savingrewrite.SavingrewriteMod;

@Mixin(MinecraftServer.class)
public abstract class MixinMinecraftServer {
    @Shadow
    private MinecraftServer.ReloadableResources resources;
    @Shadow
    @Final
    protected LevelStorageSource.LevelStorageAccess storageSource;
    @Shadow
    @Final
    private static Logger LOGGER;
    @Shadow
    private PlayerList playerList;
    @Shadow
    private MetricsRecorder metricsRecorder;

    @Inject(method = "stopServer", at = @At("HEAD"), cancellable = true)
    public void savingrewrite_stopServer(CallbackInfo ci) {
        SavingrewriteMod.LOGGER.info("Avoided saving on stop");
        MinecraftServer thisInstance = (MinecraftServer) (Object) this;

        if (metricsRecorder.isRecording()) {
            thisInstance.cancelRecordingMetrics();
        }

        LOGGER.info("Stopping server");
        if (thisInstance.getConnection() != null) {
            thisInstance.getConnection().stop();
        }

        resources.close();

        if (playerList != null) {
            playerList.removeAll();
        }

        try {
            storageSource.close();
        } catch (IOException ioException) {
            LOGGER.error("Failed to unlock level {}", storageSource.getLevelId(), ioException);
        }

        ci.cancel();
    }

    @Redirect(method = "tickServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;saveEverything(ZZZ)Z"))
    public boolean savingrewrite_tickServer(MinecraftServer instance, boolean b, boolean p_195515_, boolean p_195516_) {
        SavingrewriteMod.LOGGER.info("Avoided autosave.");
        return false;
    }
    
    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/players/PlayerList;saveAll()V"), method = "lambda$reloadResources$26")
    private void savingrewrite_reloadResources(PlayerList instance) {
        SavingrewriteMod.LOGGER.info("Avoided playerlist saving on resource reload");
    }
}
