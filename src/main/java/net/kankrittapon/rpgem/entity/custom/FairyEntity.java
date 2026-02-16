package net.kankrittapon.rpgem.entity.custom;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.kankrittapon.rpgem.item.custom.FairyWingItem;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.items.ItemStackHandler;

public class FairyEntity extends PathfinderMob implements GeoEntity, MenuProvider {
    public final ItemStackHandler inventory = new ItemStackHandler(6) {
        @Override
        protected void onContentsChanged(int slot) {
            // Mark entity as changed/needs saving if needed
        }
    };

    @Override
    public Component getDisplayName() {
        return this.getName();
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new net.kankrittapon.rpgem.menu.FairyMenu(containerId, playerInventory, this.inventory, this);
    }

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private static final net.minecraft.network.syncher.EntityDataAccessor<Integer> TIER = net.minecraft.network.syncher.SynchedEntityData
            .defineId(FairyEntity.class, net.minecraft.network.syncher.EntityDataSerializers.INT);
    private static final net.minecraft.network.syncher.EntityDataAccessor<Integer> LEVEL = net.minecraft.network.syncher.SynchedEntityData
            .defineId(FairyEntity.class, net.minecraft.network.syncher.EntityDataSerializers.INT);
    private static final net.minecraft.network.syncher.EntityDataAccessor<Integer> EXP = net.minecraft.network.syncher.SynchedEntityData
            .defineId(FairyEntity.class, net.minecraft.network.syncher.EntityDataSerializers.INT);

    // Constant for Owner UUID (Optional for now, but good practice)
    private static final net.minecraft.network.syncher.EntityDataAccessor<java.util.Optional<java.util.UUID>> OWNER_UUID = net.minecraft.network.syncher.SynchedEntityData
            .defineId(FairyEntity.class, net.minecraft.network.syncher.EntityDataSerializers.OPTIONAL_UUID);

    public FairyEntity(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    public static FairyEntity createDummy(Level level, net.minecraft.nbt.CompoundTag tag) {
        FairyEntity dummy = new FairyEntity(net.kankrittapon.rpgem.init.ModEntities.FAIRY.get(), level);
        if (tag != null) {
            dummy.readAdditionalSaveData(tag);
        }
        return dummy;
    }

    @Override
    protected void defineSynchedData(net.minecraft.network.syncher.SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(TIER, 1);
        builder.define(LEVEL, 1);
        builder.define(EXP, 0);
        builder.define(OWNER_UUID, java.util.Optional.empty());
    }

    @Override
    public void addAdditionalSaveData(net.minecraft.nbt.CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Tier", this.getTier());
        compound.putInt("Level", this.getLevel());
        compound.putInt("Exp", this.getExp());
        compound.putInt("TearCooldown", this.tearCooldown);
        compound.putInt("CureCooldown", this.cureCooldown);
        if (this.getOwnerUUID().isPresent()) {
            compound.putUUID("Owner", this.getOwnerUUID().get());
        }
        compound.put("Inventory", this.inventory.serializeNBT(this.registryAccess()));
    }

    @Override
    public void readAdditionalSaveData(net.minecraft.nbt.CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("Tier")) {
            this.setTier(compound.getInt("Tier"));
        }
        if (compound.contains("Level")) {
            this.setLevel(compound.getInt("Level"));
        }
        if (compound.contains("Exp")) {
            this.setExp(compound.getInt("Exp"));
        }
        if (compound.contains("TearCooldown")) {
            this.tearCooldown = compound.getInt("TearCooldown");
        }
        if (compound.contains("CureCooldown")) {
            this.cureCooldown = compound.getInt("CureCooldown");
        }
        if (compound.contains("Owner")) {
            this.setOwnerUUID(compound.getUUID("Owner"));
        }
        if (compound.contains("Inventory")) {
            this.inventory.deserializeNBT(this.registryAccess(), compound.getCompound("Inventory"));
        }
    }

    // Getters and Setters
    public int getTier() {
        return this.entityData.get(TIER);
    }

    public void setTier(int tier) {
        this.entityData.set(TIER, Math.max(1, Math.min(tier, 4))); // Cap Tier at 4
    }

    public int getLevel() {
        return this.entityData.get(LEVEL);
    }

    public void setLevel(int level) {
        // Cap Level based on Tier
        int maxLevel = this.getTier() * 10;
        this.entityData.set(LEVEL, Math.max(1, Math.min(level, maxLevel)));
    }

    public java.util.Optional<java.util.UUID> getOwnerUUID() {
        return this.entityData.get(OWNER_UUID);
    }

    public void setOwnerUUID(java.util.UUID uuid) {
        this.entityData.set(OWNER_UUID, java.util.Optional.ofNullable(uuid));
    }

    public int getExp() {
        return this.entityData.get(EXP);
    }

    public void setExp(int exp) {
        this.entityData.set(EXP, Math.max(0, exp));
    }

    public int getMaxLevelForTier() {
        return this.getTier() * 10;
    }

    public int getMaxExpForLevel(int level) {
        return level * 100; // Base formula: 100 EXP per level
    }

    public void addExp(int amount) {
        if (this.getLevel() >= this.getMaxLevelForTier())
            return;

        int currentExp = this.getExp() + amount;
        int maxExp = this.getMaxExpForLevel(this.getLevel());

        while (currentExp >= maxExp) {
            currentExp -= maxExp;
            this.levelUp();
            maxExp = this.getMaxExpForLevel(this.getLevel());
            if (this.getLevel() >= this.getMaxLevelForTier()) {
                currentExp = 0;
                break;
            }
        }
        this.setExp(currentExp);
    }

    private void levelUp() {
        this.setLevel(this.getLevel() + 1);
        this.refreshDimensions();
        this.heal(this.getMaxHealth());
        this.playSound(net.minecraft.sounds.SoundEvents.PLAYER_LEVELUP, 1.0F, 1.0F);
        if (this.level().isClientSide) {
            // Add particles
            for (int i = 0; i < 5; i++) {
                this.level().addParticle(net.minecraft.core.particles.ParticleTypes.HAPPY_VILLAGER,
                        this.getRandomX(0.5D), this.getRandomY() + 0.5D, this.getRandomZ(0.5D), 0, 0, 0);
            }
        }
    }

    public boolean canSprout() {
        return this.getLevel() >= this.getMaxLevelForTier() && this.getTier() < 4;
    }

    public void sprout() {
        if (this.level().isClientSide)
            return;

        // Simple success logic for now: 70% success rate
        // TODO: Implement luck/rarity modifiers later
        boolean success = this.random.nextFloat() < 0.7f;

        if (success) {
            this.setTier(this.getTier() + 1);
            this.setLevel(1);
            this.setExp(0);
            this.playSound(net.minecraft.sounds.SoundEvents.TOTEM_USE, 1.0F, 1.0F);
            // Broadcast Sprout Success Message
            if (this.getOwnerUUID().isPresent()) {
                Player owner = this.level().getPlayerByUUID(this.getOwnerUUID().get());
                if (owner != null) {
                    owner.sendSystemMessage(net.minecraft.network.chat.Component.translatable(
                            "message.rpgem.fairy_sprout_success", this.getName().getString(), this.getTier()));
                }
            }
        } else {
            // Failure penalty (e.g. lose levels)
            this.setLevel(1);
            this.setExp(0);
            this.playSound(net.minecraft.sounds.SoundEvents.GLASS_BREAK, 1.0F, 1.0F);
            // Broadcast Sprout Fail Message
            if (this.getOwnerUUID().isPresent()) {
                Player owner = this.level().getPlayerByUUID(this.getOwnerUUID().get());
                if (owner != null) {
                    owner.sendSystemMessage(net.minecraft.network.chat.Component
                            .translatable("message.rpgem.fairy_sprout_fail", this.getName().getString()));
                }
            }
        }
        this.refreshDimensions();
        this.heal(this.getMaxHealth());
    }

    // Cooldowns
    private int cheerCooldown = 0;
    private int careCooldown = 0; // Ticks
    private int tearCooldown = 0; // Ticks
    private int cureCooldown = 0; // Ticks

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {
            this.tickSkills();
            if (this.tearCooldown > 0) {
                this.tearCooldown--;
            }
        }
    }

    private void tickSkills() {
        // Miraculous Cheer (Low HP -> Heal)
        if (this.cheerCooldown > 0) {
            this.cheerCooldown--;
        } else {
            this.performMiraculousCheer();
        }

        // Continuous Care (Hungry -> Eat / Burning -> Fire Res)
        if (this.careCooldown > 0) {
            this.careCooldown--;
        } else {
            this.performContinuousCare();
        }

        // Inexhaustible Well (Debuff Cure)
        if (this.cureCooldown > 0) {
            this.cureCooldown--;
        } else {
            this.performInexhaustibleWell();
        }

        // Tingling Breath (Underwater -> Water Breathing)
        this.performTinglingBreath();

        // Morning Star (Dark -> Night Vision)
        this.performMorningStar();
    }

    private void performMorningStar() {
        if (this.getOwnerUUID().isPresent()) {
            Player owner = this.level().getPlayerByUUID(this.getOwnerUUID().get());
            if (owner != null && owner.isAlive()) {
                // Logic: If light level < 7 or Night, apply Night Vision
                // Simplified: Just always apply if not present, to act as a permanent light
                // source feel.
                // Maybe check if it's actually dark to be more "smart".
                // Let's check light level at player pos.
                int lightLevel = this.level().getBrightness(net.minecraft.world.level.LightLayer.BLOCK,
                        owner.blockPosition());
                if (lightLevel < 8 && !owner.hasEffect(net.minecraft.world.effect.MobEffects.NIGHT_VISION)) {
                    owner.addEffect(new net.minecraft.world.effect.MobEffectInstance(
                            net.minecraft.world.effect.MobEffects.NIGHT_VISION, 300, 0, false, false, true));
                }
            }
        }
    }

    private void performInexhaustibleWell() {
        if (this.getOwnerUUID().isPresent()) {
            Player owner = this.level().getPlayerByUUID(this.getOwnerUUID().get());
            if (owner != null && owner.isAlive()) {
                // Scan for harmful effects
                java.util.List<net.minecraft.world.effect.MobEffect> harmfulEffects = new java.util.ArrayList<>();

                for (net.minecraft.world.effect.MobEffectInstance effectInstance : owner.getActiveEffects()) {
                    if (!effectInstance.getEffect().value().isBeneficial()) {
                        harmfulEffects.add(effectInstance.getEffect().value());
                    }
                }

                if (!harmfulEffects.isEmpty()) {
                    // Cure one Random harmful effect
                    net.minecraft.world.effect.MobEffect toRemove = harmfulEffects
                            .get(this.random.nextInt(harmfulEffects.size()));
                    owner.removeEffect(net.minecraft.core.Holder.direct(toRemove));

                    this.playSound(net.minecraft.sounds.SoundEvents.AMETHYST_BLOCK_CHIME, 1.0F, 1.0F);
                    owner.sendSystemMessage(net.minecraft.network.chat.Component
                            .translatable("message.rpgem.fairy_cure_used", this.getName().getString()));

                    // Cooldown: Tier 1=30s, Tier 4=5s
                    int cooldownSeconds = 35 - (this.getTier() * 5) - (this.getTier() * 2); // T1=28s, T2=21s, T3=14s,
                                                                                            // T4=7s roughly
                    // Let's match plan: 30s -> 5s
                    // T1: 30s, T2: 20s, T3: 10s, T4: 5s
                    if (this.getTier() == 1)
                        this.cureCooldown = 600;
                    else if (this.getTier() == 2)
                        this.cureCooldown = 400;
                    else if (this.getTier() == 3)
                        this.cureCooldown = 200;
                    else
                        this.cureCooldown = 100;
                }
            }
        }
    }

    private void performTinglingBreath() {
        if (this.getOwnerUUID().isPresent()) {
            Player owner = this.level().getPlayerByUUID(this.getOwnerUUID().get());
            if (owner != null && owner.isAlive()) {
                if (owner.isUnderWater() && !owner.hasEffect(net.minecraft.world.effect.MobEffects.WATER_BREATHING)) {
                    // Logic: Apply 10s Water Breathing constantly while underwater
                    owner.addEffect(new net.minecraft.world.effect.MobEffectInstance(
                            net.minecraft.world.effect.MobEffects.WATER_BREATHING, 200, 0, false, false, true));
                }
            }
        }
    }

    public boolean tryUseFairyTear(Player player) {
        if (this.tearCooldown > 0)
            return false;

        // Effect
        player.setHealth(player.getMaxHealth() * 0.5f);
        player.removeAllEffects();
        player.addEffect(new net.minecraft.world.effect.MobEffectInstance(
                net.minecraft.world.effect.MobEffects.REGENERATION, 400, 1)); // Regen II 20s
        player.addEffect(new net.minecraft.world.effect.MobEffectInstance(
                net.minecraft.world.effect.MobEffects.FIRE_RESISTANCE, 400, 0)); // Fire Res 20s
        player.addEffect(new net.minecraft.world.effect.MobEffectInstance(
                net.minecraft.world.effect.MobEffects.ABSORPTION, 400, 4)); // Absorption IV 20s

        // Particles & Sound
        if (player.level() instanceof net.minecraft.server.level.ServerLevel serverLevel) {
            serverLevel.sendParticles(net.minecraft.core.particles.ParticleTypes.TOTEM_OF_UNDYING,
                    player.getX(), player.getY() + 1.0, player.getZ(),
                    60, 0.5, 0.5, 0.5, 0.5);
            serverLevel.playSound(null, player.blockPosition(), net.minecraft.sounds.SoundEvents.TOTEM_USE,
                    net.minecraft.sounds.SoundSource.PLAYERS, 1.0f, 1.0f);
        }

        player.sendSystemMessage(net.minecraft.network.chat.Component.translatable("message.rpgem.fairy_tear_used",
                this.getName().getString()));

        // Cooldown Calculation (Minutes)
        int cooldownMinutes = 75 - (this.getTier() * 15);
        this.tearCooldown = cooldownMinutes * 60 * 20; // Convert to ticks

        return true;
    }

    private void performContinuousCare() {
        if (this.getOwnerUUID().isPresent()) {
            Player owner = this.level().getPlayerByUUID(this.getOwnerUUID().get());
            if (owner != null && owner.isAlive()) {

                // 1. Smart Buff: Fire Resistance if Burning
                if (owner.isOnFire()) {
                    for (int i = 0; i < owner.getInventory().getContainerSize(); i++) {
                        ItemStack stack = owner.getInventory().getItem(i);
                        if (stack.getItem() instanceof net.minecraft.world.item.PotionItem) {
                            net.minecraft.world.item.alchemy.PotionContents contents = stack
                                    .get(net.minecraft.core.component.DataComponents.POTION_CONTENTS);
                            if (contents != null && contents.potion().isPresent()) {
                                if (contents.potion().get().is(net.minecraft.world.item.alchemy.Potions.FIRE_RESISTANCE)
                                        ||
                                        contents.potion().get()
                                                .is(net.minecraft.world.item.alchemy.Potions.LONG_FIRE_RESISTANCE)) {

                                    for (net.minecraft.world.effect.MobEffectInstance effect : contents.potion().get()
                                            .value().getEffects()) {
                                        owner.addEffect(new net.minecraft.world.effect.MobEffectInstance(effect));
                                    }
                                    stack.shrink(1);
                                    this.playSound(net.minecraft.sounds.SoundEvents.GENERIC_DRINK, 1.0F, 1.0F);
                                    owner.sendSystemMessage(net.minecraft.network.chat.Component.translatable(
                                            "message.rpgem.fairy_care_buff", this.getName().getString(),
                                            "Fire Resistance"));
                                    this.careCooldown = 100; // 5s cooldown
                                    return;
                                }
                            }
                        }
                    }
                }

                // 2. Auto-Eat: If Food < 18
                // 2. Auto-Eat: If Food < 18
                if (owner.getFoodData().getFoodLevel() < 18) {
                    for (int i = 0; i < owner.getInventory().getContainerSize(); i++) {
                        ItemStack stack = owner.getInventory().getItem(i);
                        if (stack.has(net.minecraft.core.component.DataComponents.FOOD)) {
                            // Eat item
                            net.minecraft.world.food.FoodProperties food = stack
                                    .get(net.minecraft.core.component.DataComponents.FOOD);
                            if (food != null) {
                                owner.getFoodData().eat(food.nutrition(), food.saturation());
                                stack.shrink(1);
                                this.playSound(net.minecraft.sounds.SoundEvents.GENERIC_EAT, 1.0F, 1.0F);
                                this.careCooldown = 60; // 3s delay
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    private void performMiraculousCheer() {
        if (this.getOwnerUUID().isPresent()) {
            Player owner = this.level().getPlayerByUUID(this.getOwnerUUID().get());
            if (owner != null && owner.isAlive()) {
                // Check HP < 50%
                if (owner.getHealth() < owner.getMaxHealth() * 0.5f) {
                    // Scan Inventory
                    for (int i = 0; i < owner.getInventory().getContainerSize(); i++) {
                        ItemStack stack = owner.getInventory().getItem(i);
                        if (stack.getItem() instanceof net.minecraft.world.item.PotionItem) {
                            net.minecraft.world.item.alchemy.PotionContents contents = stack
                                    .get(net.minecraft.core.component.DataComponents.POTION_CONTENTS);
                            if (contents != null && contents.potion().isPresent()) {
                                if (contents.potion().get().is(net.minecraft.world.item.alchemy.Potions.HEALING) ||
                                        contents.potion().get()
                                                .is(net.minecraft.world.item.alchemy.Potions.STRONG_HEALING)) {

                                    // Use Potion
                                    for (net.minecraft.world.effect.MobEffectInstance effect : contents.potion().get()
                                            .value().getEffects()) {
                                        owner.addEffect(new net.minecraft.world.effect.MobEffectInstance(effect));
                                    }
                                    stack.shrink(1);
                                    this.playSound(net.minecraft.sounds.SoundEvents.GENERIC_DRINK, 1.0F, 1.0F);
                                    owner.sendSystemMessage(net.minecraft.network.chat.Component.translatable(
                                            "message.rpgem.fairy_cheer_used", this.getName().getString()));

                                    // Set Cooldown
                                    this.cheerCooldown = 140 - (this.getTier() * 20);
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);

        // 1. Recall Logic (Handled by Item, but just in case we need entity-side logic
        // later)
        if (itemStack.getItem() instanceof FairyWingItem) {
            // Let the item handle the interaction
            return super.mobInteract(player, hand);
        }

        // 2. Feeding Logic
        int expGain = getExpFromItem(itemStack);
        if (expGain > 0) {
            if (!this.level().isClientSide) {
                if (this.getLevel() < this.getMaxLevelForTier()) {
                    itemStack.shrink(1);
                    this.addExp(expGain);
                    this.playSound(net.minecraft.sounds.SoundEvents.GENERIC_EAT, 1.0F, 1.0F);
                    return InteractionResult.SUCCESS;
                } else if (this.canSprout()) {
                    // Trigger Sprouting if max level and fed valid item
                    itemStack.shrink(1);
                    this.sprout();
                    return InteractionResult.SUCCESS;
                } else {
                    // Send message: "Fairy is Max Tier!"
                    if (this.getOwnerUUID().isPresent()) {
                        Player owner = this.level().getPlayerByUUID(this.getOwnerUUID().get());
                        if (owner != null) {
                            owner.sendSystemMessage(
                                    net.minecraft.network.chat.Component.translatable("message.rpgem.fairy_max_tier"));
                        }
                    }
                    return InteractionResult.FAIL;
                }
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }

        return super.mobInteract(player, hand);
    }

    public boolean feed(ItemStack stack) {
        int expGain = getExpFromItem(stack);
        if (expGain > 0) {
            if (this.getLevel() < this.getMaxLevelForTier()) {
                stack.shrink(1);
                this.addExp(expGain);
                this.playSound(net.minecraft.sounds.SoundEvents.GENERIC_EAT, 1.0F, 1.0F);
                return true;
            }
        }
        return false;
    }

    public static int getExpFromItem(ItemStack stack) {
        if (stack.is(Items.IRON_NUGGET))
            return 10;
        if (stack.is(Items.IRON_INGOT))
            return 90;
        if (stack.is(Items.GOLD_NUGGET))
            return 30;
        if (stack.is(Items.GOLD_INGOT))
            return 270;
        if (stack.is(Items.DIAMOND))
            return 500;
        if (stack.is(Items.EMERALD))
            return 400;
        if (stack.is(Items.NETHERITE_SCRAP))
            return 1000;
        if (stack.is(Items.NETHERITE_INGOT))
            return 4000;
        // Apotheosis Gems? (TODO)
        return 0;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.FLYING_SPEED, 0.6D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        // Note: FollowPlayerGoal to be implemented in Phase 4.2
        this.goalSelector.addGoal(1, new WaterAvoidingRandomFlyingGoal(this, 1.0D));
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 6.0F));
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 5, this::predicate));
    }

    private software.bernie.geckolib.animation.PlayState predicate(AnimationState<FairyEntity> state) {
        if (state.isMoving()) {
            state.getController().setAnimation(RawAnimation.begin().thenLoop("animation.fairy.fly"));
            return software.bernie.geckolib.animation.PlayState.CONTINUE;
        }

        state.getController().setAnimation(RawAnimation.begin().thenLoop("animation.fairy.idle"));
        return software.bernie.geckolib.animation.PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    public enum Skill {
        CHEER,
        CARE,
        FEATHERY_STEPS,
        TEAR,
        BREATH,
        WELL,
        MORNING_STAR
    }

    public boolean hasSkill(Skill skill) {
        // TODO: Implement actual learning logic (NBT/Level-based)
        // For now, return true to allow testing GUI buttons
        return true;
    }
}
