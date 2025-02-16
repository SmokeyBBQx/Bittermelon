package com.site21.bittermelon.content.blocks.devices;

import com.site21.bittermelon.content.blocks.devices.containmentpanel.ContainmentPanelBlockEntity;
import com.site21.bittermelon.content.containment.ContainmentData;

public interface ContainmentDevice {
    void activateDevice(ContainmentPanelBlockEntity containment);
    ContainmentData getData(ContainmentPanelBlockEntity containment);
    // canContainMultiple check
}
