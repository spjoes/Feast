package dev.persn.feast.integrations;

import dev.persn.feast.Feast;
import dev.persn.feast.block.ModBlocks;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.LinkedList;
import java.util.List;

public class CuttingBoardCategory implements DisplayCategory<BasicDisplay> {
    @Override
    public CategoryIdentifier<? extends BasicDisplay> getCategoryIdentifier() {
        return null;
    }

    @Override
    public Text getTitle() {
        return null;
    }

    @Override
    public Renderer getIcon() {
        return null;
    }
//
//    public static final Identifier TEXTURE = Identifier.of(Feast.MOD_ID, "textures/gui/cutting_board.png");
//    public static final CategoryIdentifier<CuttingBoardDisplay> CUTTING_BOARD = CategoryIdentifier.of(Identifier.of(Feast.MOD_ID, "cutting_board"));
//
//    @Override
//    public CategoryIdentifier<? extends BasicDisplay> getCategoryIdentifier() {
//        return CUTTING_BOARD;
//    }
//
//    @Override
//    public Text getTitle() {
//        return Text.translatable("category.feast.cutting_board");
//    }
//
//    @Override
//    public Renderer getIcon() {
//        return EntryStacks.of(ModBlocks.CUTTING_BOARD.asItem().getDefaultStack());
//    }
//
//    @Override
//    public List<Widget> setupDisplay(BasicDisplay display, Rectangle bounds) {
//
//        final Point startPoint = new Point(bounds.getCenterX() - 41, bounds.getCenterY() - 27);
//        List<Widget> widgets = new LinkedList<>();
//
//        widgets.add(Widgets.createTexturedWidget(TEXTURE, new Rectangle(startPoint.x, startPoint.y, 175, 82)));
//        widgets.add(Widgets.createSlot(new Point(startPoint.x + 1, startPoint.y + 11))
//                .entries(display.getInputEntries().get(0)).markInput());
//
//        widgets.add(Widgets.createSlot(new Point(startPoint.x + 1, startPoint.y + 59))
//                .entries(display.getInputEntries().get(0)).markOutput());
//
//        return DisplayCategory.super.setupDisplay(display, bounds);
//    }
//
//    @Override
//    public int getDisplayHeight() {
//        return 90;
//    }
}
