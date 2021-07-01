package me.untouchedodin0.privatemines.utils;

import me.untouchedodin0.privatemines.region.WorldEditRegion;
import org.bukkit.Location;

public interface MineFactoryCompat<S> {

    WorldEditRegion pasteSchematic(S mineSchematic, Location location);

}
