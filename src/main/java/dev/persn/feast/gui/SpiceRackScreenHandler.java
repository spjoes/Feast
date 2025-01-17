package dev.persn.feast.gui;

import dev.persn.feast.Feast;
import dev.persn.feast.block.entity.SpiceRackEntity;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SpiceRackScreenHandler extends ScreenHandler {

    private final Inventory inventory;

    public SpiceRackScreenHandler(int syncId, PlayerInventory playerInv, BlockPos pos) {
        super(Feast.SPICE_RACK_SCREEN_HANDLER, syncId);

        // Attempt to get the same SpiceRackEntity on the client
        World world = playerInv.player.getWorld();
        SpiceRackEntity blockEntity = null;
        if (world != null && world.isClient) {
            BlockEntity be = world.getBlockEntity(pos);
            if (be instanceof SpiceRackEntity rack) {
                blockEntity = rack;
            }
        }

        // If found the block entity, use that inventory, else fallback
        BlockEntity be = playerInv.player.getWorld().getBlockEntity(pos);
        if (be instanceof SpiceRackEntity spiceRack) {
            this.inventory = spiceRack.getInventory();
        } else {
            // fallback
            this.inventory = new SimpleInventory(6);
        }

        // Standard slot setup
        checkSize(inventory, 6);
        inventory.onOpen(playerInv.player);

        // Spice Rack's own 6 slots but with a customizable padding in between the 1st and 2nd row on the y axis
        int padding = 13;
        for (int row = 0; row < 2; ++row) {
            for (int col = 0; col < 3; ++col) {
                this.addSlot(new Slot(inventory, col + row * 3, 62 + col * 18, 32 + row * 18 + ((row - 1) * padding)));
            }
        }

        // Player Inventory (3 rows)
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInv, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }

        // Player Hotbar
        for (int hotbarSlot = 0; hotbarSlot < 9; ++hotbarSlot) {
            this.addSlot(new Slot(playerInv, hotbarSlot, 8 + hotbarSlot * 18, 142));
        }
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        this.inventory.onClose(player);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot != null && slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (invSlot < this.inventory.size()) {
                // Move from block-entity slots to player inventory
                if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                // Move from player inventory to block-entity slots
                if (!this.insertItem(originalStack, 0, this.inventory.size(), false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }

            if (originalStack.getCount() == newStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTakeItem(player, originalStack);
        }
        return newStack;
    }
}
