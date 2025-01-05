package dev.persn.villager;

import dev.persn.Feast;
import dev.persn.item.ModItems;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradedItem;

public class ModTrades {
    private static final float LOW_PRICE_MULTIPLER = 0.05F;
    private static final float HIGH_PRICE_MULTIPLER = 0.2F;

    public static void registerTrades() {
        Feast.LOGGER.info("Registering trades");
        registerFarawayTraderTrades();
    }

    private static void registerFarawayTraderTrades() {
        TradeOfferHelper.registerVillagerOffers(ModVillagersNew.FARAWAY_TRAVELER, 1, factories -> {
            factories.add((entity, random) -> new TradeOffer(
                    new TradedItem(Items.EMERALD, 1),
                    new ItemStack(ModItems.RICE_CROP_ITEM, 4),
                    5, 2, LOW_PRICE_MULTIPLER
            ));
        });
        TradeOfferHelper.registerVillagerOffers(ModVillagersNew.FARAWAY_TRAVELER, 2, factories -> {
            factories.add((entity, random) -> new TradeOffer(
                    new TradedItem(Items.EMERALD, 1),
                    new ItemStack(ModItems.RICE_CROP_ITEM, 6),
                    5, 2, LOW_PRICE_MULTIPLER
            ));
        });

        TradeOfferHelper.registerVillagerOffers(ModVillagersNew.FARAWAY_TRAVELER, 3, factories -> {
            factories.add((entity, random) -> new TradeOffer(
                    new TradedItem(Items.EMERALD, 1),
                    new ItemStack(ModItems.RICE_CROP_ITEM, 8),
                    5, 2, LOW_PRICE_MULTIPLER
            ));
        });

        TradeOfferHelper.registerVillagerOffers(ModVillagersNew.FARAWAY_TRAVELER, 4, factories -> {
            factories.add((entity, random) -> new TradeOffer(
                    new TradedItem(Items.EMERALD, 1),
                    new ItemStack(ModItems.SUSHI_ITEM, 2),
                    5, 2, LOW_PRICE_MULTIPLER
            ));
        });

        TradeOfferHelper.registerVillagerOffers(ModVillagersNew.FARAWAY_TRAVELER, 5, factories -> {
            factories.add((entity, random) -> new TradeOffer(
                    new TradedItem(Items.EMERALD, 1),
                    new ItemStack(ModItems.SUSHI_ITEM, 4),
                    5, 2, LOW_PRICE_MULTIPLER
            ));
        });
    }
}
