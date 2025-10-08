package com.site21.bittermelon.init.neoforge;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.blocks.devices.implementations.containmentalarm.ContainmentAlarm;
import com.site21.bittermelon.content.blocks.devices.implementations.detonator.DetonatorBlock;
import com.site21.bittermelon.content.blocks.devices.implementations.environmentsensor.EnvironmentSensor;
import com.site21.bittermelon.content.blocks.devices.implementations.intercom.IntercomBlock;
import com.site21.bittermelon.content.blocks.devices.implementations.keycardprinter.KeycardPrinter;
import com.site21.bittermelon.content.blocks.devices.implementations.personnelterminal.PersonnelTerminalBlock;
import com.site21.bittermelon.content.blocks.devices.implementations.securedoor.KeycardReaderSecureDoorBlock;
import com.site21.bittermelon.content.blocks.devices.implementations.securedoor.SecureDoorBlock;
import com.site21.bittermelon.content.blocks.devices.implementations.largeslidingdoor.LargeSlidingDoorBlock;
import com.site21.bittermelon.content.blocks.devices.implementations.slidingdoor.SlidingDoorBlock;
import com.site21.bittermelon.content.blocks.devices.implementations.speaker.SpeakerBlock;
import com.site21.bittermelon.content.blocks.dirtyfloor.DirtyFloorBlock;
import com.site21.bittermelon.content.blocks.base.structuralblock.StructuralBlock;
import com.site21.bittermelon.content.blocks.container.smallbox.SmallBox;
import com.site21.bittermelon.content.blocks.devices.implementations.ATMBlock;
import com.site21.bittermelon.content.blocks.devices.implementations.containmentpanel.ContainmentPanelBlock;
import com.site21.bittermelon.content.blocks.devices.implementations.thermometer.ThermometerBlock;
import com.site21.bittermelon.content.blocks.poster.SmallPosterBlock;
import com.site21.bittermelon.content.blocks.powergrid.distributionboard.DistributionBoardBlock;
import com.site21.bittermelon.content.blocks.scp.scp151.SCP151Block;
import com.site21.bittermelon.content.blocks.stickynote.StickyNoteBlock;
import com.site21.bittermelon.content.blocks.substance.fluid.FluidBlock;
import com.site21.bittermelon.content.blocks.wallwriting.WallWritingBlock;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BitterBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Bittermelon.MOD_ID);

    public static final DeferredBlock<FluidBlock> FLUID = BLOCKS.register("fluid", () -> new FluidBlock(BlockBehaviour.Properties.of()
            .mapColor(MapColor.WATER)
            .noOcclusion()
            .destroyTime(-1)
            .sound(
                    new SoundType(
                         1.0f,
                            1.0f,
                            BitterSounds.SPLATTER.get(),
                            BitterSounds.SOGGY.get(),
                            BitterSounds.SPLAT.get(),
                            BitterSounds.SOGGY.get(),
                            BitterSounds.SPLATTER.get()
                    )
            )
            .pushReaction(PushReaction.NORMAL)
    ));

    public static final DeferredBlock<SmallBox> SMALL_CARDBOARD_BOX = BLOCKS.register("small_cardboard_box", () -> new SmallBox(BlockBehaviour.Properties.of()
            .destroyTime(-1)
            .sound(SoundType.WOOL)
    ));

    public static final DeferredBlock<StructuralBlock> STRUCTURAL_BLOCK = BLOCKS.register("structural_block", () -> new StructuralBlock(BlockBehaviour.Properties.of()
            .destroyTime(1.5f)
    ));

    public static final DeferredBlock<ATMBlock> ATM = BLOCKS.register("atm", () -> new ATMBlock(BlockBehaviour.Properties.of()
            .noOcclusion()
    ));

    public static final DeferredBlock<ContainmentPanelBlock> CONTAINMENT_PANEL = BLOCKS.register("containment_panel",
            () -> new ContainmentPanelBlock(BlockBehaviour.Properties.of().noOcclusion()
    ));

    public static final DeferredBlock<DirtyFloorBlock> DIRTY_FLOOR = BLOCKS.register("dirty_floor",
            () -> new DirtyFloorBlock(BlockBehaviour.Properties.of()
                    .noOcclusion()
                    .noCollission()
                    .destroyTime(-1)
                    .replaceable()
            ));

    public static final DeferredBlock<ThermometerBlock> THERMOMETER = BLOCKS.register("thermometer",
            () -> new ThermometerBlock(BlockBehaviour.Properties.of().noOcclusion()
            ));

    public static final DeferredBlock<IntercomBlock> INTERCOM = BLOCKS.register("intercom",
            () -> new IntercomBlock(BlockBehaviour.Properties.of().noOcclusion()
            ));

    public static final DeferredBlock<EnvironmentSensor> ENVIRONMENT_SENSOR = BLOCKS.register("environment_sensor",
            () -> new EnvironmentSensor(BlockBehaviour.Properties.of().noOcclusion()
            ));

    public static final DeferredBlock<ContainmentAlarm> CONTAINMENT_ALARM = BLOCKS.register("containment_alarm",
            () -> new ContainmentAlarm(BlockBehaviour.Properties.of().noOcclusion()
            ));

    public static final DeferredBlock<DetonatorBlock> DETONATOR = BLOCKS.register("detonator",
            () -> new DetonatorBlock(BlockBehaviour.Properties.of().noOcclusion()
            ));

    public static final DeferredBlock<SpeakerBlock> SPEAKER = BLOCKS.register("speaker",
            () -> new SpeakerBlock(BlockBehaviour.Properties.of().noOcclusion()
            ));

    public static final DeferredBlock<SecureDoorBlock> SECURE_DOOR = BLOCKS.register("secure_door",
            () -> new SecureDoorBlock(BlockSetType.IRON, BlockBehaviour.Properties.of().noOcclusion()));

    public static final DeferredBlock<KeycardReaderSecureDoorBlock> KEYCARD_READER_SECURE_DOOR = BLOCKS.register("keycard_reader_secure_door",
            () -> new KeycardReaderSecureDoorBlock(BlockSetType.IRON, BlockBehaviour.Properties.of().noOcclusion()));

    public static final DeferredBlock<LargeSlidingDoorBlock> LARGE_SLIDING_DOOR = BLOCKS.register("large_sliding_door",
            () -> new LargeSlidingDoorBlock(BlockBehaviour.Properties.of()));

    public static final DeferredBlock<SmallPosterBlock> YELLOW_INSPECTION_POSTER = BLOCKS.register("yellow_inspection_poster",
            () -> new SmallPosterBlock(BlockBehaviour.Properties.of()));

    public static final DeferredBlock<DistributionBoardBlock> DISTRIBUTION_BOARD = BLOCKS.register("distribution_board",
            () -> new DistributionBoardBlock(BlockBehaviour.Properties.of()));

    public static final DeferredBlock<SCP151Block> SCP151 = BLOCKS.register("scp151",
            () -> new SCP151Block(BlockBehaviour.Properties.of()));

    public static final DeferredBlock<PersonnelTerminalBlock> PERSONNEL_TERMINAL = BLOCKS.register("personnel_terminal_block",
            () -> new PersonnelTerminalBlock(BlockBehaviour.Properties.of()));

    public static final DeferredBlock<KeycardPrinter> KEYCARD_PRINTER = BLOCKS.register("keycard_printer",
            () -> new KeycardPrinter(BlockBehaviour.Properties.of()));

    public static final DeferredBlock<SlidingDoorBlock> SLIDING_DOOR = BLOCKS.register("sliding_door",
            () -> new SlidingDoorBlock(BlockBehaviour.Properties.of().noOcclusion()));

    public static final DeferredBlock<WallWritingBlock> WALL_WRITING = BLOCKS.register("wall_writing",
            () -> new WallWritingBlock(BlockBehaviour.Properties.of()
                    .noOcclusion()
                    .noCollission()
                    .replaceable()
                    .destroyTime(-1)
                    .sound(SoundType.SAND)
            ));

    public static final DeferredBlock<StickyNoteBlock> STICKY_NOTE = BLOCKS.register("sticky_note",
            () -> new StickyNoteBlock(BlockBehaviour.Properties.of()
                    .noOcclusion()
                    .noCollission()
                    .sound(new SoundType(
                            1.0f,
                            1.0f,
                            SoundEvents.BOOK_PUT,
                            SoundEvents.BOOK_PUT,
                            SoundEvents.BOOK_PUT,
                            SoundEvents.BOOK_PUT,
                            SoundEvents.BOOK_PUT
                    ))
            ));
}
