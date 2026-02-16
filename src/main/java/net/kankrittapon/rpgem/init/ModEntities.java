package net.kankrittapon.rpgem.init;

import net.kankrittapon.rpgem.RPGEasyMode;
import net.kankrittapon.rpgem.entity.custom.SkeletonLord;
import net.kankrittapon.rpgem.entity.custom.ZombieKing;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEntities {
        public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE,
                        RPGEasyMode.MODID);

        public static final DeferredHolder<EntityType<?>, EntityType<ZombieKing>> ZOMBIE_KING = ENTITIES.register(
                        "zombie_king",
                        () -> EntityType.Builder.of(ZombieKing::new, MobCategory.MONSTER)
                                        .sized(1.2f, 3.0f)
                                        .build("zombie_king"));

        public static final DeferredHolder<EntityType<?>, EntityType<SkeletonLord>> SKELETON_LORD = ENTITIES.register(
                        "skeleton_lord",
                        () -> EntityType.Builder.of(SkeletonLord::new, MobCategory.MONSTER)
                                        .sized(1.2f, 3.0f)
                                        .build("skeleton_lord"));

        public static final DeferredHolder<EntityType<?>, EntityType<net.kankrittapon.rpgem.entity.custom.FairyEntity>> FAIRY = ENTITIES
                        .register(
                                        "fairy",
                                        () -> EntityType.Builder
                                                        .of(net.kankrittapon.rpgem.entity.custom.FairyEntity::new,
                                                                        MobCategory.CREATURE)
                                                        .sized(0.5f, 0.5f)
                                                        .build("fairy"));
}
