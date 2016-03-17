package de.canitzp.rarmor.items;

import cofh.api.energy.ItemEnergyContainer;
import de.canitzp.rarmor.Rarmor;
import de.canitzp.rarmor.api.IToolModule;
import de.canitzp.rarmor.inventory.GuiHandler;
import de.canitzp.rarmor.util.NBTUtil;
import de.canitzp.rarmor.util.inventory.InventoryBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

import java.util.HashSet;
import java.util.Set;

/**
 * @author canitzp
 */
public class ItemModularTool extends ItemEnergyContainer {

    public static int slots = 3;

    public ItemModularTool(int maxEnergy, int maxTransfer, String name){
        super(maxEnergy, maxTransfer);
        this.setHasSubtypes(true);
        this.setMaxStackSize(1);
        Rarmor.registerItem(this, name);
    }

    @Override
    public Set<String> getToolClasses(ItemStack stack) {
        Set<String> toolClasses =  new HashSet<>();
        toolClasses.add("pickaxe");
        toolClasses.add("axe");
        toolClasses.add("shovel");
        toolClasses.add("hoe");
        toolClasses.add("sword");
        return toolClasses;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if(Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
            player.openGui(Rarmor.instance, GuiHandler.MODULARTOOL, world, player.serverPosX, player.serverPosY, player.serverPosZ);
            return stack;
        }
        InventoryBase inventory = NBTUtil.readSlotsBase(stack, slots);
        for(ItemStack stack1 : inventory.slots){
            if(stack1 != null && stack1.getItem() instanceof IToolModule){
                stack = ((IToolModule) stack1.getItem()).onRightClick(stack, world, player);
            }
        }
        return super.onItemRightClick(stack, world, player);
    }

}
