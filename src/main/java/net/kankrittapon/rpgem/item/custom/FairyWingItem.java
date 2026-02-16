package net.kankrittapon.rpgem.item.custom;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.network.chat.Component;
import java.util.List;

public class FairyWingItem extends Item {
    public FairyWingItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        // NeoForge 1.21.1 DataComponents Logic for NBT
        net.minecraft.nbt.CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();

        if (!level.isClientSide()) {
            net.kankrittapon.rpgem.entity.custom.FairyEntity fairy = net.kankrittapon.rpgem.init.ModEntities.FAIRY.get()
                    .create(level);
            if (fairy != null) {
                fairy.setPos(player.getX(), player.getY() + 1.5, player.getZ());
                fairy.setOwnerUUID(player.getUUID());

                // Load data from Item NBT
                if (tag.contains("Tier")) {
                    fairy.setTier(tag.getInt("Tier"));
                } else {
                    fairy.setTier(1); // Default Tier 1
                    tag.putInt("Tier", 1); // Save back to item
                }

                if (tag.contains("Level")) {
                    fairy.setLevel(tag.getInt("Level"));
                } else {
                    fairy.setLevel(1); // Default Level 1
                    tag.putInt("Level", 1); // Save back to item
                }

                // Save updated tag back to item
                stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));

                level.addFreshEntity(fairy);
                player.displayClientMessage(
                        Component.literal("§aFairy Summoned! Tier: " + fairy.getTier() + " Level: " + fairy.getLevel()),
                        true);
                player.getCooldowns().addCooldown(this, 20); // 1 sec cooldown
            }
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents,
            TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);

        net.minecraft.nbt.CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        int tier = (tag.contains("Tier")) ? tag.getInt("Tier") : 1;
        int level = (tag.contains("Level")) ? tag.getInt("Level") : 1;

        tooltipComponents.add(Component.literal("§7Tier: §e" + tier));
        tooltipComponents.add(Component.literal("§7Level: §a" + level));
    }
}
