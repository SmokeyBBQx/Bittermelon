package com.site21.bittermelon.content.medical.compartments;

import com.site21.bittermelon.content.medical.client.screen.HealthScreenV2;
import com.site21.bittermelon.content.medical.client.screen.widget.CompartmentSpaceWidget;
import com.site21.bittermelon.content.medical.medicalstats.MedicalStats;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.init.custom.Compartments.CUT;
import static com.site21.bittermelon.init.custom.Compartments.LIVER;

public class SharpObject extends Compartment {
    public SharpObject(String id, Properties properties) {
        super(id, properties);
    }

    @Override
    public void performAction(@NotNull CompartmentSpaceWidget widget, double mouseX, double mouseY, int button) {
        if (button == 0) {
            CompartmentInstance cut = CUT.get().toInstance();
            cut.getVisualData().width(20).height(20).x((int) mouseX - 20 / 2).y((int) mouseY - 20 / 2).isHidden(false);
            widget.getHealthScreen().getMedicalStats().addCompartment(cut);
            widget.getCompartment().tryToInsert(0, cut);
            widget.refreshCompartmentNodes();
        } else {
            super.performAction(widget, mouseX, mouseY, button);
        }
    }
}
