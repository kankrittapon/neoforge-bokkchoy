package net.kankrittapon.rpgem.init;

import net.kankrittapon.rpgem.RPGEasyMode;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Custom Attributes for Trait Counter System.
 * Weapon: Life Steal, Crit Chance, Element Damage
 * Armor: Damage Reduction, Evasion, Reflect Resist, Seal Resist
 */
public class ModAttributes {
        public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(Registries.ATTRIBUTE,
                        RPGEasyMode.MODID);

        // === Weapon Attributes ===

        /** Life Steal: ดูดเลือด % ต่อ Hit (0.0 ~ 1.0, แสดงเป็น 0~100%) */
        public static final Holder<Attribute> LIFE_STEAL = ATTRIBUTES.register("life_steal",
                        () -> new RangedAttribute("attribute." + RPGEasyMode.MODID + ".life_steal",
                                        0.0, 0.0, 1.0)
                                        .setSyncable(true));

        /** Crit Chance: โอกาสดาเมจ ×2 (0.0 ~ 1.0) */
        public static final Holder<Attribute> CRIT_CHANCE = ATTRIBUTES.register("crit_chance",
                        () -> new RangedAttribute("attribute." + RPGEasyMode.MODID + ".crit_chance",
                                        0.0, 0.0, 1.0)
                                        .setSyncable(true));

        /** Element Damage: เพิ่ม Magic damage % (0.0 ~ 1.0) */
        public static final Holder<Attribute> ELEMENT_DAMAGE = ATTRIBUTES.register("element_damage",
                        () -> new RangedAttribute("attribute." + RPGEasyMode.MODID + ".element_damage",
                                        0.0, 0.0, 1.0)
                                        .setSyncable(true));

        /** Accuracy: ความแม่นยำเพื่อหักล้าง Evasion (0.0 ~ 1.0) */
        public static final Holder<Attribute> ACCURACY = ATTRIBUTES.register("accuracy",
                        () -> new RangedAttribute("attribute." + RPGEasyMode.MODID + ".accuracy",
                                        0.0, 0.0, 1.0)
                                        .setSyncable(true));

        /**
         * Armor Penetration: ดาเมจ % เลเลือด (0.0 ~ 1.0, e.g., 0.05 = 5% HP per hit)
         */
        public static final Holder<Attribute> ARMOR_PENETRATION = ATTRIBUTES.register("armor_penetration",
                        () -> new RangedAttribute("attribute." + RPGEasyMode.MODID + ".armor_penetration",
                                        0.0, 0.0, 1.0)
                                        .setSyncable(true));

        /** Anti Heal: ลดการฟื้นฟูเลือดของศัตรู (Soul Purge) (0.0 ~ 1.0) */
        public static final Holder<Attribute> ANTI_HEAL = ATTRIBUTES.register("anti_heal",
                        () -> new RangedAttribute("attribute." + RPGEasyMode.MODID + ".anti_heal",
                                        0.0, 0.0, 1.0)
                                        .setSyncable(true));

        // === Armor Attributes ===

        /** Damage Reduction: ลดดาเมจตรง % (0.0 ~ 1.0) */
        public static final Holder<Attribute> DAMAGE_REDUCTION = ATTRIBUTES.register("damage_reduction",
                        () -> new RangedAttribute("attribute." + RPGEasyMode.MODID + ".damage_reduction",
                                        0.0, 0.0, 1.0)
                                        .setSyncable(true));

        /** Evasion: โอกาสหลบโจมตี % (0.0 ~ 1.0) */
        public static final Holder<Attribute> EVASION = ATTRIBUTES.register("evasion",
                        () -> new RangedAttribute("attribute." + RPGEasyMode.MODID + ".evasion",
                                        0.0, 0.0, 1.0)
                                        .setSyncable(true));

        /** Reflect Resist: ลด reflected damage % (0.0 ~ 1.0) */
        public static final Holder<Attribute> REFLECT_RESIST = ATTRIBUTES.register("reflect_resist",
                        () -> new RangedAttribute("attribute." + RPGEasyMode.MODID + ".reflect_resist",
                                        0.0, 0.0, 1.0)
                                        .setSyncable(true));

        /** Seal Resist: โอกาสกัน equipment seal % (0.0 ~ 1.0) */
        public static final Holder<Attribute> SEAL_RESIST = ATTRIBUTES.register("seal_resist",
                        () -> new RangedAttribute("attribute." + RPGEasyMode.MODID + ".seal_resist",
                                        0.0, 0.0, 1.0)
                                        .setSyncable(true));

        public static void register(IEventBus eventBus) {
                ATTRIBUTES.register(eventBus);
        }
}
