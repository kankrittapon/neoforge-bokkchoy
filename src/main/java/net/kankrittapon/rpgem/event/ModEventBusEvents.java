package net.kankrittapon.rpgem.event;

import net.kankrittapon.rpgem.RPGEasyMode;
import net.kankrittapon.rpgem.entity.custom.FairyEntity;
import net.kankrittapon.rpgem.entity.custom.SkeletonLord;
import net.kankrittapon.rpgem.entity.custom.ZombieKing;
import net.kankrittapon.rpgem.init.ModAttributes;
import net.kankrittapon.rpgem.init.ModEntities;
import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;

@EventBusSubscriber(modid = RPGEasyMode.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.ZOMBIE_KING.get(), ZombieKing.createAttributes().build());
        event.put(ModEntities.SKELETON_LORD.get(), SkeletonLord.createAttributes().build());
        event.put(ModEntities.FAIRY.get(), FairyEntity.createAttributes().build());
    }

    /**
     * Attach custom RPG attributes to Player entity.
     * ถ้าไม่ทำขั้นตอนนี้ player.getAttributeValue(CRIT_CHANCE) จะ throw
     * IllegalArgumentException เพราะ Player ไม่รู้จัก attribute
     */
    @SubscribeEvent
    public static void modifyEntityAttributes(EntityAttributeModificationEvent event) {
        // Weapon attributes
        event.add(EntityType.PLAYER, ModAttributes.LIFE_STEAL);
        event.add(EntityType.PLAYER, ModAttributes.CRIT_CHANCE);
        event.add(EntityType.PLAYER, ModAttributes.ELEMENT_DAMAGE);
        event.add(EntityType.PLAYER, ModAttributes.ACCURACY);
        event.add(EntityType.PLAYER, ModAttributes.ARMOR_PENETRATION);
        event.add(EntityType.PLAYER, ModAttributes.ANTI_HEAL);
        // Armor attributes
        event.add(EntityType.PLAYER, ModAttributes.DAMAGE_REDUCTION);
        event.add(EntityType.PLAYER, ModAttributes.EVASION);
        event.add(EntityType.PLAYER, ModAttributes.REFLECT_RESIST);
        event.add(EntityType.PLAYER, ModAttributes.SEAL_RESIST);
    }

    @SubscribeEvent
    public static void registerSpawnPlacements(net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent event) {
        event.register(ModEntities.ZOMBIE_KING.get(),
                net.minecraft.world.entity.SpawnPlacementTypes.ON_GROUND,
                net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                net.minecraft.world.entity.monster.Monster::checkMonsterSpawnRules,
                net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent.Operation.REPLACE);

        event.register(ModEntities.SKELETON_LORD.get(),
                net.minecraft.world.entity.SpawnPlacementTypes.ON_GROUND,
                net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                net.minecraft.world.entity.monster.Monster::checkMonsterSpawnRules,
                net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent.Operation.REPLACE);
    }
}
