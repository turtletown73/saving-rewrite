package net.scoobis.savingrewrite.event;

import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.scoobis.savingrewrite.SavingrewriteMod;
import net.scoobis.savingrewrite.command.SaveCommand;

@Mod.EventBusSubscriber(modid = SavingrewriteMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModEvents {
    @SubscribeEvent
    public static void onCommandsRegister(RegisterCommandsEvent event) {
        new SaveCommand(event.getDispatcher());
    }
}
