package me.untouchedodin0.privatemines.utils;

import org.bukkit.Material;

public enum BlockTypes {

    SPAWNPOINT(Material.CHEST),
    CORNER(Material.POWERED_RAIL);

    private final Material material;

    BlockTypes(Material material) {
        this.material = material;
    }
}
