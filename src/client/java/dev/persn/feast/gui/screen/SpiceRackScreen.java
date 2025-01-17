package dev.persn.feast.gui.screen;

import dev.persn.feast.Feast;
import dev.persn.feast.gui.SpiceRackScreenHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class SpiceRackScreen extends HandledScreen<SpiceRackScreenHandler> {

    private static final Identifier TEXTURE = Identifier.of(Feast.MOD_ID,"textures/gui/container/spice_rack.png");

    public SpiceRackScreen(SpiceRackScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    // These values are taken from HandledScreen.class and are used here
    protected int backgroundWidth = 176;
    protected int backgroundHeight = 166;

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int i = (this.width - this.backgroundWidth) / 2;
        int j = (this.height - this.backgroundHeight) / 2;
        context.drawTexture(RenderLayer::getGuiTextured, TEXTURE, i, j, 0.0F, 0.0F, this.backgroundWidth, this.backgroundHeight, 256, 256);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        MatrixStack matrices = context.getMatrices();
        float scale = 0.85f;
        matrices.push();
        matrices.scale(scale, scale, 1.0f); // Apply scaling


//        context.drawText(this.textRenderer, this.playerInventoryTitle, this.playerInventoryTitleX, this.playerInventoryTitleY, 4210752, false);
        context.drawText(this.textRenderer, this.playerInventoryTitle, 5, this.backgroundHeight - 80, 4210752, false); //scale this text to 0.75
        //scale this text to 0.75

        matrices.pop();
    }

    @Override
    protected void init() {
        super.init();
    }
}
