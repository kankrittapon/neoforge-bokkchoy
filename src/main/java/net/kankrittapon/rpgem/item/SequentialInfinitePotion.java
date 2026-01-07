package net.kankrittapon.rpgem.item;

import net.kankrittapon.rpgem.init.ModMobEffects;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;

import java.util.ArrayList;
import java.util.List;

public class SequentialInfinitePotion extends Item {

    public SequentialInfinitePotion(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        Player player = livingEntity instanceof Player ? (Player) livingEntity : null;
        if (player == null)
            return stack;

        if (!level.isClientSide) {
            List<String> history = getIngredientHistory(stack);
            int tier = history.size();

            // 1. Perform Cleanse
            if (tier >= 2) {
                performCleanse(player, tier);
            }

            // 2. Apply Custom Effects
            applyEffects(player, history);

            // 3. Handle Cooldown
            int cooldownSeconds = switch (tier) {
                case 1 -> 8;
                case 2 -> 6;
                case 3 -> 2; // Very fast ("OP")
                default -> 8;
            };
            player.getCooldowns().addCooldown(this, cooldownSeconds * 20);
        }

        return super.finishUsingItem(stack, level, livingEntity);
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 32; // Standard drink time
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return getIngredientHistory(stack).size() >= 3; // Tier 3 Rainbow Glint
    }

    @Override
    public Component getName(ItemStack stack) {
        List<String> history = getIngredientHistory(stack);
        int tier = history.size();

        if (tier == 1 && !history.isEmpty()) {
            return switch (history.get(0)) {
                case "H" -> Component.literal("Potion of Undying Vitality");
                case "B" -> Component.literal("Potion of Unyielding Structure");
                case "C" -> Component.literal("Potion of Cosmic Clarity");
                default -> super.getName(stack);
            };
        } else if (tier == 2 && history.size() >= 2) {
            String combo = history.get(0) + history.get(1);
            return switch (combo) {
                case "HB" -> Component.literal("Potion of Armored Vitality");
                case "HC" -> Component.literal("Potion of Enlightened Pulse");
                case "BH" -> Component.literal("Potion of Living Structure");
                case "BC" -> Component.literal("Potion of Astral Spine");
                case "CH" -> Component.literal("Potion of Cosmic Flesh");
                case "CB" -> Component.literal("Potion of Solidified Void");
                default -> super.getName(stack);
            };
        } else if (tier >= 3) {
            return Component.literal("The Elixir of Boundless Eternity").withStyle(ChatFormatting.GOLD);
        }

        return super.getName(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents,
            TooltipFlag tooltipFlag) {
        List<String> history = getIngredientHistory(stack);
        int tier = history.size();

        if (tier > 0) {
            tooltipComponents.add(Component.literal("Tier: " + tier).withStyle(ChatFormatting.GRAY));
            tooltipComponents.add(Component.literal("Ingredients: " + String.join(", ", history))
                    .withStyle(ChatFormatting.DARK_GRAY));
        }

        if (tier == 3) {
            tooltipComponents.add(Component.literal("The Savior's Grace").withStyle(ChatFormatting.GOLD));
        }

        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

    private void performCleanse(Player player, int tier) {
        List<MobEffectInstance> activeEffects = new ArrayList<>(player.getActiveEffects());

        for (MobEffectInstance instance : activeEffects) {
            if (instance.getEffect().value().getCategory() == net.minecraft.world.effect.MobEffectCategory.HARMFUL) {
                boolean isBlacklisted = instance.getEffect().value() == MobEffects.WITHER ||
                        instance.getEffect().value() == MobEffects.LEVITATION ||
                        instance.getEffect().value() == MobEffects.DARKNESS;

                if (tier == 2) {
                    // Tier 2: Partial Cleanse (Skip Blacklist)
                    if (!isBlacklisted) {
                        player.removeEffect(instance.getEffect());
                    }
                } else if (tier == 3) {
                    // Tier 3: Total Cleanse (Remove Everything)
                    player.removeEffect(instance.getEffect());
                }
            }
        }
    }

    private void applyEffects(Player player, List<String> history) {
        int tier = history.size();

        // Base Healing
        if (tier == 1)
            player.heal(8.0F); // 4 Hearts
        else if (tier == 2)
            player.heal(16.0F); // 8 Hearts
        else if (tier == 3)
            player.setHealth(player.getMaxHealth()); // 100%

        if (tier == 1 && !history.isEmpty()) {
            String primary = history.get(0);
            switch (primary) {
                case "H" -> {
                    player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 300, 1)); // 15s Regen II
                    player.addEffect(new MobEffectInstance(MobEffects.SATURATION, 1, 0));
                }
                case "B" -> {
                    player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 400, 0)); // 20s Resist I
                }
                case "C" -> {
                    player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 2400, 1)); // 2m Absorp II
                }
            }

        } else if (tier == 2 && history.size() >= 2) {
            String combo = history.get(0) + history.get(1);
            switch (combo) {
                case "HB" -> { // Juggernaut
                    player.addEffect(new MobEffectInstance(MobEffects.HEALTH_BOOST, 600, 4)); // 30s +20HP
                    player.setHealth(player.getMaxHealth());
                }
                case "HC" -> { // Purification
                    player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200, 2)); // 10s Regen III
                }
                case "BH" -> { // Thornmail
                    player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 300, 1)); // 15s Resist II
                    // Thorns Logic
                    player.level().getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(5.0))
                            .stream().filter(e -> e != player)
                            .forEach(e -> e.hurt(player.damageSources().magic(), 6.0f));
                }
                case "BC" -> { // Unstoppable
                    player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 300, 1)); // 15s Resist II
                }
                case "CH" -> { // Evasion
                    player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 300, 1)); // 15s Speed II
                    player.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 300, 0));
                    player.addEffect(new MobEffectInstance(MobEffects.JUMP, 300, 1)); // Jump II
                }
                case "CB" -> { // Diamond Skin
                    player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 600, 3)); // Absorp IV
                    player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 600, 0)); // 30s Fire Res
                }
            }

        } else if (tier == 3) {
            // "The Savior"
            player.setHealth(player.getMaxHealth());
            player.addEffect(new MobEffectInstance(ModMobEffects.BOUNDLESS_GRACE, 1200, 0)); // 60s

            // Extra Buffs per user request
            player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 300, 2)); // 15s Regen III
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 300, 1)); // 15s Res II
            player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 2400, 3)); // 2m Absorp IV
            player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 400, 0)); // 20s Fire Res
        }
    }

    @Override
    public net.minecraft.world.InteractionResultHolder<ItemStack> use(Level level, Player player,
            net.minecraft.world.InteractionHand hand) {
        return net.minecraft.world.item.ItemUtils.startUsingInstantly(level, player, hand);
    }

    private List<String> getIngredientHistory(ItemStack stack) {
        List<String> history = new ArrayList<>();
        if (stack.has(DataComponents.CUSTOM_DATA)) {
            CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
            CompoundTag tag = customData.copyTag();
            if (tag.contains("IngredientHistory")) {
                ListTag list = tag.getList("IngredientHistory", 8); // 8 = String
                for (Tag t : list) {
                    history.add(t.getAsString());
                }
            }
        }
        return history;
    }
}
