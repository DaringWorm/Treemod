package com.daringworm.antmod.item;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ModCreativeModeTab {

    public static final CreativeModeTab TREEMOD_CTAB = new CreativeModeTab("antmodctab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(Items.OAK_SAPLING);
        }
    };

}
