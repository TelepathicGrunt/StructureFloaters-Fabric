package com.telepathicgrunt.structurefloaters.mixin.worldgen;

import net.minecraft.structure.StructureGeneratorFactory;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.OceanMonumentFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(OceanMonumentFeature.class)
public class OceanMonumentStructureMixin {

    /**
     * @author TelepathicGrunt
     * @reason make Ocean Monuments skip their RIVER/OCEAN category checks if in floating island world. Otherwise, Monuments don't spawn. Mojank lmao
     */
    @Inject(
            method = "canGenerate(Lnet/minecraft/structure/StructureGeneratorFactory$Context;)Z",
            at = @At(value = "HEAD"),
            cancellable = true
    )
    private static void structurefloaters_skipCategoryChecks(StructureGeneratorFactory.Context<DefaultFeatureConfig> context, CallbackInfoReturnable<Boolean> cir)
    {
        if(context.chunkGenerator().getSeaLevel() <= context.chunkGenerator().getMinimumY() && context.isBiomeValid(Heightmap.Type.OCEAN_FLOOR_WG)){
            cir.setReturnValue(true);
        }
    }
}
