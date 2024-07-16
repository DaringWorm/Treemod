package com.daringworm.antmod.mixin.tomixin;

import com.daringworm.antmod.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.*;

public interface TrunkAndFoliageMixinUtils {

    static boolean isDirt(LevelSimulatedReader pLevel, BlockPos pPos) {
        return pLevel.isStateAtPosition(pPos, (p_70304_) -> {
            return Feature.isDirt(p_70304_) && !p_70304_.is(Blocks.GRASS_BLOCK) && !p_70304_.is(Blocks.MYCELIUM);
        });
    }

    static boolean canGrowSomethingHere(LevelSimulatedReader pLevel, BlockPos pPos) {
        return pLevel.isStateAtPosition(pPos, (blockState) -> {return blockState.is(Blocks.AIR);});
    }

    static BranchTip getBranchForGenerator(LevelSimulatedReader pLevel, BlockPos startPos, List<Direction> possibleDirs, Random rand, int age){
        List<Direction> allowedDirList = new ArrayList<>();
        boolean allowDown = rand.nextBoolean() && rand.nextBoolean() && rand.nextBoolean();
        for(Direction dir : possibleDirs){
            BlockPos tempPos = startPos.relative(dir);
            if(canGrowSomethingHere(pLevel,tempPos)){
                allowedDirList.add(dir);
            }
        }
        if(!allowDown){allowedDirList.remove(Direction.DOWN);}
        Direction chosenDir;
        boolean isValid = true;
        if(!allowedDirList.isEmpty()){
            chosenDir = allowedDirList.get(rand.nextInt(allowedDirList.size()));
        }
        else{
            isValid = false;
            chosenDir = Direction.DOWN;
        }

        return new BranchTip(startPos.relative(chosenDir),chosenDir, age, 0, isValid);
    }

    static void setDirtAt(LevelSimulatedReader pLevel, BiConsumer<BlockPos, BlockState> pBlockSetter, Random pRandom, BlockPos pPos, TreeConfiguration pConfig) {
        if (pConfig.forceDirt || !isDirt(pLevel, pPos)) {
            pBlockSetter.accept(pPos, pConfig.dirtProvider.getState(pRandom, pPos));
        }

    }

    static boolean placeLog(LevelSimulatedReader pLevel, BiConsumer<BlockPos, BlockState> pBlockSetter, Random pRandom, BlockPos pPos, TreeConfiguration pConfig, int size, Direction shouldFace) {
        return placeLog(pLevel, pBlockSetter, pPos, pConfig, Function.identity(), size, shouldFace);
    }

    static boolean placeLog(LevelSimulatedReader pLevel, BiConsumer<BlockPos, BlockState> pBlockSetter, BlockPos pPos, TreeConfiguration pConfig, Function<BlockState, BlockState> pPropertySetter, int size, Direction shouldFace) {
        if (TreeFeature.validTreePos(pLevel, pPos)) {
            int cookedSize = size;
            if(cookedSize>15){cookedSize=15;} else if(cookedSize<0){cookedSize=0;}
            pBlockSetter.accept(pPos, pPropertySetter.apply(ModBlocks.DEFAULT_TRUNK.get().defaultBlockState().setValue(AGE_15, cookedSize).setValue(FACING, shouldFace)));
            return true;
        } else {
            return false;
        }
    }

    /*default void placeLogIfFree(LevelSimulatedReader pLevel, BiConsumer<BlockPos, BlockState> pBlockSetter, Random pRandom, BlockPos.MutableBlockPos pPos, TreeConfiguration pConfig, int step) {
        if (TreeFeature.isFree(pLevel, pPos)) {
            placeLog(pLevel, pBlockSetter, pRandom, pPos, pConfig, step,Direction.DOWN);
        }

    }*/
}
