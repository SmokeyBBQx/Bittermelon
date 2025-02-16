package com.site21.bittermelon.hvac;

import net.minecraft.core.BlockPos;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class HVACNetwork {
    private Set<BlockPos> ducts = new HashSet<>();
    private Map<BlockPos, IHVACConnector> connectors = new HashMap<>();


}
