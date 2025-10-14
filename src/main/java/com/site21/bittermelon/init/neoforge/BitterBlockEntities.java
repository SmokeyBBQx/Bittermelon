package com.site21.bittermelon.init.neoforge;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.blocks.base.structuralblock.StructuralBlockEntity;
import com.site21.bittermelon.content.blocks.container.smallbox.BoxBlockEntity;
import com.site21.bittermelon.content.blocks.devices.implementations.containmentalarm.ContainmentAlarmBlockEntity;
import com.site21.bittermelon.content.blocks.devices.implementations.containmentpanel.ContainmentPanelBlockEntity;
import com.site21.bittermelon.content.blocks.devices.implementations.detonator.DetonatorBlockEntity;
import com.site21.bittermelon.content.blocks.devices.implementations.environmentsensor.EnvironmentSensorBlockEntity;
import com.site21.bittermelon.content.blocks.devices.implementations.intercom.IntercomBlockEntity;
import com.site21.bittermelon.content.blocks.devices.implementations.keycardreader.KeycardReaderBlockEntity;
import com.site21.bittermelon.content.blocks.devices.implementations.personnelterminal.PersonnelTerminalBlockEntity;
import com.site21.bittermelon.content.blocks.devices.implementations.redstonedevice.RedstoneDeviceBlockEntity;
import com.site21.bittermelon.content.blocks.devices.implementations.securedoor.KeycardReaderSecureDoorBlockEntity;
import com.site21.bittermelon.content.blocks.devices.implementations.securedoor.SecureDoorBlockEntity;
import com.site21.bittermelon.content.blocks.devices.implementations.largeslidingdoor.LargeSlidingDoorBlockEntity;
import com.site21.bittermelon.content.blocks.devices.implementations.slidingdoor.SlidingDoorBlockEntity;
import com.site21.bittermelon.content.blocks.devices.implementations.speaker.SpeakerBlockEntity;
import com.site21.bittermelon.content.blocks.devices.implementations.thermometer.ThermometerBlockEntity;
import com.site21.bittermelon.content.blocks.powergrid.distributionboard.DistributionBoardBlockEntity;
import com.site21.bittermelon.content.blocks.scp.scp151.SCP151BlockEntity;
import com.site21.bittermelon.content.blocks.stickynote.StickyNoteBlockEntity;
import com.site21.bittermelon.content.blocks.substance.fluid.FluidBlockEntity;
import com.site21.bittermelon.content.blocks.wallwriting.WallWritingBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.init.neoforge.BitterBlocks.*;

public class BitterBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Bittermelon.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<FluidBlockEntity>> FLUID_BLOCK_ENTITY =
            register("fluid", FluidBlockEntity::new, FLUID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BoxBlockEntity>> BOX_BLOCK_ENTITY =
            register("box", BoxBlockEntity::new, SMALL_CARDBOARD_BOX);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<StructuralBlockEntity>> STRUCTURAL_BLOCK_ENTITY =
            register("structural", StructuralBlockEntity::new, STRUCTURAL_BLOCK);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ContainmentPanelBlockEntity>> CONTAINMENT_PANEL_BLOCK_ENTITY =
            register("containment_panel", ContainmentPanelBlockEntity::new, CONTAINMENT_PANEL);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<PersonnelTerminalBlockEntity>> PERSONNEL_TERMINAL_BLOCK_ENTITY =
            register("personnel_terminal", PersonnelTerminalBlockEntity::new, PERSONNEL_TERMINAL);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<KeycardReaderBlockEntity>> KEYCARD_READER_BLOCK_ENTITY =
            register("keycard_reader", KeycardReaderBlockEntity::new, KEYCARD_READER);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ThermometerBlockEntity>> THERMOMETER_BLOCK_ENTITY =
            register("thermometer", ThermometerBlockEntity::new, THERMOMETER);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<EnvironmentSensorBlockEntity>> ENVIRONMENT_SENSOR_BLOCK_ENTITY =
            register("environment_sensor", EnvironmentSensorBlockEntity::new, ENVIRONMENT_SENSOR);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<IntercomBlockEntity>> INTERCOM_BLOCK_ENTITY =
            register("intercom", IntercomBlockEntity::new, INTERCOM);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SpeakerBlockEntity>> SPEAKER_BLOCK_ENTITY =
            register("speaker", SpeakerBlockEntity::new, SPEAKER);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ContainmentAlarmBlockEntity>> CONTAINMENT_ALARM_BLOCK_ENTITY =
            register("containment_alarm", ContainmentAlarmBlockEntity::new, CONTAINMENT_ALARM);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<DetonatorBlockEntity>> DETONATOR_BLOCK_ENTITY =
            register("detonator", DetonatorBlockEntity::new, DETONATOR);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SecureDoorBlockEntity>> SECURE_DOOR_BLOCK_ENTITY =
            register("secure_door", SecureDoorBlockEntity::new, SECURE_DOOR);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<KeycardReaderSecureDoorBlockEntity>> KEYCARD_READER_SECURE_DOOR_BLOCK_ENTITY =
            register("keycard_reader_secure_door", KeycardReaderSecureDoorBlockEntity::new, KEYCARD_READER_SECURE_DOOR);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SlidingDoorBlockEntity>> SLIDING_DOOR_BLOCK_ENTITY =
            register("sliding_door", SlidingDoorBlockEntity::new, SLIDING_DOOR);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<LargeSlidingDoorBlockEntity>> LARGE_SLIDING_DOOR_BLOCK_ENTITY =
            register("large_sliding_door", LargeSlidingDoorBlockEntity::new, LARGE_SLIDING_DOOR);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<DistributionBoardBlockEntity>> DISTRIBUTION_BOARD_BLOCK_ENTITY =
            register("distribution_board", DistributionBoardBlockEntity::new, DISTRIBUTION_BOARD);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<RedstoneDeviceBlockEntity>> REDSTONE_DEVICE_BLOCK_ENTITY =
            register("redstone_device", RedstoneDeviceBlockEntity::new, REDSTONE_DEVICE);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SCP151BlockEntity>> SCP151_BLOCK_ENTITY =
            register("scp151", SCP151BlockEntity::new, SCP_151);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<WallWritingBlockEntity>> WALL_WRITING_BLOCK_ENTITY =
            register("wall_writing", WallWritingBlockEntity::new, WALL_WRITING);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<StickyNoteBlockEntity>> STICKY_NOTE_BLOCK_ENTITY =
            register("sticky_note", StickyNoteBlockEntity::new, STICKY_NOTE);

    private static <T extends BlockEntity> @NotNull DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> register(
            String name,
            BlockEntityType.BlockEntitySupplier<T> supplier,
            DeferredHolder<?, ? extends Block> block) {
        return BLOCK_ENTITY_TYPES.register(name + "_block_entity",
                () -> BlockEntityType.Builder.of(supplier, block.get()).build(null));
    }
}
