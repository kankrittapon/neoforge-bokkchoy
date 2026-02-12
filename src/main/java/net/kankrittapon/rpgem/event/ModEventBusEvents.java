package net.kankrittapon.rpgem.event;

import net.kankrittapon.rpgem.RPGEasyMode;
import net.kankrittapon.rpgem.entity.custom.SkeletonLord;
import net.kankrittapon.rpgem.entity.custom.ZombieKing;
import net.kankrittapon.rpgem.init.ModEntities;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

@EventBusSubscriber(modid = RPGEasyMode.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.ZOMBIE_KING.get(), ZombieKing.createAttributes().build());
        event.put(ModEntities.SKELETON_LORD.get(), SkeletonLord.createAttributes().build());
    }
}
