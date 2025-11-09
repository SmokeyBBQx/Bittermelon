package com.site21.bittermelon.common.systems.electronics.wiring;

import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public record Instruction(String inputID, String outputID, LogicalOperator logicalOperator) {
    public @NotNull CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putString("connectorID", inputID);
        tag.putString("outputID", outputID);
        tag.put("logicalOperator", logicalOperator.save());
        return tag;
    }

    @Contract("_ -> new")
    public static @NotNull Instruction load(@NotNull CompoundTag tag) {
        return new Instruction(
                tag.getString("connectorID"),
                tag.getString("outputID"),
                LogicalOperator.load(tag.getCompound("logicalOperator"))
        );
    }
}
