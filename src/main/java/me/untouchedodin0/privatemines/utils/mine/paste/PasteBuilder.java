package me.untouchedodin0.privatemines.utils.mine.paste;

import org.bukkit.Location;
import redempt.redlib.multiblock.MultiBlockStructure;

import java.io.File;

public class PasteBuilder {

    private File file;
    private MultiBlockStructure multiBlockStructure;
    private Location placeLocation;

    public PasteBuilder() {
    }

    public PasteBuilder to(Location location) {
        this.placeLocation = location;
        return this;
    }


    public PasteBuilder setFile(File file) {
        this.file = file;
        return this;
    }

    public File getFile() {
        return file;
    }

    public PasteBuilder setMultiBlockStructure(MultiBlockStructure multiBlockStructure) {
        this.multiBlockStructure = multiBlockStructure;
        return this;
    }

    public MultiBlockStructure getMultiBlockStructure() {
        return multiBlockStructure;
    }

    public Location getPlaceLocation() {
        return placeLocation;
    }
}
