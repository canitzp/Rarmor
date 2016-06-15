package de.canitzp.rarmor.armor;

import de.canitzp.rarmor.Rarmor;
import de.canitzp.rarmor.api.IRarmorTab;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * @author canitzp
 */
public class RarmorInventoryTab implements IRarmorTab{

    private final ResourceLocation tabLoc = new ResourceLocation(Rarmor.MODID, "textures/gui/guiTabs.png");

    @Override
    public String getTabIdentifier(){
        return Rarmor.MODID + ":inventoryTab";
    }

    @Override
    public ItemStack getTabIcon(){
        return new ItemStack(Items.COAL);
    }

    @Override
    public String getTabHoveringText(){
        return "Inventory";
    }

    @Override
    public void drawGui(GuiContainer gui, int guiLeft, int guiTop, int mouseX, int mouseY, float partialTicks){
        gui.mc.getTextureManager().bindTexture(this.tabLoc);
        gui.drawTexturedModalRect(guiLeft + 4, guiTop + 4, 0, 0, 239, 138);
    }
}
