package com.telepathicgrunt.structurefloaters.mixin.worldgen;

import com.telepathicgrunt.structurefloaters.GeneralUtils;
import com.telepathicgrunt.structurefloaters.StructureFloaters;
import com.telepathicgrunt.structurefloaters.mixin.ChunkAccessor;
import net.minecraft.structure.ShiftableStructurePiece;
import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.structure.StructurePiecesCollector;
import net.minecraft.structure.StructurePiecesHolder;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;


@Mixin(ShiftableStructurePiece.class)
public abstract class ShiftableStructurePieceMixin extends StructurePiece {

    protected ShiftableStructurePieceMixin(StructurePieceType type, int length, BlockBox boundingBox) {
        super(type, length, boundingBox);
    }

    /**
     * @author TelepathicGrunt
     * @reason Prevent structures from being placed at world bottom if disallowed in config
     */
    @Inject(
            method = "adjustToAverageHeight(Lnet/minecraft/world/WorldAccess;Lnet/minecraft/util/math/BlockBox;I)Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/WorldAccess;getTopPosition(Lnet/minecraft/world/Heightmap$Type;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/util/math/BlockPos;"),
            cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void structurefloaters_disableHeightmapSnap(WorldAccess world, BlockBox boundingBox, int height, CallbackInfoReturnable<Boolean> cir,
                                                   int j, int k, BlockPos.Mutable mutable)
    {
        if (GeneralUtils.isWorldDisallowed(world)) return;
        if(StructureFloaters.SF_CONFIG.removeStructuresOffIslands &&
                world instanceof ChunkRegion &&
                ((ChunkRegion)world).toServerWorld().getChunkManager().getChunkGenerator().getSeaLevel() <= ((ChunkRegion)world).toServerWorld().getChunkManager().getChunkGenerator().getMinimumY() &&
                world.getTopPosition(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, mutable).getY() <= world.getBottomY())
        {
            cir.setReturnValue(false);
        }
    }

    /**
     * @author TelepathicGrunt
     * @reason Prevent structures from being placed at world bottom if disallowed in config
     */
    @ModifyVariable(
            method = "adjustToMinHeight(Lnet/minecraft/world/WorldAccess;I)Z",
            at = @At(value = "INVOKE_ASSIGN", target = "Ljava/lang/Math;min(II)I"),
            index = 3)
    private int structurefloaters_setHeightmapSnap(int heightmapY, WorldAccess world)
    {
        if (GeneralUtils.isWorldDisallowed(world)) return heightmapY;
        if(!StructureFloaters.SF_CONFIG.removeStructuresOffIslands &&
                world instanceof ChunkRegion &&
                ((ChunkRegion) world).toServerWorld().getChunkManager().getChunkGenerator().getSeaLevel() <= ((ChunkRegion) world).toServerWorld().getChunkManager().getChunkGenerator().getMinimumY() &&
                heightmapY < StructureFloaters.SF_CONFIG.snapStructureToHeight)
        {
            return StructureFloaters.SF_CONFIG.snapStructureToHeight;
        }
        return heightmapY;
    }

}
