package net.kankrittapon.rpgem.attachment;

import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.minecraft.core.HolderLookup;

public class WeightData implements INBTSerializable<CompoundTag> {
    private float currentWeight;
    private float maxWeight;

    public WeightData() {
        this.currentWeight = 0.0f;
        this.maxWeight = 100.0f; // Default base weight
    }

    public float getCurrentWeight() {
        return currentWeight;
    }

    public void setCurrentWeight(float currentWeight) {
        this.currentWeight = currentWeight;
    }

    public float getMaxWeight() {
        return maxWeight;
    }

    public void setMaxWeight(float maxWeight) {
        this.maxWeight = maxWeight;
    }

    public void copyFrom(WeightData source) {
        this.currentWeight = source.currentWeight;
        this.maxWeight = source.maxWeight;
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag nbt = new CompoundTag();
        nbt.putFloat("CurrentWeight", currentWeight);
        nbt.putFloat("MaxWeight", maxWeight);
        return nbt;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        currentWeight = nbt.getFloat("CurrentWeight");
        maxWeight = nbt.getFloat("MaxWeight");
    }
}
