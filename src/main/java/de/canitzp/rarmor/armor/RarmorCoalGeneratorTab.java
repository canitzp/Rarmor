package de.canitzp.rarmor.armor;

import de.canitzp.rarmor.NBTUtil;
import de.canitzp.rarmor.RarmorUtil;
import de.canitzp.rarmor.api.GuiUtils;
import de.canitzp.rarmor.api.RarmorAPI;
import de.canitzp.rarmor.api.RarmorValues;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * @author canitzp
 */
public class RarmorCoalGeneratorTab extends RarmorOneSlotTab{

    private int currentBurnTime, currentMaxBurnTime = 1, energy;

    public RarmorCoalGeneratorTab(){
        super("Furnace Generator");
    }

    @Override
    public void tick(World world, EntityPlayer player, ItemStack stack){
        if(!world.isRemote && this.inventory != null){
            if(this.currentBurnTime <= 0){
                ItemStack input = this.inventory.getStackInSlot(0);
                if(input != null){
                    int burnTime = TileEntityFurnace.getItemBurnTime(input);
                    if(burnTime > 0 && NBTUtil.getEnergy(stack) + (burnTime*RarmorValues.generatorTabTickValue) <= RarmorValues.rarmorMaxEnergy){
                        if(input.stackSize == 1){
                            this.inventory.setInventorySlotContents(0, null);
                        } else {
                            input.stackSize--;
                        }
                        this.currentBurnTime = burnTime;
                        this.currentMaxBurnTime = burnTime;
                        RarmorUtil.syncTab((EntityPlayerMP) player, this);
                    }
                }
            } else {
                this.currentBurnTime--;
                RarmorAPI.receiveEnergy(stack, RarmorValues.generatorTabTickValue, false);
                RarmorUtil.syncTab((EntityPlayerMP) player, this);
            }
        }
    }

    @Override
    public List<Slot> manipulateSlots(Container container, EntityPlayer player, List<Slot> slotList, InventoryBasic energyField, int containerOffsetLeft, int containerOffsetTop) {
        if(player instanceof EntityPlayerMP) RarmorUtil.syncTab((EntityPlayerMP) player, this);
        GuiUtils.addEnergyField(slotList, energyField, 6, 6);
        return super.manipulateSlots(container, player, slotList, energyField, containerOffsetLeft, containerOffsetTop);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void drawGui(GuiContainer gui, EntityPlayer player, int guiLeft, int guiTop, int mouseX, int mouseY, float partialTicks) {
        super.drawGui(gui, player, guiLeft, guiTop, mouseX, mouseY, partialTicks);
        GuiUtils.drawBurnFlame(gui, guiLeft + 116, guiTop + 83, this.currentMaxBurnTime, this.currentBurnTime);
        GuiUtils.drawEnergyField(gui, guiLeft + 6, guiTop + 6, RarmorValues.rarmorMaxEnergy, this.energy);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt){
        super.readFromNBT(nbt);
        this.currentMaxBurnTime = nbt.getInteger("CurrentMaxBurnTime");
        this.currentBurnTime = nbt.getInteger("CurrentBurnTime");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt){
        nbt.setInteger("CurrentMaxBurnTime", this.currentMaxBurnTime);
        nbt.setInteger("CurrentBurnTime", this.currentBurnTime);
        return super.writeToNBT(nbt);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public ItemStack getTabIcon(){
        return new ItemStack(Items.COAL);
    }

    @Override
    public NBTTagCompound getPacketData(EntityPlayerMP player, ItemStack stack) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("CurrentMaxBurnTime", this.currentMaxBurnTime);
        nbt.setInteger("CurrentBurnTime", this.currentBurnTime);
        nbt.setInteger("Energy", NBTUtil.getEnergy(stack));
        return nbt;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void onPacketData(EntityPlayer player, ItemStack stack, NBTTagCompound nbt) {
        this.currentMaxBurnTime = nbt.getInteger("CurrentMaxBurnTime");
        this.currentBurnTime = nbt.getInteger("CurrentBurnTime");
        this.energy = nbt.getInteger("Energy");
    }
}
