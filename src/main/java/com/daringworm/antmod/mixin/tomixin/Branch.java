package com.daringworm.antmod.mixin.tomixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public class Branch {
    public final BlockPos blockPosition;
    public final Direction blockDirection;
    public final int branchSizeAt;
    public final boolean exists;

    public Branch(BlockPos pPos, Direction dir, int age, boolean exists) {
        this.blockPosition = pPos;
        this.blockDirection = dir;
        this.branchSizeAt = age;
        this.exists = exists;
    }
}
