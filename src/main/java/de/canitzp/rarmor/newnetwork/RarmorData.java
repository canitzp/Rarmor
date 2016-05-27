package de.canitzp.rarmor.newnetwork;

import de.canitzp.rarmor.api.InventoryBase;
import de.canitzp.rarmor.items.rfarmor.ItemRFArmorBody;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.Map;
import java.util.UUID;

public class RarmorData{

    public NBTTagCompound rarmorCompound = new NBTTagCompound();
    public InventoryBase inventory = new InventoryBase("Rarmor", ItemRFArmorBody.slotAmount);

    public void writeToNBT(NBTTagCompound compound, boolean forSync){
        compound.setTag("RarmorData", this.rarmorCompound);

        NBTTagList list = new NBTTagList();
        for(int i = 0; i < this.inventory.getSizeInventory(); i++){
            ItemStack slot = this.inventory.getStackInSlot(i);
            if(slot != null){
                NBTTagCompound tag = new NBTTagCompound();
                tag.setInteger("Slot", i);
                tag = slot.writeToNBT(tag);
                list.appendTag(tag);
            }
        }
        compound.setTag("Items", list);
    }

    public void readFromNBT(NBTTagCompound compound, boolean forSync){
        if(compound.hasKey("RarmorData")){
            this.rarmorCompound = compound.getCompoundTag("RarmorData");
        }

        NBTTagList list = compound.getTagList("Items", 10);
        for(int i = 0; i < list.tagCount(); i++){
            NBTTagCompound tag = list.getCompoundTagAt(i);
            int slot = tag.getInteger("Slot");
            ItemStack stack = ItemStack.loadItemStackFromNBT(tag);
            if(stack != null){
                this.inventory.setInventorySlotContents(slot, stack.copy());
            }
        }
    }

    public IMessage getSyncMessage(){
        for(Map.Entry<UUID, RarmorData> data : WorldData.getRarmorData(false).entrySet()){
            if(data.getValue() == this){
                return new PacketUpdateRarmorData(data.getKey(), this);
            }
        }
        return null;
    }

    public static RarmorData getDataForRarmor(ItemStack stack, boolean client){
        if(!stack.hasTagCompound()){
            stack.setTagCompound(new NBTTagCompound());
        }
        NBTTagCompound compound = stack.getTagCompound();

        UUID rarmorID = null;
        if(compound.hasKey("RarmorIDLeast")){
            rarmorID = compound.getUniqueId("RarmorID");
            RarmorData data = WorldData.getRarmorData(client).get(rarmorID);
            if(data != null){
                return data;
            }
        }

        if(rarmorID == null){
            rarmorID = UUID.randomUUID();
        }
        RarmorData newData = new RarmorData();
        compound.setUniqueId("RarmorID", rarmorID);
        WorldData.getRarmorData(client).put(rarmorID, newData);
        return newData;
    }

}
