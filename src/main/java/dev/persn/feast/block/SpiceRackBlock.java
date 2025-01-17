package dev.persn.feast.block;

import com.mojang.serialization.MapCodec;
import dev.persn.feast.Feast;
import dev.persn.feast.block.entity.SpiceRackEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
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

public class SpiceRackBlock extends BlockWithEntity {
    public static final EnumProperty<Direction> FACING;

    public SpiceRackBlock(Settings settings) {
        super(settings);
        this.setDefaultState((BlockState)((BlockState)this.stateManager.getDefaultState()).with(FACING, Direction.NORTH));
    }

    @Override
    protected MapCodec<? extends SpiceRackBlock> getCodec() { return createCodec(SpiceRackBlock::new); }

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
            NamedScreenHandlerFactory screenHandlerFactory = state.createScreenHandlerFactory(world, pos);
            if (screenHandlerFactory != null) {
                player.openHandledScreen(screenHandlerFactory);
            }
        }
        return ActionResult.SUCCESS;
    }

    @Override
    protected void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.hasBlockEntity() && !state.isOf(newState.getBlock())) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if(blockEntity instanceof SpiceRackEntity spiceRackEntity) {
                ItemScatterer.spawn(world, pos, spiceRackEntity.getInventory());
                world.updateComparators(pos, this);
                world.removeBlockEntity(pos);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }


    @Override
    protected boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    @Override
    protected int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return ScreenHandler.calculateComparatorOutput(world.getBlockEntity(pos));
    }

    public static final VoxelShape WEST_SHAPE = Stream.of(
            Block.createCuboidShape(2, 0, 9, 14, 2, 16),
            Block.createCuboidShape(0, 14, 9, 16, 16, 16),
            Block.createCuboidShape(0, 0, 9, 2, 14, 16),
            Block.createCuboidShape(14, 0, 9, 16, 14, 16),
            Block.createCuboidShape(2, 7, 9, 14, 9, 16)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();


    // 90° rotation from WEST => NORTH
    public static final VoxelShape NORTH_SHAPE = Stream.of(
            Block.createCuboidShape(0, 0, 2, 7, 2, 14),
            Block.createCuboidShape(0, 14, 0, 7, 16, 16),
            Block.createCuboidShape(0, 0, 0, 7, 14, 2),
            Block.createCuboidShape(0, 0, 14, 7, 14, 16),
            Block.createCuboidShape(0, 7, 2, 7, 9, 14)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();

    // 180° rotation from WEST => EAST
    public static final VoxelShape EAST_SHAPE = Stream.of(
            Block.createCuboidShape(2, 0, 0, 14, 2, 7),
            Block.createCuboidShape(0, 14, 0, 16, 16, 7),
            Block.createCuboidShape(14, 0, 0, 16, 14, 7),
            Block.createCuboidShape(0, 0, 0, 2, 14, 7),
            Block.createCuboidShape(2, 7, 0, 14, 9, 7)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();

    // 270° rotation from WEST => SOUTH
    public static final VoxelShape SOUTH_SHAPE = Stream.of(
            Block.createCuboidShape(9, 0, 2, 16, 2, 14),
            Block.createCuboidShape(9, 14, 0, 16, 16, 16),
            Block.createCuboidShape(9, 0, 14, 16, 14, 16),
            Block.createCuboidShape(9, 0, 0, 16, 14, 2),
            Block.createCuboidShape(9, 7, 2, 16, 9, 14)
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
        return new SpiceRackEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, ModBlocks.SPICE_RACK_ENTITY, world.isClient() ? SpiceRackEntity::clientTick : SpiceRackEntity::serverTick);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }
}
