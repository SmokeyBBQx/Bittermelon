package com.site21.bittermelon.common.systems.personnel.privilege.networking;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.systems.personnel.privilege.PrivilegeGroup;
import com.site21.bittermelon.common.systems.personnel.privilege.PrivilegeManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public record SyncPrivileges(Map<String, PrivilegeGroup> privilegeGroups, Set<String> privileges) implements CustomPacketPayload {
    public static final Type<SyncPrivileges> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "sync_privileges"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<ByteBuf, SyncPrivileges> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.map(
                    HashMap::new,
                    ByteBufCodecs.STRING_UTF8,
                    PrivilegeGroup.STREAM_CODEC
            ),
            SyncPrivileges::privilegeGroups,
            ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.collection(HashSet::new)),
            SyncPrivileges::privileges,
            SyncPrivileges::new
    );

    public void handle(@NotNull IPayloadContext ctx) {
        PrivilegeManager manager = PrivilegeManager.get(ctx.player().level());
        manager.getPrivilegeGroups().clear();
        manager.getPrivilegeGroups().putAll(privilegeGroups);
        manager.getPrivileges().clear();
        manager.getPrivileges().addAll(privileges);
    }
}
