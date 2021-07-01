package me.untouchedodin0.privatemines.utils;

import org.bukkit.Location;
import org.bukkit.Material;

public class Util {

    public void fillRegion(Location one, Location two, Material material) {
        Location toFill = one.add(two);
        toFill.getBlock().setType(material);
    }
}
