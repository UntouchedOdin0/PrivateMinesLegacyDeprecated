package me.untouchedodin0.privatemines.listener;

import me.untouchedodin0.privatemines.PrivateMines;
import me.untouchedodin0.privatemines.utils.mine.Mine;
import me.untouchedodin0.privatemines.utils.storage.MineStorage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import static org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class FallInVoidListener implements Listener {

    PrivateMines privateMines;
    MineStorage mineStorage;
    Mine mine;

    public FallInVoidListener(PrivateMines privateMines, MineStorage mineStorage) {
        this.privateMines = privateMines;
        this.mineStorage = mineStorage;
    }

    @EventHandler
    public void onFallInVoidEvent(EntityDamageEvent event) {

        if (privateMines.getConfig().getBoolean("teleportToMineFromVoid") && event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (event.getCause() == DamageCause.VOID) {
                if (mineStorage.hasMine(player)) {
                    mine = mineStorage.getMine(player);
                    mine.teleportToMine(player);
                }
            }
        }
    }
}

