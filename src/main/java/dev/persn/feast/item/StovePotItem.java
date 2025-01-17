package dev.persn.feast.item;

import dev.persn.feast.Feast;
import dev.persn.feast.components.ModComponents;
import net.minecraft.block.Blocks;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.potion.Potions;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.List;

public class StovePotItem extends Item {

    public StovePotItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        BlockHitResult blockHitResult = raycast(world, user, RaycastContext.FluidHandling.SOURCE_ONLY);
        Feast.LOGGER.info("Use on block");
        if (blockHitResult.getType() == HitResult.Type.BLOCK) {
            BlockPos blockPos = blockHitResult.getBlockPos();
            if (!world.canPlayerModifyAt(user, blockPos)) {
                return ActionResult.PASS;
            }

            if (world.getFluidState(blockPos).isIn(FluidTags.WATER)) {
                world.playSound(user, user.getX(), user.getY(), user.getZ(), SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
                world.emitGameEvent(user, GameEvent.FLUID_PICKUP, blockPos);

                ItemStack stack = user.getStackInHand(hand);
                ItemStack newStack = new ItemStack(stack.getItem());
                newStack.set(ModComponents.HAS_WATER, true);
                user.giveOrDropStack(newStack);
                stack.decrementUnlessCreative(1, user);

                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.PASS;
    }

    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        boolean has_water = stack.getOrDefault(ModComponents.HAS_WATER, false);

        List<ItemStack> ingredients = stack.getOrDefault(ModComponents.INGREDIENTS, List.of());
        if (!ingredients.isEmpty()) {
            tooltip.add(Text.translatable("tooltip.feast.ingredients").formatted(Formatting.GRAY));
            for (ItemStack ingredient : ingredients) {
                tooltip.add(Text.literal("  " + ingredient.getName().getString()).formatted(Formatting.GRAY));
            }
        }
        tooltip.add(has_water ? Text.translatable("tooltip.feast.has_water.full").formatted(Formatting.GREEN) : Text.translatable("tooltip.feast.has_water.empty").formatted(Formatting.RED));
    }
}
