package me.untouchedodin0.privatemines.world;

import me.untouchedodin0.privatemines.world.utils.Direction;
import org.bukkit.*;

import static me.untouchedodin0.privatemines.world.utils.Direction.NORTH;

public class MineWorldManager {

    private final World minesWorld;
    private final Location defaultLocation;
    private final int borderDistance;
    private int distance = 0;
    private Direction direction;
    private Location location;

    public MineWorldManager() {
        this.minesWorld = Bukkit.createWorld(
                new WorldCreator("privatemines")
                        .type(WorldType.FLAT)
                        .generator(new EmptyWorldGenerator()));
        this.defaultLocation = new Location(minesWorld, 0, 0, 0);
        this.borderDistance = 150;
        this.direction = NORTH;
    }

    public World getMinesWorld() {
        return minesWorld;
    }

    public synchronized Location nextFreeLocation() {
        if (distance == 0) {
            distance++;
            return defaultLocation;
        }

        if (direction == null) {
            direction = NORTH;
            location = direction.addTo(defaultLocation, distance * borderDistance);
            direction = direction.next();
            if (direction == NORTH)
                distance++;
        }
        return location;
    }
}
