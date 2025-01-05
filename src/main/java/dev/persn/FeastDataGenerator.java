package dev.persn;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import dev.persn.datagen.ModPoiTagProvider;

public class FeastDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {

        Feast.LOGGER.info("Hello from the data generator!");

        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(ModPoiTagProvider::new);
    }
}
