package net.kankrittapon.rpgem.client;

import net.kankrittapon.rpgem.RPGEasyMode;
import net.kankrittapon.rpgem.init.ModItems;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.CustomData;

import java.util.ArrayList;
import java.util.List;

public class ModItemProperties {
    public static void addCustomItemProperties() {
        makeInfinitePotion(ModItems.INFINITE_POTION_TIER_1.get());
        makeInfinitePotion(ModItems.INFINITE_POTION_TIER_2.get());
        makeInfinitePotion(ModItems.INFINITE_POTION_TIER_3.get());
    }

    private static void makeInfinitePotion(Item item) {
        ItemProperties.register(item, ResourceLocation.fromNamespaceAndPath(RPGEasyMode.MODID, "variant"),
                (stack, level, entity, seed) -> {
                    if (!stack.has(DataComponents.CUSTOM_DATA)) {
                        return 0.0f;
                    }

                    CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
                    CompoundTag tag = customData.copyTag();

                    if (!tag.contains("IngredientHistory")) {
                        return 0.0f;
                    }

                    ListTag list = tag.getList("IngredientHistory", 8); // 8 = String
                    List<String> history = new ArrayList<>();
                    for (Tag t : list) {
                        history.add(t.getAsString());
                    }

                    if (history.isEmpty())
                        return 0.0f;

                    // Tier 1 Logic
                    if (history.size() == 1) {
                        String h1 = history.get(0);
                        if (h1.equals("H"))
                            return 0.1f; // Heart (Red)
                        if (h1.equals("B"))
                            return 0.2f; // Bone (White)
                        if (h1.equals("C"))
                            return 0.3f; // Cosmic (Green)
                    }

                    // Tier 2 Logic
                    if (history.size() == 2) {
                        String h1 = history.get(0);
                        String h2 = history.get(1);

                        // Check for combinations (Order independent for color grouping, or specific if
                        // needed)
                        // User requested: HB (Pink), HC (Yellow), BC (Cyan)
                        // Mapping both HB and BH to same color for now unless strictly separate

                        boolean hasH = h1.equals("H") || h2.equals("H");
                        boolean hasB = h1.equals("B") || h2.equals("B");
                        boolean hasC = h1.equals("C") || h2.equals("C");

                        if (hasH && hasB)
                            return 0.4f; // HB/BH (Pink)
                        if (hasH && hasC)
                            return 0.5f; // HC/CH (Yellow)
                        if (hasB && hasC)
                            return 0.6f; // BC/CB (Cyan)
                    }

                    // Tier 3 is handled by default texture usually, but if we need a variant:
                    if (history.size() >= 3) {
                        return 1.0f; // Rainbow
                    }

                    return 0.0f;
                });
    }
}
