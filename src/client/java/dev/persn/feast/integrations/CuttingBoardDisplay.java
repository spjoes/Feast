package dev.persn.feast.integrations;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.display.DisplaySerializer;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.recipe.RecipeEntry;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CuttingBoardDisplay extends BasicDisplay {
    public CuttingBoardDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs) {
        super(inputs, outputs);
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return null;
    }

    @Override
    public @Nullable DisplaySerializer<? extends Display> getSerializer() {
        return null;
    }
//    public CuttingBoardDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs) {
//        super(inputs, outputs);
//    }
//
//    public CuttingBoardDisplay(RecipeEntry<CuttingBoardRecipe> recipe) {
//        super(getInputList(recipe.value()), List.of(EntryIngredients.of(EntryStacks.of(recipe.value().getResult(null)))));
//    }
//
//    private static List<EntryIngredients> getInputList(CuttingBoardRecipe recipe) {
//        if(recipe == null) return Collections.emptyList();
//        List<EntryIngredients> list = new ArrayList<>();
//        list.add(EntryIngredients.of(recipe.getIngredients().get(0)));
//        return list;
//    }
//
//    @Override
//    public CategoryIdentifier<?> getCategoryIdentifier() {
//        return CuttingBoardCategory.CUTTING_BOARD;
//    }
//
//    @Override
//    public @Nullable DisplaySerializer<? extends Display> getSerializer() {
//        return null;
//    }
}
