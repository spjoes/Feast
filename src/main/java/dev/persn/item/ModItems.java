package dev.persn.item;

import dev.persn.Feast;
import dev.persn.block.ModBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public class ModItems {

    //Items
    public static final FoodComponent RICE_FOOD_COMPONENT = new FoodComponent.Builder()
            .nutrition(2)
            .saturationModifier(0.3F)
            .build();
    public static final FoodComponent SUSHI_FOOD_COMPONENT = new FoodComponent.Builder()
            .nutrition(6)
            .saturationModifier(0.8F)
            .build();

    public static final Item SUSHI_ITEM = registerItem("sushi", Item::new, new Item.Settings().food(SUSHI_FOOD_COMPONENT));

    //Block Items
    public static final Item RICE_CROP_ITEM = registerItem("rice_crop", settings -> new BlockItem(ModBlocks.RICE_CROP, settings), new Item.Settings().food(RICE_FOOD_COMPONENT));
    public static final Item CUTTING_BOARD_ITEM = registerItem("cutting_board", settings -> new BlockItem(ModBlocks.CUTTING_BOARD, settings), new Item.Settings());



    public static Item registerItem(String name, Function<Item.Settings, Item> factory, Item.Settings settings) {
        final RegistryKey<Item> registerKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Feast.MOD_ID, name));
        return Items.register(registerKey, factory, settings);
    }

    private static void customFoodAndDrink(FabricItemGroupEntries entries) {
        // Add custom ingredients to the ingredients tab
        entries.add(RICE_CROP_ITEM);
        entries.add(SUSHI_ITEM);
    }
    private static void customBuildingBlocks(FabricItemGroupEntries entries) {
        // Add custom ingredients to the ingredients tab
        entries.add(CUTTING_BOARD_ITEM);
    }

    public static void registerModItems() {
        // Register items here
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(ModItems::customFoodAndDrink);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(ModItems::customBuildingBlocks);
    }

}
