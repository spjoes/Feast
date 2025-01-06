package dev.persn.block;

import dev.persn.Feast;
import dev.persn.block.entity.CuttingBoardEntity;
import dev.persn.block.entity.StoveEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntityType;
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
    public static final Block STOVE = register("stove", StoveBlock::new, AbstractBlock.Settings.create().strength(2.5F, 2.5F).nonOpaque().sounds(BlockSoundGroup.METAL));

    public static final BlockEntityType<StoveEntity> STOVE_ENTITY = FabricBlockEntityTypeBuilder.create(StoveEntity::new, STOVE).build(null);
    public static final BlockEntityType<CuttingBoardEntity> CUTTING_BOARD_ENTITY = FabricBlockEntityTypeBuilder.create(CuttingBoardEntity::new, CUTTING_BOARD).build(null);


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

    public static void initBlockEntities() {
        Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(Feast.MOD_ID, "stove_entity"), STOVE_ENTITY);
        Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(Feast.MOD_ID, "cutting_board_entity"), CUTTING_BOARD_ENTITY);
    }
}
