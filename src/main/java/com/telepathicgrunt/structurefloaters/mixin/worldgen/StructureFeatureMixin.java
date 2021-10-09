package com.telepathicgrunt.structurefloaters.mixin.worldgen;

import com.telepathicgrunt.structurefloaters.StructureFloaters;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.StructureConfig;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.OptionalInt;
import java.util.stream.IntStream;


@Mixin(StructureFeature.class)
public class StructureFeatureMixin {

    /**
     * @author TelepathicGrunt
     */
    @Inject(
            method = "tryPlaceStart(Lnet/minecraft/util/registry/DynamicRegistryManager;Lnet/minecraft/world/gen/chunk/ChunkGenerator;Lnet/minecraft/world/biome/source/BiomeSource;Lnet/minecraft/structure/StructureManager;JLnet/minecraft/util/math/ChunkPos;Lnet/minecraft/world/biome/Biome;ILnet/minecraft/world/gen/ChunkRandom;Lnet/minecraft/world/gen/chunk/StructureConfig;Lnet/minecraft/world/gen/feature/FeatureConfig;Lnet/minecraft/world/HeightLimitView;)Lnet/minecraft/structure/StructureStart;",
            at = @At(value = "INVOKE", target = "net/minecraft/structure/StructureStart.init(Lnet/minecraft/util/registry/DynamicRegistryManager;Lnet/minecraft/world/gen/chunk/ChunkGenerator;Lnet/minecraft/structure/StructureManager;Lnet/minecraft/util/math/ChunkPos;Lnet/minecraft/world/biome/Biome;Lnet/minecraft/world/gen/feature/FeatureConfig;Lnet/minecraft/world/HeightLimitView;)V", shift = At.Shift.AFTER),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private <C extends FeatureConfig> void structurefloaters_offsetJigsawStructures(DynamicRegistryManager dynamicRegistryManager, ChunkGenerator generator,
                                                          BiomeSource biomeSource, StructureManager manager, long worldSeed,
                                                          ChunkPos pos, Biome biome, int referenceCount, ChunkRandom random,
                                                          StructureConfig structureConfig, C config, HeightLimitView world,
                                                          CallbackInfoReturnable<StructureStart<?>> cir, ChunkPos chunkPos,
                                                          StructureStart<C> structureStart)
    {
        StructureFloaters.offsetStructurePieces(structureStart);
    }
}
