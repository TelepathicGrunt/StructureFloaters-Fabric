package com.telepathicgrunt.structurefloaters.mixin.worldgen;

import com.telepathicgrunt.structurefloaters.StructureFloaters;
import net.minecraft.structure.OceanRuinGenerator;
import net.minecraft.structure.SimpleStructurePiece;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Mixin(OceanRuinGenerator.Piece.class)
public abstract class OceanRuinGeneratorPieceMixin extends SimpleStructurePiece {

    @Unique
    private static final Identifier SF_OCEAN_RUINS_ID = new Identifier("minecraft:ocean_ruin");

    @Unique
    private static final Pattern SF_PATTERN = Pattern.compile("minecraft:ocean_ruin (\\d+)");

    public OceanRuinGeneratorPieceMixin(StructurePieceType type, int i, StructureManager structureManager, Identifier identifier, String string, StructurePlacementData placementData, BlockPos pos) {
        super(type, i, structureManager, identifier, string, placementData, pos);
    }


    /**
     * @author TelepathicGrunt
     * @reason Prevent structures from being placed at world bottom if disallowed in config
     */
    @Inject(
            method = "generate(Lnet/minecraft/world/StructureWorldAccess;Lnet/minecraft/world/gen/StructureAccessor;Lnet/minecraft/world/gen/chunk/ChunkGenerator;Ljava/util/Random;Lnet/minecraft/util/math/BlockBox;Lnet/minecraft/util/math/ChunkPos;Lnet/minecraft/util/math/BlockPos;)V",
            at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/StructureWorldAccess;getTopY(Lnet/minecraft/world/Heightmap$Type;II)I", ordinal = 0),
            cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void structurefloaters_disableHeightmapSnap(StructureWorldAccess world, StructureAccessor structureAccessor,
                                                   ChunkGenerator chunkGenerator, Random random, BlockBox boundingBox,
                                                   ChunkPos chunkPos, BlockPos pos, CallbackInfo ci, int heightmapY)
    {
        if(!StructureFloaters.STRUCTURES_TO_IGNORE.contains(SF_OCEAN_RUINS_ID) &&
                StructureFloaters.SF_CONFIG.removeStructuresOffIslands &&
                chunkGenerator.getSeaLevel() <= chunkGenerator.getMinimumY() &&
                heightmapY <= world.getBottomY() + 1)
        {
            ci.cancel();
        }
    }

    /**
     * @author TelepathicGrunt
     * @reason Prevent structures from being placed at world bottom if disallowed in config
     */
    @ModifyVariable(
            method = "generate(Lnet/minecraft/world/StructureWorldAccess;Lnet/minecraft/world/gen/StructureAccessor;Lnet/minecraft/world/gen/chunk/ChunkGenerator;Ljava/util/Random;Lnet/minecraft/util/math/BlockBox;Lnet/minecraft/util/math/ChunkPos;Lnet/minecraft/util/math/BlockPos;)V",
            at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/StructureWorldAccess;getTopY(Lnet/minecraft/world/Heightmap$Type;II)I", ordinal = 0),
            ordinal = 0
    )
    private int structurefloaters_setHeightmapSnap(int heightmapY, StructureWorldAccess world,
                                                   StructureAccessor structureAccessor, ChunkGenerator chunkGenerator)
    {
        if(!StructureFloaters.STRUCTURES_TO_IGNORE.contains(SF_OCEAN_RUINS_ID) &&
            chunkGenerator.getSeaLevel() <= chunkGenerator.getMinimumY() &&
            !(StructureFloaters.SF_CONFIG.removeStructuresOffIslands &&
                world.getTopY(Heightmap.Type.OCEAN_FLOOR_WG, this.pos.getX(), this.pos.getZ()) <= chunkGenerator.getMinimumY() + 1))
        {
            AtomicInteger targetYValue = new AtomicInteger(StructureFloaters.SF_CONFIG.snapStructureToHeight);
            Matcher matcher = SF_PATTERN.matcher(StructureFloaters.SF_CONFIG.yValueOverridePerStructure);
            matcher.results().forEach(e -> targetYValue.set(Integer.parseInt(e.group(1))));

            if (heightmapY < targetYValue.get()) {
                return targetYValue.get();
            }
        }
        return heightmapY;
    }

    /**
     * @author TelepathicGrunt
     * @reason Prevent structures from being placed at world bottom if disallowed in config
     */
    @Inject(
            method = "method_14829(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;)I",
            at = @At(value = "INVOKE_ASSIGN", target = "Ljava/lang/Math;abs(I)I", remap = false),
            cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void structurefloaters_disableHeightmapSnap2(BlockPos start, BlockView world, BlockPos end,
                                                         CallbackInfoReturnable<Integer> cir,
                                                         int i, int j)
    {
        if(!StructureFloaters.STRUCTURES_TO_IGNORE.contains(SF_OCEAN_RUINS_ID) &&
            world instanceof ChunkRegion &&
            ((ChunkRegion) world).toServerWorld().getChunkManager().getChunkGenerator().getSeaLevel() <= ((ChunkRegion) world).toServerWorld().getChunkManager().getChunkGenerator().getMinimumY())
        {
            if(StructureFloaters.SF_CONFIG.removeStructuresOffIslands &&
                    j <= world.getBottomY() + 1)
            {
                // Force it to return work bottom so StructureMixin can yeet this ocean ruins piece as otherwise, it would hover a few blocks over world bottom.
                cir.setReturnValue(world.getBottomY() - 1);
            }
            else {
                AtomicInteger targetYValue = new AtomicInteger(StructureFloaters.SF_CONFIG.snapStructureToHeight);
                Matcher matcher = SF_PATTERN.matcher(StructureFloaters.SF_CONFIG.yValueOverridePerStructure);
                matcher.results().forEach(e -> targetYValue.set(Integer.parseInt(e.group(1))));

                if(j < targetYValue.get())
                {
                    cir.setReturnValue(targetYValue.intValue());
                }
            }
        }
    }
}
