package net.kankrittapon.rpgem.event;

import net.kankrittapon.rpgem.RPGEasyMode;
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
}
