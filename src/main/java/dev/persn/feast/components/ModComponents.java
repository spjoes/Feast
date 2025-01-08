package dev.persn.feast.components;

import com.mojang.serialization.Codec;
import dev.persn.feast.Feast;
import net.minecraft.component.ComponentType;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.List;

public class ModComponents {

    public static final ComponentType<Boolean> HAS_WATER = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(Feast.MOD_ID, "has_water"),
            ComponentType.<Boolean>builder().codec(Codec.BOOL).build()
    );
    //ItemStack ingredients java LIST
    public static final ComponentType<List<ItemStack>> INGREDIENTS = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(Feast.MOD_ID, "ingredients"),
            ComponentType.<List<ItemStack>>builder().codec(Codec.list(ItemStack.CODEC)).build()
    );

    public static void initComponents() {
        Feast.LOGGER.info("Registering {} components", Feast.MOD_ID);
        // Technically this method can stay empty, but some developers like to notify
        // the console, that certain parts of the mod have been successfully initialized
    }
}
