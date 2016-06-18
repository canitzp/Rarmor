package de.canitzp.rarmor.armor;

import com.google.common.collect.Lists;
import de.canitzp.rarmor.NBTUtil;
import de.canitzp.rarmor.Rarmor;
import de.canitzp.rarmor.Registry;
import de.canitzp.rarmor.api.IRarmorTab;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;


/**
 * @author canitzp
 */
public class RarmorOverviewTab implements IRarmorTab{

    private final ResourceLocation tabLoc = new ResourceLocation(Rarmor.MODID, "textures/gui/guiTabOverview.png");
    private static final EntityEquipmentSlot[] VALID_EQUIPMENT_SLOTS = new EntityEquipmentSlot[] {EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET};
    private InventoryBasic inventory;

    @Override
    public List<Slot> manipulateSlots(Container container, EntityPlayer player, List<Slot> slotList, int containerOffsetLeft, int containerOffsetTop){
        for (int k = 0; k < 4; ++k) {
            final EntityEquipmentSlot entityequipmentslot = VALID_EQUIPMENT_SLOTS[k];
            slotList.add(new Slot(player.inventory, 36 + (3 - k), 58 + containerOffsetLeft, 35 + containerOffsetTop + k * 18) {
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
        slotList.add(new Slot(player.inventory, 40, 58 + containerOffsetLeft, 112 + containerOffsetTop) {
            @SideOnly(Side.CLIENT)
            public String getSlotTexture()
            {
                return "minecraft:items/empty_armor_slot_shield";
            }
        });
        slotList.add(new SlotDependency(player, Lists.newArrayList(new ItemStack(Blocks.CHEST), new ItemStack(Blocks.ENDER_CHEST), new ItemStack(Blocks.TRAPPED_CHEST)), this.inventory, 0, 202 + containerOffsetLeft, 6 + containerOffsetTop));
        return slotList;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt){
        return NBTUtil.writeInventory(nbt, this.inventory);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt){
        this.inventory = NBTUtil.readInventory(nbt, "Rarmor Overview Tab", 1);
    }

    @Override
    public IInventory getInventoryForDependencies(){
        return this.inventory;
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
        gui.drawTexturedModalRect(guiLeft + 4, guiTop + 4, 0, 0, 239, 138);
        GuiInventory.drawEntityOnScreen(guiLeft + 137, guiTop + 125, 55, guiLeft + 133 - mouseX, guiTop + 37 - mouseY, gui.mc.thePlayer);
    }
}
