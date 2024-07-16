package com.daringworm.antmod.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.AGE_15;


public class BasicTrunkBlock extends DirectionalBlock {
    public BasicTrunkBlock(BlockBehaviour.Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.UP));
    }

    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        int age = 0;
        boolean dirHasBeenChosen = false;
        Direction dir2place = Direction.DOWN;
        Direction clickDir = pContext.getHorizontalDirection();
        BlockState clickDirState = pContext.getLevel().getBlockState(pContext.getClickedPos().relative(clickDir));
        BlockState stateUnder = pContext.getLevel().getBlockState(pContext.getClickedPos().below());
        Block blockUnder = stateUnder.getBlock();
        if(blockUnder == Blocks.DIRT || blockUnder == Blocks.GRASS_BLOCK){
            age = AGE_15.getPossibleValues().size()-1;
            dirHasBeenChosen = true;
        }
        if(clickDirState.getBlock() == this){
            age = clickDirState.getValue(AGE_15) - 1;
            dir2place = clickDir;
            dirHasBeenChosen = true;
        }
        else if(blockUnder == this){
            age = stateUnder.getValue(AGE_15)-1;
        }

        if(dir2place == Direction.DOWN && !dirHasBeenChosen){
            List<Direction> dirList = new ArrayList<>();
            dirList.add(Direction.NORTH); dirList.add(Direction.SOUTH); dirList.add(Direction.EAST); dirList.add(Direction.WEST);
            for(Direction tempDir : dirList){
                BlockState tempState = pContext.getLevel().getBlockState(pContext.getClickedPos().relative(tempDir));
                if(tempState.getBlock() == this){
                    dir2place = tempDir;
                    age = tempState.getValue(AGE_15)-1;
                }
            }
        }

        if(age<0){
            age = 0;
        }
        return this.defaultBlockState().setValue(FACING, dir2place).setValue(AGE_15, age);
    }


    /**
     * Update the provided state given the provided neighbor direction and neighbor state, returning a new state.
     * For example, fences make their connections to the passed in state if possible, and wet concrete powder immediately
     * returns its solidified counterpart.
     * Note that this method should ideally consider only the specific direction passed in.
     */
    public BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pNeighborPos) {
        BlockState changedState = pLevel.getBlockState(pCurrentPos.relative(pDirection));
        if(pDirection == pState.getValue(FACING)){
            if(changedState.getRenderShape() == RenderShape.INVISIBLE){
                return Blocks.AIR.defaultBlockState();
            }
        }

        //else
        return pState;
    }

    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, Random pRand) {
        if (!pState.canSurvive(pLevel, pPos)) {
            pLevel.destroyBlock(pPos, true);
        }

    }

    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        BlockState feedingState = pLevel.getBlockState(pPos.relative(pState.getValue(FACING)));

        boolean isattached = feedingState.getBlock() == Blocks.DIRT || feedingState.getBlock() == Blocks.GRASS_BLOCK ||
                (feedingState.getBlock() == this && feedingState.getValue(AGE_15) >= pState.getValue(AGE_15));

        for(Direction direction : Direction.Plane.HORIZONTAL) {
            BlockPos blockpos = pPos.relative(direction);
            BlockState blockstate1 = pLevel.getBlockState(blockpos);
            if (blockstate1.is(this)) {
                if (blockstate1.getValue(AGE_15) > 0) {
                    return true;
                }
            }
        }
        if(feedingState.getRenderShape() == RenderShape.INVISIBLE){
            return false;
        }

        return isattached;
    }

    public int getSignal(BlockState pState, BlockGetter pLevel, BlockPos pPos, Direction pDirection) {
        return pState.getValue(AGE_15)/5;
    }

    private void updateNeighbours(BlockState p_153765_, Level p_153766_, BlockPos p_153767_) {
        p_153766_.updateNeighborsAt(p_153767_.relative(p_153765_.getValue(FACING).getOpposite()), this);
    }

    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        this.updateNeighbours(pState, pLevel, pPos);
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pIsMoving) {
        if (!pState.is(pOldState.getBlock())) {
            //if (!pLevel.getBlockTicks().hasScheduledTick(pPos, this)) {
                //pLevel.setBlock(pPos, pState.setValue(AGE_15, 1), 18);
            //}
        }
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING, AGE_15);
    }

    public boolean isSignalSource(BlockState pState) {
        return true;
    }
}
