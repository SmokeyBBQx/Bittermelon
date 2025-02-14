package com.site21.bittermelon.blocks;

import com.site21.bittermelon.blocks.blockentities.ContainmentPanelBlockEntity;
import com.site21.bittermelon.containment.ContainmentData;

public interface ContainmentDevice {
    void activateDevice(ContainmentPanelBlockEntity containment);
    ContainmentData getData(ContainmentPanelBlockEntity containment);
    // canContainMultiple check
}
