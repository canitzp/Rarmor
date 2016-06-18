package de.canitzp.rarmor.armor;

import de.canitzp.rarmor.RarmorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author canitzp
 */
public class SlotDependency extends Slot{

    private EntityPlayer player;
    private List<ItemStack> blocks;

    public SlotDependency(EntityPlayer player, List<ItemStack> validStacks, IInventory inventoryIn, int index, int xPosition, int yPosition){
        super(inventoryIn, index, xPosition, yPosition);
        this.player = player;
        this.blocks = validStacks;
    }

    @Override
    public void onSlotChanged(){
        if(player.worldObj.isRemote){
            GuiScreen gui = Minecraft.getMinecraft().currentScreen;
            if(gui instanceof GuiContainerRarmor.GuiRarmor){
                ((GuiContainerRarmor.GuiRarmor) gui).sortTabs(this.inventory);
            }
        }
        super.onSlotChanged();
    }

    @Override
    public boolean isItemValid(@Nullable ItemStack stack){
        for(ItemStack stack1 : blocks){
            if(ItemStack.areItemStacksEqual(stack, stack1)){
                return true;
            }
        }
        return false;
    }
}
