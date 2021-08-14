package me.untouchedodin0.privatemines.commands;

import me.untouchedodin0.privatemines.factory.MineFactory;
import me.untouchedodin0.privatemines.utils.mine.Mine;
import me.untouchedodin0.privatemines.utils.storage.MineStorage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import redempt.redlib.commandmanager.CommandHook;
import redempt.redlib.commandmanager.Messages;

@SuppressWarnings("unused")
public class PrivateMinesCmd {

    MineStorage mineStorage;
    MineFactory mineFactory;
    Mine mine;
    boolean hasMine;

    public PrivateMinesCmd(MineStorage mineStorage, MineFactory mineFactory) {
        this.mineStorage = mineStorage;
        this.mineFactory = mineFactory;
    }

    @CommandHook("give")
    public void giveCommand(CommandSender sender, Player target) {
        if (sender != null) {
            mineFactory.createMine(target);
            Messages.msg("recievedMine");
        }
    }

    @CommandHook("delete")
    public void deleteCommand(CommandSender sender, Player target) {
        if (sender != null) {
            mineFactory.deleteMine(target);
            Messages.msg("deletedMine");
        }
    }

    @CommandHook("reset")
    public void resetCommand(CommandSender sender) {
        Player player = (Player) sender;
        if (sender != null) {
            this.hasMine = mineStorage.hasMine(player);
            if (!hasMine) {
                sender.sendMessage("Target didn't own a mine!");
                return;
            }
            mine = mineStorage.getMine(player);
            mine.resetMine();
        }
    }

    @CommandHook("reset")
    public void resetOther(CommandSender sender, Player target) {
        if (sender != null) {
            this.hasMine = mineStorage.hasMine(target);
            if (!hasMine) {
                sender.sendMessage("Target didn't own a mine!");
                return;
            }
            mine = mineStorage.getMine(target);
            mine.resetMine();
        }
    }
}
