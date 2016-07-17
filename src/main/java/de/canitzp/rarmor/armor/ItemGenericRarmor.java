package de.canitzp.rarmor.armor;

import de.canitzp.rarmor.NBTUtil;
import de.canitzp.rarmor.Rarmor;
import de.canitzp.rarmor.Registry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * @author canitzp
 */
public class ItemGenericRarmor extends ItemArmor{

    public ItemGenericRarmor(EntityEquipmentSlot equipmentSlotIn){
        super(Rarmor.rarmorMaterial, 0, equipmentSlotIn);
        this.setMaxDamage(0);
        Registry.registerItem(this, "rarmor" + equipmentSlotIn.getName());
    }

    @SideOnly(Side.CLIENT)
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type){
        if((type != null && type.equals("overlay")) || NBTUtil.getTagFromStack(stack).getBoolean("isTransparent")){
            return "rarmor:textures/models/armor/rfarmorOverlay.png";
        }
        return Rarmor.MODID + ":textures/models/armor/rfarmorLayer" + (slot == EntityEquipmentSlot.LEGS ? "2" : "1") + ".png";
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced){
        super.addInformation(stack, playerIn, tooltip, advanced);
        NBTTagCompound nbt = NBTUtil.getTagFromStack(stack);
        if(nbt.hasKey("ColorCode")){
            tooltip.add(nbt.getString("ColorName"));
        }
    }

    @Override
    public boolean hasColor(ItemStack stack){
        return NBTUtil.getTagFromStack(stack).hasKey("ColorCode");
    }

    @Override
    public int getColor(ItemStack stack){
        return NBTUtil.getColor(stack);
    }
}
