package dev.persn.feast.data;

import dev.persn.feast.Feast;
import dev.persn.feast.datagen.ModPoiTagProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.ItemModelOutput;
import net.minecraft.util.Identifier;

public class ModClientDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {

        final FabricDataGenerator.Pack pack = generator.createPack();
        pack.addProvider(StovePotItemModelGenerator::new);
        pack.addProvider(ModPoiTagProvider::new);
    }
}