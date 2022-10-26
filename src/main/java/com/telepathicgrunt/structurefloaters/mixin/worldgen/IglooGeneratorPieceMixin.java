package com.telepathicgrunt.structurefloaters.mixin.worldgen;

import com.telepathicgrunt.structurefloaters.StructureFloaters;
import net.minecraft.structure.IglooGenerator;
import net.minecraft.util.Identifier;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mixin(IglooGenerator.Piece.class)
public abstract class IglooGeneratorPieceMixin {

    @Unique
    private static final Pattern SF_PATTERN = Pattern.compile("minecraft:igloo (\\d+)");

    /**
     * @author TelepathicGrunt
     * @reason Place igloo correctly into air
     */
    @ModifyVariable(
            method = "generate(Lnet/minecraft/world/StructureWorldAccess;Lnet/minecraft/world/gen/StructureAccessor;Lnet/minecraft/world/gen/chunk/ChunkGenerator;Ljava/util/Random;Lnet/minecraft/util/math/BlockBox;Lnet/minecraft/util/math/ChunkPos;Lnet/minecraft/util/math/BlockPos;)V",
            at = @At(target = "Lnet/minecraft/world/StructureWorldAccess;getTopY(Lnet/minecraft/world/Heightmap$Type;II)I",
                    value = "INVOKE_ASSIGN", ordinal = 0), ordinal = 0
    )
    private int structurefloaters_fixedYHeight(int y, StructureWorldAccess world, StructureAccessor structureManager, ChunkGenerator chunkGenerator)
    {
        if(!StructureFloaters.STRUCTURES_TO_IGNORE.contains(new Identifier("minecraft:igloo")) &&
            world.toServerWorld().getChunkManager().getChunkGenerator().getSeaLevel() <= world.toServerWorld().getChunkManager().getChunkGenerator().getMinimumY())
        {
            AtomicInteger targetYValue = new AtomicInteger(StructureFloaters.SF_CONFIG.snapStructureToHeight);
            Matcher matcher = SF_PATTERN.matcher(StructureFloaters.SF_CONFIG.yValueOverridePerStructure);
            matcher.results().forEach(e -> targetYValue.set(Integer.parseInt(e.group(1))));

            if(!StructureFloaters.SF_CONFIG.removeStructuresOffIslands && y < targetYValue.get())
            {
                return targetYValue.get();
            }
            else if(StructureFloaters.SF_CONFIG.removeStructuresOffIslands && y <= world.getBottomY())
            {
                // Force it to return work bottom so StructureMixin can yeet this igloo piece as otherwise, it would hover a few blocks over world bottom.
                return (world.getBottomY() - 1) - 90;
            }
        }

        return y;
    }
}