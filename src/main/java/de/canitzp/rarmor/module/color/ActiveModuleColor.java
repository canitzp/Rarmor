package de.canitzp.rarmor.module.color;

import de.canitzp.rarmor.api.RarmorAPI;
import de.canitzp.rarmor.api.internal.IRarmorData;
import de.canitzp.rarmor.api.inventory.RarmorModuleContainer;
import de.canitzp.rarmor.api.inventory.RarmorModuleGui;
import de.canitzp.rarmor.api.module.ActiveRarmorModule;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * @author canitzp
 */
public class ActiveModuleColor extends ActiveRarmorModule{

    public static final String IDENTIFIER = RarmorAPI.MOD_ID+"Color";
    public ItemStack stack = new ItemStack(Items.BLACK_DYE);

    public ActiveModuleColor(IRarmorData data){
        super(data);
    }

    @Override
    public String getIdentifier(){
        return IDENTIFIER;
    }

    @Override
    public RarmorModuleContainer createContainer(Player player, AbstractContainerMenu container){
        return new ContainerModuleColor(player, container, this);
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    public RarmorModuleGui createGui(){
        return new GuiModuleColor(this);
    }
    
    @Override
    public boolean hasTab(Player player){
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public ItemStack getDisplayIcon(){
        return stack;
    }
    
    @Override
    public boolean doesRenderOnOverlay(Minecraft mc, Player player, IRarmorData data) {
        return false;
    }
}
