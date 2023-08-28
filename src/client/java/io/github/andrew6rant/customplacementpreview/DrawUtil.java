package io.github.andrew6rant.customplacementpreview;

import io.github.andrew6rant.customplacementpreview.api.ICustomPlacementPreview;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;


public class DrawUtil {
    public static VoxelShape getCustomOutlineShape(Block block, BlockState heldState, World world, BlockPos pos, Entity entity) {
        VoxelShape outlineShape;
        if (block instanceof ICustomPlacementPreview customPlacementPreviewBlock) {
             outlineShape = customPlacementPreviewBlock.getCustomPlacementVoxelShape(heldState, world, pos, ShapeContext.of(entity));
        } else {
            outlineShape = heldState.getOutlineShape(world, pos, ShapeContext.of(entity));
        }
        return outlineShape;
    }
}
