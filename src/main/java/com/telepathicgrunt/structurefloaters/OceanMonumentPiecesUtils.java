package com.telepathicgrunt.structurefloaters;

import com.telepathicgrunt.structurefloaters.mixin.worldgen.StructurePieceAccessor;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.structure.OceanMonumentGenerator;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;

public class OceanMonumentPiecesUtils {
    // called in structures/OceanMonumentPiecesMonumentBuildingMixin
    public static void generateWaterBox(StructureWorldAccess world, ChunkGenerator chunkGenerator, OceanMonumentGenerator.Base monument, BlockBox mutableBoundingBox) {
        BlockState water = Blocks.WATER.getDefaultState();
        BlockState prismarineDark = Blocks.DARK_PRISMARINE.getDefaultState();
        BlockState prismarineBricks = Blocks.PRISMARINE_BRICKS.getDefaultState();
        BlockState prismarineRough = Blocks.PRISMARINE.getDefaultState();
        BlockState seaLantern = Blocks.SEA_LANTERN.getDefaultState();

        // right leg
        ((StructurePieceAccessor)monument).structurefloaters_callFillWithOutline(world, mutableBoundingBox, 0, 1, 0, 24, 1, 57, water, water, false);
        ((StructurePieceAccessor)monument).structurefloaters_callFillWithOutline(world, mutableBoundingBox, 1, 2, 1, 23, 2, 56, water, water, false);
        ((StructurePieceAccessor)monument).structurefloaters_callFillWithOutline(world, mutableBoundingBox, 2, 3, 2, 22, 3, 55, water, water, false);
        ((StructurePieceAccessor)monument).structurefloaters_callFillWithOutline(world, mutableBoundingBox, 3, 4, 3, 21, 4, 54, water, water, false);
        ((StructurePieceAccessor)monument).structurefloaters_callFillWithOutline(world, mutableBoundingBox, 7, 5, 7, 17, 5, 54, water, water, false);
        ((StructurePieceAccessor)monument).structurefloaters_callFillWithOutline(world, mutableBoundingBox, 8, 6, 8, 16, 6, 54, water, water, false);
        ((StructurePieceAccessor)monument).structurefloaters_callFillWithOutline(world, mutableBoundingBox, 9, 7, 9, 15, 7, 54, water, water, false);
        ((StructurePieceAccessor)monument).structurefloaters_callFillWithOutline(world, mutableBoundingBox, 10, 8, 10, 14, 8, 54, water, water, false);

        // left leg
        ((StructurePieceAccessor)monument).structurefloaters_callFillWithOutline(world, mutableBoundingBox, 33, 1, 0, 57, 1, 57, water, water, false);
        ((StructurePieceAccessor)monument).structurefloaters_callFillWithOutline(world, mutableBoundingBox, 34, 2, 1, 56, 2, 56, water, water, false);
        ((StructurePieceAccessor)monument).structurefloaters_callFillWithOutline(world, mutableBoundingBox, 35, 3, 2, 55, 3, 55, water, water, false);
        ((StructurePieceAccessor)monument).structurefloaters_callFillWithOutline(world, mutableBoundingBox, 36, 4, 3, 54, 4, 54, water, water, false);
        ((StructurePieceAccessor)monument).structurefloaters_callFillWithOutline(world, mutableBoundingBox, 40, 5, 7, 50, 5, 54, water, water, false);
        ((StructurePieceAccessor)monument).structurefloaters_callFillWithOutline(world, mutableBoundingBox, 41, 6, 8, 49, 6, 54, water, water, false);
        ((StructurePieceAccessor)monument).structurefloaters_callFillWithOutline(world, mutableBoundingBox, 42, 7, 9, 48, 7, 54, water, water, false);
        ((StructurePieceAccessor)monument).structurefloaters_callFillWithOutline(world, mutableBoundingBox, 43, 8, 10, 47, 8, 54, water, water, false);

        // main body
        ((StructurePieceAccessor)monument).structurefloaters_callFillWithOutline(world, mutableBoundingBox, 14, 1, 22, 44, 1, 57, water, water, false);
        ((StructurePieceAccessor)monument).structurefloaters_callFillWithOutline(world, mutableBoundingBox, 14, 2, 22, 44, 2, 56, water, water, false);
        ((StructurePieceAccessor)monument).structurefloaters_callFillWithOutline(world, mutableBoundingBox, 14, 1, 22, 44, 8, 54, water, water, false);
        ((StructurePieceAccessor)monument).structurefloaters_callFillWithOutline(world, mutableBoundingBox, 14, 1, 22, 44, 8, 54, water, water, false);
        ((StructurePieceAccessor)monument).structurefloaters_callFillWithOutline(world, mutableBoundingBox, 15, 8, 21, 43, 9, 42, water, water, false);
        ((StructurePieceAccessor)monument).structurefloaters_callFillWithOutline(world, mutableBoundingBox, 16, 10, 21, 42, 10, 41, water, water, false);
        ((StructurePieceAccessor)monument).structurefloaters_callFillWithOutline(world, mutableBoundingBox, 17, 11, 21, 41, 11, 40, water, water, false);

        // top
        ((StructurePieceAccessor)monument).structurefloaters_callFillWithOutline(world, mutableBoundingBox, 21, 12, 21, 36, 12, 36, water, water, false);
        ((StructurePieceAccessor)monument).structurefloaters_callFillWithOutline(world, mutableBoundingBox, 22, 13, 22, 35, 13, 35, water, water, false);
        ((StructurePieceAccessor)monument).structurefloaters_callFillWithOutline(world, mutableBoundingBox, 23, 14, 23, 34, 14, 34, water, water, false);
        ((StructurePieceAccessor)monument).structurefloaters_callFillWithOutline(world, mutableBoundingBox, 24, 15, 24, 33, 15, 33, water, water, false);

        // creates opening in front of entrance
        ((StructurePieceAccessor)monument).structurefloaters_callFillWithOutline(world, mutableBoundingBox, 25, 0, 10, 32, 3, 20, prismarineDark, water, false);
        ((StructurePieceAccessor)monument).structurefloaters_callFillWithOutline(world, mutableBoundingBox, 25, 0, 10, 32, 0, 10, prismarineBricks, prismarineBricks, false);
        ((StructurePieceAccessor)monument).structurefloaters_callFillWithOutline(world, mutableBoundingBox, 32, 0, 10, 32, 0, 20, prismarineBricks, prismarineBricks, false);
        ((StructurePieceAccessor)monument).structurefloaters_callFillWithOutline(world, mutableBoundingBox, 25, 0, 10, 25, 0, 20, prismarineBricks, prismarineBricks, false);
        ((StructurePieceAccessor)monument).structurefloaters_callFillWithOutline(world, mutableBoundingBox, 25, 3, 10, 32, 3, 10, prismarineBricks, prismarineBricks, false);
        ((StructurePieceAccessor)monument).structurefloaters_callFillWithOutline(world, mutableBoundingBox, 32, 3, 10, 32, 3, 20, prismarineBricks, prismarineBricks, false);
        ((StructurePieceAccessor)monument).structurefloaters_callFillWithOutline(world, mutableBoundingBox, 25, 3, 10, 25, 3, 20, prismarineBricks, prismarineBricks, false);
        ((StructurePieceAccessor)monument).structurefloaters_callFillWithOutline(world, mutableBoundingBox, 25, 1, 10, 25, 2, 10, prismarineRough, prismarineRough, false);
        ((StructurePieceAccessor)monument).structurefloaters_callFillWithOutline(world, mutableBoundingBox, 32, 1, 10, 32, 2, 10, prismarineRough, prismarineRough, false);
        ((StructurePieceAccessor)monument).structurefloaters_callAddBlock(world, seaLantern, 25, 1, 12, mutableBoundingBox);
        ((StructurePieceAccessor)monument).structurefloaters_callAddBlock(world, seaLantern, 32, 1, 12, mutableBoundingBox);
        ((StructurePieceAccessor)monument).structurefloaters_callAddBlock(world, seaLantern, 25, 1, 14, mutableBoundingBox);
        ((StructurePieceAccessor)monument).structurefloaters_callAddBlock(world, seaLantern, 32, 1, 14, mutableBoundingBox);
        ((StructurePieceAccessor)monument).structurefloaters_callAddBlock(world, seaLantern, 25, 1, 16, mutableBoundingBox);
        ((StructurePieceAccessor)monument).structurefloaters_callAddBlock(world, seaLantern, 32, 1, 16, mutableBoundingBox);
        ((StructurePieceAccessor)monument).structurefloaters_callAddBlock(world, seaLantern, 25, 1, 18, mutableBoundingBox);
        ((StructurePieceAccessor)monument).structurefloaters_callAddBlock(world, seaLantern, 32, 1, 18, mutableBoundingBox);
        ((StructurePieceAccessor)monument).structurefloaters_callAddBlock(world, seaLantern, 25, 1, 20, mutableBoundingBox);
        ((StructurePieceAccessor)monument).structurefloaters_callAddBlock(world, seaLantern, 32, 1, 20, mutableBoundingBox);
        ((StructurePieceAccessor)monument).structurefloaters_callAddBlock(world, seaLantern, 27, 1, 10, mutableBoundingBox);
        ((StructurePieceAccessor)monument).structurefloaters_callAddBlock(world, seaLantern, 30, 1, 10, mutableBoundingBox);
        ((StructurePieceAccessor)monument).structurefloaters_callFillWithOutline(world, mutableBoundingBox, 26, 3, 11, 31, 3, 20, water, water, false);
        ((StructurePieceAccessor)monument).structurefloaters_callFillWithOutline(world, mutableBoundingBox, 26, 1, 20, 31, 3, 20, water, water, false);
        ((StructurePieceAccessor)monument).structurefloaters_callFillWithOutline(world, mutableBoundingBox, 26, 1, 21, 31, 3, 21, water, water, false);

        // clears out all solid blocks in a column until it hits non-solid blocks to
        // create an opening to entrance
        BlockPos.Mutable blockpos = new BlockPos.Mutable();
        for (int y = 4; y < 16; y++) {
            for (int x = 26; x < 32; x++) {
                for (int z = 11; z < 21; z++) {

                    BlockState blockState = ((StructurePieceAccessor)monument).structurefloaters_callGetBlockAt(world, x, y, z, mutableBoundingBox);
                    int i = ((StructurePieceAccessor)monument).structurefloaters_callApplyXTransform(x, z);
                    int j = ((StructurePieceAccessor)monument).structurefloaters_callApplyYTransform(y);
                    int k = ((StructurePieceAccessor)monument).structurefloaters_callApplyZTransform(x, z);
                    blockpos.set(i, j, k);
                    int offset = 0;

                    if (blockState.isOpaque() || !blockState.canPlaceAt(world, blockpos)) {
                        ((StructurePieceAccessor)monument).structurefloaters_callAddBlock(world, water, x, y, z, mutableBoundingBox);

                        offset++;
                        blockpos.move(Direction.UP);
                        while(blockpos.getY() < chunkGenerator.getSeaLevel() && !blockState.canPlaceAt(world, blockpos)){
                            ((StructurePieceAccessor)monument).structurefloaters_callAddBlock(world, water, x, y + offset, z, mutableBoundingBox);
                            blockpos.move(Direction.UP);
                            offset++;
                        }
                    }
                    else {
                        x = 32;
                        y = 16;
                        break;
                    }
                }
            }
        }
    }
}
