package com.site21.bittermelon.systems.electronics;

import com.site21.bittermelon.content.blocks.electronics.containmentpanel.ContainmentPanelBlockEntity;
import com.site21.bittermelon.systems.containment.ContainmentData;

public interface ContainmentDevice {
    void activateDevice(ContainmentPanelBlockEntity containment);
    ContainmentData getData(ContainmentPanelBlockEntity containment);
    // canContainMultiple condition
}
