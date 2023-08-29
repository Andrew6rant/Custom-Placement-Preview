package io.github.andrew6rant.customplacementpreview.mixin.client;

import io.github.andrew6rant.customplacementpreview.Util;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
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
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static io.github.andrew6rant.customplacementpreview.config.ClientConfig.*;

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
                ItemPlacementContext context = new ItemPlacementContext(this.world, playerEntity, hand, playerEntity.getStackInHand(hand), blockHitResult);
                Block block = blockItem.getBlock();
                if (context.canPlace()) {
                    BlockState heldState = Util.getValidPlacementState(block, context);
                    if (heldState != null) {
                        if (context.canReplaceExisting()) {
                            customplacementpreview$drawWireframe(
                                matrices, vertexConsumer,
                                cameraX, cameraY, cameraZ,
                                Util.getIntersectingWireframe(block, heldState, this.world, pos, entity),
                                pos,
                                Util.parseConfigHex(outlineColorAARRGGBB),
                                ci);
                        } else {
                            if (block.canPlaceAt(heldState, this.world, pos)) {
                                customplacementpreview$drawWireframe(
                                    matrices, vertexConsumer,
                                    cameraX, cameraY, cameraZ,
                                    Util.getValidWireframe(block, heldState, this.world, pos, entity),
                                    pos.offset(blockHitResult.getSide()),
                                    Util.parseConfigHex(replaceableColorAARRGGBB),
                                    ci);
                            }
                        }
                    } else {
                        // null blockstate (bad placement)
                        customplacementpreview$drawInvalidWireframe(
                            block, context, pos, entity, blockHitResult,
                            matrices, vertexConsumer,
                            cameraX, cameraY, cameraZ,
                            ci);
                    }
                } else {
                    // actually invalid (within block)
                    customplacementpreview$drawInvalidWireframe(
                        block, context, pos, entity, blockHitResult,
                        matrices, vertexConsumer,
                        cameraX, cameraY, cameraZ,
                        ci);
                }
            }
        }
    }

    @Unique
    private void customplacementpreview$drawInvalidWireframe(Block block, ItemPlacementContext context, BlockPos pos, Entity entity, BlockHitResult blockHitResult, MatrixStack matrices, VertexConsumer vertexConsumer, double cameraX, double cameraY, double cameraZ, CallbackInfo ci) {
        BlockPos NewPos = pos.offset(blockHitResult.getSide());
        if (hideInvalidEqualBlocks && this.world.getBlockState(NewPos).getBlock().equals(block)) {
            ci.cancel();
        } else {
            VoxelShape outlineShape = Util.getInvalidWireframe(block, Util.getInvalidPlacementState(block, context), this.world, pos, entity);
            int hexColor = Util.parseConfigHex(invalidColorAARRGGBB);
            customplacementpreview$drawWireframe(matrices, vertexConsumer, cameraX, cameraY, cameraZ, outlineShape, NewPos, hexColor, ci);
        }
    }

    @Unique
    private void customplacementpreview$drawWireframe(MatrixStack matrices, VertexConsumer vertexConsumer, double cameraX, double cameraY, double cameraZ, VoxelShape outlineShape, BlockPos pos, int hexColor, CallbackInfo ci) {
        drawCuboidShapeOutline(
            matrices, vertexConsumer, outlineShape,
            (double) pos.getX() - cameraX, (double) pos.getY() - cameraY, (double) pos.getZ() - cameraZ,
            (hexColor >> 16 & 255) / 255f, (hexColor >> 8 & 255) / 255f, (hexColor & 255) / 255f, (hexColor >>> 24) / 255f);
        ci.cancel();
    }
}

