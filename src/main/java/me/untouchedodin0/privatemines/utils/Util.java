package me.untouchedodin0.privatemines.utils;

import org.bukkit.block.BlockFace;

public class Util {

    /*
    holy fuck it's empty here, i'll put more stuff later
     */

    // stops it from saying the class is empty.

    public static float getYaw(BlockFace blockFace) {
        switch (blockFace) {
            case NORTH:
                return 180f;
            case EAST:
                return -90f;
            case SOUTH:
                return -180f;
            case WEST:
                return 90f;
            default:
                return 0f;
        }
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
