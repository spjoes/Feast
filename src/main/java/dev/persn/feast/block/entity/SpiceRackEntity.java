package dev.persn.feast.block.entity;

import dev.persn.feast.Feast;
import dev.persn.feast.block.ModBlocks;
import dev.persn.feast.gui.SpiceRackScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class SpiceRackEntity extends BlockEntity implements ExtendedScreenHandlerFactory<BlockPos> {

    protected final SimpleInventory inventory = new SimpleInventory(6) {
        @Override
        public int getMaxCountPerStack() {
            return 1;
        }
    };

    public SpiceRackEntity(BlockPos pos, BlockState state) {
        super(ModBlocks.SPICE_RACK_ENTITY, pos, state);
        inventory.addListener(sender -> {
//            if (!world.isClient()) Feast.LOGGER.info("markDirty called; new stack[0] = {}", inventory.getStack(0));
            markDirty();
        });
    }

    public SimpleInventory getInventory() {
        return inventory;
    }

    @Override
    public BlockPos getScreenOpeningData(ServerPlayerEntity serverPlayerEntity) {
        // The server will call this to figure out what data to send to the client
        return this.pos;
    }


    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        // This is the server-side constructor call
        return new SpiceRackScreenHandler(syncId, playerInventory, this.pos);
    }

    @Override
    public void markDirty() {
        super.markDirty();
        if (!world.isClient) {
            ((ServerWorld)world).getChunkManager().markForUpdate(pos);
        }
        world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_ALL);

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

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        super.readNbt(nbt, registries);
        Inventories.readNbt(nbt, inventory.getHeldStacks(), registries);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        super.writeNbt(nbt, registries);
        Inventories.writeNbt(nbt, inventory.getHeldStacks(), registries);
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable(getCachedState().getBlock().getTranslationKey());
    }

    // Unused for now
    public static void clientTick(World world, BlockPos pos, BlockState state, SpiceRackEntity blockEntity) {}
    public static void serverTick(World world, BlockPos pos, BlockState state, SpiceRackEntity blockEntity) {}
}
