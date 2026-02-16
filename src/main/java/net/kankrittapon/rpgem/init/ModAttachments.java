package net.kankrittapon.rpgem.init;

import net.kankrittapon.rpgem.RPGEasyMode;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.bus.api.IEventBus;
import java.util.function.Supplier;
import com.mojang.serialization.Codec;

public class ModAttachments {
        public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister
                        .create(NeoForgeRegistries.ATTACHMENT_TYPES, RPGEasyMode.MODID);

        // Fail Stack: Integer, Default 0
        public static final Supplier<AttachmentType<Integer>> FAIL_STACK = ATTACHMENT_TYPES.register("fail_stack",
                        () -> AttachmentType.builder(() -> 0).serialize(Codec.INT).build());

        // Quest: Has Eternal Bottle Quest Started?
        public static final Supplier<AttachmentType<Boolean>> HAS_POTION_QUEST = ATTACHMENT_TYPES.register(
                        "has_potion_quest",
                        () -> AttachmentType.builder(() -> false).serialize(Codec.BOOL).build());

        // Weight Data: Current/Max Weight
        public static final Supplier<AttachmentType<net.kankrittapon.rpgem.attachment.WeightData>> WEIGHT_DATA = ATTACHMENT_TYPES
                        .register(
                                        "weight_data",
                                        () -> AttachmentType.builder(
                                                        () -> new net.kankrittapon.rpgem.attachment.WeightData())
                                                        .serialize(new net.neoforged.neoforge.attachment.IAttachmentSerializer<net.minecraft.nbt.CompoundTag, net.kankrittapon.rpgem.attachment.WeightData>() {
                                                                @Override
                                                                public net.kankrittapon.rpgem.attachment.WeightData read(
                                                                                net.neoforged.neoforge.attachment.IAttachmentHolder holder,
                                                                                net.minecraft.nbt.CompoundTag tag,
                                                                                net.minecraft.core.HolderLookup.Provider provider) {
                                                                        net.kankrittapon.rpgem.attachment.WeightData data = new net.kankrittapon.rpgem.attachment.WeightData();
                                                                        data.deserializeNBT(provider, tag);
                                                                        return data;
                                                                }

                                                                @Override
                                                                public net.minecraft.nbt.CompoundTag write(
                                                                                net.kankrittapon.rpgem.attachment.WeightData attachment,
                                                                                net.minecraft.core.HolderLookup.Provider provider) {
                                                                        return attachment.serializeNBT(provider);
                                                                }
                                                        })
                                                        .build());

        public static void register(IEventBus eventBus) {
                ATTACHMENT_TYPES.register(eventBus);
        }
}
