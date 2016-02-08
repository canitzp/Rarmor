package de.canitzp.rarmor.blocks;

import net.minecraft.block.Block;

/**
 * @author canitzp
 */
public class BlockRegistry {

    public static Block circuitCreator;

    public static void preInit(){
        circuitCreator = new BlockCircuitCreator();
    }

}
