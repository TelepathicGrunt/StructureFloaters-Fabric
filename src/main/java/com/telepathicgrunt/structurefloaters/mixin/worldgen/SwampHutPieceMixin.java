package com.telepathicgrunt.structurefloaters.mixin.worldgen;

import com.telepathicgrunt.structurefloaters.GeneralUtils;
import com.telepathicgrunt.structurefloaters.StructureFloaters;
import net.minecraft.structure.SwampHutGenerator;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mixin(SwampHutGenerator.class)
public abstract class SwampHutPieceMixin {

    @Unique
    private static final Identifier WITCH_HUT_ID = new Identifier("minecraft:swamp_hut");

    @Unique
    private static final Pattern SF_PATTERN = Pattern.compile("minecraft:swamp_hut (\\d+)");

    /**
     * @author TelepathicGrunt
     * @reason Place Witch Huts on top of land properly
     */
    @Inject(
            method = "generate(Lnet/minecraft/world/StructureWorldAccess;Lnet/minecraft/world/gen/StructureAccessor;Lnet/minecraft/world/gen/chunk/ChunkGenerator;Ljava/util/Random;Lnet/minecraft/util/math/BlockBox;Lnet/minecraft/util/math/ChunkPos;Lnet/minecraft/util/math/BlockPos;)V",
            at = @At(target = "Lnet/minecraft/structure/SwampHutGenerator;fillWithOutline(Lnet/minecraft/world/StructureWorldAccess;Lnet/minecraft/util/math/BlockBox;IIIIIILnet/minecraft/block/BlockState;Lnet/minecraft/block/BlockState;Z)V",
                    value = "INVOKE", ordinal = 0)
    )
    private void structurefloaters_fixedYHeight(StructureWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockBox chunkBox, ChunkPos chunkPos, BlockPos pos, CallbackInfo ci) {

        if (GeneralUtils.isWorldDisallowed(world)) return;
        BlockBox box = ((SwampHutGenerator)(Object)this).getBoundingBox();
        if(!StructureFloaters.STRUCTURES_TO_IGNORE.contains(WITCH_HUT_ID) &&
            chunkGenerator.getSeaLevel() <= chunkGenerator.getMinimumY())
        {
            AtomicInteger targetYValue = new AtomicInteger(StructureFloaters.SF_CONFIG.snapStructureToHeight);
            Matcher matcher = SF_PATTERN.matcher(StructureFloaters.SF_CONFIG.yValueOverridePerStructure);
            matcher.results().forEach(e -> targetYValue.set(Integer.parseInt(e.group(1))));

            if (box.getMinY() < targetYValue.get()) {
                box.move(0, targetYValue.get() - box.getMinY(), 0);
            }
        }
    }
}