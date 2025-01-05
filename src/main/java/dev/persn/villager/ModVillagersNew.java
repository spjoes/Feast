package dev.persn.villager;


import com.google.common.collect.ImmutableSet;
import dev.persn.Feast;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.poi.PointOfInterestType;

public class ModVillagersNew {
    public static final RegistryKey<PointOfInterestType> DECORATED_POT_POI_KEY = poiKey("decorated_pot");
    public static final PointOfInterestType DECORATED_POT_POI = registerPoi("decorated_pot", Blocks.DECORATED_POT);

    public static final VillagerProfession FARAWAY_TRAVELER = registerProfession("faraway_traveler", DECORATED_POT_POI_KEY, SoundEvents.BLOCK_WOOL_PLACE);

    private static VillagerProfession registerProfession(String name, RegistryKey<PointOfInterestType> type, SoundEvent sound) {

        Feast.LOGGER.info("Registering profession: " + name);

        return Registry.register(Registries.VILLAGER_PROFESSION, Identifier.of(Feast.MOD_ID, name),
                new VillagerProfession(name, entry -> entry.matchesKey(type), entry -> entry.matchesKey(type),
                        ImmutableSet.of(), ImmutableSet.of(), sound));
    }

    private static PointOfInterestType registerPoi(String name, Block block) {
        return PointOfInterestHelper.register(Identifier.of(Feast.MOD_ID, name), 1, 1, block);
    }

    private static RegistryKey<PointOfInterestType> poiKey(String name) {
        return RegistryKey.of(RegistryKeys.POINT_OF_INTEREST_TYPE, Identifier.of(Feast.MOD_ID, name));
    }

    public static void registerVillagers() {

    }
}
