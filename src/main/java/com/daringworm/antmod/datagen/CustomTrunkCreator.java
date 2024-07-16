package com.daringworm.antmod.datagen;

import com.daringworm.antmod.TreeMod;
import com.daringworm.antmod.block.ModBlocks;
import com.daringworm.antmod.block.custom.BasicTrunkBlock;
import com.daringworm.antmod.item.ModCreativeModeTab;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.RegistryObject;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

public class CustomTrunkCreator {

    private static String templateBlockstates = "";

    public String getResourcesFile(){
        File file = new File("./null");
        Path runPath = Path.of(file.getPath()).getParent().toAbsolutePath().getParent();
        String resourcesPath = runPath.toString().replace("run", "src") + "\\main\\resources";
        File resourcesFile = new File(resourcesPath);

        System.out.println(resourcesFile.getAbsolutePath());
        return resourcesFile.getAbsolutePath();
    }

    private String getChildName(Block parent){
        return parent.getName().getString().toLowerCase().replace(" ", "_") + "_trunk";
    }

    private boolean getTemplates(){

        if(templateBlockstates.equals("")) {

            String resourcesDir = getResourcesFile();
            String blockstatesDir = resourcesDir + "/assets/treemod/blockstates";


            try {
                File templateBlockstateFile = new File(blockstatesDir + "/default_trunk.json");
                Scanner myReader = new Scanner(templateBlockstateFile);
                StringBuilder templateBlockstatesBuilder = new StringBuilder();
                while (myReader.hasNextLine()) {
                    templateBlockstatesBuilder.append(myReader.nextLine());
                }
                myReader.close();
                templateBlockstates = templateBlockstatesBuilder.toString();

            } catch (FileNotFoundException e) {
                System.out.println("(TreeMod: Fatal Error) Couldn't find the Trunk Blockstates Default File");
                e.printStackTrace();
            }
        }
        return true;
    }

    private boolean generateResources(String newName, Block parentBlock){
        getTemplates();
        String resourcesDir = getResourcesFile();
        String blockstatesDir = resourcesDir + "/assets/treemod/blockstates/";
        String modelsDir = resourcesDir + "/assets/treemod/models/block/";

        try {
            File newBlockstateFile = new File(blockstatesDir + newName + ".json");
            if(newBlockstateFile.exists()){return true;}


            FileWriter fileWriter = new FileWriter(newBlockstateFile);
            fileWriter.write(templateBlockstates);
            fileWriter.close();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        return true;
    }

    //make sure there are no duplicates going in!
    public CustomTrunkCreator(ArrayList<Block> parentBlocks, DeferredRegister<Block> destinationRegistry, DeferredRegister<Item> itemDestination) {

        for(Block tempBlock : parentBlocks){
            String newName = getChildName(tempBlock);

            RegistryObject<Block> newTrunk = destinationRegistry.register(newName, ()-> new BasicTrunkBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(0.2f).strength(0.8f).requiresCorrectToolForDrops().noOcclusion()));
            RegistryObject<Item> newItem = itemDestination.register(newName, () -> new BlockItem(newTrunk.get(), new Item.Properties().tab(ModCreativeModeTab.TREEMOD_CTAB)));

            generateResources(newName,tempBlock);

        }
    }
}









