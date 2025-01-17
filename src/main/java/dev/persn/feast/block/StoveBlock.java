package dev.persn.feast.block;

import com.mojang.serialization.MapCodec;
import dev.persn.feast.Feast;
import dev.persn.feast.block.entity.SpiceRackEntity;
import dev.persn.feast.block.entity.StoveEntity;
import dev.persn.feast.item.ModItems;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Property;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class StoveBlock extends BlockWithEntity {
    public static final EnumProperty<Direction> FACING;

    public StoveBlock(Settings settings) {
        super(settings);
        this.setDefaultState((BlockState)((BlockState)this.stateManager.getDefaultState()).with(FACING, Direction.NORTH));
    }

    @Override
    protected MapCodec<? extends StoveBlock> getCodec() {
        return createCodec(StoveBlock::new);
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
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!world.isClient) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof StoveEntity stoveBlockEntity) {
                Feast.LOGGER.info("Stove block entity found");
                if(player.getMainHandStack().getItem() == ModItems.STOVE_POT && stoveBlockEntity.getInventory().isEmpty()) {
                    stoveBlockEntity.getInventory().setStack(0, player.getMainHandStack().copy());
                    if(!player.getAbilities().creativeMode) player.getMainHandStack().decrement(1);


                    stoveBlockEntity.markDirty();
                    world.updateListeners(pos, state, state, 0);
                    return ActionResult.SUCCESS;
                }
            }
        }
        return ActionResult.SUCCESS;
    }

    @Override
    protected void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.hasBlockEntity() && !state.isOf(newState.getBlock())) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if(blockEntity instanceof StoveEntity stoveEntity) {
                ItemScatterer.spawn(world, pos, stoveEntity.getInventory());
                world.updateComparators(pos, this);
                world.removeBlockEntity(pos);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    public static final VoxelShape WEST_SHAPE = Stream.of(
            Block.createCuboidShape(0, 0, 2, 2, 9, 16),
            Block.createCuboidShape(2, 0, 15, 14, 9, 16),
            Block.createCuboidShape(0, 9, 2, 14, 14, 16),
            Block.createCuboidShape(14, 0, 2, 16, 14, 16),
            Block.createCuboidShape(2, 0, 1, 14, 9, 2),
            Block.createCuboidShape(2, 11, 1, 14, 14, 2),
            Block.createCuboidShape(3, 9, 0, 4, 10, 2),
            Block.createCuboidShape(12, 9, 0, 13, 10, 2),
            Block.createCuboidShape(4, 9, 0, 12, 10, 1),
            Block.createCuboidShape(0, 14, 14, 16, 16, 16),
            Block.createCuboidShape(4, 14, 4, 12, 15, 12),
            Block.createCuboidShape(3, 14, 5, 4, 15, 11),
            Block.createCuboidShape(12, 14, 5, 13, 15, 11),
            Block.createCuboidShape(5, 14, 3, 11, 15, 4),
            Block.createCuboidShape(5, 14, 12, 11, 15, 13),
            Block.createCuboidShape(3, 12, 0.5, 4, 13, 1),
            Block.createCuboidShape(6, 12, 0.5, 7, 13, 1),
            Block.createCuboidShape(9, 12, 0.5, 10, 13, 1),
            Block.createCuboidShape(12, 12, 0.5, 13, 13, 1),
            Block.createCuboidShape(2, 0, 2, 14, 1, 15)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();


    // 90° rotation from WEST => NORTH
    public static final VoxelShape NORTH_SHAPE = Stream.of(
            Block.createCuboidShape(0, 0, 0, 14, 9, 2),
            Block.createCuboidShape(0, 0, 2, 1, 9, 14),
            Block.createCuboidShape(0, 9, 0, 14, 14, 14),
            Block.createCuboidShape(0, 0, 14, 14, 14, 16),
            Block.createCuboidShape(14, 0, 2, 15, 9, 14),
            Block.createCuboidShape(14, 11, 2, 15, 14, 14),
            Block.createCuboidShape(14, 9, 3, 16, 10, 4),
            Block.createCuboidShape(14, 9, 12, 16, 10, 13),
            Block.createCuboidShape(15, 9, 4, 16, 10, 12),
            Block.createCuboidShape(0, 14, 0, 2, 16, 16),
            Block.createCuboidShape(4, 14, 4, 12, 15, 12),
            Block.createCuboidShape(5, 14, 3, 11, 15, 4),
            Block.createCuboidShape(5, 14, 12, 11, 15, 13),
            Block.createCuboidShape(12, 14, 5, 13, 15, 11),
            Block.createCuboidShape(3, 14, 5, 4, 15, 11),
            Block.createCuboidShape(15, 12, 3, 15.5, 13, 4),
            Block.createCuboidShape(15, 12, 6, 15.5, 13, 7),
            Block.createCuboidShape(15, 12, 9, 15.5, 13, 10),
            Block.createCuboidShape(15, 12, 12, 15.5, 13, 13),
            Block.createCuboidShape(1, 0, 2, 14, 1, 14)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();

    // 180° rotation from WEST => EAST
    public static final VoxelShape EAST_SHAPE = Stream.of(
            Block.createCuboidShape(14, 0, 0, 16, 9, 14),
            Block.createCuboidShape(2, 0, 0, 14, 9, 1),
            Block.createCuboidShape(2, 9, 0, 16, 14, 14),
            Block.createCuboidShape(0, 0, 0, 2, 14, 14),
            Block.createCuboidShape(2, 0, 14, 14, 9, 15),
            Block.createCuboidShape(2, 11, 14, 14, 14, 15),
            Block.createCuboidShape(12, 9, 14, 13, 10, 16),
            Block.createCuboidShape(3, 9, 14, 4, 10, 16),
            Block.createCuboidShape(4, 9, 15, 12, 10, 16),
            Block.createCuboidShape(0, 14, 0, 16, 16, 2),
            Block.createCuboidShape(4, 14, 4, 12, 15, 12),
            Block.createCuboidShape(12, 14, 5, 13, 15, 11),
            Block.createCuboidShape(3, 14, 5, 4, 15, 11),
            Block.createCuboidShape(5, 14, 12, 11, 15, 13),
            Block.createCuboidShape(5, 14, 3, 11, 15, 4),
            Block.createCuboidShape(12, 12, 15, 13, 13, 15.5),
            Block.createCuboidShape(9, 12, 15, 10, 13, 15.5),
            Block.createCuboidShape(6, 12, 15, 7, 13, 15.5),
            Block.createCuboidShape(3, 12, 15, 4, 13, 15.5),
            Block.createCuboidShape(2, 0, 1, 14, 1, 14)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();

    // 270° rotation from WEST => SOUTH
    public static final VoxelShape SOUTH_SHAPE = Stream.of(
            Block.createCuboidShape(2, 0, 14, 16, 9, 16),
            Block.createCuboidShape(15, 0, 2, 16, 9, 14),
            Block.createCuboidShape(2, 9, 2, 16, 14, 16),
            Block.createCuboidShape(2, 0, 0, 16, 14, 2),
            Block.createCuboidShape(1, 0, 2, 2, 9, 14),
            Block.createCuboidShape(1, 11, 2, 2, 14, 14),
            Block.createCuboidShape(0, 9, 12, 2, 10, 13),
            Block.createCuboidShape(0, 9, 3, 2, 10, 4),
            Block.createCuboidShape(0, 9, 4, 1, 10, 12),
            Block.createCuboidShape(14, 14, 0, 16, 16, 16),
            Block.createCuboidShape(4, 14, 4, 12, 15, 12),
            Block.createCuboidShape(5, 14, 12, 11, 15, 13),
            Block.createCuboidShape(5, 14, 3, 11, 15, 4),
            Block.createCuboidShape(3, 14, 5, 4, 15, 11),
            Block.createCuboidShape(12, 14, 5, 13, 15, 11),
            Block.createCuboidShape(0.5, 12, 12, 1, 13, 13),
            Block.createCuboidShape(0.5, 12, 9, 1, 13, 10),
            Block.createCuboidShape(0.5, 12, 6, 1, 13, 7),
            Block.createCuboidShape(0.5, 12, 3, 1, 13, 4),
            Block.createCuboidShape(2, 0, 2, 15, 1, 14)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction facing = state.get(FACING);

        // If your block only uses horizontal directions,
        // we handle NORTH, SOUTH, EAST, WEST (no up/down).
        return switch (facing) {
            case WEST -> WEST_SHAPE;
            case NORTH -> NORTH_SHAPE;
            case EAST -> EAST_SHAPE;
            case SOUTH -> SOUTH_SHAPE;
            default -> WEST_SHAPE; // fallback
        };
    }


    static {
        FACING = HorizontalFacingBlock.FACING;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new StoveEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, ModBlocks.STOVE_ENTITY, world.isClient() ? StoveEntity::clientTick : StoveEntity::serverTick);
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }
}
