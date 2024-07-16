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

import java.util.ArrayList;
import java.util.Objects;

public class ModVaryingTrunkBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, TreeMod.MOD_ID);

    public static ArrayList<String> usedNames = new ArrayList<>();
    public static ArrayList<Block> blocksToGenerateTrunksFor = new ArrayList<>();

    public static void registerTrunk(Block parentBlock){
        String newName = parentBlock.getName().getString().toLowerCase().replaceAll(" ", "_") + "_trunk";
        boolean flag = false;
        for(String tempString : usedNames){
            if(Objects.equals(tempString, newName)){
                flag = true;
            }
        }

        if(!flag) {
            RegistryObject<Block> blockRegistryObject = BLOCKS.register(newName,
                    () -> new BasicTrunkBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(0.2f).strength(0.8f).requiresCorrectToolForDrops().noOcclusion()));

            usedNames.add(newName);
            blocksToGenerateTrunksFor.add(parentBlock);

            registerBlockItem(newName, blockRegistryObject, ModCreativeModeTab.TREEMOD_CTAB);
        }
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block, CreativeModeTab tab){
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(),
                new Item.Properties().tab(tab)));
    }

    public static void registerToBus(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
