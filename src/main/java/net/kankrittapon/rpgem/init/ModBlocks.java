package net.kankrittapon.rpgem.init;

import net.kankrittapon.rpgem.RPGEasyMode;
import net.kankrittapon.rpgem.block.AlchemyTableBlock;
import net.kankrittapon.rpgem.block.AncientForgeBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(RPGEasyMode.MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(RPGEasyMode.MODID);

    public static final DeferredBlock<Block> ALCHEMY_TABLE = registerBlock("alchemy_table",
            () -> new AlchemyTableBlock(
                    BlockBehaviour.Properties.of().strength(2.5f).requiresCorrectToolForDrops().noOcclusion()));
    public static final DeferredBlock<Block> ANCIENT_FORGE_TABLE = registerBlock("ancient_forge_table",
            () -> new AncientForgeBlock(BlockBehaviour.Properties.of().strength(3.5f).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> TOME_OF_FORGOTTEN_TABLE = registerBlock("tome_of_forgotten_table",
            () -> new Block(BlockBehaviour.Properties.of().strength(1.5f)));

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> DeferredItem<BlockItem> registerBlockItem(String name, DeferredBlock<T> block) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
