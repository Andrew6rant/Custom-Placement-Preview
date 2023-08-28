package io.github.andrew6rant.customplacementpreview.api;

import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

// Massive thanks to LarsMans from Diopside for some of this code.
// https://github.com/TeamDiopside/Seamless/blob/1.20.1/common/src/main/java/nl/teamdiopside/seamless/SeamlessShapes.java
public class VoxelShapeUtil {

    public static VoxelShape doubleBlock(Direction direction, boolean bl, VoxelShape voxelShape, VoxelShape movingShape) {
        return bl ? VoxelShapes.union(voxelShape, moveShape(direction, 1, movingShape)) : VoxelShapes.union(movingShape, moveShape(direction.getOpposite(), 1, voxelShape));
    }

    public static VoxelShape moveShape(Direction direction, int distance, VoxelShape shape) {
        if (shape.isEmpty()) {
            return shape;
        }
        VoxelShape[] newshape = new VoxelShape[]{VoxelShapes.empty()};
        shape.forEachBox((a, b, c, d, e, f) -> newshape[0] = VoxelShapes.union(newshape[0], moveShape(direction, distance, a, b, c, d, e, f)));
        return newshape[0];
    }

    private static VoxelShape moveShape(Direction direction, int distance, double x1, double y1, double z1, double x2, double y2, double z2) {
        switch (direction) {
            case NORTH -> {
                z1-=distance;
                z2-=distance;
            }
            case EAST -> {
                x1+=distance;
                x2+=distance;
            }
            case SOUTH -> {
                z1+=distance;
                z2+=distance;
            }
            case WEST -> {
                x1-=distance;
                x2-=distance;
            }
            case UP -> {
                y1+=distance;
                y2+=distance;
            }
            case DOWN -> {
                y1-=distance;
                y2-=distance;
            }
        }
        return VoxelShapes.cuboid(x1, y1, z1, x2, y2, z2);
    }

    public static VoxelShape rotateShapeDirectional(VoxelShape northShape, Direction direction) {
        int i = switch (direction) {
            case NORTH -> 0;
            case UP, EAST -> 3;
            case SOUTH -> 2;
            case DOWN, WEST -> 1;
        };
        if (direction == Direction.UP || direction == Direction.DOWN) return rotateShape(northShape, Direction.Axis.X, i);
        return rotateShape(northShape, Direction.Axis.Y, i);
    }

    public static VoxelShape rotateShape(VoxelShape shape, Direction.Axis axis, int quarterTurns) {
        if (shape.isEmpty()) {
            return shape;
        }
        VoxelShape[] newshape = new VoxelShape[]{VoxelShapes.empty()};
        shape.forEachBox((x1, y1, z1, x2, y2, z2) -> {
            float angle = 0.5f * (float)Math.PI * quarterTurns;
            float align = 0.5f;
            Vec3d minVector = new Vec3d(x1 - align, y1 - align, z1 - align);
            Vec3d maxVector = new Vec3d(x2 - align, y2 - align, z2 - align);

            switch (axis) {
                case X -> {
                    minVector = minVector.rotateX(angle);
                    maxVector = maxVector.rotateX(angle);
                }
                case Y -> {
                    minVector = minVector.rotateY(angle);
                    maxVector = maxVector.rotateY(angle);
                }
                case Z -> {
                    minVector = minVector.rotateZ(angle);
                    maxVector = maxVector.rotateZ(angle);
                }
            }

            double a = Math.min(minVector.x, maxVector.x) + align;
            double b = Math.min(minVector.y, maxVector.y) + align;
            double c = Math.min(minVector.z, maxVector.z) + align;
            double d = Math.max(minVector.x, maxVector.x) + align;
            double e = Math.max(minVector.y, maxVector.y) + align;
            double f = Math.max(minVector.z, maxVector.z) + align;

            newshape[0] = VoxelShapes.union(newshape[0], VoxelShapes.cuboid(a, b, c, d, e, f));
        });
        return newshape[0];
    }
}
