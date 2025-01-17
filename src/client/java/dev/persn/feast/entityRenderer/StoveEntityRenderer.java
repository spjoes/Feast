package dev.persn.feast.entityRenderer;


import dev.persn.feast.Feast;
import dev.persn.feast.block.entity.StoveEntity;
import dev.persn.feast.components.ModComponents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ModelTransformationMode;
import net.minecraft.util.math.RotationAxis;

@Environment(EnvType.CLIENT)
public class StoveEntityRenderer implements BlockEntityRenderer<StoveEntity> {

    public StoveEntityRenderer(BlockEntityRendererFactory.Context ctx) {}

    @Override
    public void render(StoveEntity blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();
        // Calculate the current offset in the y value
        double offset = Math.sin((blockEntity.getWorld().getTime() + tickDelta) / 8.0) / 4.0;

        matrices.scale(3.0f, 3.0f, 3.0f);

        // Move the item
        matrices.translate((0.5 / 3), (0.8 / 3), (0.5 / 3));

        // Rotate the item
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((blockEntity.getWorld().getTime() + tickDelta) * 2));

        int lightAbove = WorldRenderer.getLightmapCoordinates(blockEntity.getWorld(), blockEntity.getPos().up());

        ItemStack stack;

        //check item components
        if(Boolean.TRUE.equals(blockEntity.getInventory().getStack(0).get(ModComponents.HAS_WATER))) {
//            Feast.LOGGER.info(blockEntity.inventory.getFirst().getItem().getComponents().toString());
            stack = new ItemStack(blockEntity.getInventory().getStack(0).getItem());
        } else {
            stack = new ItemStack(blockEntity.getInventory().getStack(0).getItem());
        }


        MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformationMode.GROUND, lightAbove, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, blockEntity.getWorld(), 0);

        // Mandatory call after GL calls
        matrices.pop();
    }
}
