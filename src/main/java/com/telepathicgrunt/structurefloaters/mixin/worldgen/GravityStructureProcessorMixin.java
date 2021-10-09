package com.telepathicgrunt.structurefloaters.mixin.worldgen;

import com.telepathicgrunt.structurefloaters.StructureFloaters;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.processor.GravityStructureProcessor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;


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
