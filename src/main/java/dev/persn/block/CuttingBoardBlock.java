package dev.persn.block;

import com.mojang.serialization.MapCodec;
import dev.persn.block.entity.CuttingBoardEntity;
import dev.persn.item.ModItems;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Property;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class CuttingBoardBlock extends BlockWithEntity {
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
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!world.isClient) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof CuttingBoardEntity cuttingBoardBlockEntity) {

                // SHIFT-RIGHT-CLICK --> Try to craft
                if (player.isSneaking()) {
                    // 1) Gather items on the board
                    Map<Item, Integer> boardItems = collectItems(cuttingBoardBlockEntity);

                    // 2) Compare with our single recipe: kelp + cod + log -> dirt
                    Map<Item, Integer> requiredItems = new HashMap<>();
                    requiredItems.put(Items.DRIED_KELP, 1);
                    requiredItems.put(Items.COOKED_COD, 1);
                    requiredItems.put(ModItems.RICE_CROP_ITEM, 1);

                    // 3) Check if board matches the recipe exactly
                    if (matchesRecipe(boardItems, requiredItems)) {
                        // If so, give dirt
                        giveItemOrDrop(world, player, new ItemStack(ModItems.SUSHI_ITEM, 1));
                        player.sendMessage(Text.of("You made an item!"), true);

                        // Clear the board
                        clearBoard(cuttingBoardBlockEntity);
                    } else {
                        player.sendMessage(Text.of("Recipe didn't match."), true);
                    }

                    cuttingBoardBlockEntity.markDirty();
                    world.updateListeners(pos, state, state, 3);
                    return ActionResult.SUCCESS;

                    // NORMAL RIGHT-CLICK --> Place item
                } else {
                    // If no item in hand, do nothing
                    if (player.getMainHandStack().isEmpty()) {
                        return ActionResult.SUCCESS;
                    }

                    // Place the item on the board
                    cuttingBoardBlockEntity.inventory.set(
                            cuttingBoardBlockEntity.number,
                            player.getMainHandStack().getItem().getDefaultStack()
                    );

                    // Decrement if not creative
                    if (!player.getAbilities().creativeMode) {
                        player.getMainHandStack().decrement(1);
                    }

                    cuttingBoardBlockEntity.number++;
                    player.sendMessage(Text.of("Item placed on board. Total: " + cuttingBoardBlockEntity.number), true);

                    // Mark dirty + update
                    cuttingBoardBlockEntity.markDirty();
                    world.updateListeners(pos, state, state, 3);

                    return ActionResult.SUCCESS;
                }
            }
        }

        return ActionResult.SUCCESS;
    }

    private Map<Item, Integer> collectItems(CuttingBoardEntity boardEntity) {
        Map<Item, Integer> map = new HashMap<>();
        for (ItemStack stack : boardEntity.inventory) {
            if (!stack.isEmpty()) {
                map.merge(stack.getItem(), stack.getCount(), Integer::sum);
            }
        }
        return map;
    }

    private boolean matchesRecipe(Map<Item, Integer> boardItems, Map<Item, Integer> requiredItems) {
        // 1) Check if the total # of items on the board == total # of items in the recipe
        //    If there's more items on the board than needed or something extra, fail.
        int boardCount = boardItems.values().stream().mapToInt(i -> i).sum();
        int recipeCount = requiredItems.values().stream().mapToInt(i -> i).sum();
        if (boardCount != recipeCount) {
            return false;
        }

        // 2) Check that for every required item, the board has at least that many
        for (Map.Entry<Item, Integer> entry : requiredItems.entrySet()) {
            Item item = entry.getKey();
            int requiredCount = entry.getValue();
            int boardCountForItem = boardItems.getOrDefault(item, 0);

            if (boardCountForItem < requiredCount) {
                return false;
            }
        }

        return true;
    }

    private void clearBoard(CuttingBoardEntity boardEntity) {
        // Clear the entire inventory
        for (int i = 0; i < boardEntity.inventory.size(); i++) {
            boardEntity.inventory.set(i, ItemStack.EMPTY);
        }
        boardEntity.number = 0; // reset the "next index"
        boardEntity.markDirty();
    }

    private void giveItemOrDrop(World world, PlayerEntity player, ItemStack stack) {
        // Try to put it in player inventory. If no space, drop in world.
        if (!player.getInventory().insertStack(stack)) {
            player.dropItem(stack, false);
        }
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

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CuttingBoardEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, ModBlocks.CUTTING_BOARD_ENTITY, world.isClient() ? CuttingBoardEntity::clientTick : CuttingBoardEntity::serverTick);
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }
}
