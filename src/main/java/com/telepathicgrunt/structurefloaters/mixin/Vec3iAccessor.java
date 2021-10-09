package com.telepathicgrunt.structurefloaters.mixin;


import net.minecraft.util.math.Vec3i;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Vec3i.class)
public interface Vec3iAccessor {

    @Mutable
    @Accessor("y")
    void sf_setY(int y);
}

