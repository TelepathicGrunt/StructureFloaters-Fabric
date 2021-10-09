package com.telepathicgrunt.structurefloaters;

import com.telepathicgrunt.structurefloaters.configs.SFConfig;
import com.telepathicgrunt.structurefloaters.mixin.worldgen.StructurePieceAccessor;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.structure.OceanMonumentGenerator;
import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.OceanMonumentFeature;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.plaf.nimbus.State;
import java.util.Arrays;
import java.util.Locale;
import java.util.OptionalInt;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StructureFloaters implements ModInitializer {
	public static String MODID = "structure_floaters";
	public static final Logger LOGGER = LogManager.getLogger(MODID);
	public static SFConfig SF_CONFIG;
	public static Set<Identifier> STRUCTURES_TO_IGNORE;
	//public static final WBDimensionOmegaConfigs omegaConfig = OmegaConfig.register(WBDimensionOmegaConfigs.class);

	@Override
	public void onInitialize() {
		//Set up config
		AutoConfig.register(SFConfig.class, JanksonConfigSerializer::new);
		SF_CONFIG = AutoConfig.getConfigHolder(SFConfig.class).getConfig();
		STRUCTURES_TO_IGNORE = Arrays.stream(SF_CONFIG.structureToIgnore
				.toLowerCase(Locale.ROOT)
				.replace(" ", "")
				.replace("	", "")
				.split(","))
				.map(Identifier::new)
				.collect(Collectors.toSet());
	}

	public static <C extends FeatureConfig> void offsetStructurePieces(StructureStart<C> structureStart) {
		if(structureStart instanceof OceanMonumentFeature.Start) {
			return;
		}

		if(STRUCTURES_TO_IGNORE.contains(Registry.STRUCTURE_FEATURE.getId(structureStart.getFeature()))) {
			return;
		}

		OptionalInt minY = structureStart.getChildren().stream().flatMapToInt(piece -> IntStream.of(piece.getBoundingBox().getMinY())).min();
		minY.ifPresent(y -> {
			if(!StructureFloaters.SF_CONFIG.removeWorldBottomStructures && y < StructureFloaters.SF_CONFIG.snapStructureToHeight) {
				structureStart.getChildren().forEach(piece -> piece.translate(0, StructureFloaters.SF_CONFIG.snapStructureToHeight - y, 0));
			}
		});
	}

	public static boolean cancelPillars(StructureWorldAccess world, StructurePiece piece, int x, int y, int z) {
		if(StructureFloaters.SF_CONFIG.removeStructurePillars &&
				world.toServerWorld().getChunkManager().getChunkGenerator().getSeaLevel() <= world.toServerWorld().getChunkManager().getChunkGenerator().getMinimumY())
		{
			int heightmapY = world.getTopY(Heightmap.Type.OCEAN_FLOOR_WG, ((StructurePieceAccessor)piece).structurefloaters_callApplyXTransform(x, z), ((StructurePieceAccessor)piece).structurefloaters_callApplyZTransform(x, z));
			if(heightmapY <= world.getBottomY() + 2 || heightmapY > y){
				return true;
			}
		}
		return false;
	}
}
