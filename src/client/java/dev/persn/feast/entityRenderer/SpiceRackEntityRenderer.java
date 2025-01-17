package dev.persn.feast.entityRenderer;


import dev.persn.feast.block.entity.SpiceRackEntity;
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
import net.minecraft.item.ModelTransformationMode;
import net.minecraft.util.math.RotationAxis;

@Environment(EnvType.CLIENT)
public class SpiceRackEntityRenderer implements BlockEntityRenderer<SpiceRackEntity> {

    public SpiceRackEntityRenderer(BlockEntityRendererFactory.Context ctx) {}

    @Override
    public void render(SpiceRackEntity blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        for (int i = 0; i < blockEntity.getInventory().size(); i++) {
            //get item from inventory
            ItemStack stack = blockEntity.getInventory().getStack(i);

            matrices.push();

            //down scale
            matrices.scale(0.5f, 0.5f, 0.5f);

            //row should be 1 for the first 3 items and 2 for the next 3
            int row = i < 3 ? 1 : 2;
            double paddingBetweenRows = -0.875;

            //this but the x value needs to "reset" after 3 items
            matrices.translate(1.4 - (0.45*(i % 3)), 1.225 + (paddingBetweenRows * (row - 1)), 1.65f);


            int lightAbove = WorldRenderer.getLightmapCoordinates(blockEntity.getWorld(), blockEntity.getPos().up());

            MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformationMode.GROUND, lightAbove, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, blockEntity.getWorld(), 0);

            // Mandatory call after GL calls
            matrices.pop();
        }
    }
}
