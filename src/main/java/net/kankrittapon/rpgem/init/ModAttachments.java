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

    public static void register(IEventBus eventBus) {
        ATTACHMENT_TYPES.register(eventBus);
    }
}
