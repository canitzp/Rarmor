package de.canitzp.rarmor.armor;

import de.canitzp.rarmor.api.RarmorAPI;
import de.canitzp.rarmor.api.RarmorValues;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.config.GuiUtils;

/**
 * @author canitzp
 */
public class RarmorCoalGeneratorTab extends RarmorOneSlotTab{

    private int currentBurnTime;

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
                    if(burnTime > 0){
                        if(input.stackSize == 1){
                            this.inventory.setInventorySlotContents(0, null);
                        } else {
                            input.stackSize--;
                        }
                        this.currentBurnTime = burnTime;
                    }
                }
            } else {
                this.currentBurnTime--;
                RarmorAPI.receiveEnergy(stack, RarmorValues.generatorTabTickValue, false);
                System.out.println(this.currentBurnTime);
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt){
        super.readFromNBT(nbt);
        this.currentBurnTime = nbt.getInteger("currentBurnTime");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt){
        nbt.setInteger("currentBurnTime", this.currentBurnTime);
        return super.writeToNBT(nbt);
    }

    @Override
    public ItemStack getTabIcon(){
        return new ItemStack(Items.COAL);
    }
}
