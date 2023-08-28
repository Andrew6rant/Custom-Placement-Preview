package io.github.andrew6rant.customplacementpreview.api;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
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
}
