package com.telepathicgrunt.structurefloaters.mixin.worldgen;

import com.telepathicgrunt.structurefloaters.GeneralUtils;
import com.telepathicgrunt.structurefloaters.StructureFloaters;
import com.telepathicgrunt.structurefloaters.mixin.ChunkAccessor;
import net.minecraft.structure.processor.GravityStructureProcessor;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;


@Mixin(GravityStructureProcessor.class)
public class GravityStructureProcessorMixin {

    /**
     * @author TelepathicGrunt
     * @reason floating roads
     */
    @ModifyVariable(
            method = "process(Lnet/minecraft/world/WorldView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/structure/Structure$StructureBlockInfo;Lnet/minecraft/structure/Structure$StructureBlockInfo;Lnet/minecraft/structure/StructurePlacementData;)Lnet/minecraft/structure/Structure$StructureBlockInfo;",
            at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/util/math/BlockPos;getY()I", ordinal = 0),
            ordinal = 0
    )
    private int structurefloaters_floatTerrainSnappers(int y, WorldView world)
    {
        if (GeneralUtils.isWorldDisallowed(world)) return y;

        if(world instanceof ServerWorldAccess && ((ServerWorldAccess)world).toServerWorld().getChunkManager().getChunkGenerator().getSeaLevel() <= ((ServerWorldAccess)world).toServerWorld().getChunkManager().getChunkGenerator().getMinimumY())
        {
            if(y <= world.getBottomY())
            {
                return StructureFloaters.SF_CONFIG.snapStructureToHeight;
            }
        }
        return y;
    }
}
