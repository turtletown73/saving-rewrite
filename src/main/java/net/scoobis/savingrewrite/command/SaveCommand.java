package net.scoobis.savingrewrite.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class SaveCommand {
    public SaveCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("save").executes(this::execute)
        );
    }

    private int execute(CommandContext<CommandSourceStack> context) {
        boolean flag = context.getSource().getServer().saveEverything(true, true, true);
        if (flag) {
            context.getSource().sendSuccess(() -> Component.literal("Game saved!"), true);
        } else {
            context.getSource().sendSuccess(() -> Component.literal("Game failed to fully save?"), true);
        }
        return 1;
    }
}
