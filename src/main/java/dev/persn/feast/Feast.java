package dev.persn.feast;

import dev.persn.feast.block.ModBlocks;
import dev.persn.feast.components.ModComponents;
import dev.persn.feast.gui.SpiceRackScreenHandler;
import dev.persn.feast.item.ModItems;
import dev.persn.feast.util.ModLootTableModifiers;
import dev.persn.feast.villager.ModTrades;
import dev.persn.feast.villager.ModVillagersNew;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Feast implements ModInitializer {
	public static final String MOD_ID = "feast";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

public static final ScreenHandlerType<SpiceRackScreenHandler> SPICE_RACK_SCREEN_HANDLER =
		new ExtendedScreenHandlerType<>(
				// 'data' here is a BlockPos, not a PacketByteBuf!
				(syncId, playerInv, pos) -> new SpiceRackScreenHandler(syncId, playerInv, pos),
				BlockPos.PACKET_CODEC
		);


	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Hello Fabric world!");
		ModItems.registerModItems();
		ModVillagersNew.registerVillagers();
		ModTrades.registerTrades();
		ModLootTableModifiers.modifyLootTables();
		ModBlocks.initBlockEntities();
		ModComponents.initComponents();
		Registry.register(Registries.SCREEN_HANDLER, Identifier.of(MOD_ID, "spice_rack"), SPICE_RACK_SCREEN_HANDLER);
	}
}

