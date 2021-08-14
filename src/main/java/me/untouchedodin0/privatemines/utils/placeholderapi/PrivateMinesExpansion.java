package me.untouchedodin0.privatemines.utils.placeholderapi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.untouchedodin0.privatemines.utils.mine.Mine;
import me.untouchedodin0.privatemines.utils.storage.MineStorage;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class PrivateMinesExpansion extends PlaceholderExpansion {

    private final MineStorage mineStorage;
    private Mine mine;

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
        switch (placeholder) {
            case "owner":
                if (mineStorage.hasMine(player.getPlayer())) {
                    return "Yes";
                } else {
                    return "No";
                }
            case "location":
                if (mine == null) {
                    return "Player doesn't own a mine";
                } else {
                    return mine.getMineLocation().toString();
                }
            default:
                break;
        }
        return placeholder;
    }
}
