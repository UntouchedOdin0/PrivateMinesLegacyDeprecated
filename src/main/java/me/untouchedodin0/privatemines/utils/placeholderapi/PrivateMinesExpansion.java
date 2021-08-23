package me.untouchedodin0.privatemines.utils.placeholderapi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.untouchedodin0.privatemines.utils.mine.Mine;
import me.untouchedodin0.privatemines.utils.storage.MineStorage;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PrivateMinesExpansion extends PlaceholderExpansion {

    private final MineStorage mineStorage;
    private Mine mine;
    Map<String, String> placeholders = new HashMap<>();


    public PrivateMinesExpansion(MineStorage mineStorage) {
        this.mineStorage = mineStorage;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "privatemines";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public @NotNull String getAuthor() {
        return "UntouchedOdin0";
    }

    @Override
    public boolean persist() {
        return true; // This is required or else PlaceholderAPI will unregister the Expansion on reload
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String placeholder) {
        if (mineStorage.hasMine(player.getPlayer())) {
            mine = mineStorage.getMine(player.getPlayer());
        }
        boolean hasMine = mineStorage.hasMine(player.getPlayer());

        placeholders.putIfAbsent("owner", String.valueOf(hasMine));
        if (hasMine) {
            placeholders.put("location", mine.getMineLocation().toString());
            placeholders.put("spawnlocation", mine.getSpawnLocation().toString());
            placeholders.put("npclocation", mine.getNpcLocation().toString());
            placeholders.put("weightedrandom", mine.getWeightedRandom().toString());
            placeholders.put("cornerlocations", Arrays.toString(mine.getCornerLocations()));
            placeholders.put("owneruuid", mine.getMineOwner().toString());
            placeholders.put("structure", mine.getStructure().getType().getName());
            placeholders.put("cuboidregion", mine.getCuboidRegion().toString());
            placeholders.put("npcrelative", Arrays.toString(mine.getNPCLocationRelative()));
        }
        return placeholders.get(placeholder);
    }
}
