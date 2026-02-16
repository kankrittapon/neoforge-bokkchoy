package net.kankrittapon.rpgem.client.model;

import net.kankrittapon.rpgem.RPGEasyMode;
import net.kankrittapon.rpgem.entity.custom.FairyEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class FairyModel extends GeoModel<FairyEntity> {
    @Override
    public ResourceLocation getModelResource(FairyEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(RPGEasyMode.MODID, "geo/fairy.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(FairyEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(RPGEasyMode.MODID, "textures/entity/fairy/fairy.png");
    }

    @Override
    public ResourceLocation getAnimationResource(FairyEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(RPGEasyMode.MODID, "animations/fairy.animation.json");
    }
}
