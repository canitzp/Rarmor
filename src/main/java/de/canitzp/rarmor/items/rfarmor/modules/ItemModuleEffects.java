package de.canitzp.rarmor.items.rfarmor.modules;

import de.canitzp.rarmor.api.InventoryBase;
import de.canitzp.rarmor.api.RarmorResources;
import de.canitzp.rarmor.api.container.ContainerBase;
import de.canitzp.rarmor.api.gui.GuiCheckBox;
import de.canitzp.rarmor.api.gui.GuiContainerBase;
import de.canitzp.rarmor.api.modules.IRarmorModule;
import de.canitzp.rarmor.api.slots.SlotUpdate;
import de.canitzp.rarmor.items.ItemRegistry;
import de.canitzp.rarmor.items.rfarmor.ItemModule;
import de.canitzp.rarmor.network.NetworkHandler;
import de.canitzp.rarmor.network.PacketSendNBTBoolean;
import de.canitzp.rarmor.util.EnergyUtil;
import de.canitzp.rarmor.util.GuiUtil;
import de.canitzp.rarmor.util.NBTUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * @author canitzp
 */
public class ItemModuleEffects extends ItemModule implements IRarmorModule{

    public static ResourceLocation checkBox = RarmorResources.GUIELEMENTS.getNewLocation();
    private static List<Potion> availableEffects = new ArrayList<>();
    private static List<EffectCheckBox> effectBoxes = new ArrayList<>();
    private int energyPerUse;

    public static void addPotionEffect(Potion effect){
        availableEffects.add(effect);
        effectBoxes.add(new EffectCheckBox(-115, availableEffects.size() * 10 - 40, effect));
    }

    public ItemModuleEffects(){
        super("moduleEffects");
        this.energyPerUse = 100;
    }

    @Override
    public String getUniqueName(){
        return "Effects";
    }

    @Override
    public List<String> getDescription(EntityPlayer player, ItemStack stack, boolean advancedTooltips){
        return null;
    }

    @Override
    public ModuleType getModuleType(){
        return ModuleType.ACTIVE;
    }

    @Override
    public void drawGuiContainerBackgroundLayer(Minecraft minecraft, GuiContainerBase gui, ItemStack armorChestplate, ItemStack module, boolean settingActivated, float partialTicks, int mouseX, int mouseY, int guiLeft, int guiTop){
        ItemStack mod = gui.inventorySlots.getSlot(75).getStack().getItem() == ItemRegistry.moduleEffects ? gui.inventorySlots.getSlot(75).getStack() : gui.inventorySlots.getSlot(81).getStack();
        GuiUtil.bindTexture(checkBox);
        gui.drawTexturedModalRect(-119 + guiLeft, guiTop - 35, 18, 0, 96, 4);
        gui.drawTexturedModalRect(-119 + guiLeft, guiTop + availableEffects.size() * 10 - 31, 18, 15, 96, 4);
        for (EffectCheckBox checkBox : effectBoxes){
            checkBox.drawCheckBox(guiLeft, guiTop);
            if(NBTUtil.getBoolean(mod, checkBox.effect.getName())){
                checkBox.paintActivated(guiLeft, guiTop);
            }
        }
    }

    @Override
    public void onMouseClicked(Minecraft minecraft, GuiContainerBase gui, ItemStack armorChestplate, ItemStack module, boolean settingActivated, int type, int mouseX, int mouseY, int guiLeft, int guiTop){
        ItemStack mod = gui.inventorySlots.getSlot(75).getStack().getItem() == ItemRegistry.moduleEffects ? gui.inventorySlots.getSlot(75).getStack() : gui.inventorySlots.getSlot(81).getStack();
        for(EffectCheckBox box : effectBoxes){
            if(box.mouseClicked(mouseX, mouseY, guiLeft, guiTop)){
                NetworkHandler.wrapper.sendToServer(new PacketSendNBTBoolean(minecraft.thePlayer,gui.inventorySlots.getSlot(75).getStack().getItem() == ItemRegistry.moduleEffects ? 75 : 81 , box.effect.getName(), !NBTUtil.getBoolean(mod, box.effect.getName())));
                NBTUtil.setBoolean(mod, box.effect.getName(), !NBTUtil.getBoolean(mod, box.effect.getName()));
            }
        }
    }

    @Override
    public void onContainerTick(ContainerBase container, EntityPlayer player, InventoryBase inventory, ItemStack armorChestplate, ItemStack module){
        ItemStack mod = container.getSlot(75).getStack().getItem() == ItemRegistry.moduleEffects ? container.getSlot(75).getStack() : container.getSlot(81).getStack();
        int activeEffects = 0;
        for(EffectCheckBox box : effectBoxes){
            if(NBTUtil.getBoolean(mod, box.effect.getName()) && EnergyUtil.getEnergy(armorChestplate) >= this.energyPerUse){
                box.applyEffect(player);
                activeEffects++;
            } else {
                box.removeEffect(player);
            }
        }
        EnergyUtil.reduceEnergy(armorChestplate, activeEffects * this.energyPerUse);
    }

    @Override
    public void onPickupFromSlot(World world, EntityPlayer player, ItemStack armorChestplate, ItemStack module, InventoryBase inventory, SlotUpdate slot){
        ItemStack mod = inventory.slots[75].getItem() == ItemRegistry.moduleEffects ? inventory.slots[75] : inventory.slots[81];
        for(EffectCheckBox box : effectBoxes){
            if(NBTUtil.getBoolean(mod, box.effect.getName())){
                box.removeEffect(player);
            }
        }
    }

    public static class EffectCheckBox extends GuiCheckBox{
        public Potion effect;
        public EffectCheckBox(int x, int y, Potion effect){
            super(checkBox, x, y, 9, I18n.translateToLocal(effect.getName()), null);
            this.effect = effect;
        }
        public void applyEffect(EntityPlayer player){
            player.addPotionEffect(new PotionEffect(this.effect, Short.MAX_VALUE, 1));
        }
        public void removeEffect(EntityPlayer player){
            player.removePotionEffect(this.effect);
        }

        @Override
        public void drawCheckBox(int guiLeft, int guiTop){
            this.drawTexturedModalRect(-4 + guiLeft + x, -1 + guiTop + y, 18, 5, 96, 10);
            super.drawCheckBox(guiLeft, guiTop);
        }

        @Override
        public boolean mouseClicked(int mouseX, int mouseY, int guiLeft, int guiTop){
            if (mouseX >= x + guiLeft && mouseY >= y + guiTop){
                if (mouseX <= x + width + guiLeft && mouseY <= y + height + guiTop){
                    return true;
                }
            }
            return false;
        }

        public void paintActivated(int guiLeft, int guiTop){
            this.drawTexturedModalRect(this.x + guiLeft, this.y + guiTop, 0, 8, 8, 8);
        }
    }
}
