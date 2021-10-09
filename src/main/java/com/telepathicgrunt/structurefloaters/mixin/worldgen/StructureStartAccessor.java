package com.telepathicgrunt.structurefloaters.mixin.worldgen;

import net.minecraft.structure.StructureStart;
import net.minecraft.world.gen.ChunkRandom;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Random;

@Mixin(StructureStart.class)
public interface StructureStartAccessor {
    @Invoker("randomUpwardTranslation")
    void structurefloaters_callRandomUpwardTranslation(Random p_214626_1_, int p_214626_2_, int p_214626_3_);

    @Accessor("random")
    ChunkRandom structurefloaters_getRandom();
}
