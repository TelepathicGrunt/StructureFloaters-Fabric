package com.telepathicgrunt.structurefloaters.mixin.worldgen;

import com.telepathicgrunt.structurefloaters.GeneralUtils;
import com.telepathicgrunt.structurefloaters.StructureFloaters;
import com.telepathicgrunt.structurefloaters.mixin.ChunkAccessor;
import net.minecraft.structure.ShipwreckGenerator;
import net.minecraft.structure.SimpleStructurePiece;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Mixin(ShipwreckGenerator.Piece.class)
public abstract class ShipwreckGeneratorPieceMixin extends SimpleStructurePiece {

    @Unique
    private static final Identifier SHIPWRECK_ID = new Identifier("minecraft:shipwreck");

    @Unique
    private static final Pattern SF_PATTERN = Pattern.compile("minecraft:shipwreck (\\d+)");

    @Final
    @Shadow
    private boolean grounded;

    public ShipwreckGeneratorPieceMixin(StructurePieceType type, int i, StructureManager structureManager, Identifier identifier, String string, StructurePlacementData placementData, BlockPos pos) {
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
                                                   ChunkPos chunkPos, BlockPos pos, CallbackInfo ci,
                                                   int i, int j)
    {
        if (GeneralUtils.isWorldDisallowed(world)) return;
        if(!StructureFloaters.STRUCTURES_TO_IGNORE.contains(SHIPWRECK_ID) &&
                StructureFloaters.SF_CONFIG.removeStructuresOffIslands &&
                chunkGenerator.getSeaLevel() <= chunkGenerator.getMinimumY() &&
                j <= world.getBottomY() + 1)
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
            ordinal = 1
    )
    private int structurefloaters_setHeightmapSnap(int heightmapY, StructureWorldAccess world,
                                              StructureAccessor structureAccessor, ChunkGenerator chunkGenerator)
    {
        if (GeneralUtils.isWorldDisallowed(world)) return heightmapY;
        Heightmap.Type type = this.grounded ? Heightmap.Type.WORLD_SURFACE_WG : Heightmap.Type.OCEAN_FLOOR_WG;
        if(!StructureFloaters.STRUCTURES_TO_IGNORE.contains(SHIPWRECK_ID) &&
                chunkGenerator.getSeaLevel() <= chunkGenerator.getMinimumY() &&
                !(StructureFloaters.SF_CONFIG.removeStructuresOffIslands &&
                world.getTopY(type, this.pos.getX(), this.pos.getZ()) <= chunkGenerator.getMinimumY() + 1))
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
            method = "generate(Lnet/minecraft/world/StructureWorldAccess;Lnet/minecraft/world/gen/StructureAccessor;Lnet/minecraft/world/gen/chunk/ChunkGenerator;Ljava/util/Random;Lnet/minecraft/util/math/BlockBox;Lnet/minecraft/util/math/ChunkPos;Lnet/minecraft/util/math/BlockPos;)V",
            at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/StructureWorldAccess;getTopY(Lnet/minecraft/world/Heightmap$Type;II)I", ordinal = 1, shift = At.Shift.AFTER),
            cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void structurefloaters_disableHeightmapSnap2(StructureWorldAccess world, StructureAccessor structureAccessor,
                                                    ChunkGenerator chunkGenerator, Random random, BlockBox boundingBox,
                                                    ChunkPos chunkPos, BlockPos pos, CallbackInfo ci,
                                                    int i, int j, Vec3i vec3i, Heightmap.Type type, int k, BlockPos blockPos,
                                                    Iterator<BlockPos> var14, BlockPos blockPos2, int l)
    {
        if (GeneralUtils.isWorldDisallowed(world)) return;
        if(!StructureFloaters.STRUCTURES_TO_IGNORE.contains(SHIPWRECK_ID) &&
                StructureFloaters.SF_CONFIG.removeStructuresOffIslands &&
                chunkGenerator.getSeaLevel() <= chunkGenerator.getMinimumY() &&
                l <= world.getBottomY() + 1)
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
            at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/StructureWorldAccess;getTopY(Lnet/minecraft/world/Heightmap$Type;II)I", ordinal = 1),
            ordinal = 3
    )
    private int structurefloaters_setHeightmapSnap2(int heightmapY,
                                                    StructureWorldAccess world,
                                                    StructureAccessor structureAccessor,
                                                    ChunkGenerator chunkGenerator)
    {
        if (GeneralUtils.isWorldDisallowed(world)) return heightmapY;
        Heightmap.Type type = this.grounded ? Heightmap.Type.WORLD_SURFACE_WG : Heightmap.Type.OCEAN_FLOOR_WG;
        if(!StructureFloaters.STRUCTURES_TO_IGNORE.contains(SHIPWRECK_ID) &&
                chunkGenerator.getSeaLevel() <= chunkGenerator.getMinimumY() &&
                !(StructureFloaters.SF_CONFIG.removeStructuresOffIslands &&
                        world.getTopY(type, this.pos.getX(), this.pos.getZ()) <= chunkGenerator.getMinimumY() + 1))
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

}
