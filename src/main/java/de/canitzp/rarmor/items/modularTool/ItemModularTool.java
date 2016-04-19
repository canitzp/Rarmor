package de.canitzp.rarmor.items.modularTool;

import cofh.api.energy.ItemEnergyContainer;
import de.canitzp.rarmor.Rarmor;
import de.canitzp.rarmor.api.InventoryBase;
import de.canitzp.rarmor.api.modules.IToolModule;
import de.canitzp.rarmor.inventory.GuiHandler;
import de.canitzp.rarmor.util.EnergyUtil;
import de.canitzp.rarmor.util.NBTUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.lwjgl.input.Keyboard;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static de.canitzp.rarmor.Rarmor.MODID;
import static de.canitzp.rarmor.Rarmor.rarmorTab;

/**
 * @author canitzp
 */
public class ItemModularTool extends ItemEnergyContainer{

    public static int slots = 3;

    public ItemModularTool(int maxEnergy, int maxTransfer, String name){
        super(maxEnergy, maxTransfer);
        this.setHasSubtypes(true);
        this.setMaxStackSize(1);
        this.setUnlocalizedName(MODID + "." + name);
        this.setRegistryName(name);
        this.setCreativeTab(rarmorTab);
        Rarmor.proxy.addRenderer(new ItemStack(this), name);
        Rarmor.proxy.addSpecialRenderer(new ItemStack(this, 1, ToolTypes.PICKAXE.meta), name);
        Rarmor.proxy.addSpecialRenderer(new ItemStack(this, 1, ToolTypes.AXE.meta), name);
        Rarmor.proxy.addSpecialRenderer(new ItemStack(this, 1, ToolTypes.SHOVEL.meta), name);
        Rarmor.proxy.addSpecialRenderer(new ItemStack(this, 1, ToolTypes.SWORD.meta), name);
        Rarmor.proxy.addSpecialRenderer(new ItemStack(this, 1, ToolTypes.HOE.meta), name);
        GameRegistry.register(this);
    }

    public static int getModulesAmount(ItemStack tool){
        int amount = 0;
        InventoryBase inventory = NBTUtil.readSlots(tool, slots);
        for (ItemStack stack1 : inventory.slots){
            if (stack1 != null && stack1.getItem() instanceof IToolModule){
                amount++;
            }
        }
        return amount;
    }

    @Override
    public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems){
        ItemStack stackFull = new ItemStack(this);
        EnergyUtil.setEnergy(stackFull, this.getMaxEnergyStored(stackFull));
        subItems.add(stackFull);
        ItemStack stackEmpty = new ItemStack(this);
        EnergyUtil.setEnergy(stackEmpty, 0);
        subItems.add(stackEmpty);
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack){
        return EnergyUtil.getEnergy(stack) != capacity;
    }

    @Override
    public void onCreated(ItemStack stack, World world, EntityPlayer player){
        EnergyUtil.setEnergy(stack, 0);
    }

    @Override
    public Set<String> getToolClasses(ItemStack stack){
        Set<String> toolClasses = new HashSet<>();
        toolClasses.add("pickaxe");
        toolClasses.add("axe");
        toolClasses.add("shovel");
        toolClasses.add("hoe");
        toolClasses.add("sword");
        return toolClasses;
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker){
        float attack = 0;
        InventoryBase inventory = NBTUtil.readSlots(stack, slots);
        for (ItemStack stack1 : inventory.slots){
            if (stack1 != null && stack1.getItem() instanceof IToolModule){
                attack += ((IToolModule) stack1.getItem()).onHitEntity(stack1, stack, target, attacker);
            }
        }
        target.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) attacker), attack / getModulesAmount(stack));
        return super.hitEntity(stack, target, attacker);
    }

    @Override
    public float getStrVsBlock(ItemStack stack, IBlockState state){
        float lowest = Float.MAX_VALUE;
        InventoryBase inventory = NBTUtil.readSlots(stack, slots);
        for (ItemStack stack1 : inventory.slots){
            if (stack1 != null && stack1.getItem() instanceof IToolModule){
                float f = ((IToolModule) stack1.getItem()).getStrengthAgainstBlock(stack1, state, stack);
                if (f < lowest) lowest = f;
            }
        }
        return lowest == Float.MAX_VALUE ? super.getStrVsBlock(stack, state) : lowest;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand){
        stack.setItemDamage(1);
        if (Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
            player.openGui(Rarmor.instance, GuiHandler.MODULARTOOL, world, (int) player.serverPosX, (int) player.serverPosY, (int) player.serverPosZ);
            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        }
        InventoryBase inventory = NBTUtil.readSlots(stack, slots);
        for (ItemStack stack1 : inventory.slots){
            if (stack1 != null && stack1.getItem() instanceof IToolModule){
                stack = ((IToolModule) stack1.getItem()).onRightClick(stack1, stack, world, player);
            }
        }
        return super.onItemRightClick(stack, world, player, hand);
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState blockIn, BlockPos pos, EntityLivingBase entityLiving){
        InventoryBase inventory = NBTUtil.readSlots(stack, slots);
        for (ItemStack stack1 : inventory.slots){
            if (stack1 != null && stack1.getItem() instanceof IToolModule){
                return ((IToolModule) stack1.getItem()).onBlockDestroyed(stack1, stack, worldIn, blockIn, pos, entityLiving);
            }
        }
        return super.onBlockDestroyed(stack, worldIn, blockIn, pos, entityLiving);
    }

    @Override
    public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, EntityPlayer player){
        InventoryBase inventory = NBTUtil.readSlots(stack, slots);
        for (ItemStack stack1 : inventory.slots){
            if (stack1 != null && stack1.getItem() instanceof IToolModule){
                if (((IToolModule) stack1.getItem()).onBlockStartBreak(stack1, stack, player.worldObj.getBlockState(pos), player, pos))
                    return true;
            }
        }
        return super.onBlockStartBreak(stack, pos, player);
    }


    @Override
    public boolean canHarvestBlock(IBlockState state, ItemStack stack){
        InventoryBase inventory = NBTUtil.readSlots(stack, slots);
        for (ItemStack stack1 : inventory.slots){
            if (stack1 != null && stack1.getItem() instanceof IToolModule){
                if (((IToolModule) stack1.getItem()).canHarvestBlock(stack1, state, stack)) return true;
            }
        }

        return super.canHarvestBlock(state, stack);
    }

    public enum ToolTypes{
        PICKAXE(1),
        AXE(2),
        SHOVEL(3),
        SWORD(4),
        HOE(5);

        public int meta;

        ToolTypes(int meta){
            this.meta = meta;
        }
    }
}
