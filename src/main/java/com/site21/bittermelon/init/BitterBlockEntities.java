package com.site21.bittermelon.init;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.blocks.base.structuralblock.StructuralBlockEntity;
import com.site21.bittermelon.content.blocks.container.smallbox.BoxBlockEntity;
import com.site21.bittermelon.content.blocks.devices.containmentpanel.ContainmentPanelBlockEntity;
import com.site21.bittermelon.content.blocks.devices.thermometer.ThermometerBlockEntity;
import com.site21.bittermelon.content.blocks.substance.fluid.FluidBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.site21.bittermelon.init.BitterBlocks.*;

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
}
