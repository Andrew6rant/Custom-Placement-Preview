package io.github.andrew6rant.customplacementpreview;

import io.github.andrew6rant.customplacementpreview.api.ICustomPlacementPreview;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;


public class Util {

    public static BlockState getCustomPlacementState(Block block, ItemPlacementContext context) {
        BlockState heldState;
        if (block instanceof ICustomPlacementPreview customPlacementPreviewBlock) {
            heldState = customPlacementPreviewBlock.getCustomPlacementState(block, context);
        } else {
            heldState = block.getPlacementState(context);
        }
        return heldState;
    }

    public static VoxelShape getCustomVoxelShape(Block block, BlockState heldState, World world, BlockPos pos, Entity entity) {
        VoxelShape outlineShape;
        if (block instanceof ICustomPlacementPreview customPlacementPreviewBlock) {
             outlineShape = customPlacementPreviewBlock.getCustomPlacementVoxelShape(heldState, world, pos, ShapeContext.of(entity));
        } else {
            outlineShape = heldState.getOutlineShape(world, pos, ShapeContext.of(entity));
        }
        return outlineShape;
    }

    public static VoxelShape getCanReplaceVoxelShape(Block block, BlockState heldState, World world, BlockPos pos, Entity entity) {
        VoxelShape outlineShape;
        if (block instanceof ICustomPlacementPreview customPlacementPreviewBlock) {
            outlineShape = customPlacementPreviewBlock.getCanReplaceVoxelShape(heldState, world, pos, ShapeContext.of(entity));
        } else {
            outlineShape = heldState.getOutlineShape(world, pos, ShapeContext.of(entity));
        }
        return outlineShape;
    }

    public static VoxelShape getInvalidVoxelShape(Block block, BlockState heldState, World world, BlockPos pos, Entity entity) {
        VoxelShape outlineShape;
        if (block instanceof ICustomPlacementPreview customPlacementPreviewBlock) {
            outlineShape = customPlacementPreviewBlock.getInvalidVoxelShape(heldState, world, pos, ShapeContext.of(entity));
        } else {
            outlineShape = heldState.getOutlineShape(world, pos, ShapeContext.of(entity));
        }
        return outlineShape;
    }

    public static int parseConfigHex(String configColor) {
        if (configColor.isEmpty() || configColor.substring(1).isEmpty()) return 0;
        return (int) Long.parseUnsignedLong(configColor.substring(1), 16);
    }
}
