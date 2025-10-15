package com.site21.bittermelon.data;

import com.site21.bittermelon.Bittermelon;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.data.PackOutput;
import org.jetbrains.annotations.NotNull;

public class BlockStateProvider extends ModelProvider {
    public BlockStateProvider(PackOutput output) {
        super(output, Bittermelon.MOD_ID);
    }

    @Override
    protected void registerModels(@NotNull BlockModelGenerators blockModels, @NotNull ItemModelGenerators itemModels) {
    }

}
