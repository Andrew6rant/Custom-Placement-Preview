package io.github.andrew6rant.customplacementpreview.mixin.client;

import io.github.andrew6rant.customplacementpreview.DrawUtil;
import io.github.andrew6rant.customplacementpreview.api.ICustomPlacementPreview;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {

    @Shadow @Final private MinecraftClient client;

    @Shadow @Nullable private ClientWorld world;

    @Shadow
    private static void drawCuboidShapeOutline(MatrixStack matrices, VertexConsumer vertexConsumer, VoxelShape shape, double offsetX, double offsetY, double offsetZ, float red, float green, float blue, float alpha) {
    }

    @Inject(method = "drawBlockOutline(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;Lnet/minecraft/entity/Entity;DDDLnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V", at = @At("HEAD"), cancellable = true)
    private void customplacementpreview$injectPlacementPreview(MatrixStack matrices, VertexConsumer vertexConsumer, Entity entity, double cameraX, double cameraY, double cameraZ, BlockPos pos, BlockState state, CallbackInfo ci) {
        if (entity instanceof PlayerEntity playerEntity) {
            Hand hand = playerEntity.getActiveHand();
            if (playerEntity.getStackInHand(hand) != null && playerEntity.getStackInHand(hand).getItem() instanceof BlockItem blockItem) {
                BlockHitResult blockHitResult = (BlockHitResult)this.client.crosshairTarget;
                ItemPlacementContext context = new ItemPlacementContext(this.world, playerEntity, playerEntity.getActiveHand(), playerEntity.getStackInHand(playerEntity.getActiveHand()), blockHitResult);
                if (context.canPlace()) {
                    Block block = blockItem.getBlock();
                    BlockState heldState;
                    if (block instanceof ICustomPlacementPreview customPlacementPreviewBlock) {
                        heldState = customPlacementPreviewBlock.getCustomPlacementState(block, context);
                    } else {
                        heldState = block.getPlacementState(context);
                    }

                    if (heldState != null) {
                        if (context.canReplaceExisting()) {
                            VoxelShape outlineShape = DrawUtil.getCustomOutlineShape(block, heldState, this.world, pos, entity);
                            drawCuboidShapeOutline(matrices, vertexConsumer, outlineShape, (double) pos.getX() - cameraX, (double) pos.getY() - cameraY, (double) pos.getZ() - cameraZ, 0.0F, 0.0F, 0.0F, 0.4F);
                            ci.cancel();
                        } else {
                            BlockPos newPos = pos.offset(blockHitResult.getSide());
                            if (blockItem.getBlock().canPlaceAt(heldState, this.world, pos)) {
                                VoxelShape outlineShape = DrawUtil.getCustomOutlineShape(block, heldState, this.world, pos, entity);
                                drawCuboidShapeOutline(matrices, vertexConsumer, outlineShape, (double) newPos.getX() - cameraX, (double) newPos.getY() - cameraY, (double) newPos.getZ() - cameraZ, 0.0F, 0.0F, 0.0F, 0.4F);
                                ci.cancel();
                            }
                        }
                    }
                }
            }
        }
    }
}

