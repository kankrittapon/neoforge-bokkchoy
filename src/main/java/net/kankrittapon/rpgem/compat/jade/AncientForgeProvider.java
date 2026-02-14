package net.kankrittapon.rpgem.compat.jade;

import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import net.kankrittapon.rpgem.RPGEasyMode;
import net.kankrittapon.rpgem.block.entity.AncientForgeBlockEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public enum AncientForgeProvider implements IBlockComponentProvider {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        if (accessor.getBlockEntity() instanceof AncientForgeBlockEntity forge) {
            ItemStack equipment = forge.itemHandler.getStackInSlot(0);
            ItemStack stone = forge.itemHandler.getStackInSlot(1);

            if (!equipment.isEmpty() && !stone.isEmpty()) {
                tooltip.add(Component.translatable("tooltip.rpgem.ancient_forge.ready"));
            } else if (!equipment.isEmpty()) {
                tooltip.add(Component.translatable("tooltip.rpgem.ancient_forge.needs_stone"));
            } else {
                tooltip.add(Component.translatable("tooltip.rpgem.ancient_forge.idle"));
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return ResourceLocation.fromNamespaceAndPath(RPGEasyMode.MODID, "ancient_forge");
    }
}
