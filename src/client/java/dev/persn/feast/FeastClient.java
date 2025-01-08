package dev.persn.feast;

import dev.persn.feast.block.ModBlocks;
import dev.persn.feast.entityRenderer.CuttingBoardEntityRenderer;
import dev.persn.feast.entityRenderer.SpiceRackEntityRenderer;
import dev.persn.feast.entityRenderer.StoveEntityRenderer;
import dev.persn.feast.gui.screen.SpiceRackScreen;
import dev.persn.feast.item.ModItems;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

@Environment(EnvType.CLIENT)
public class FeastClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.RICE_CROP, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.STOVE, RenderLayer.getTranslucent());

		BlockEntityRendererFactories.register(ModBlocks.STOVE_ENTITY, StoveEntityRenderer::new);
		BlockEntityRendererFactories.register(ModBlocks.CUTTING_BOARD_ENTITY, CuttingBoardEntityRenderer::new);
		BlockEntityRendererFactories.register(ModBlocks.SPICE_RACK_ENTITY, SpiceRackEntityRenderer::new);

		HandledScreens.register(Feast.SPICE_RACK_SCREEN_HANDLER, SpiceRackScreen::new);

		HasWaterProperty.register();
	}
}