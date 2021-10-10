package com.telepathicgrunt.structurefloaters.mixin.worldgen;

import com.telepathicgrunt.structurefloaters.GeneralUtils;
import com.telepathicgrunt.structurefloaters.StructureFloaters;
import com.telepathicgrunt.structurefloaters.mixin.Vec3iAccessor;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.WoodlandMansionFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Random;

@Mixin(WoodlandMansionFeature.Start.class)
public abstract class WoodlandMansionFeatureStartMixin {
    
    /**
     * @author TelepathicGrunt
     * @reason fixed mansion pillar
     */
    @Inject(
            method = "generateStructure(Lnet/minecraft/world/StructureWorldAccess;Lnet/minecraft/world/gen/StructureAccessor;Lnet/minecraft/world/gen/chunk/ChunkGenerator;Ljava/util/Random;Lnet/minecraft/util/math/BlockBox;Lnet/minecraft/util/math/ChunkPos;)V",
            at = @At(target = "Lnet/minecraft/world/StructureWorldAccess;isAir(Lnet/minecraft/util/math/BlockPos;)Z", value = "INVOKE", ordinal = 0),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void structurefloaters_removePillar(StructureWorldAccess world, StructureAccessor structureAccessor,
                                                ChunkGenerator chunkGenerator, Random random, BlockBox box,
                                                ChunkPos chunkPos, CallbackInfo ci, BlockBox blockBox,
                                                int y, int x, int z, BlockPos blockPos) {
        if(StructureFloaters.SF_CONFIG.removeStructurePillars &&
            chunkGenerator.getSeaLevel() <= chunkGenerator.getMinimumY() &&
            GeneralUtils.getFirstLandYFromPos(world, new BlockPos(x, y - 1, z), GeneralUtils::isReplaceableByMansions) <= world.getBottomY() + 1)
        {
            ((Vec3iAccessor)blockPos).sf_setY(world.getBottomY() - 5);
        }
    }
}