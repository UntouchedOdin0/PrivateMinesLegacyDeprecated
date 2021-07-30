/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2021 Kyle Hicks
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package me.untouchedodin0.privatemines.world;

import org.bukkit.*;
import static me.untouchedodin0.privatemines.world.utils.Direction.NORTH;

public class MineWorldManager {
    private final World minesWorld;
    private final Location defaultLocation;
    private final int borderDistance;
    private int distance = 0;
    private me.untouchedodin0.privatemines.world.utils.Direction direction;

    public MineWorldManager() {
        this.minesWorld = Bukkit.createWorld(
                new WorldCreator("privatemines")
                        .type(WorldType.FLAT)
                        .generator(new EmptyWorldGenerator()));

        this.borderDistance = 150;
        this.direction = NORTH;
        defaultLocation = new Location(minesWorld, 0, 1, 0); // may need to raise the Y sometime?
    }

    public World getMinesWorld() {
        return minesWorld;
    }

    public synchronized Location nextFreeLocation() {
        if (distance == 0) {
            distance++;
            return defaultLocation;
        }

        if (direction == null) direction = NORTH;
        Location loc = direction.addTo(defaultLocation, distance * borderDistance);
        direction = direction.next();
        if (direction == NORTH) distance++;
        return loc;
    }

    public enum Direction {
        NORTH(0, -1), NORTH_EAST(1, -1),
        EAST(1, 0), SOUTH_EAST(1, 1),
        SOUTH(0, 1), SOUTH_WEST(-1, 1),
        WEST(-1, 0), NORTH_WEST(-1, -1);

        private final int xMulti;
        private final int zMulti;

        Direction(int xMulti, int zMulti) {
            this.xMulti = xMulti;
            this.zMulti = zMulti;
        }

        Direction next() {
            return values()[(ordinal() + 1) % (values().length)];
        }

        Location addTo(Location loc, int value) {

            return loc.clone().add(value * (double) xMulti, 0, value * (double) zMulti);
        }
    }
}
