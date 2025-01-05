package dev.persn.util;

import dev.persn.item.ModItems;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.entry.ItemEntry;
import org.apache.commons.lang3.mutable.MutableBoolean;

public class ModLootTableModifiers {

    public static void modifyLootTables() {
        LootTableEvents.MODIFY.register((key, tableBuilder, lootTableSource, lookup) -> {
            if(lootTableSource.isBuiltin() && key.equals(LootTables.FISHING_FISH_GAMEPLAY)) {
                var canModify = new MutableBoolean(true);
                tableBuilder.modifyPools(lpb -> {
                    if (canModify.booleanValue()) {
                        canModify.setFalse();
                    } else {
                        return;
                    }
                    lpb.with(ItemEntry.builder(ModItems.SHRIMP_ITEM).weight(40).build());
                });
            }
        });
    }
}
