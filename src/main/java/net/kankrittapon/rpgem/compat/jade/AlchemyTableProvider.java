package net.kankrittapon.rpgem.compat.jade;

/*
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import net.kankrittapon.rpgem.RPGEasyMode;
import net.kankrittapon.rpgem.block.entity.AlchemyTableBlockEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public enum AlchemyTableProvider implements IBlockComponentProvider {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        if (accessor.getBlockEntity() instanceof AlchemyTableBlockEntity table) {
            int progress = table.getProgress();
            int maxProgress = table.getMaxProgress();

            if (maxProgress > 0 && progress > 0) {
                float percent = (float) progress / maxProgress * 100;
                tooltip.add(Component.translatable("tooltip.rpgem.alchemy_table.progress",
                        String.format("%.1f", percent)));
            } else {
                tooltip.add(Component.translatable("tooltip.rpgem.alchemy_table.idle"));
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return ResourceLocation.fromNamespaceAndPath(RPGEasyMode.MODID, "alchemy_table");
    }
}
*/
public class AlchemyTableProvider {
} // Placeholder
