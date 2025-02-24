package com.site21.bittermelon.content.blocks.devices.connection;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.blocks.devices.IElectronic;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

import static com.site21.bittermelon.init.neoforge.BitterRegistries.CONDITION_REGISTRY;

public class Connection<T, U> {
    private final OutputPort<T> outputPort;
    private final InputPort<U> inputPort;
    private final Function<T, U> condition;
    private final String conditionID;

    public Connection(OutputPort<T> outputPort, InputPort<U> inputPort,
                      Function<T, U> condition, String conditionID) {
        this.outputPort = outputPort;
        this.inputPort = inputPort;
        this.condition = condition;
        this.conditionID = conditionID;
    }

    public void update() {
        T value = outputPort.output().get();
        inputPort.action().accept(condition.apply(value));
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putLong("outputPos", outputPort.pos().asLong());
        tag.putLong("inputPos", inputPort.pos().asLong());

        tag.putString("outputID", outputPort.id());
        tag.putString("inputID", inputPort.id());

        tag.putString("conditionID", conditionID);

        return tag;
    }

    public static @NotNull Connection<?, ?> load(@NotNull CompoundTag tag, Level level) {
        return tryCreateConnection(
                tag.getString("outputID"),
                tag.getString("inputID"),
                BlockPos.of(tag.getLong("outputPos")),
                BlockPos.of(tag.getLong("inputPos")),
                tag.getString("conditionID"),
                level
        );
    }

    @Contract("_, _, _, _, _, _ -> new")
    @SuppressWarnings("unchecked")
    public static @NotNull Connection<?, ?> tryCreateConnection(String outputID, String inputID, BlockPos outputPos, BlockPos inputPos, String conditionID, @NotNull Level level) {
        OutputPort<?> outputPort = null;
        InputPort<?> inputPort = null;

        if (level.getBlockEntity(outputPos) instanceof IElectronic outputDevice) {
            outputPort = outputDevice.findOutputPort(outputID);
        }
        if (level.getBlockEntity(inputPos) instanceof IElectronic inputDevice) {
            inputPort = inputDevice.findInputPort(inputID);
        }

        if (outputPort == null || inputPort == null) {
            throw new IllegalArgumentException("Could not find ports");
        }

        Function<?, ?> condition = CONDITION_REGISTRY
                .getOptional(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, conditionID))
                .orElseThrow(() -> new IllegalArgumentException("Could not find condition: " + conditionID));

        return new Connection(outputPort, inputPort, condition, conditionID);
    }
}
