package de.canitzp.rarmor.armor;

import cofh.api.energy.IEnergyContainerItem;
import cofh.api.energy.IEnergyProvider;
import de.canitzp.rarmor.NBTUtil;
import de.canitzp.rarmor.Rarmor;
import de.canitzp.rarmor.RarmorUtil;
import de.canitzp.rarmor.Registry;
import de.canitzp.rarmor.api.GuiUtils;
import de.canitzp.rarmor.api.IRarmorTab;
import de.canitzp.rarmor.api.RarmorAPI;
import de.canitzp.rarmor.api.RarmorValues;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;


/**
 * @author canitzp
 */
public class RarmorOverviewTab implements IRarmorTab{

    private final ResourceLocation tabLoc = new ResourceLocation(Rarmor.MODID, "textures/gui/guiTabOverview.png");
    public static final EntityEquipmentSlot[] VALID_EQUIPMENT_SLOTS = new EntityEquipmentSlot[] {EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET};
    private InventoryBasic energyField;

    @Override
    public List<Slot> manipulateSlots(Container container, EntityPlayer player, List<Slot> slotList, InventoryBasic energyField, int containerOffsetLeft, int containerOffsetTop){
        for (int k = 0; k < 4; ++k) {
            final EntityEquipmentSlot entityequipmentslot = VALID_EQUIPMENT_SLOTS[k];
            slotList.add(new Slot(player.inventory, 36 + (3 - k), 77 + containerOffsetLeft, 22 + containerOffsetTop + k * 18) {
                @SideOnly(Side.CLIENT)
                public String getSlotTexture(){
                    return ItemArmor.EMPTY_SLOT_NAMES[entityequipmentslot.getIndex()];
                }
                @Override
                public boolean canTakeStack(EntityPlayer playerIn){
                    return false;
                }
            });
        }
        slotList.add(new Slot(player.inventory, 40, 77 + containerOffsetLeft, 99 + containerOffsetTop) {
            @SideOnly(Side.CLIENT)
            public String getSlotTexture()
            {
                return "minecraft:items/empty_armor_slot_shield";
            }
        });
        GuiUtils.addEnergyField(slotList, energyField, 6, 6);
        this.energyField = energyField;
        return slotList;
    }

    @Override
    public void tick(World world, EntityPlayer player, ItemStack stack) {
        if (!world.isRemote && this.energyField != null) {
            ItemStack input1 = this.energyField.getStackInSlot(0);
            ItemStack input2 = this.energyField.getStackInSlot(1);
            if (input1 != null) {
                if (input1.getItem() instanceof IEnergyContainerItem) {
                    if(NBTUtil.getEnergy(stack) + RarmorValues.rarmorMaxTransfer <= RarmorAPI.extractEnergy(input1, RarmorValues.rarmorMaxTransfer, true)){
                        RarmorAPI.receiveEnergy(stack, RarmorValues.rarmorMaxTransfer, true);
                        ((IEnergyContainerItem) input1.getItem()).extractEnergy(input1, RarmorValues.rarmorMaxTransfer, false);
                    }
                }
            }
        }
    }

    @Override
    public String getTabIdentifier(ItemStack rarmor, EntityPlayer player){
        return Rarmor.MODID + ":overviewTab";
    }

    @Override
    public ItemStack getTabIcon(){
        return new ItemStack(Registry.rarmorChestplate);
    }

    @Override
    public String getTabHoveringText(){
        return "Overview";
    }

    @Override
    public void drawGui(GuiContainer gui, EntityPlayer player, int guiLeft, int guiTop, int mouseX, int mouseY, float partialTicks){
        gui.mc.getTextureManager().bindTexture(this.tabLoc);
        gui.drawTexturedModalRect(guiLeft + 99, guiTop + 6, 0, 0, 239, 138);
        GuiUtils.drawSlotField(gui, guiLeft + 80, guiTop + 25, 1, 4);
        GuiUtils.drawSlot(gui, guiLeft + 80, guiTop + 102);

        GuiUtils.drawEnergyField(gui, guiLeft + 6, guiTop + 6, RarmorValues.rarmorMaxEnergy, NBTUtil.getEnergy(RarmorUtil.getRarmorChestplate(player)));


        //GuiInventory.drawEntityOnScreen(guiLeft + 137, guiTop + 125, 55, guiLeft + 133 - mouseX, guiTop + 37 - mouseY, gui.mc.thePlayer);
    }

}
