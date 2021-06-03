package me.untouchedodin0.privatemines.worldedit.factory;

import me.untouchedodin0.privatemines.worldedit.WorldEditRegion;
import me.untouchedodin0.privatemines.worldedit.WorldEditVector;
import org.bukkit.Location;

import java.io.File;

public interface MineFactoryCompat {

    WorldEditRegion pasteSchematic(File schematic, Location location);

    Iterable<WorldEditVector> loop(WorldEditRegion region);
}
