package dev.persn.feast.data;

import dev.persn.feast.Feast;
import dev.persn.feast.HasWaterProperty;
import dev.persn.feast.item.ModItems;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.*;
import net.minecraft.client.render.item.model.ItemModel;
import net.minecraft.client.render.item.property.bool.BundleHasSelectedItemProperty;
import net.minecraft.client.render.item.tint.PotionTintSource;
import net.minecraft.client.render.item.tint.TintSource;
import net.minecraft.data.DataWriter;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public class StovePotItemModelGenerator extends FabricModelProvider {
    public final FabricDataOutput output;

    public StovePotItemModelGenerator(FabricDataOutput output) {
        super(output);
        this.output = output;

    }

    @Override
    public CompletableFuture<?> run(DataWriter writer) {
        return super.run(writer);
    }

//    @Override
//    public void generateItemModels(Item item) {
//        Identifier modelId = ModelIds.getItemModelId(item);
//        ItemModel.Unbaked unbaked = ItemModels.basic(modelId);
//        ItemModel.Unbaked unbaked2 = ItemModels.basic(Identifier.of(Feast.MOD_ID, "item/stove_pot_open"));
//        //condition(property, onTrue, onFalse)
//        ItemModel.Unbaked unbaked3 = ItemModels.condition(new HasWaterProperty(), unbaked2, unbaked);
//
//        this.output.accept(item, unbaked3);
//    }


    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        Item item = ModItems.STOVE_POT;
        Identifier modelId = ModelIds.getItemModelId(item);
        ItemModel.Unbaked unbaked = ItemModels.basic(modelId);
        ItemModel.Unbaked unbaked2 = ItemModels.tinted(Identifier.of(Feast.MOD_ID, "item/stove_pot_filled"), new TintSource[]{new PotionTintSource()});



        //condition(property, onTrue, onFalse)
        ItemModel.Unbaked unbaked3 = ItemModels.condition(new HasWaterProperty(), unbaked2, unbaked);

        itemModelGenerator.output.accept(item, unbaked3);
//        itemModelGenerator.output.accept(item, ItemModels.tinted(Identifier.of(Feast.MOD_ID, "item/stove_pot_filled"), new TintSource[]{new PotionTintSource()}));
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
    }

}
