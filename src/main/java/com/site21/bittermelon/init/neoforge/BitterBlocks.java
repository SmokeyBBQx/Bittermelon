package com.site21.bittermelon.init.neoforge;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.content.blocks.electronics.television.StandingTelevisionBlock;
import com.site21.bittermelon.common.content.blocks.electronics.television.WallTelevisionBlock;
import com.site21.bittermelon.common.content.blocks.flamingo.FlamingoBlock;
import com.site21.bittermelon.common.content.blocks.poster.SmallPosterBlock;
import com.site21.bittermelon.common.content.blocks.base.structuralblock.StructuralBlock;
import com.site21.bittermelon.common.content.blocks.container.smallbox.SmallBox;
import com.site21.bittermelon.common.content.blocks.dirtyfloor.DirtyFloorBlock;
import com.site21.bittermelon.common.content.blocks.electronics.ATMBlock;
import com.site21.bittermelon.common.content.blocks.electronics.containmentalarm.ContainmentAlarm;
import com.site21.bittermelon.common.content.blocks.electronics.containmentpanel.ContainmentPanelBlock;
import com.site21.bittermelon.common.content.blocks.electronics.detonator.DetonatorBlock;
import com.site21.bittermelon.common.content.blocks.electronics.environmentsensor.EnvironmentSensor;
import com.site21.bittermelon.common.content.blocks.electronics.intercom.IntercomBlock;
import com.site21.bittermelon.common.content.blocks.electronics.keycardprinter.KeycardPrinter;
import com.site21.bittermelon.common.content.blocks.electronics.keycardreader.KeycardReaderBlock;
import com.site21.bittermelon.common.content.blocks.electronics.largeslidingdoor.LargeSlidingDoorBlock;
import com.site21.bittermelon.common.content.blocks.electronics.personnelterminal.PersonnelTerminalBlock;
import com.site21.bittermelon.common.content.blocks.electronics.redstonedevice.RedstoneDeviceBlock;
import com.site21.bittermelon.common.content.blocks.electronics.securedoor.KeycardReaderSecureDoorBlock;
import com.site21.bittermelon.common.content.blocks.electronics.securedoor.SecureDoorBlock;
import com.site21.bittermelon.common.content.blocks.electronics.slidingdoor.SlidingDoorBlock;
import com.site21.bittermelon.common.content.blocks.electronics.speaker.SpeakerBlock;
import com.site21.bittermelon.common.content.blocks.electronics.thermometer.ThermometerBlock;
import com.site21.bittermelon.common.content.blocks.lights.CageLampBlock;
import com.site21.bittermelon.common.content.blocks.lights.emergencyexitlight.EmergencyExitLampBlock;
import com.site21.bittermelon.common.content.blocks.powergrid.distributionboard.DistributionBoardBlock;
import com.site21.bittermelon.common.content.blocks.scp.scp151.SCP151Block;
import com.site21.bittermelon.common.content.blocks.stickynote.StickyNoteBlock;
import com.site21.bittermelon.common.content.blocks.substance.fluid.FluidBlock;
import com.site21.bittermelon.common.content.blocks.wallwriting.WallWritingBlock;
import com.site21.bittermelon.common.systems.fluid.SubstanceFluidBlock;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.common.util.DeferredSoundType;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.ToIntFunction;

public class BitterBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Bittermelon.MOD_ID);

    @Contract(pure = true)
    private static @NotNull ToIntFunction<BlockState> litBlockEmission(int lightValue) {
        return (state) -> (Boolean) state.getValue(BlockStateProperties.LIT) ? lightValue : 0;
    }

    private static BlockBehaviour.Properties wallVariant(@NotNull Block baseBlock, boolean overrideDescription) {
        BlockBehaviour.Properties properties = BlockBehaviour.Properties.of().overrideLootTable(baseBlock.getLootTable());
        if (overrideDescription) {
            properties = properties.overrideDescription(baseBlock.getDescriptionId());
        }

        return properties;
    }

    public static final DeferredBlock<FluidBlock> FLUID = BLOCKS.registerBlock("fluid", FluidBlock::new, BlockBehaviour.Properties.of()
            .mapColor(MapColor.WATER)
            .noOcclusion()
            .destroyTime(-1)
            .sound(new DeferredSoundType(1.0f,
                    1.0f,
                    BitterSounds.SPLATTER::value,
                    BitterSounds.SOGGY::value,
                    BitterSounds.SPLAT::value,
                    BitterSounds.SOGGY::value,
                    BitterSounds.SPLATTER::value
            ))
    );

    public static final DeferredBlock<SmallBox> SMALL_CARDBOARD_BOX = BLOCKS.registerBlock("small_cardboard_box", SmallBox::new, BlockBehaviour.Properties.of()
            .destroyTime(-1)
            .sound(SoundType.WOOL)
    );

    public static final DeferredBlock<StructuralBlock> STRUCTURAL_BLOCK = BLOCKS.registerBlock("structural_block", StructuralBlock::new, BlockBehaviour.Properties.of()
            .destroyTime(1.5f)
    );

    public static final DeferredBlock<ATMBlock> ATM = BLOCKS.registerBlock("atm", ATMBlock::new, BlockBehaviour.Properties.of()
            .noOcclusion()
    );

    public static final DeferredBlock<ContainmentPanelBlock> CONTAINMENT_PANEL = BLOCKS.registerBlock("containment_panel", ContainmentPanelBlock::new, BlockBehaviour.Properties.of()
            .noOcclusion()
    );

    public static final DeferredBlock<DirtyFloorBlock> DIRTY_FLOOR = BLOCKS.registerBlock("dirty_floor", DirtyFloorBlock::new, BlockBehaviour.Properties.of()
            .noOcclusion()
            .noCollission()
            .destroyTime(-1)
            .replaceable()
    );

    public static final DeferredBlock<ThermometerBlock> THERMOMETER = BLOCKS.registerBlock("thermometer", ThermometerBlock::new, BlockBehaviour.Properties.of()
            .noOcclusion()
    );

    public static final DeferredBlock<IntercomBlock> INTERCOM = BLOCKS.registerBlock("intercom", IntercomBlock::new, BlockBehaviour.Properties.of()
            .noOcclusion()
    );

    public static final DeferredBlock<EnvironmentSensor> ENVIRONMENT_SENSOR = BLOCKS.registerBlock("environment_sensor", EnvironmentSensor::new, BlockBehaviour.Properties.of()
            .noOcclusion()
    );

    public static final DeferredBlock<ContainmentAlarm> CONTAINMENT_ALARM = BLOCKS.registerBlock("containment_alarm", ContainmentAlarm::new, BlockBehaviour.Properties.of()
            .noOcclusion()
    );

    public static final DeferredBlock<DetonatorBlock> DETONATOR = BLOCKS.registerBlock("detonator", DetonatorBlock::new, BlockBehaviour.Properties.of()
            .noOcclusion()
    );

    public static final DeferredBlock<SpeakerBlock> SPEAKER = BLOCKS.registerBlock("speaker", SpeakerBlock::new, BlockBehaviour.Properties.of()
            .noOcclusion()
    );

    public static final DeferredBlock<SecureDoorBlock> SECURE_DOOR = BLOCKS.registerBlock("secure_door", props -> new SecureDoorBlock(BlockSetType.IRON, props), BlockBehaviour.Properties.of()
            .noOcclusion()
    );

    public static final DeferredBlock<KeycardReaderSecureDoorBlock> KEYCARD_READER_SECURE_DOOR = BLOCKS.registerBlock("keycard_reader_secure_door", props -> new KeycardReaderSecureDoorBlock(BlockSetType.IRON, props), BlockBehaviour.Properties.of()
            .noOcclusion()
    );

    public static final DeferredBlock<LargeSlidingDoorBlock> LARGE_SLIDING_DOOR = BLOCKS.registerBlock("large_sliding_door", LargeSlidingDoorBlock::new, BlockBehaviour.Properties.of());

    public static final DeferredBlock<SmallPosterBlock> YELLOW_INSPECTION_POSTER = BLOCKS.registerBlock("yellow_inspection_poster", SmallPosterBlock::new, BlockBehaviour.Properties.of());

    public static final DeferredBlock<DistributionBoardBlock> DISTRIBUTION_BOARD = BLOCKS.registerBlock("distribution_board", DistributionBoardBlock::new, BlockBehaviour.Properties.of());

    public static final DeferredBlock<SCP151Block> SCP_151 = BLOCKS.registerBlock("scp_151", SCP151Block::new, BlockBehaviour.Properties.of());

    public static final DeferredBlock<PersonnelTerminalBlock> PERSONNEL_TERMINAL = BLOCKS.registerBlock("personnel_terminal_block", PersonnelTerminalBlock::new, BlockBehaviour.Properties.of());

    public static final DeferredBlock<KeycardPrinter> KEYCARD_PRINTER = BLOCKS.registerBlock("keycard_printer", KeycardPrinter::new, BlockBehaviour.Properties.of());

    public static final DeferredBlock<SlidingDoorBlock> WINDOWED_SLIDING_DOOR = BLOCKS.registerBlock("windowed_sliding_door", SlidingDoorBlock::new, BlockBehaviour.Properties.of()
            .noOcclusion()
    );

    public static final DeferredBlock<WallWritingBlock> WALL_WRITING = BLOCKS.registerBlock("wall_writing", WallWritingBlock::new, BlockBehaviour.Properties.of()
            .noOcclusion()
            .noCollission()
            .replaceable()
            .destroyTime(-1)
            .sound(SoundType.EMPTY)
            .noTerrainParticles()
    );

    public static final DeferredBlock<StickyNoteBlock> STICKY_NOTE = BLOCKS.registerBlock("sticky_note", StickyNoteBlock::new, BlockBehaviour.Properties.of()
            .noOcclusion()
            .noCollission()
            .sound(new SoundType(1.0f, 1.0f, SoundEvents.BOOK_PUT, SoundEvents.BOOK_PUT, SoundEvents.BOOK_PUT, SoundEvents.BOOK_PUT, SoundEvents.BOOK_PUT))
    );

    public static final DeferredBlock<KeycardReaderBlock> KEYCARD_READER = BLOCKS.registerBlock("keycard_reader", KeycardReaderBlock::new, BlockBehaviour.Properties.of()
            .sound(SoundType.METAL)
            .destroyTime(1.5f)
    );

    public static final DeferredBlock<RedstoneDeviceBlock> REDSTONE_DEVICE = BLOCKS.registerBlock("redstone_device", RedstoneDeviceBlock::new, BlockBehaviour.Properties.of()
            .sound(SoundType.METAL)
            .destroyTime(1.5f)
    );

    public static final DeferredBlock<SubstanceFluidBlock> SUBSTANCE_FLUID_BLOCK = BLOCKS.registerBlock("substance_fluid_block", SubstanceFluidBlock::new, BlockBehaviour.Properties.of()
            .mapColor(MapColor.WATER)
            .noOcclusion()
            .destroyTime(-1)
            .sound(new DeferredSoundType(1.0f,
                    1.0f,
                    BitterSounds.SPLATTER::value,
                    BitterSounds.SOGGY::value,
                    BitterSounds.SPLAT::value,
                    BitterSounds.SOGGY::value,
                    BitterSounds.SPLATTER::value
            ))
    );

    public static final DeferredBlock<EmergencyExitLampBlock> EMERGENCY_EXIT_LAMP = BLOCKS.registerBlock("emergency_exit_lamp", EmergencyExitLampBlock::new, BlockBehaviour.Properties.of()
            .lightLevel(litBlockEmission(10))
            .destroyTime(1.5f)
            .sound(SoundType.METAL)
    );

    public static final DeferredBlock<CageLampBlock> RED_CAGE_LAMP = BLOCKS.registerBlock("red_cage_lamp", CageLampBlock::new, BlockBehaviour.Properties.of()
            .lightLevel(litBlockEmission(14))
            .destroyTime(1.5f)
            .sound(SoundType.METAL)
    );

    public static final DeferredBlock<CageLampBlock> BLUE_CAGE_LAMP = BLOCKS.registerBlock("blue_cage_lamp", CageLampBlock::new, BlockBehaviour.Properties.of()
            .lightLevel(litBlockEmission(14))
            .destroyTime(1.5f)
            .sound(SoundType.METAL)
    );

    public static final DeferredBlock<CageLampBlock> GREEN_CAGE_LAMP = BLOCKS.registerBlock("green_cage_lamp", CageLampBlock::new, BlockBehaviour.Properties.of()
            .lightLevel(litBlockEmission(14))
            .destroyTime(1.5f)
            .sound(SoundType.METAL)
    );

    public static final DeferredBlock<CageLampBlock> ORANGE_CAGE_LAMP = BLOCKS.registerBlock("orange_cage_lamp", CageLampBlock::new, BlockBehaviour.Properties.of()
            .lightLevel(litBlockEmission(14))
            .destroyTime(1.5f)
            .sound(SoundType.METAL)
    );

    public static final DeferredBlock<CageLampBlock> YELLOW_CAGE_LAMP = BLOCKS.registerBlock("yellow_cage_lamp", CageLampBlock::new, BlockBehaviour.Properties.of()
            .lightLevel(litBlockEmission(14))
            .destroyTime(1.5f)
            .sound(SoundType.METAL)
    );

    public static final DeferredBlock<CageLampBlock> PURPLE_CAGE_LAMP = BLOCKS.registerBlock("purple_cage_lamp", CageLampBlock::new, BlockBehaviour.Properties.of()
            .lightLevel(litBlockEmission(14))
            .destroyTime(1.5f)
            .sound(SoundType.METAL)
    );

    public static final DeferredBlock<CageLampBlock> LIME_CAGE_LAMP = BLOCKS.registerBlock("lime_cage_lamp", CageLampBlock::new, BlockBehaviour.Properties.of()
            .lightLevel(litBlockEmission(14))
            .destroyTime(1.5f)
            .sound(SoundType.METAL)
    );

    public static final DeferredBlock<CageLampBlock> PINK_CAGE_LAMP = BLOCKS.registerBlock("pink_cage_lamp", CageLampBlock::new, BlockBehaviour.Properties.of()
            .lightLevel(litBlockEmission(14))
            .destroyTime(1.5f)
            .sound(SoundType.METAL)
    );

    public static final DeferredBlock<CageLampBlock> MAGENTA_CAGE_LAMP = BLOCKS.registerBlock("magenta_cage_lamp", CageLampBlock::new, BlockBehaviour.Properties.of()
            .lightLevel(litBlockEmission(14))
            .destroyTime(1.5f)
            .sound(SoundType.METAL)
    );

    public static final DeferredBlock<CageLampBlock> CYAN_CAGE_LAMP = BLOCKS.registerBlock("cyan_cage_lamp", CageLampBlock::new, BlockBehaviour.Properties.of()
            .lightLevel(litBlockEmission(14))
            .destroyTime(1.5f)
            .sound(SoundType.METAL)
    );

    public static final DeferredBlock<CageLampBlock> LIGHT_BLUE_CAGE_LAMP = BLOCKS.registerBlock("light_blue_cage_lamp", CageLampBlock::new, BlockBehaviour.Properties.of()
            .lightLevel(litBlockEmission(14))
            .destroyTime(1.5f)
            .sound(SoundType.METAL)
    );

    public static final DeferredBlock<CageLampBlock> CAGE_LAMP = BLOCKS.registerBlock("cage_lamp", CageLampBlock::new, BlockBehaviour.Properties.of()
            .lightLevel(litBlockEmission(14))
            .destroyTime(1.5f)
            .sound(SoundType.METAL)
    );

    public static final DeferredBlock<StandingTelevisionBlock> LIGHT_GRAY_TELEVISION = BLOCKS.registerBlock("light_gray_television", StandingTelevisionBlock::new, BlockBehaviour.Properties.of()
            .noOcclusion()
    );

    public static final DeferredBlock<WallTelevisionBlock> LIGHT_GRAY_WALL_TELEVISION = BLOCKS.registerBlock("light_gray_wall_television",
            WallTelevisionBlock::new, BlockBehaviour.Properties.of()
                    .noOcclusion()
    );

    public static final DeferredBlock<FlamingoBlock> PLASTIC_FLAMINGO = BLOCKS.registerBlock("plastic_flamingo", FlamingoBlock::new, BlockBehaviour.Properties.of()
            .noOcclusion()
    );
}
