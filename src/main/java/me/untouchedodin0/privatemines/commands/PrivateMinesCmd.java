package me.untouchedodin0.privatemines.commands;

import me.untouchedodin0.privatemines.factory.MineFactory;
import me.untouchedodin0.privatemines.utils.storage.MineStorage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import redempt.redlib.commandmanager.CommandHook;
import redempt.redlib.commandmanager.Messages;

public class PrivateMinesCmd {

    MineStorage mineStorage;
    MineFactory mineFactory;

    public PrivateMinesCmd(MineStorage mineStorage, MineFactory mineFactory) {
        this.mineStorage = mineStorage;
        this.mineFactory = mineFactory;
    }

    @CommandHook("give")
    public void giveCommand(CommandSender sender, Player target) {
        sender.sendMessage("Private Mines Command");
        sender.sendMessage(target.getDisplayName());
        mineFactory.createMine(target);
        Messages.msg("recievedMine");
        target.sendMessage("You've been given a private mine!");
    }
}
