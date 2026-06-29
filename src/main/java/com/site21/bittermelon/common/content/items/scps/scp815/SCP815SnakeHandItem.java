package com.site21.bittermelon.common.content.items.scps.scp815;

import com.site21.bittermelon.common.content.entities.scp815snake.SCP815Snake;
import com.site21.bittermelon.init.neoforge.BitterEntities;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

public class SCP815SnakeHandItem extends Item {
    public SCP815SnakeHandItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide()) {
            SCP815Snake snake = BitterEntities.SCP_815_SNAKE.get().create(level, EntitySpawnReason.TRIGGERED);
            HitResult hit = player.pick(10.0, 0.0f, false);
            snake.teleportTo(hit.getLocation().x, hit.getLocation().y, hit.getLocation().z);
            level.addFreshEntity(snake);
            player.getItemInHand(hand).shrink(1);
        }
        return InteractionResult.SUCCESS;
    }
}
