package com.daringworm.antmod.mixin.tomixin;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

import java.util.ArrayList;
import java.util.List;

public class BranchTip{
    public final BlockPos blockPosition;
    public final Direction blockDirection;
    public List<Direction> pssbleBrnchDrs;
    public final int branchSizeAt;
    public final int stepsSinceBranch;

    public final boolean isValid;

    public BranchTip(BlockPos pPos, Direction dir, int age, int stepssince, boolean valid) {
        List<Direction> tempList = new ArrayList<>();
        tempList.add(Direction.UP);tempList.add(Direction.DOWN);tempList.add(Direction.EAST);tempList.add(Direction.WEST);tempList.add(Direction.NORTH);tempList.add(Direction.SOUTH);
        tempList.remove(dir.getOpposite());tempList.remove(dir);
        this.blockPosition = pPos;
        this.blockDirection = dir;
        this.branchSizeAt = age;
        this.isValid = valid;
        this.stepsSinceBranch = stepssince;
        this.pssbleBrnchDrs = tempList;
    }
}
