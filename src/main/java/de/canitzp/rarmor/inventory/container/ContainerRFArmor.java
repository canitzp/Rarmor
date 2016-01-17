package de.canitzp.rarmor.inventory.container;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.canitzp.api.inventory.InventoryBase;
import de.canitzp.api.util.ContainerUtil;
import de.canitzp.api.util.PacketUtil;
import de.canitzp.rarmor.inventory.container.Slots.*;
import de.canitzp.rarmor.items.rfarmor.ItemRFArmor;
import de.canitzp.rarmor.items.rfarmor.ItemRFArmorBody;
import de.canitzp.api.util.NBTUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import net.minecraft.util.IIcon;

import java.util.List;

/**
 * @author canitzp
 */
public class ContainerRFArmor extends Container {

    public InventoryBase inventory;
    public InventoryCrafting craftMatrix = new InventoryCrafting(this, 3, 3);
    public IInventory craftResult = new InventoryCraftResult();
    public EntityPlayer player;
    public SlotArmorInventory generatorSlot;
    public ItemRFArmorBody body;
    private ItemStack foot, leg, head;
    public ItemStack armor;
    public int currentEnergy, currentEFoot, currentELeg, currentEHead, currentBurnTime, currentItemBurnTime, currentGenBurnTime;

    public ContainerRFArmor(EntityPlayer player){
        this.armor = player.getCurrentArmor(2);
        this.foot = player.getCurrentArmor(0);
        this.leg = player.getCurrentArmor(1);
       // this.head = player.getCurrentArmor(3);
        this.body = (ItemRFArmorBody) armor.getItem();
        this.inventory = NBTUtil.readSlots(this.armor, this.body.slotAmount);//this.body.getArmorInventoryNBT(player);
        this.player = player;
        armor.stackTagCompound.setBoolean("click", false);

        //Armor Inventory:
        for(int i = 0; i < 3; ++i){
            for(int j = 0; j < 9; j++){
                this.addSlotToContainer(new SlotArmorInventory(this.inventory, j + i * 9, 44 + j * 18, 89 + i * 18, player));
            }
        }
        //Player Inventory:
        for (int j = 0; j < 3; ++j) {
            for (int k = 0; k < 9; ++k) {
                this.addSlotToContainer(new Slot(player.inventory, k + j * 9 + 9, 44 + k * 18, 144 + j * 18));
            }
        }
        //Player Hotbar:
        for (int j = 0; j < 9; ++j) {
            this.addSlotToContainer(new Slot(player.inventory, j, 44 + j * 18, 202));
        }
        //Armor Crafting Grid:
        this.addSlotToContainer(new SlotCrafting(player, this.craftMatrix, this.craftResult, 0, 217, 99));
        for (int l = 0; l < 3; ++l) {
            for (int i1 = 0; i1 < 3; ++i1) {
                this.addSlotToContainer(new Slot(this.craftMatrix, i1 + l * 3, 180 + i1 * 18, 14 + l * 18));
            }
        }
        //Armor Furnace Input:
        this.addSlotToContainer(new SlotArmorInventory(this.inventory, 27, 15, 57, player));
        //Armor Furnace Output:
        this.addSlotToContainer(new SlotFurnaceOutput(this.inventory, 28, 15, 98, player));
        //Armor Module Slot:
        this.addSlotToContainer(new SlotInputModule(this.inventory, 29, 15, 34, player));

        final EntityPlayer p = player;
        for(int i = 0; i < 4; ++i){
            final int finalI = i;
            this.addSlotToContainer(new SlotUnmovable(player.inventory, player.inventory.getSizeInventory()-1-i, 44, 10+i*18){
                @Override
                public boolean isItemValid(ItemStack stack){
                    return stack != null && stack.getItem().isValidArmor(stack, finalI, p);
                }

                @Override
                public int getSlotStackLimit(){
                    return 1;
                }

                @Override
                @SideOnly(Side.CLIENT)
                public IIcon getBackgroundIconIndex(){
                    return ItemArmor.func_94602_b(finalI);
                }
            });
        }

        this.generatorSlot = new SlotModule(this.inventory, 30, 140, 18, player);
        this.addSlotToContainer(this.generatorSlot);
    }

    @Override
    public void detectAndSendChanges() {
        InventoryBase inv = NBTUtil.readSlots(this.armor, this.body.slotAmount);
        if(this.inventory != inv){
            for(int i = 0; inv.getSizeInventory() < i; i++){
                this.inventory.setInventorySlotContents(i, inv.getStackInSlot(i));
            }
        }
        this.inventory.slots = inv.slots;


        super.detectAndSendChanges();
    }

    @Override
    public void addCraftingToCrafters(ICrafting listener){
        super.addCraftingToCrafters(listener);

    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int data){

    }

    @Override
    public boolean canInteractWith(EntityPlayer p_75145_1_) {
        return this.inventory.isUseableByPlayer(p_75145_1_);
    }

    @Override
    public void onContainerClosed(EntityPlayer player){
        dropCraftMatrix(player);
        super.onContainerClosed(player);
    }

    private void dropCraftMatrix(EntityPlayer player) {
        if (!player.worldObj.isRemote) {
            for (int i = 0; i < 9; ++i) {
                ItemStack itemstack = this.craftMatrix.getStackInSlotOnClosing(i);
                if (itemstack != null) {
                    player.dropPlayerItemWithRandomChoice(itemstack, false);
                }
            }
        }
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slot){
        return ContainerUtil.transferStackInSlot((List<Slot>)this.inventorySlots, player, slot);
    }

    @Override
    public void onCraftMatrixChanged(IInventory p_75130_1_) {
        this.craftResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(this.craftMatrix, this.player.getEntityWorld()));
    }

}
