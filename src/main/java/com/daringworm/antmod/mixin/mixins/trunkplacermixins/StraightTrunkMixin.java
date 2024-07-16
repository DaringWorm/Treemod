package com.daringworm.antmod.mixin.mixins.trunkplacermixins;


import com.daringworm.antmod.mixin.tomixin.Branch;
import com.daringworm.antmod.mixin.tomixin.BranchTip;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;

import static com.daringworm.antmod.mixin.tomixin.TrunkAndFoliageMixinUtils.*;

@Mixin(StraightTrunkPlacer.class)
public abstract class StraightTrunkMixin{



    @Inject(at = @At("HEAD"), method = "placeTrunk", cancellable = true)
    public void mixin(LevelSimulatedReader simLevel, BiConsumer<BlockPos, BlockState> biConsumer, Random rand, int desiredHeight, BlockPos pPos, TreeConfiguration pConfiguration, CallbackInfoReturnable<List<FoliagePlacer.FoliageAttachment>> callback) {
        setDirtAt(simLevel, biConsumer, rand, pPos.below(), pConfiguration);
        List<FoliagePlacer.FoliageAttachment> foliageAttachments = new ArrayList<>();

        float branchIfSmallerThan = 20;

        boolean shouldItFork = true;
        //does NOT work
        if(pConfiguration.foliageProvider.equals(BlockStateProvider.simple(Blocks.SPRUCE_LEAVES))){
            shouldItFork = false;
        }

        int height = rand.nextInt(7)+desiredHeight;
        if(height> 15){height = 15;}else if(height<0){height = 1;}

        if(!shouldItFork) {
            for (int i = 0; i < height; ++i) {
                placeLog(simLevel, biConsumer, rand, pPos.above(i), pConfiguration, 15-i, Direction.DOWN);
                foliageAttachments.add(new FoliagePlacer.FoliageAttachment(pPos.above(height), 0, false));
            }
        }
        else{
            List<BranchTip> branchTipList = new ArrayList<>();
            branchTipList.add(new BranchTip(pPos,Direction.DOWN,height, 0,true));
            List<BranchTip> nextRotTipList = new ArrayList<>();
            placeLog(simLevel, biConsumer, rand, pPos, pConfiguration, height, Direction.DOWN);
            int stepsWithoutBranching = 0;

            for(int step = 0; step < height*2; step++) {
                for(BranchTip tip : branchTipList) {
                    boolean shouldBranch = rand.nextInt(100)-(tip.stepsSinceBranch) < branchIfSmallerThan;
                    boolean shouldLookBackUp = rand.nextInt(100)-(tip.branchSizeAt)-tip.stepsSinceBranch < branchIfSmallerThan;
                    int branchSizeAfterReduction = tip.branchSizeAt-(rand.nextInt(tip.branchSizeAt)/2);
                    if(branchSizeAfterReduction < 1){branchSizeAfterReduction=1;}
                    boolean cancelOriginalBranch = (tip.branchSizeAt - branchSizeAfterReduction) < 2;

                    if(tip.branchSizeAt <= 1){
                        nextRotTipList.remove(tip);
                        foliageAttachments.add(new FoliagePlacer.FoliageAttachment(tip.blockPosition, 0, false));
                    }
                    else {
                        if (shouldBranch) {
                            BranchTip newBranch = getBranchForGenerator(simLevel, tip.blockPosition, tip.pssbleBrnchDrs, rand, branchSizeAfterReduction);
                            if (newBranch.isValid) {
                                placeLog(simLevel, biConsumer, rand, newBranch.blockPosition, pConfiguration, branchSizeAfterReduction, newBranch.blockDirection.getOpposite());
                                nextRotTipList.add(newBranch);
                                if(!cancelOriginalBranch){nextRotTipList.add(tip);}
                            }
                        }
                        if(!shouldBranch || !cancelOriginalBranch){
                            if(!shouldLookBackUp) {
                                BlockPos nextLinearPos = tip.blockPosition.relative(tip.blockDirection.getOpposite());
                                placeLog(simLevel, biConsumer, rand, nextLinearPos, pConfiguration, tip.branchSizeAt - 1, tip.blockDirection);
                                nextRotTipList.add(new BranchTip(nextLinearPos, tip.blockDirection, tip.branchSizeAt - 1, tip.stepsSinceBranch+1, true));
                            }
                            else{
                                BlockPos upPos = tip.blockPosition.above();
                                placeLog(simLevel, biConsumer, rand, upPos, pConfiguration, tip.branchSizeAt - 1, Direction.DOWN);
                                nextRotTipList.add(new BranchTip(upPos, Direction.DOWN, tip.branchSizeAt - 1, 0, true));
                            }
                        }
                    }
                }
                branchTipList.clear();
                branchTipList.addAll(nextRotTipList);
                nextRotTipList.clear();
                if(branchTipList.isEmpty()){step = (height*2)+1;}
            }
        }

        foliageAttachments.clear();
        callback.setReturnValue(foliageAttachments);
    }
}


