package me.untouchedodin0.privatemines.utils.mine;

import org.bukkit.Bukkit;
import redempt.redlib.region.CuboidRegion;

public class ExpandingMineUtil {

    CuboidRegion cuboidRegion;
    double amount = 0;

    public void expandMine(CuboidRegion region, double expand) {
        this.cuboidRegion = region;
        this.amount = expand;

        Bukkit.broadcastMessage("Before expanding start: " + cuboidRegion.getStart());
        Bukkit.broadcastMessage("Before expanding end: " + cuboidRegion.getEnd());

        cuboidRegion.expand(amount, 0, amount, 0, amount, 0);

        Bukkit.broadcastMessage("After expanding start: " + cuboidRegion.getStart());
        Bukkit.broadcastMessage("After expanding end: " + cuboidRegion.getEnd());
    }
}
