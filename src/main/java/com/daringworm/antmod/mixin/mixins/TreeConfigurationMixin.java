package com.daringworm.antmod.mixin.mixins;


import com.daringworm.antmod.TreeMod;
import com.daringworm.antmod.block.ModVaryingTrunkBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TreeConfiguration.TreeConfigurationBuilder.class)
public abstract class TreeConfigurationMixin {

    @Final
    @Shadow
    public BlockStateProvider trunkProvider;

    @Final
    @Shadow
    public BlockStateProvider foliageProvider;


    @Inject(at = @At("HEAD"), method = "build", cancellable = true)
    public void build(CallbackInfoReturnable<TreeConfiguration> cir) {
        //ModVaryingTrunkBlocks.registerTrunk(trunkProvider.getState(TreeMod.getRandom(), BlockPos.ZERO).getBlock());
    }

}


