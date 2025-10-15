package com.site21.bittermelon.common.systems.electronics;

import com.site21.bittermelon.common.content.blocks.electronics.containmentpanel.ContainmentPanelBlockEntity;
import com.site21.bittermelon.common.systems.containment.ContainmentData;

public interface ContainmentDevice {
    void activateDevice(ContainmentPanelBlockEntity containment);
    ContainmentData getData(ContainmentPanelBlockEntity containment);
    // canContainMultiple condition
}
