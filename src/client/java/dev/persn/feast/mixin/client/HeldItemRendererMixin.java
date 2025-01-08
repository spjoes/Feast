package dev.persn.feast.mixin.client;

import dev.persn.feast.item.ModItems;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemRenderer.class)
public abstract class HeldItemRendererMixin {

    /**
     * Shadows so we can call or access these from within the Mixin.
     */
    @Shadow @Final private MinecraftClient client;

    // If Yarn changes method names or access, you might need to adapt these:
    @Shadow
    private void renderArm(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, Arm arm) {
        throw new AssertionError("Mixin shadow failed to apply: renderArm not found!");
    }

    /**
     * This is the inject target: the main method that draws first-person items.
     * We'll check if the item is our STOVE_POT. If so, we cancel vanilla and do
     * our custom "map-like" rendering.
     */
    @Inject(
            method = "renderFirstPersonItem(Lnet/minecraft/client/network/AbstractClientPlayerEntity;F" +
                    "FLnet/minecraft/util/Hand;FLnet/minecraft/item/ItemStack;FLnet/minecraft/client/util/math/MatrixStack;" +
                    "Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void feast_renderStovePotLikeMap(
            AbstractClientPlayerEntity player,
            float tickDelta,
            float pitch,
            Hand hand,
            float swingProgress,
            ItemStack itemStack,
            float equipProgress,
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            int light,
            CallbackInfo ci
    ) {
        // 1) Check if it’s our STOVE_POT item
        if (itemStack.isOf(ModItems.STOVE_POT)) {
            // 2) Cancel vanilla's normal item rendering
            ci.cancel();

            // 3) Do a “map-like” two-arm render, but substituting our pot
            //    so it stays mostly out of view unless you look down
            this.renderStovePotLikeMap(player, pitch, equipProgress, swingProgress,
                    itemStack, matrices, vertexConsumers, light);
        }
    }

    /**
     * Mimics vanilla's "renderMapInBothHands(...)" logic so that the pot
     * is held low/out-of-sight, tilting into view as you look down.
     */
    private void renderStovePotLikeMap(
            AbstractClientPlayerEntity player,
            float pitch,         // player's pitch
            float equipProgress,
            float swingProgress,
            ItemStack stovePot,
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            int light
    ) {
        matrices.push();

        // A) We replicate the logic from HeldItemRenderer#renderMapInBothHands
        //    where it calculates "mapAngle" to tilt the item out of view.

        float angle = this.getMapLikeAngle(pitch);
        // This is the key that shifts the pot mostly out of view
        // until you tilt your camera down.

        // Some minor wobbles from swinging animation
        float sqrtSwing = MathHelper.sqrt(swingProgress);
        float sinSwing  = -0.2F * MathHelper.sin(swingProgress * (float)Math.PI);

        // B) Apply the same translation as maps do
        matrices.translate(0.0F, -sinSwing / 2.0F, 0.0F);
        matrices.translate(0.0F, 0.04F + equipProgress * -1.2F + angle * -0.5F, -0.72F);

        // C) Rotate downward based on the angle
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(angle * -85.0F));

        // D) Render the player's arms if they're not invisible
        if (!player.isInvisible()) {
            matrices.push();
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90.0F));
            // Right arm
            this.renderArm(matrices, vertexConsumers, light, Arm.RIGHT);
            // Left arm
            this.renderArm(matrices, vertexConsumers, light, Arm.LEFT);
            matrices.pop();
        }

        // E) Do more subtle bobbing based on swing
        float j = MathHelper.sin(sqrtSwing * (float)Math.PI);
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(j * 20.0F));

        // F) Enlarge the pot. Maps do "scale(2,2,2)" to draw bigger in the center
        matrices.scale(1.9F, 1.9F, 1.9F);

        // G) Now actually render the pot item
        matrices.push();

        matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(45.0F));
        //bring it down a bit
        matrices.translate(0.0, -0.05, 0.0);

        this.client.getItemRenderer().renderItem(
                player,
                stovePot,
                // FIRST_PERSON_RIGHT_HAND is fine, or you can use GUI to reduce bobbing
                net.minecraft.item.ModelTransformationMode.FIRST_PERSON_RIGHT_HAND,
                false, // leftHanded
                matrices,
                vertexConsumers,
                player.getWorld(),
                light,
                net.minecraft.client.render.OverlayTexture.DEFAULT_UV,
                0
        );
        matrices.pop();

        matrices.pop();
    }

    /**
     * This replicates the logic from HeldItemRenderer#getMapAngle(float tickDelta),
     * except in vanilla it calls the parameter 'tickDelta' but actually passes 'pitch'.
     *
     * If you want EXACT map behavior, just copy the method:
     *
     *   private float getMapAngle(float pitch) {
     *       float f = 1.0F - pitch / 45.0F + 0.1F;
     *       f = MathHelper.clamp(f, 0.0F, 1.0F);
     *       f = -MathHelper.cos(f * (float)Math.PI) * 0.5F + 0.5F;
     *       return f;
     *   }
     *
     * That means if pitch = 0 (looking straight), item is basically hidden.
     * If pitch = 90 (looking straight down), you see it fully.
     */
    private float getMapLikeAngle(float pitch) {
        // The map code uses "f = 1.0F - pitch / 45.0F + 0.1F;"
        // But note, if pitch is negative (looking up), we clamp at 0 anyway.

        float f = 1.0F - pitch / 45.0F + 0.1F;
        f = MathHelper.clamp(f, 0.0F, 1.0F);
        f = -MathHelper.cos(f * (float)Math.PI) * 0.5F + 0.5F;
        return f;
    }
}
