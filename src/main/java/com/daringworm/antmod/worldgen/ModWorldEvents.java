package com.daringworm.antmod.worldgen;

import com.daringworm.antmod.TreeMod;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TreeMod.MOD_ID)
public class ModWorldEvents {
    @SubscribeEvent
    public static void biomeLoadingEvent(final BiomeLoadingEvent event) {
        //ModTreeGeneration.generateTrees(event);
        //ModColonyGeneration.generateColonies(event);
    }
}