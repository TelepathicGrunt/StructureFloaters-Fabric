package com.telepathicgrunt.structurefloaters.mixin.worldgen;

import com.telepathicgrunt.structurefloaters.StructureFloaters;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePiecesCollector;
import net.minecraft.structure.StructurePiecesGenerator;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.ConfiguredStructureFeature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.random.ChunkRandom;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Optional;
import java.util.function.Predicate;


@Mixin(ConfiguredStructureFeature.class)
public class ConfiguredStructureFeatureMixin {

    /**
     * @author TelepathicGrunt
     */
    @Inject(
            method = "tryPlaceStart(Lnet/minecraft/util/registry/DynamicRegistryManager;Lnet/minecraft/world/gen/chunk/ChunkGenerator;Lnet/minecraft/world/biome/source/BiomeSource;Lnet/minecraft/structure/StructureManager;JLnet/minecraft/util/math/ChunkPos;ILnet/minecraft/world/HeightLimitView;Ljava/util/function/Predicate;)Lnet/minecraft/structure/StructureStart;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/structure/StructureStart;hasChildren()Z"),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private <C extends FeatureConfig> void structurefloaters_offsetJigsawStructures(DynamicRegistryManager registryManager, ChunkGenerator chunkGenerator,
                                                                                    BiomeSource biomeSource, StructureManager structureManager,
                                                                                    long worldSeed, ChunkPos pos, int structureReferences,
                                                                                    HeightLimitView world, Predicate<Biome> biomePredicate,
                                                                                    CallbackInfoReturnable<StructureStart> cir,
                                                                                    Optional<StructurePiecesGenerator<C>> optional,
                                                                                    StructurePiecesCollector structurePiecesCollector,
                                                                                    ChunkRandom chunkRandom, StructureStart structureStart)
    {
        StructureFloaters.offsetStructurePieces(registryManager, structureStart, chunkGenerator, world);
    }
}
