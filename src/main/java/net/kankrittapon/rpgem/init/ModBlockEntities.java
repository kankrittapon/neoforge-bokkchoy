package net.kankrittapon.rpgem.init;

import net.kankrittapon.rpgem.RPGEasyMode;
import net.kankrittapon.rpgem.block.entity.AlchemyTableBlockEntity;
import net.kankrittapon.rpgem.block.entity.AncientForgeBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlockEntities {
        public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister
                        .create(BuiltInRegistries.BLOCK_ENTITY_TYPE, RPGEasyMode.MODID);

        public static final Supplier<BlockEntityType<AlchemyTableBlockEntity>> ALCHEMY_TABLE_BE = BLOCK_ENTITIES
                        .register("alchemy_table_be", () -> BlockEntityType.Builder.of(AlchemyTableBlockEntity::new,
                                        ModBlocks.ALCHEMY_TABLE.get()).build(null));

        public static final Supplier<BlockEntityType<AncientForgeBlockEntity>> ANCIENT_FORGE_BE = BLOCK_ENTITIES
                        .register("ancient_forge_be", () -> BlockEntityType.Builder.of(AncientForgeBlockEntity::new,
                                        ModBlocks.ANCIENT_FORGE_TABLE.get()).build(null));

        public static void register(IEventBus eventBus) {
                BLOCK_ENTITIES.register(eventBus);
        }
}
