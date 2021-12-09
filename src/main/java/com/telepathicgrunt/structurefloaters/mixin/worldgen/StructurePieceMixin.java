package com.telepathicgrunt.structurefloaters.mixin.worldgen;

import com.telepathicgrunt.structurefloaters.StructureFloaters;
import net.minecraft.block.BlockState;
import net.minecraft.structure.StructurePiece;
import net.minecraft.util.math.BlockBox;
import net.minecraft.world.StructureWorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(StructurePiece.class)
public class StructurePieceMixin {

    /**
     * @author TelepathicGrunt
     * @reason Prevent some structures from placing pillars if disallowed in config
     */
    @Inject(
            method = "fillDownwards(Lnet/minecraft/world/StructureWorldAccess;Lnet/minecraft/block/BlockState;IIILnet/minecraft/util/math/BlockBox;)V",
            at = @At(value = "HEAD"),
            cancellable = true
    )
    private void structurefloaters_disablePillars(StructureWorldAccess world, BlockState state, int x, int y, int z, BlockBox box, CallbackInfo ci) {
        if(StructureFloaters.cancelPillars(world, ((StructurePiece)(Object)this), x, y, z)){
            ci.cancel();
        }
    }
}
