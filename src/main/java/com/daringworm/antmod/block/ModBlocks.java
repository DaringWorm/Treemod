package com.daringworm.antmod.block;


import com.daringworm.antmod.TreeMod;
import com.daringworm.antmod.block.custom.BasicTrunkBlock;
import com.daringworm.antmod.item.ModCreativeModeTab;
import com.daringworm.antmod.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;


public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, TreeMod.MOD_ID);

    public static final RegistryObject<Block> DEFAULT_TRUNK = registerBlock("default_trunk",
            () -> new BasicTrunkBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(0.2f).strength(0.8f)
                    .requiresCorrectToolForDrops().noOcclusion()), ModCreativeModeTab.TREEMOD_CTAB);




    public static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block, CreativeModeTab tab){
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn, tab);
        return toReturn;
    }


    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block, CreativeModeTab tab){
    return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(),
            new Item.Properties().tab(tab)));
    }



    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}

