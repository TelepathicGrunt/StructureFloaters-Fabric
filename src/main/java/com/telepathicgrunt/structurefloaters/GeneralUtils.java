package com.telepathicgrunt.structurefloaters;

import com.telepathicgrunt.structurefloaters.mixin.ChunkAccessor;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.chunk.Chunk;

import java.util.function.Predicate;

public class GeneralUtils {

    //////////////////////////////////////////////

    public static int getFirstLandYFromPos(WorldView worldView, BlockPos pos, Predicate<BlockState> predicate) {
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        mutable.set(pos);
        Chunk currentChunk = worldView.getChunk(mutable);
        BlockState currentState = currentChunk.getBlockState(mutable);

        while(mutable.getY() >= worldView.getBottomY() && predicate.test(currentState)) {
            mutable.move(Direction.DOWN);
            currentState = currentChunk.getBlockState(mutable);
        }

        return mutable.getY();
    }

    public static boolean isReplaceableByStructures(BlockState blockState) {
        return blockState.isAir() || blockState.getMaterial().isLiquid()  || blockState.isOf(Blocks.GLOW_LICHEN) || blockState.isOf(Blocks.SEAGRASS) || blockState.isOf(Blocks.TALL_SEAGRASS);
    }

    public static boolean isReplaceableByMansions(BlockState blockState) {
        return blockState.isAir() || blockState.getMaterial().isLiquid();
    }

    public static boolean isWorldDisallowed(HeightLimitView world) {
        World castedWorld = null;
        if (world instanceof Chunk chunk) {
            HeightLimitView view = ((ChunkAccessor)chunk).getHeightLimitViewField();
            if (view instanceof World chunkWorld) {
                castedWorld = chunkWorld;
            }
        }
        else if (world instanceof World rawWorld) {
            castedWorld = rawWorld;
        }
        return castedWorld != null && StructureFloaters.DIMENSIONS_DISALLOWED.contains(castedWorld.getRegistryKey());
    }
}