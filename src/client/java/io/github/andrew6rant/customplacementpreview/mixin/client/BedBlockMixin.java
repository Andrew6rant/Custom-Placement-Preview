package io.github.andrew6rant.customplacementpreview.mixin.client;

import io.github.andrew6rant.customplacementpreview.api.ICustomPlacementPreview;
import io.github.andrew6rant.customplacementpreview.api.VoxelShapeUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.block.enums.BedPart;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;

import static net.minecraft.block.BedBlock.PART;

@Environment(EnvType.CLIENT)
@Mixin(BedBlock.class)
public class BedBlockMixin extends HorizontalFacingBlock implements ICustomPlacementPreview {
    protected BedBlockMixin(Settings settings) {
        super(settings);
    }

    @Override
    public BlockState getInvalidPlacementState(Block block, ItemPlacementContext ctx) {
        return block.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing());
    }

    @Override
    public VoxelShape getValidWireframe(BlockState blockState, BlockView world, BlockPos pos, ShapeContext context) {
        VoxelShape leg1 = Block.createCuboidShape(0, 0, 13, 3, 3, 16);
        VoxelShape leg2 = Block.createCuboidShape(13, 0, 13, 16, 3, 16);
        VoxelShape body = Block.createCuboidShape(0, 3, 0, 16, 9, 16);
        Direction direction = blockState.get(FACING);
        VoxelShape shape = VoxelShapeUtil.rotateShapeDirectional(VoxelShapes.union(body, leg1, leg2), direction);
        return VoxelShapeUtil.doubleBlock(direction, blockState.get(PART) == BedPart.FOOT, shape, VoxelShapeUtil.rotateShape(shape, Direction.Axis.Y, 2));
    }
}
