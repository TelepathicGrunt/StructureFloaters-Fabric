package com.telepathicgrunt.structurefloaters.mixin.worldgen;

import com.telepathicgrunt.structurefloaters.StructureFloaters;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;


@Mixin(Structure.class)
public class StructureMixin {

    /**
     * @author TelepathicGrunt
     * @reason Prevent template from being placed at world bottom if disallowed in config
     */
    @Inject(
            method = "place(Lnet/minecraft/world/ServerWorldAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/structure/StructurePlacementData;Ljava/util/Random;I)Z",
            at = @At(value = "HEAD"),
            cancellable = true
    )
    private void structurefloaters_removeWorldBottomStructures(ServerWorldAccess world, BlockPos pos, BlockPos pivot,
                                                  StructurePlacementData placementData, Random random, int i,
                                                  CallbackInfoReturnable<Boolean> cir)
    {
        if(StructureFloaters.SF_CONFIG.removeWorldBottomPieces &&
            !world.getClass().getSimpleName().contains("SchematicWorld") &&
            !world.getClass().getSimpleName().contains("PonderWorld") &&
            world.toServerWorld().getChunkManager().getChunkGenerator().getSeaLevel() <= world.toServerWorld().getChunkManager().getChunkGenerator().getMinimumY())
        {
            if(pos.getY() <= world.getBottomY())
            {
                cir.setReturnValue(false);
            }
        }
    }
}
