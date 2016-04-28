/*
 * This file 'ItemModuleEffects.java' is part of Rarmor by canitzp.
 * It isn't allowed to use more than 15% of the code
 * or redistribute the compiled jar file.
 * The source code can be found here: https://github.com/canitzp/Rarmor
 * © canitzp, 2016
 */

package de.canitzp.rarmor.items.rfarmor.modules;

import de.canitzp.rarmor.RarmorProperties;
import de.canitzp.rarmor.RarmorUtil;
import de.canitzp.rarmor.api.InventoryBase;
import de.canitzp.rarmor.api.RarmorResources;
import de.canitzp.rarmor.api.container.ContainerBase;
import de.canitzp.rarmor.api.gui.GuiCheckBox;
import de.canitzp.rarmor.api.gui.GuiContainerBase;
import de.canitzp.rarmor.api.modules.IRarmorModule;
import de.canitzp.rarmor.api.slots.SlotUpdate;
import de.canitzp.rarmor.items.ItemRegistry;
import de.canitzp.rarmor.items.rfarmor.ItemModule;
import de.canitzp.rarmor.items.rfarmor.ItemRFArmorBody;
import de.canitzp.rarmor.network.NetworkHandler;
import de.canitzp.rarmor.network.PacketSendNBTBoolean;
import de.canitzp.rarmor.util.EnergyUtil;
import de.canitzp.rarmor.util.GuiUtil;
import de.canitzp.rarmor.util.JavaUtil;
import de.canitzp.rarmor.util.NBTUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

/**
 * @author canitzp
 */
public class ItemModuleEffects extends ItemModule implements IRarmorModule{

    public static ResourceLocation checkBox = RarmorResources.GUIELEMENTS.getNewLocation();
    public static HashMap<Potion, Integer> energyEffect = new LinkedHashMap<>();
    public static List<EffectCheckBox> effectBoxes = new ArrayList<>();
    private static int ySize, yToTop;

    public ItemModuleEffects(){
        super("moduleEffects");
    }

    public static void addPotionEffect(Potion effect, int energyPerTick){
        energyEffect.put(effect, energyPerTick);
    }

    public static void postInit(){
        ySize = energyEffect.size() * 10;
        Map<String, Pair<Potion, Integer>> sorted = new LinkedHashMap<>();
        for(Map.Entry<Potion, Integer> entry : energyEffect.entrySet()){
            sorted.put(I18n.translateToLocal(entry.getKey().getName()), Pair.of(entry.getKey(), entry.getValue()));
        }
        sorted = JavaUtil.sortByKeys(sorted);
        energyEffect = new LinkedHashMap<>();
        for(Map.Entry<String, Pair<Potion, Integer>> entry : sorted.entrySet()){
            energyEffect.put(entry.getValue().getKey(), entry.getValue().getValue());
        }
        int i = 0;
        for(Map.Entry<Potion, Integer> entry : energyEffect.entrySet()){
            effectBoxes.add(new EffectCheckBox(-146, i * 10, entry.getKey(), entry.getValue()));
            i++;
        }
    }

    public static ItemStack getModule(EntityPlayer player){
        InventoryBase inventory = RarmorUtil.readRarmor(player);
        ItemStack stack = inventory.getStackInSlot(ItemRFArmorBody.MODULESLOT);
        if((stack != null && stack.getItem() != ItemRegistry.moduleEffects) || stack == null){
            stack = inventory.getStackInSlot(31);
        }
        return stack;
    }

    @Override
    public String getUniqueName(){
        return "Effects";
    }

    @Override
    public String getDescription(EntityPlayer player, ItemStack stack, boolean advancedTooltips){
        String s = "This is a awesome Module, it let you feel the world of effects. Every effect cost a big amount of energy per tick.";
        if(RarmorProperties.getBoolean("YouTubeMode")){
            s += "\n" + TextFormatting.DARK_GRAY + "You" + TextFormatting.RED + "Tube" + TextFormatting.GRAY + " Mode active. NightVision costs " + TextFormatting.RED + "0RF" + TextFormatting.GRAY;
        }
        return s;
    }

    @Override
    public ModuleType getModuleType(){
        return ModuleType.ACTIVE;
    }

    @Override
    public void drawGuiContainerBackgroundLayer(Minecraft minecraft, GuiContainerBase gui, ItemStack armorChestplate, ItemStack module, boolean settingActivated, float partialTicks, int mouseX, int mouseY, int guiLeft, int guiTop){
        yToTop = (gui.height / 2 - ySize / 2);
        ItemStack mod = gui.inventorySlots.getSlot(75).getStack().getItem() == ItemRegistry.moduleEffects ? gui.inventorySlots.getSlot(75).getStack() : gui.inventorySlots.getSlot(81).getStack();
        GuiUtil.bindTexture(checkBox);
        gui.drawTexturedModalRect(-150 + guiLeft, yToTop - 4, 18, 0, 144, 4);
        gui.drawTexturedModalRect(-150 + guiLeft, yToTop + ySize - 1, 18, 15, 144, 4);
        for(EffectCheckBox checkBox : effectBoxes){
            checkBox.drawCheckBox(guiLeft, yToTop);
            if(NBTUtil.getBoolean(mod, checkBox.effect.getName())){
                checkBox.paintActivated(guiLeft, yToTop);
            }
        }
    }

    @Override
    public void drawScreen(Minecraft minecraft, GuiContainerBase gui, ItemStack armorChestplate, ItemStack module, boolean settingActivated, float partialTicks, int mouseX, int mouseY){
        for(EffectCheckBox checkBox : effectBoxes){
            checkBox.drawScreen(gui, mouseX, mouseY, gui.getGuiLeft(), gui.height / 2 - ySize / 2, minecraft.fontRendererObj);
        }
    }

    @Override
    public void onMouseClicked(Minecraft minecraft, GuiContainerBase gui, ItemStack armorChestplate, ItemStack module, boolean settingActivated, int type, int mouseX, int mouseY, int guiLeft, int guiTop){
        ItemStack mod = getModule(minecraft.thePlayer);
        if(mod != null){
            for(EffectCheckBox box : effectBoxes){
                if(box.mouseClicked(mouseX, mouseY, guiLeft, gui.height / 2 - ySize / 2)){
                    NetworkHandler.wrapper.sendToServer(new PacketSendNBTBoolean(minecraft.thePlayer, gui.inventorySlots.getSlot(75).getStack().getItem() == ItemRegistry.moduleEffects ? 75 : 81, box.effect.getName(), !NBTUtil.getBoolean(mod, box.effect.getName())));
                    NBTUtil.setBoolean(mod, box.effect.getName(), !NBTUtil.getBoolean(mod, box.effect.getName()));
                }
            }
        }
    }

    @Override
    public void onContainerTick(ContainerBase container, EntityPlayer player, InventoryBase inventory, ItemStack armorChestplate, ItemStack module){
        ItemStack mod = getModule(player);
        if(mod != null){
            int energyToReduce = 0;
            for(EffectCheckBox box : effectBoxes){
                if(NBTUtil.getBoolean(mod, box.effect.getName())){
                    if(EnergyUtil.getEnergy(armorChestplate) >= box.energy){
                        box.applyEffect(player);
                        energyToReduce += box.energy;
                    }
                } else {
                    box.removeEffect(player);
                }
            }
            EnergyUtil.reduceEnergy(armorChestplate, energyToReduce);
        }
    }

    @Override
    public void onPickupFromSlot(World world, EntityPlayer player, ItemStack armorChestplate, ItemStack module, InventoryBase inventory, SlotUpdate slot){
        ItemStack mod = getModule(player);
        if(mod != null){
            for(EffectCheckBox box : effectBoxes){
                if(NBTUtil.getBoolean(mod, box.effect.getName())){
                    box.removeEffect(player);
                }
            }
        }
    }

    public static class EffectCheckBox extends GuiCheckBox{
        public Potion effect;
        public int energy;

        public EffectCheckBox(int x, int y, Potion effect, int energy){
            super(checkBox, x, y, 9, I18n.translateToLocal(effect.getName()), JavaUtil.newList("Energy per Tick: " + TextFormatting.RED + energy + "RF" + TextFormatting.RESET));
            this.effect = effect;
            this.energy = energy;
        }

        public void applyEffect(EntityPlayer player){
            player.addPotionEffect(new PotionEffect(this.effect, Short.MAX_VALUE, 0, true, false));
        }

        public void removeEffect(EntityPlayer player){
            player.removePotionEffect(this.effect);
        }

        @Override
        public void drawCheckBox(int guiLeft, int guiTop){
            this.drawTexturedModalRect(guiLeft + x - 4, guiTop + y, 18, 5, 144, 10);
            super.drawCheckBox(guiLeft, guiTop);
        }

        @Override
        public boolean mouseClicked(int mouseX, int mouseY, int guiLeft, int guiTop){
            if(mouseX >= x + guiLeft && mouseY >= y + guiTop){
                if(mouseX <= x + width + guiLeft && mouseY <= y + height + guiTop){
                    return true;
                }
            }
            return false;
        }

        public void paintActivated(int guiLeft, int guiTop){
            this.drawTexturedModalRect(this.x + guiLeft, this.y + guiTop, 0, 8, 8, 8);
        }

        public void drawScreen(GuiScreen gui, int mouseX, int mouseY, int guiLeft, int guiTop, FontRenderer fontRenderer){
            if(description != null && mouseX >= x + guiLeft && mouseY >= y + guiTop){
                if(mouseX <= x + width + guiLeft && mouseY <= y + height + guiTop){
                    GuiUtil.drawHoveringText(gui, description, mouseX, mouseY, fontRenderer);
                }
            }
        }
    }
}
