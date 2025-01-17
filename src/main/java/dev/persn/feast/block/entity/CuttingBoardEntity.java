package dev.persn.feast.block.entity;

import dev.persn.feast.block.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.item.ItemStack;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class CuttingBoardEntity extends BlockEntity {

    public int number = 0;
    //array of ItemStacks to store the items.
    public DefaultedList<ItemStack> inventory;
    public int size() {
        return 64;
    }

    public CuttingBoardEntity(BlockPos pos, BlockState state) {
        super(ModBlocks.CUTTING_BOARD_ENTITY, pos, state);
        this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
    }

    public static void clientTick(World world, BlockPos pos, BlockState state, CuttingBoardEntity blockEntity) {
    }
    public static void serverTick(World world, BlockPos pos, BlockState state, CuttingBoardEntity blockEntity) {
    }


    public DefaultedList<ItemStack> getInventory() {
        return inventory;
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        super.writeNbt(nbt, registries);
        nbt.putInt("number", number);
        Inventories.writeNbt(nbt, this.inventory, registries);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        super.readNbt(nbt, registries);
        number = nbt.getInt("number");
        this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
        Inventories.readNbt(nbt, this.inventory, registries);

    }

    @Override
    public @Nullable Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registries) {
        return createNbt(registries);

    }
}
