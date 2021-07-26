package me.untouchedodin0.privatemines.structure;

import org.bukkit.Location;
import redempt.redlib.multiblock.Structure;

import java.util.List;

public class PrivateMineStructure {

    Structure structure;
    List<Location> cornerLocations;
    Location spawnLocation;
    Location npcLocation;

    public Structure getStructure() {
        return structure;
    }

    public void setStructure(Structure structure) {
        this.structure = structure;
    }

    public List<Location> getCornerLocations() {
        return cornerLocations;
    }

    public void setCornerLocations(List<Location> cornerLocations) {
        this.cornerLocations = cornerLocations;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public void setSpawnLocation(Location spawnLocation) {
        this.spawnLocation = spawnLocation;
    }

    public Location getNpcLocation() {
        return npcLocation;
    }

    public void setNpcLocation(Location npcLocation) {
        this.npcLocation = npcLocation;
    }
}
