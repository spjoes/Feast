package dev.persn.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Property;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class CuttingBoardBlock extends Block {
    public static final EnumProperty<Direction> FACING;

    public CuttingBoardBlock(Settings settings) {
        super(settings);
        this.setDefaultState((BlockState)((BlockState)this.stateManager.getDefaultState()).with(FACING, Direction.NORTH));
    }

    @Override
    protected MapCodec<? extends CuttingBoardBlock> getCodec() {
        return createCodec(CuttingBoardBlock::new);
    }

    @Override
    public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
        return (BlockState)this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().rotateYClockwise());
    }

    @Override
    protected BlockState rotate(BlockState state, BlockRotation rotation) {
        return (BlockState)state.with(FACING, rotation.rotate((Direction)state.get(FACING)));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{FACING});
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        switch (state.get(FACING)) {
            case Direction.NORTH, Direction.SOUTH -> {
                return Stream.of(
                        Block.createCuboidShape(5, 0, 2.5, 11, 1, 13.5),
                        Block.createCuboidShape(4.5, 0, 3, 5, 1, 13),
                        Block.createCuboidShape(4, 0, 3.5, 4.5, 1, 12.5),
                        Block.createCuboidShape(11, 0, 3, 11.5, 1, 13),
                        Block.createCuboidShape(11.5, 0, 3.5, 12, 1, 12.5)
                ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();
            }
            case Direction.EAST, Direction.WEST -> {
                return Stream.of(
                        Block.createCuboidShape(2.5, 0, 5, 13.5, 1, 11),
                        Block.createCuboidShape(3, 0, 4.5, 13, 1, 5),
                        Block.createCuboidShape(3.5, 0, 4, 12.5, 1, 4.5),
                        Block.createCuboidShape(3, 0, 11, 13, 1, 11.5),
                        Block.createCuboidShape(3.5, 0, 11.5, 12.5, 1, 12)
                ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();
            }
            default -> throw new IllegalStateException("Unexpected value: " + state.get(FACING));
        }
    }

    static {
        FACING = HorizontalFacingBlock.FACING;
    }
}
