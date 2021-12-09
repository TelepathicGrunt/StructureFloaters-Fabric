package com.telepathicgrunt.structurefloaters;

import com.telepathicgrunt.structurefloaters.configs.SFConfig;
import com.telepathicgrunt.structurefloaters.mixin.worldgen.StructurePieceAccessor;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.structure.PoolStructurePiece;
import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.StructureStart;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.OceanMonumentFeature;
import net.minecraft.world.gen.feature.StructureFeature;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
	public static Set<Identifier> STRUCTURES_TO_RAISE_PIECES_INDIVIDUALLY;
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

		STRUCTURES_TO_RAISE_PIECES_INDIVIDUALLY = Arrays.stream(SF_CONFIG.structureToRaiseEachPieceSeparately
						.toLowerCase(Locale.ROOT)
						.replace(" ", "")
						.replace("	", "")
						.split(","))
				.map(Identifier::new)
				.collect(Collectors.toSet());
	}

	public static <C extends FeatureConfig> void offsetStructurePieces(StructureStart<C> structureStart, ChunkGenerator generator) {
		if(structureStart.getFeature() == StructureFeature.MONUMENT) {
			return;
		}

		Identifier structureID = Registry.STRUCTURE_FEATURE.getId(structureStart.getFeature());
		if(STRUCTURES_TO_IGNORE.contains(structureID)) {
			return;
		}

		OptionalInt minY = structureStart.getChildren().stream().flatMapToInt(piece -> IntStream.of(piece.getBoundingBox().getMinY())).min();
		if(StructureFloaters.SF_CONFIG.removeStructuresOffIslands && minY.isPresent() && minY.getAsInt() <= generator.getMinimumY()) {
			structureStart.getChildren().clear();
			return;
		}

		boolean raisePiecesSeparately = STRUCTURES_TO_RAISE_PIECES_INDIVIDUALLY.contains(structureID);
		minY.ifPresent(y -> {
			if(y < StructureFloaters.SF_CONFIG.snapStructureToHeight) {
				structureStart.getChildren().forEach(piece -> {
					if(raisePiecesSeparately) {
						if(piece.getBoundingBox().getMinY() <= StructureFloaters.SF_CONFIG.snapStructureToHeight) {
							piece.translate(0, StructureFloaters.SF_CONFIG.snapStructureToHeight - piece.getBoundingBox().getMinY(), 0);
							if(piece instanceof PoolStructurePiece poolStructurePiece) {
								poolStructurePiece.getPoolElement().setProjection(StructurePool.Projection.RIGID);
							}
						}
					}
					else {
						piece.translate(0, StructureFloaters.SF_CONFIG.snapStructureToHeight - y, 0);
					}
				});
			}
		});
	}

	public static boolean cancelPillars(StructureWorldAccess world, StructurePiece piece, int x, int y, int z) {
		if(StructureFloaters.SF_CONFIG.removeStructurePillars &&
				world.toServerWorld().getChunkManager().getChunkGenerator().getSeaLevel() <= world.toServerWorld().getChunkManager().getChunkGenerator().getMinimumY())
		{
			int trueX = ((StructurePieceAccessor)piece).structurefloaters_callApplyXTransform(x, z);
			int trueZ = ((StructurePieceAccessor)piece).structurefloaters_callApplyZTransform(x, z);
			int trueY = ((StructurePieceAccessor)piece).structurefloaters_callApplyYTransform(y);
			int heightmapY = GeneralUtils.getFirstLandYFromPos(world, new BlockPos(trueX, trueY, trueZ), GeneralUtils::isReplaceableByStructures);

			if(heightmapY <= world.getBottomY()){
				return true;
			}
		}
		return false;
	}
}
