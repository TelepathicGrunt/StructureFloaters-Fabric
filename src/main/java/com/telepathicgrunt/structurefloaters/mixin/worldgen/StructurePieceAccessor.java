package com.telepathicgrunt.structurefloaters.mixin.worldgen;

import net.minecraft.block.BlockState;
import net.minecraft.structure.StructurePiece;
import net.minecraft.util.math.BlockBox;
import net.minecraft.world.BlockView;
import net.minecraft.world.StructureWorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(StructurePiece.class)
public interface StructurePieceAccessor {
    @Invoker("getBlockAt")
    BlockState structurefloaters_callGetBlockAt(BlockView worldIn, int x, int y, int z, BlockBox boundingboxIn);

    @Invoker("applyXTransform")
    int structurefloaters_callApplyXTransform(int x, int z);

    @Invoker("applyYTransform")
    int structurefloaters_callApplyYTransform(int y);

    @Invoker("applyZTransform")
    int structurefloaters_callApplyZTransform(int x, int z);

    @Invoker("fillWithOutline")
    void structurefloaters_callFillWithOutline(StructureWorldAccess worldIn, BlockBox boundingboxIn, int xMin, int yMin, int zMin, int xMax, int yMax, int zMax, BlockState boundaryBlockState, BlockState insideBlockState, boolean existingOnly);

    @Invoker("addBlock")
    void structurefloaters_callAddBlock(StructureWorldAccess worldIn, BlockState blockstateIn, int x, int y, int z, BlockBox boundingboxIn);
}
