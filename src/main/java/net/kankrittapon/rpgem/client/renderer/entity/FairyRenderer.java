package net.kankrittapon.rpgem.client.renderer.entity;

import net.kankrittapon.rpgem.client.model.FairyModel;
import net.kankrittapon.rpgem.entity.custom.FairyEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class FairyRenderer extends GeoEntityRenderer<FairyEntity> {
    public FairyRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new FairyModel());
    }
}
