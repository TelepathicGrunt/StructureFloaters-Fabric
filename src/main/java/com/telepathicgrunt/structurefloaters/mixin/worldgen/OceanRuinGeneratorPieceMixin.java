package com.telepathicgrunt.structurefloaters.mixin.worldgen;

import com.telepathicgrunt.structurefloaters.StructureFloaters;
import net.minecraft.structure.OceanRuinGenerator;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Random;


@Mixin(OceanRuinGenerator.Piece.class)
public class OceanRuinGeneratorPieceMixin {

    @Unique
    private static final Identifier OCEAN_RUINS_ID = new Identifier("minecraft:ocean_ruin");

    /**
     * @author TelepathicGrunt
     * @reason Prevent structures from being placed at world bottom if disallowed in config
     */
    @Inject(
            method = "generate(Lnet/minecraft/world/StructureWorldAccess;Lnet/minecraft/world/gen/StructureAccessor;Lnet/minecraft/world/gen/chunk/ChunkGenerator;Ljava/util/Random;Lnet/minecraft/util/math/BlockBox;Lnet/minecraft/util/math/ChunkPos;Lnet/minecraft/util/math/BlockPos;)Z",
            at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/StructureWorldAccess;getTopY(Lnet/minecraft/world/Heightmap$Type;II)I", ordinal = 0),
            cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void structurefloaters_disableHeightmapSnap(StructureWorldAccess world, StructureAccessor structureAccessor,
                                                   ChunkGenerator chunkGenerator, Random random, BlockBox boundingBox,
                                                   ChunkPos chunkPos, BlockPos pos, CallbackInfoReturnable<Boolean> cir,
                                                   int heightmapY)
    {
        if(!StructureFloaters.STRUCTURES_TO_IGNORE.contains(OCEAN_RUINS_ID) &&
                StructureFloaters.SF_CONFIG.removeWorldBottomStructures &&
                chunkGenerator.getSeaLevel() <= chunkGenerator.getMinimumY() &&
                heightmapY <= world.getBottomY())
        {
            cir.setReturnValue(false);
        }
    }

    /**
     * @author TelepathicGrunt
     * @reason Prevent structures from being placed at world bottom if disallowed in config
     */
    @ModifyVariable(
            method = "generate(Lnet/minecraft/world/StructureWorldAccess;Lnet/minecraft/world/gen/StructureAccessor;Lnet/minecraft/world/gen/chunk/ChunkGenerator;Ljava/util/Random;Lnet/minecraft/util/math/BlockBox;Lnet/minecraft/util/math/ChunkPos;Lnet/minecraft/util/math/BlockPos;)Z",
            at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/StructureWorldAccess;getTopY(Lnet/minecraft/world/Heightmap$Type;II)I", ordinal = 0),
            ordinal = 0
    )
    private int structurefloaters_setHeightmapSnap(int heightmapY, StructureWorldAccess world,
                                              StructureAccessor structureAccessor, ChunkGenerator chunkGenerator)
    {
        if(!StructureFloaters.STRUCTURES_TO_IGNORE.contains(OCEAN_RUINS_ID) &&
            !StructureFloaters.SF_CONFIG.removeWorldBottomStructures &&
            chunkGenerator.getSeaLevel() <= chunkGenerator.getMinimumY() &&
            heightmapY < StructureFloaters.SF_CONFIG.snapStructureToHeight)
        {
            return StructureFloaters.SF_CONFIG.snapStructureToHeight;
        }
        return heightmapY;
    }

    /**
     * @author TelepathicGrunt
     * @reason Prevent structures from being placed at world bottom if disallowed in config
     */
    @Inject(
            method = "method_14829(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;)I",
            at = @At(value = "INVOKE_ASSIGN", target = "Ljava/lang/Math;abs(I)I", remap = false),
            cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void structurefloaters_disableHeightmapSnap2(BlockPos start, BlockView world, BlockPos end, CallbackInfoReturnable<Integer> cir,
                                                    int i, int j)
    {
        if(!StructureFloaters.STRUCTURES_TO_IGNORE.contains(OCEAN_RUINS_ID) &&
            world instanceof ChunkRegion &&
            ((ChunkRegion) world).toServerWorld().getChunkManager().getChunkGenerator().getSeaLevel() <= ((ChunkRegion) world).toServerWorld().getChunkManager().getChunkGenerator().getMinimumY())
        {
            if(!StructureFloaters.SF_CONFIG.removeWorldBottomStructures &&
                    j < StructureFloaters.SF_CONFIG.snapStructureToHeight)
            {
                cir.setReturnValue(StructureFloaters.SF_CONFIG.snapStructureToHeight);
            }
            else if(StructureFloaters.SF_CONFIG.removeWorldBottomStructures &&
                    j <= world.getBottomY() + 1)
            {
                // Force it to return work bottom so StructureMixin can yeet this ocean ruins piece as otherwise, it would hover a few blocks over world bottom.
                cir.setReturnValue(world.getBottomY());
            }
        }
    }
}
