/*
 * This file ("ContainerModuleMain.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at
 * https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.mod.module.main;

import de.canitzp.rarmor.api.inventory.RarmorModuleContainer;
import de.canitzp.rarmor.api.module.IActiveRarmorModule;
import de.canitzp.rarmor.mod.inventory.gui.slot.SlotModule;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ContainerModuleMain extends RarmorModuleContainer{

    private static final EntityEquipmentSlot[] VALID_EQUIPMENT_SLOTS = new EntityEquipmentSlot[]{EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET};

    private final EntityPlayer player;

    public InventoryCrafting craftMatrix = new InventoryCrafting(this.actualContainer, 3, 3);
    public InventoryCraftResult craftResult = new InventoryCraftResult();

    public ContainerModuleMain(EntityPlayer player, Container actualContainer, IActiveRarmorModule module){
        super(actualContainer, module);
        this.player = player;
    }

    @Override
    public void onCraftMatrixChanged(IInventory inventory){
        this.craftResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(this.craftMatrix, this.player.worldObj));
    }

    @Override
    public void onContainerClosed(EntityPlayer player){
        ActiveModuleMain module = (ActiveModuleMain)this.module;
        for(int i = 0; i < this.craftMatrix.getSizeInventory(); i++){
            module.inventory.setInventorySlotContents(i+1, this.craftMatrix.getStackInSlot(i));
        }
        module.inventory.setInventorySlotContents(0, this.craftResult.getStackInSlot(0));
    }

    @Override
    public List<Slot> getSlots(){
        List<Slot> slots = new ArrayList<Slot>();

        ActiveModuleMain module = (ActiveModuleMain)this.module;
        for(int i = 0; i < 3; i++){
            slots.add(new SlotModule(module.inventory, i+10, 11, 10+i*26));
        }

        for(int i = 0; i < 4; i++){
            final EntityEquipmentSlot slot = VALID_EQUIPMENT_SLOTS[i];
            slots.add(new Slot(ContainerModuleMain.this.player.inventory, 36+(3-i), 49, 16+i*18){
                @Override
                public int getSlotStackLimit(){
                    return 1;
                }

                @Override
                public boolean isItemValid(ItemStack stack){
                    return slot != EntityEquipmentSlot.CHEST && stack != null && stack.getItem().isValidArmor(stack, slot, ContainerModuleMain.this.player);
                }

                @Override
                @SideOnly(Side.CLIENT)
                public String getSlotTexture(){
                    return ItemArmor.EMPTY_SLOT_NAMES[slot.getIndex()];
                }

                @Override
                public ItemStack decrStackSize(int amount){
                    return slot == EntityEquipmentSlot.CHEST ? null : super.decrStackSize(amount);
                }

                @Override
                public void putStack(@Nullable ItemStack stack){
                    if(slot != EntityEquipmentSlot.CHEST){
                        super.putStack(stack);
                    }
                }

                @Override
                public boolean canTakeStack(EntityPlayer playerIn){
                    return slot != EntityEquipmentSlot.CHEST;
                }
            });
        }

        slots.add(new Slot(ContainerModuleMain.this.player.inventory, 40, 49, 94){
            @Override
            public boolean isItemValid(ItemStack stack){
                return super.isItemValid(stack);
            }

            @Override
            @SideOnly(Side.CLIENT)
            public String getSlotTexture(){
                return "minecraft:items/empty_armor_slot_shield";
            }
        });

        slots.add(new SlotCrafting(this.player, this.craftMatrix, this.craftResult, 0, 189, 104));
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                slots.add(new Slot(this.craftMatrix, j+i*3, 170+j*18, 16+i*18));
            }
        }

        for(int i = 0; i < this.craftMatrix.getSizeInventory(); i++){
            this.craftMatrix.setInventorySlotContents(i, module.inventory.getStackInSlot(i+1));
        }
        this.craftResult.setInventorySlotContents(0, module.inventory.getStackInSlot(0));

        return slots;
    }

    @Override
    public ItemStack slotClick(int slotId, int dragType, ClickType clickType, EntityPlayer player){
        if(clickType == ClickType.THROW){
            List<Slot> slots = this.actualContainer.inventorySlots;
            if(slotId >= 0 && slotId < slots.size()){
                Slot slot = this.actualContainer.inventorySlots.get(slotId);
                if(slot.getSlotIndex() == 38){ //chestplate slot
                    ItemStack stack = slot.getStack();
                    if(stack != null){
                        ItemStack copy = stack.copy();
                        if(!player.inventory.addItemStackToInventory(copy)){
                            EntityItem item = new EntityItem(player.worldObj, player.posX, player.posY, player.posZ, copy);
                            item.setNoPickupDelay();
                            player.worldObj.spawnEntityInWorld(item);
                        }
                        slot.inventory.setInventorySlotContents(slot.getSlotIndex(), null);

                        player.closeScreen();
                    }
                }
            }
        }
        return null;
    }
}
