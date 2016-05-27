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
import de.canitzp.rarmor.api.Colors;
import de.canitzp.rarmor.api.InventoryBase;
import de.canitzp.rarmor.api.RarmorResources;
import de.canitzp.rarmor.api.modules.IRarmorModule;
import de.canitzp.rarmor.items.ItemRegistry;
import de.canitzp.rarmor.items.rfarmor.ItemModule;
import de.canitzp.rarmor.items.rfarmor.ItemRFArmorBody;
import de.canitzp.rarmor.network.NetworkHandler;
import de.canitzp.rarmor.network.PacketSendNBTBoolean;
import de.canitzp.rarmor.newnetwork.RarmorData;
import de.canitzp.rarmor.util.EnergyUtil;
import de.canitzp.rarmor.util.JavaUtil;
import de.canitzp.rarmor.util.NBTUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

/**
 * @author canitzp
 */
public class ItemModuleEffects extends ItemModule implements IRarmorModule{

    public static ResourceLocation checkBox = RarmorResources.GUIELEMENTS.getNewLocation();
    public static HashMap<Potion, Integer> energyEffect = new LinkedHashMap<>();
    public static List<EffectCheckBox> effectBoxes = new ArrayList<>();
    private static int ySize;

    public ItemModuleEffects(){
        super("moduleEffects");
    }

    @Override
    public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {
        for(Potion p : ForgeRegistries.POTIONS){
            NBTUtil.setBoolean(stack, p.getName(), false);
        }
        super.onCreated(stack, worldIn, playerIn);
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

    public static int getModuleSlot(EntityPlayer player){
        ItemStack armor = RarmorUtil.getPlayersRarmorChestplate(player);
        InventoryBase inventory = RarmorData.getDataForRarmor(armor, player.worldObj.isRemote).inventory;
        ItemStack stack = inventory.getStackInSlot(ItemRFArmorBody.MODULESLOT);
        if((stack != null && stack.getItem() != ItemRegistry.moduleEffects) || stack == null){
            return 80;
        }
        return 82;
    }

    @Override
    public String getUniqueName(){
        return "Effects";
    }

    @SideOnly(Side.CLIENT)
    @Override
    public String getDescription(EntityPlayer player, ItemStack stack, boolean advancedTooltips){
        String s = "This is a awesome Module, it let you feel the world of effects. Every effect cost a big amount of energy per tick.";
        if(RarmorProperties.getBoolean("YouTubeMode")){
            s += "\n" + TextFormatting.DARK_GRAY + "You" + TextFormatting.RED + "Tube" + TextFormatting.GRAY + " Mode active. NightVision costs " + TextFormatting.RED + "0RF" + TextFormatting.GRAY;
        }
        return s + "\n" + TextFormatting.RED + "WIP If you want to help me with this Module\nthan look at https://github.com/canitzp/Rarmor";
    }

    @SideOnly(Side.CLIENT)
    @Override
    public ModuleType getModuleType(){
        return ModuleType.ACTIVE;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void drawGuiContainerBackgroundLayer(Minecraft minecraft, GuiContainer gui, ItemStack armorChestplate, ItemStack module, boolean settingActivated, boolean isColoringTab, float partialTicks, int mouseX, int mouseY){
        int yToTop = (gui.height / 2 - ySize / 2);
        //ItemStack mod = gui.inventorySlots.getSlot(75).getStack().getItem() == ItemRegistry.moduleEffects ? gui.inventorySlots.getSlot(75).getStack() : gui.inventorySlots.getSlot(81).getStack();
        minecraft.getTextureManager().bindTexture(ItemModuleEffects.checkBox);
        gui.drawTexturedModalRect(-150 + gui.guiLeft, yToTop - 4, 18, 0, 144, 4);
        gui.drawTexturedModalRect(-150 + gui.guiLeft, yToTop + ySize - 1, 18, 15, 144, 4);
        for(EffectCheckBox checkBox : effectBoxes){
            checkBox.drawCheckBox(gui, gui.guiLeft, yToTop, NBTUtil.getBoolean(module, checkBox.effect.getName()));
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void drawScreen(Minecraft minecraft, GuiContainer gui, ItemStack armorChestplate, ItemStack module, boolean settingActivated, boolean isColoringTab, float partialTicks, int mouseX, int mouseY){
        for(EffectCheckBox checkBox : effectBoxes){
            checkBox.drawScreen(gui, mouseX, mouseY, gui.guiLeft, gui.height / 2 - ySize / 2);
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void onMouseClicked(Minecraft minecraft, GuiContainer gui, ItemStack armorChestplate, ItemStack mod, boolean settingActivated, boolean isColoringTab, int type, int mouseX, int mouseY){
        if(mod != null){
            for(EffectCheckBox box : effectBoxes){
                if(box.mouseClicked(minecraft.fontRendererObj, mouseX, mouseY, gui.guiLeft, gui.height / 2 - ySize / 2)){
                    boolean b = NBTUtil.getBoolean(mod, box.effect.getName());
                    NBTUtil.setBoolean(mod, box.effect.getName(), !b);
                    box.setState(minecraft.thePlayer, !b);
                    //minecraft.playerController.setPlayerCapabilities();thePlayer.sendPlayerAbilities();
                }
            }
        }
    }

    @Override
    public void onModuleTickInArmor(World world, EntityPlayer player, ItemStack armorChestplate, ItemStack mod, InventoryBase inventory){
        if(!world.isRemote){
            if(mod != null){
                for(EffectCheckBox box : effectBoxes){
                    if(NBTUtil.getBoolean(mod, box.effect.getName())){
                        if(EnergyUtil.getEnergy(armorChestplate) >= box.energy){
                            if(player.getActivePotionEffect(box.effect) == null){
                                player.addPotionEffect(new PotionEffect(box.effect, Short.MAX_VALUE, 0, true, false));
                            }
                            EnergyUtil.reduceEnergy(armorChestplate, box.energy);
                        } else {
                            player.removePotionEffect(box.effect);
                        }
                    } else {
                        player.removePotionEffect(box.effect);
                    }
                }
                RarmorUtil.saveRarmor(player, inventory);
            }
        }
    }

    @Override
    public void onPickupFromSlot(World world, EntityPlayer player, ItemStack armorChestplate, ItemStack mod, InventoryBase inventory){
        if(!world.isRemote){
            if(mod != null){
                for(EffectCheckBox box : effectBoxes){
                    if(NBTUtil.getBoolean(mod, box.effect.getName())){
                        player.removePotionEffect(box.effect);
                    }
                }
            }
        }
    }

    public static class EffectCheckBox{
        public Potion effect;
        public int energy, x, y, height;
        public List<String> description = new ArrayList<>();
        public String text;
        private ResourceLocation loc = RarmorResources.GUIELEMENTS.getNewLocation();
        public EffectCheckBox(int x, int y, Potion effect, int energy){
            this.effect = effect;
            this.energy = energy;
            this.description = JavaUtil.newList("Energy per Tick: " + TextFormatting.RED + energy + "RF" + TextFormatting.RESET);
            this.text = I18n.translateToLocal(effect.getName());
            this.x = x;
            this.y = y;
            this.height = 9;
        }

        @SideOnly(Side.CLIENT)
        public void drawCheckBox(GuiContainer gui, int guiLeft, int guiTop, boolean isActive){
            gui.drawTexturedModalRect(guiLeft + x - 4, guiTop + y, 18, 5, 144, 10);
            gui.mc.fontRendererObj.drawString(text, guiLeft + x + 9, guiTop + y + 1, Colors.BLACK.colorValue);
            gui.mc.getTextureManager().bindTexture(this.loc);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.blendFunc(770, 771);
            if(isActive){
                gui.drawTexturedModalRect(this.x + guiLeft, this.y + guiTop, 0, 8, 8, 8);
            } else {
                gui.drawTexturedModalRect(this.x + guiLeft, this.y + guiTop, 0, 0, 8, 8);
            }
        }

        @SideOnly(Side.CLIENT)
        public boolean mouseClicked(FontRenderer fontRenderer, int mouseX, int mouseY, int guiLeft, int guiTop){
            if(mouseX >= x + guiLeft && mouseY >= y + guiTop){
                if(mouseX <= x + fontRenderer.getStringWidth(this.text) + 9 + guiLeft && mouseY <= y + height + guiTop){
                    return true;
                }
            }
            return false;
        }

        @SideOnly(Side.CLIENT)
        public void drawScreen(GuiContainer gui, int mouseX, int mouseY, int guiLeft, int guiTop){
            if(description != null && mouseX >= x + guiLeft && mouseY >= y + guiTop){
                if(mouseX <= x + gui.mc.fontRendererObj.getStringWidth(this.text) + 9 + guiLeft && mouseY <= y + height + guiTop){
                    gui.drawHoveringText(description, mouseX, mouseY);
                }
            }
        }

        public void setState(EntityPlayer player, boolean b){
            NetworkHandler.wrapper.sendToServer(new PacketSendNBTBoolean(player, getModuleSlot(player), effect.getName(), b));
        }
    }
}
