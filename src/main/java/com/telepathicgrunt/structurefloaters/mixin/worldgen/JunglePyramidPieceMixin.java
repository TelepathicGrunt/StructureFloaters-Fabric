package com.telepathicgrunt.structurefloaters.mixin.worldgen;

import com.telepathicgrunt.structurefloaters.StructureFloaters;
import net.minecraft.structure.JungleTempleGenerator;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(JungleTempleGenerator.class)
public abstract class JunglePyramidPieceMixin {

    /**
     * @author TelepathicGrunt
     * @reason Place Pyramids on top of land properly
     */
    @Inject(
            method = "generate(Lnet/minecraft/world/StructureWorldAccess;Lnet/minecraft/world/gen/StructureAccessor;Lnet/minecraft/world/gen/chunk/ChunkGenerator;Ljava/util/Random;Lnet/minecraft/util/math/BlockBox;Lnet/minecraft/util/math/ChunkPos;Lnet/minecraft/util/math/BlockPos;)V",
            at = @At(target = "Lnet/minecraft/structure/JungleTempleGenerator;fillWithOutline(Lnet/minecraft/world/StructureWorldAccess;Lnet/minecraft/util/math/BlockBox;IIIIIIZLjava/util/Random;Lnet/minecraft/structure/StructurePiece$BlockRandomizer;)V",
                    value = "INVOKE",
                    ordinal = 0)
    )
    private void structurefloaters_fixedYHeight(StructureWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockBox chunkBox, ChunkPos chunkPos, BlockPos pos, CallbackInfo ci) {
        BlockBox box = ((JungleTempleGenerator)(Object)this).getBoundingBox();
        if(!StructureFloaters.STRUCTURES_TO_IGNORE.contains(new Identifier("minecraft:jungle_pyramid")) &&
            chunkGenerator.getSeaLevel() <= chunkGenerator.getMinimumY() &&
            box.getMinY() < StructureFloaters.SF_CONFIG.snapStructureToHeight)
        {
            box.move(0, StructureFloaters.SF_CONFIG.snapStructureToHeight - box.getMinY(), 0);
        }
    }
}