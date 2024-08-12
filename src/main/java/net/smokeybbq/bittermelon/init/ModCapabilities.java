package net.smokeybbq.bittermelon.init;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.smokeybbq.bittermelon.items.substancecontainers.ISubstanceContainer;

public class ModCapabilities {
    public static final Capability<ISubstanceContainer> SUBSTANCE_CONTAINER_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
    });

    public static void register(RegisterCapabilitiesEvent event) {
        event.register(ISubstanceContainer.class);
    }
}
