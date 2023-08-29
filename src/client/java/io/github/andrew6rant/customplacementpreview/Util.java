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

    public static BlockState getValidPlacementState(Block block, ItemPlacementContext context) {
        BlockState heldState;
        if (block instanceof ICustomPlacementPreview customPlacementPreviewBlock) {
            heldState = customPlacementPreviewBlock.getValidPlacementState(block, context);
        } else {
            heldState = block.getPlacementState(context);
        }
        return heldState;
    }

    public static BlockState getInvalidPlacementState(Block block, ItemPlacementContext context) {
        BlockState heldState;
        if (block instanceof ICustomPlacementPreview customPlacementPreviewBlock) {
            heldState = customPlacementPreviewBlock.getInvalidPlacementState(block, context);
        } else {
            heldState = block.getDefaultState();
        }
        return heldState;
    }

    public static VoxelShape getValidWireframe(Block block, BlockState heldState, World world, BlockPos pos, Entity entity) {
        VoxelShape outlineShape;
        if (block instanceof ICustomPlacementPreview customPlacementPreviewBlock) {
             outlineShape = customPlacementPreviewBlock.getValidWireframe(heldState, world, pos, ShapeContext.of(entity));
        } else {
            outlineShape = heldState.getOutlineShape(world, pos, ShapeContext.of(entity));
        }
        return outlineShape;
    }

    public static VoxelShape getIntersectingWireframe(Block block, BlockState heldState, World world, BlockPos pos, Entity entity) {
        VoxelShape outlineShape;
        if (block instanceof ICustomPlacementPreview customPlacementPreviewBlock) {
            outlineShape = customPlacementPreviewBlock.getIntersectingWireframe(heldState, world, pos, ShapeContext.of(entity));
        } else {
            outlineShape = heldState.getOutlineShape(world, pos, ShapeContext.of(entity));
        }
        return outlineShape;
    }

    public static VoxelShape getInvalidWireframe(Block block, BlockState heldState, World world, BlockPos pos, Entity entity) {
        VoxelShape outlineShape;
        if (block instanceof ICustomPlacementPreview customPlacementPreviewBlock) {
            outlineShape = customPlacementPreviewBlock.getInvalidWireframe(heldState, world, pos, ShapeContext.of(entity));
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
