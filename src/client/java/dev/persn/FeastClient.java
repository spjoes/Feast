package dev.persn;

import dev.persn.block.ModBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class FeastClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.RICE_CROP, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.STOVE, RenderLayer.getTranslucent());

		BlockEntityRendererFactories.register(ModBlocks.STOVE_ENTITY, StoveEntityRenderer::new);
		BlockEntityRendererFactories.register(ModBlocks.CUTTING_BOARD_ENTITY, CuttingBoardEntityRenderer::new);

	}
}