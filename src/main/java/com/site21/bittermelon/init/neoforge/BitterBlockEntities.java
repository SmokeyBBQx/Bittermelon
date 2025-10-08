package com.site21.bittermelon.init.neoforge;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.blocks.base.structuralblock.StructuralBlockEntity;
import com.site21.bittermelon.content.blocks.container.smallbox.BoxBlockEntity;
import com.site21.bittermelon.content.blocks.devices.implementations.containmentalarm.ContainmentAlarmBlockEntity;
import com.site21.bittermelon.content.blocks.devices.implementations.containmentpanel.ContainmentPanelBlockEntity;
import com.site21.bittermelon.content.blocks.devices.implementations.detonator.DetonatorBlockEntity;
import com.site21.bittermelon.content.blocks.devices.implementations.environmentsensor.EnvironmentSensorBlockEntity;
import com.site21.bittermelon.content.blocks.devices.implementations.intercom.IntercomBlockEntity;
import com.site21.bittermelon.content.blocks.devices.implementations.personnelterminal.PersonnelTerminalBlockEntity;
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
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.site21.bittermelon.init.neoforge.BitterBlocks.*;

public class BitterBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Bittermelon.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<FluidBlockEntity>> FLUID_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("fluid_block_entity",
            () -> BlockEntityType.Builder.of(FluidBlockEntity::new, FLUID.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BoxBlockEntity>> BOX_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("box_block_entity",
            () -> BlockEntityType.Builder.of(BoxBlockEntity::new, SMALL_CARDBOARD_BOX.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<StructuralBlockEntity>> STRUCTURAL_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("structural_block_entity",
            () -> BlockEntityType.Builder.of(StructuralBlockEntity::new, STRUCTURAL_BLOCK.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ContainmentPanelBlockEntity>> CONTAINMENT_PANEL_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("containment_panel_block_entity",
            () -> BlockEntityType.Builder.of(ContainmentPanelBlockEntity::new, CONTAINMENT_PANEL.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ThermometerBlockEntity>> THERMOMETER_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("thermometer_block_entity",
            () -> BlockEntityType.Builder.of(ThermometerBlockEntity::new, THERMOMETER.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<IntercomBlockEntity>> INTERCOM_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("intercom_block_entity",
            () -> BlockEntityType.Builder.of(IntercomBlockEntity::new, INTERCOM.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<EnvironmentSensorBlockEntity>> ENVIRONMENT_SENSOR_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("environment_sensor_block_entity",
            () -> BlockEntityType.Builder.of(EnvironmentSensorBlockEntity::new, ENVIRONMENT_SENSOR.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ContainmentAlarmBlockEntity>> CONTAINMENT_ALARM_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("containment_alarm_block_entity",
            () -> BlockEntityType.Builder.of(ContainmentAlarmBlockEntity::new, CONTAINMENT_ALARM.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<DetonatorBlockEntity>> DETONATOR_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("detonator_block_entity",
            () -> BlockEntityType.Builder.of(DetonatorBlockEntity::new, DETONATOR.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SpeakerBlockEntity>> SPEAKER_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("speaker_block_entity",
            () -> BlockEntityType.Builder.of(SpeakerBlockEntity::new, SPEAKER.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SecureDoorBlockEntity>> SECURE_DOOR_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("secure_door_block_entity",
            () -> BlockEntityType.Builder.of(SecureDoorBlockEntity::new, SECURE_DOOR.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<KeycardReaderSecureDoorBlockEntity>> KEYCARD_READER_SECURE_DOOR_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("keycard_reader_secure_door_block_entity",
            () -> BlockEntityType.Builder.of(KeycardReaderSecureDoorBlockEntity::new, KEYCARD_READER_SECURE_DOOR.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<LargeSlidingDoorBlockEntity>> LARGE_SLIDING_DOOR_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("large_sliding_door_block_entity",
            () -> BlockEntityType.Builder.of(LargeSlidingDoorBlockEntity::new, LARGE_SLIDING_DOOR.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<DistributionBoardBlockEntity>> DISTRIBUTION_BOARD_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("distribution_board_block_entity",
            () -> BlockEntityType.Builder.of(DistributionBoardBlockEntity::new, DISTRIBUTION_BOARD.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SCP151BlockEntity>> SCP151_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("scp151_block_entity",
            () -> BlockEntityType.Builder.of(SCP151BlockEntity::new, SCP151.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<PersonnelTerminalBlockEntity>> PERSONNEL_TERMINAL_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("personnel_terminal_block_entity",
            () -> BlockEntityType.Builder.of(PersonnelTerminalBlockEntity::new, PERSONNEL_TERMINAL.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SlidingDoorBlockEntity>> SLIDING_DOOR_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("sliding_door_block_entity",
            () -> BlockEntityType.Builder.of(SlidingDoorBlockEntity::new, SLIDING_DOOR.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<WallWritingBlockEntity>> WALL_WRITING_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("wall_writing_block_entity",
            () -> BlockEntityType.Builder.of(WallWritingBlockEntity::new, WALL_WRITING.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<StickyNoteBlockEntity>> STICKY_NOTE_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("sticky_note_block_entity",
            () -> BlockEntityType.Builder.of(StickyNoteBlockEntity::new, STICKY_NOTE.get()).build(null));
}
