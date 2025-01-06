package dev.persn;

import dev.persn.block.ModBlocks;
import dev.persn.item.ModItems;
import dev.persn.util.ModLootTableModifiers;
import dev.persn.villager.ModTrades;
import dev.persn.villager.ModVillagersNew;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

public class Feast implements ModInitializer {
	public static final String MOD_ID = "feast";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

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
	}
}

