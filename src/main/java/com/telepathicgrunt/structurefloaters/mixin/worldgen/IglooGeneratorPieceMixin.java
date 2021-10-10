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

@Mixin(IglooGenerator.Piece.class)
public abstract class IglooGeneratorPieceMixin {

    @Unique
    private static final Identifier IGLOO_ID = new Identifier("minecraft:igloo");

    /**
     * @author TelepathicGrunt
     * @reason Place igloo correctly into air
     */
    @ModifyVariable(
            method = "generate(Lnet/minecraft/world/StructureWorldAccess;Lnet/minecraft/world/gen/StructureAccessor;Lnet/minecraft/world/gen/chunk/ChunkGenerator;Ljava/util/Random;Lnet/minecraft/util/math/BlockBox;Lnet/minecraft/util/math/ChunkPos;Lnet/minecraft/util/math/BlockPos;)Z",
            at = @At(target = "Lnet/minecraft/world/StructureWorldAccess;getTopY(Lnet/minecraft/world/Heightmap$Type;II)I",
                    value = "INVOKE_ASSIGN", ordinal = 0), ordinal = 0
    )
    private int structurefloaters_fixedYHeight(int y, StructureWorldAccess world, StructureAccessor structureManager, ChunkGenerator chunkGenerator)
    {
        if(!StructureFloaters.STRUCTURES_TO_IGNORE.contains(IGLOO_ID) &&
            world.toServerWorld().getChunkManager().getChunkGenerator().getSeaLevel() <= world.toServerWorld().getChunkManager().getChunkGenerator().getMinimumY())
        {

            if(!StructureFloaters.SF_CONFIG.removeWorldBottomStructures &&
                    y < StructureFloaters.SF_CONFIG.snapStructureToHeight)
            {
                return StructureFloaters.SF_CONFIG.snapStructureToHeight;
            }
            else if(StructureFloaters.SF_CONFIG.removeWorldBottomStructures &&
                    y <= world.getBottomY() + 1)
            {
                // Force it to return work bottom so StructureMixin can yeet this igloo piece as otherwise, it would hover a few blocks over world bottom.
                return world.getBottomY();
            }
        }

        return y;
    }
}