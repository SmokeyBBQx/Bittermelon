package com.site21.bittermelon.common.systems.medical.legacy.client.tool;

import com.site21.bittermelon.init.neoforge.BitterDataComponents;
import net.minecraft.core.component.DataComponentType;

import java.util.HashMap;
import java.util.Map;

public class InstrumentWidgets {
    public static final Map<DataComponentType<?>, InstrumentWidgetFactory>
            INSTRUMENT_WIDGETS = new HashMap<>();

    public static void register() {
        INSTRUMENT_WIDGETS.put(BitterDataComponents.SCALPEL.get(), ScalpelWidget::new);
        INSTRUMENT_WIDGETS.put(BitterDataComponents.RETRACTOR.get(), RetractorWidget::new);
        INSTRUMENT_WIDGETS.put(BitterDataComponents.SUTURE.get(), SutureWidget::new);
    }
}
