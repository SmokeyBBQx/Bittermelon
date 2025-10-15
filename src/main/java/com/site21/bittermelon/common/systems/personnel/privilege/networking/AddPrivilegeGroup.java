package com.site21.bittermelon.common.systems.personnel.privilege.networking;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.systems.personnel.privilege.PrivilegeGroup;
import com.site21.bittermelon.common.systems.personnel.privilege.PrivilegeManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record AddPrivilegeGroup(PrivilegeGroup group) implements CustomPacketPayload {
    public static final Type<AddPrivilegeGroup> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "add_privilege_group"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<ByteBuf, AddPrivilegeGroup> STREAM_CODEC = StreamCodec.composite(
            PrivilegeGroup.STREAM_CODEC,
            AddPrivilegeGroup::group,
            AddPrivilegeGroup::new
    );

    public void handle(@NotNull IPayloadContext ctx) {
        PrivilegeManager manager = PrivilegeManager.get(ctx.player().level());
        if (ctx.flow().isServerbound()) {
            manager.addPrivilegeGroup(group);
        } else {
            manager.getPrivilegeGroups().put(group.getName(), group);
        }
    }
}
