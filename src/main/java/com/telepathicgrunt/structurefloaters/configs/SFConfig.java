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
             Configured structures to ignore modifying the height of.
            """)
    public String configuredStructures = "minecraft:stronghold,minecraft:mineshaft_mesa,minecraft:mineshaft,minecraft:buried_treasure,minecraft:monument";

    @ConfigEntry.Gui.PrefixText
    @ConfigEntry.Gui.Tooltip(count = 0)
    @Comment(value = """
             Raises up the pieces of the configured structure to the snapStructureToHeight without affecting the pieces already or above snapStructureToHeight.
             Think villages where each house is separate from each other and can be at different heights.
            """)
    public String configuredStructuresToRaiseEachPieceSeparately = "minecraft:village_taiga,minecraft:village_savanna,minecraft:village_desert,minecraft:village_plains,minecraft:village_snowy,minecraft:pillager_outpost";

    @ConfigEntry.Gui.PrefixText
    @ConfigEntry.Gui.Tooltip(count = 0)
    @Comment(value = """
             Overrides the snapStructureToHeight to the specified y value here. State structure name, space, then y value for it. Add comma after y value for next entry.
             Example:
             "minecraft:village_taiga 50, minecraft:village_desert 70, minecraft:village_snowy 20"
            """)
    public String yValueOverridePerStructure = "minecraft:village_taiga 50, minecraft:village_desert 70, minecraft:village_snowy 20";
}
