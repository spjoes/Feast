package dev.persn.feast.item;

import dev.persn.feast.Feast;
import dev.persn.feast.block.ModBlocks;
import dev.persn.feast.components.ModComponents;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.function.Function;

public class ModItems {

    //Food Components
    public static final FoodComponent RICE_FOOD_COMPONENT = new FoodComponent.Builder()
            .nutrition(2)
            .saturationModifier(0.3F)
            .build();
    public static final FoodComponent SUSHI_FOOD_COMPONENT = new FoodComponent.Builder()
            .nutrition(6)
            .saturationModifier(0.8F)
            .build();
    public static final FoodComponent SHRIMP_FOOD_COMPONENT = new FoodComponent.Builder()
            .nutrition(2)
            .saturationModifier(0.1F)
            .build();
    public static final FoodComponent COOKED_SHRIMP_FOOD_COMPONENT = new FoodComponent.Builder()
            .nutrition(5)
            .saturationModifier(0.6F)
            .build();

    //Items
    public static final Item SUSHI_ITEM = registerItem("sushi", Item::new, new Item.Settings().food(SUSHI_FOOD_COMPONENT));
    public static final Item SHRIMP_ITEM = registerItem("shrimp", Item::new, new Item.Settings().food(SHRIMP_FOOD_COMPONENT));
    public static final Item COOKED_SHRIMP_ITEM = registerItem("cooked_shrimp", Item::new, new Item.Settings().food(COOKED_SHRIMP_FOOD_COMPONENT));
    public static final Item STOVE_POT = registerItem("stove_pot", StovePotItem::new, new Item.Settings().component(ModComponents.HAS_WATER, false).component(ModComponents.INGREDIENTS, List.of(Items.COOKED_COD.getDefaultStack())));

    //Block Items
    public static final Item RICE_CROP_ITEM = registerItem("rice_crop", settings -> new BlockItem(ModBlocks.RICE_CROP, settings), new Item.Settings().food(RICE_FOOD_COMPONENT));
    public static final Item CUTTING_BOARD_ITEM = registerItem("cutting_board", settings -> new BlockItem(ModBlocks.CUTTING_BOARD, settings), new Item.Settings());
    public static final Item STOVE_ITEM = registerItem("stove", settings -> new BlockItem(ModBlocks.STOVE, settings), new Item.Settings());
    public static final Item SPICE_RACK_ITEM = registerItem("spice_rack", settings -> new BlockItem(ModBlocks.SPICE_RACK, settings), new Item.Settings());


    public static Item registerItem(String name, Function<Item.Settings, Item> factory, Item.Settings settings) {
        final RegistryKey<Item> registerKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Feast.MOD_ID, name));
        return Items.register(registerKey, factory, settings);
    }

    private static void customFoodAndDrink(FabricItemGroupEntries entries) {
        // Add custom ingredients to the "Food & Drink" tab
        entries.add(RICE_CROP_ITEM);
        entries.add(SUSHI_ITEM);
        entries.add(SHRIMP_ITEM);
        entries.add(COOKED_SHRIMP_ITEM);
    }
    private static void customFunctional(FabricItemGroupEntries entries) {
        // Add custom ingredients to the "Functional" tab
        entries.add(CUTTING_BOARD_ITEM);
        entries.add(STOVE_ITEM);
        entries.add(STOVE_POT);
        entries.add(SPICE_RACK_ITEM);
    }

    public static void registerModItems() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(ModItems::customFoodAndDrink);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(ModItems::customFunctional);
    }

}
