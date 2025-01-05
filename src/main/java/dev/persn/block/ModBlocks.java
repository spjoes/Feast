package dev.persn.block;

import dev.persn.Feast;
import net.minecraft.block.*;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public class ModBlocks {

    public static final Block RICE_CROP = register("rice_crop", RiceCropBlock::new, AbstractBlock.Settings.create().mapColor(MapColor.GREEN).ticksRandomly().nonOpaque().noCollision().sounds(BlockSoundGroup.CROP).pistonBehavior(PistonBehavior.DESTROY));

    public static final Block CUTTING_BOARD = register("cutting_board", CuttingBoardBlock::new, AbstractBlock.Settings.create().strength(2.5F, 2.5F).nonOpaque().sounds(BlockSoundGroup.WOOD));



//    public static Block registerBlock(String name, Function<Block.Settings, Block> factory, Block.Settings settings) {
//        final RegistryKey<Block> registerKey = RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(Feast.MOD_ID, name));
//        return Blocks.register(registerKey, factory, settings);
//    }

    public static Block register(RegistryKey<Block> key, Function<AbstractBlock.Settings, Block> factory, AbstractBlock.Settings settings) {
        Block block = (Block)factory.apply(settings.registryKey(key));
        return (Block)Registry.register(Registries.BLOCK, key, block);
    }

    private static RegistryKey<Block> keyOf(String id) {
        return RegistryKey.of(RegistryKeys.BLOCK, Identifier.ofVanilla(id));
    }

    private static Block register(String id, Function<AbstractBlock.Settings, Block> factory, AbstractBlock.Settings settings) {
        return register(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(Feast.MOD_ID, id)), factory, settings);
    }

    public static void registerModBlocks() {

    }
}
