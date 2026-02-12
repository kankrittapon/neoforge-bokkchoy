package net.kankrittapon.rpgem.entity.custom;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.level.Level;

public class SkeletonLord extends Skeleton {
    public SkeletonLord(EntityType<? extends Skeleton> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Skeleton.createAttributes()
                .add(Attributes.MAX_HEALTH, 250.0D)
                .add(Attributes.ATTACK_DAMAGE, 15.0D) // High damage if it hits with melee
                .add(Attributes.MOVEMENT_SPEED, 0.3D);
    }
}
