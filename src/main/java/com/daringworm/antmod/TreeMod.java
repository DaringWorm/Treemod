package com.daringworm.antmod;

import com.daringworm.antmod.block.ModBlocks;
import com.daringworm.antmod.block.ModVaryingTrunkBlocks;
import com.daringworm.antmod.datagen.CustomTrunkCreator;
import com.daringworm.antmod.item.ModItems;

import net.minecraft.FileUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(TreeMod.MOD_ID)
public class TreeMod
{
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "treemod";
    static Random modRandom = new Random(1234);



    public TreeMod() {

        System.out.println("TREEMOD INITIALIZE");
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        HashSet<ConfiguredFeature<?, ?>> treeEntries = BuiltinRegistries.CONFIGURED_FEATURE.stream().collect(Collectors.toCollection(HashSet::new));
        HashSet<Block> trunkBlocks = new HashSet<>();

        for(ConfiguredFeature<?,?> tempObj : treeEntries){

            if(tempObj.config() instanceof TreeConfiguration treeConfiguration){
                Block parentTrunkBlock = treeConfiguration.trunkProvider.getState(modRandom, BlockPos.ZERO).getBlock();
                System.out.println(parentTrunkBlock.getName().getString());
                trunkBlocks.add(parentTrunkBlock);
            }
        }

        CustomTrunkCreator trunks = new CustomTrunkCreator(new ArrayList<>(trunkBlocks), ModVaryingTrunkBlocks.BLOCKS, ModItems.ITEMS);

        System.out.println("Located file is at: " + trunks.getResourcesFile());

        System.out.println("Custom trunks is " + ModVaryingTrunkBlocks.usedNames.size());
        ModVaryingTrunkBlocks.registerToBus(eventBus);


        ModItems.register(eventBus);

        ModBlocks.register(eventBus);

        eventBus.addListener(this::setup);
        eventBus.addListener(this::clientSetup);

        MinecraftForge.EVENT_BUS.register(this);

    }




    private void clientSetup(final FMLClientSetupEvent event) {
    }

    public static Random getRandom(){
        return modRandom;
    }

    private void setup(final FMLCommonSetupEvent event)
    {

    }
}
