package dev.persn.feast.block.entity;

import dev.persn.feast.Feast;
import dev.persn.feast.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class StoveEntity extends BlockEntity {


    public boolean isBurning = false;
    protected final SimpleInventory inventory = new SimpleInventory(1) {
        @Override
        public int getMaxCountPerStack() {
            return 1;
        }
    };
    public int size() {
        return inventory.size();
    }


    public StoveEntity(BlockPos pos, BlockState state) {
        super(ModBlocks.STOVE_ENTITY, pos, state);
        inventory.addListener(sender -> {
            markDirty();
        });
    }

    @Override
    public void markDirty() {
        super.markDirty();
        if (!world.isClient) {
            ((ServerWorld)world).getChunkManager().markForUpdate(pos);
        }
        world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_ALL);

    }


    public SimpleInventory getInventory() {
        return inventory;
    }



    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        super.readNbt(nbt, registries);
        isBurning = nbt.getBoolean("isBurning");
        Inventories.readNbt(nbt, inventory.getHeldStacks(), registries);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        super.writeNbt(nbt, registries);
        nbt.putBoolean("isBurning", isBurning);
        Inventories.writeNbt(nbt, inventory.getHeldStacks(), registries);
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registries) {
        return createNbt(registries);
    }

    // Unused for now
    public static void clientTick(World world, BlockPos pos, BlockState state, StoveEntity blockEntity) {}
    public static void serverTick(World world, BlockPos pos, BlockState state, StoveEntity blockEntity) {}
}
