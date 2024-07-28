package net.smokeybbq.bittermelon.containment;

import net.smokeybbq.bittermelon.util.DataManager;

import java.util.UUID;

public class SCPManager extends DataManager<UUID, SCP> {
    protected SCPManager(String dataFolder, Class<SCP> type) {
        super(dataFolder, type);
    }

    @Override
    protected String getFileName(SCP data) {
        return null;
    }

    @Override
    protected UUID getKey(SCP data) {
        return null;
    }
}
