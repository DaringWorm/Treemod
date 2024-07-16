package com.daringworm.antmod.mixin.mixins;


import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.DarkOakFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.DarkOakTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.Function;

@Mixin(DarkOakTrunkPlacer.class)
public abstract class DarkOakMixin {

    /*@Inject(at = @At("HEAD"), method = "placeTrunk", cancellable = true)
    public void mixin(LevelSimulatedReader p_161790_, BiConsumer<BlockPos, BlockState> p_161791_, Random p_161792_, int p_161793_, BlockPos p_161794_, TreeConfiguration p_161795_, CallbackInfoReturnable<List<FoliagePlacer.FoliageAttachment>> cir) {
        List<FoliagePlacer.FoliageAttachment> list2 = new ArrayList<>();
        cir.setReturnValue(list2);
    }*/
}


