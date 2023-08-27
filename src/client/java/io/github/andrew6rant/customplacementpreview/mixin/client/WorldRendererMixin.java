package io.github.andrew6rant.customplacementpreview.mixin.client;

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
                    BlockState heldState = blockItem.getBlock().getPlacementState(context);
                    if (heldState != null) {
                        if (context.canReplaceExisting()) {
                            drawCuboidShapeOutline(matrices, vertexConsumer, heldState.getOutlineShape(this.world, pos, ShapeContext.of(entity)), (double) pos.getX() - cameraX, (double) pos.getY() - cameraY, (double) pos.getZ() - cameraZ, 0.0F, 0.0F, 0.0F, 0.4F);
                            ci.cancel();
                        } else {
                            BlockPos newPos = pos.offset(blockHitResult.getSide());
                            if (blockItem.getBlock().canPlaceAt(heldState, this.world, pos)) {
                                drawCuboidShapeOutline(matrices, vertexConsumer, heldState.getOutlineShape(this.world, newPos, ShapeContext.of(entity)), (double) newPos.getX() - cameraX, (double) newPos.getY() - cameraY, (double) newPos.getZ() - cameraZ, 0.0F, 0.0F, 0.0F, 0.4F);
                                ci.cancel();
                            }
                        }
                    }
                }
            }
        }
    }
}

