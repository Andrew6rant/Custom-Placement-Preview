package io.github.andrew6rant.customplacementpreview.api;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

public interface ICustomPlacementPreview {
    @Nullable
    default BlockState getCustomPlacementState(Block block, ItemPlacementContext ctx) {
        return block.getPlacementState(ctx);
    }

    default VoxelShape getCustomPlacementVoxelShape(BlockState blockState, BlockView world, BlockPos pos, ShapeContext context) {
        return blockState.getOutlineShape(world, pos, context);
    }

    default VoxelShape getCanReplaceVoxelShape(BlockState blockState, BlockView world, BlockPos pos, ShapeContext context) {
        return this.getCustomPlacementVoxelShape(blockState, world, pos, context);
    }

    default VoxelShape getInvalidVoxelShape(BlockState blockState, BlockView world, BlockPos pos, ShapeContext context) {
        return this.getCustomPlacementVoxelShape(blockState, world, pos, context);
    }

    default ItemStack getCustomItemStackInHand(PlayerEntity playerEntity, Hand hand) {
        if (hand == Hand.MAIN_HAND) {
            return playerEntity.getEquippedStack(EquipmentSlot.MAINHAND);
        } else if (hand == Hand.OFF_HAND) {
            return playerEntity.getEquippedStack(EquipmentSlot.OFFHAND);
        } else {
            throw new IllegalArgumentException("Invalid hand " + hand);
        }
    }
}
