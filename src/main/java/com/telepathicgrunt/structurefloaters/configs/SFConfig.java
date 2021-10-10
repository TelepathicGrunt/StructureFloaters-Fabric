package com.telepathicgrunt.structurefloaters.configs;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = "structure_floaters")
public class SFConfig implements ConfigData {

    @ConfigEntry.Gui.PrefixText
    @ConfigEntry.Gui.Tooltip(count = 0)
    @Comment(value = """
             Attempts to remove any nbt piece being place at the bottom of the world.
             Best for floating island terrain
            """)
    public boolean removeWorldBottomPieces = true;

    @ConfigEntry.Gui.PrefixText
    @ConfigEntry.Gui.Tooltip(count = 0)
    @Comment(value = """
             Attempts to remove any structure being place at the bottom of the world.
             Best for floating island terrain
            """)
    public boolean removeStructuresOffIslands = false;

    @ConfigEntry.Gui.PrefixText
    @ConfigEntry.Gui.Tooltip(count = 0)
    @Comment(value = """
             Attempts to remove the pillars from nether fortress and desert temples and the likes.
             Best for floating island terrain
            """)
    public boolean removeStructurePillars = true;

    @ConfigEntry.Gui.PrefixText
    @ConfigEntry.Gui.Tooltip(count = 0)
    @Comment(value = """
             Attempts to move structure to a specific height. Only applies if removeWorldBottomStructures is false.
            """)
    public int snapStructureToHeight = 50;

    @ConfigEntry.Gui.PrefixText
    @ConfigEntry.Gui.Tooltip(count = 0)
    @Comment(value = """
             structures to ignore modifying the height of
            """)
    public String structureToIgnore = "minecraft:stronghold,minecraft:mineshaft,minecraft:buried_treasure,minecraft:monument";
}
