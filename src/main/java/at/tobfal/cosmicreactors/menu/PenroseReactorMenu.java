package at.tobfal.cosmicreactors.menu;

import at.tobfal.cosmicreactors.init.ModMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.UUID;

public class PenroseReactorMenu extends AbstractContainerMenu {
    protected final Level level;
    protected final UUID reactorId;
    protected final ContainerData data;

    public PenroseReactorMenu(int containerId, Inventory inv, FriendlyByteBuf extraData) {
        this(containerId, inv, extraData.readUUID(), new SimpleContainerData(2));
    }

    public PenroseReactorMenu(int containerId, Inventory playerInventory, UUID reactorId, ContainerData containerData) {
        super(ModMenuTypes.PENROSE_REACTOR_MENU.get(), containerId);
        this.level = playerInventory.player.level();
        this.reactorId = reactorId;
        this.data = containerData;
        addPlayerInventorySlots(playerInventory,8, 84);
        addDataSlots(containerData);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    private void addPlayerInventorySlots(Inventory playerInventory, int x, int y) {
        for(int i = 0; i < 9; i++) {
            addSlot(new Slot(playerInventory, i, x + i * 18, y + 58));
        }

        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 9; j++) {
                addSlot(new Slot(playerInventory, j + i * 9 + 9, x + j * 18, y + i * 18));
            }
        }
    }

    public UUID getReactorId() {
        return reactorId;
    }

    public int getEnergyStored() {
        return this.data.get(0);
    }

    public int getMaxEnergyStored() {
        return this.data.get(1);
    }

    public double getEnergyLevel() {
        double percentage = ((double) getEnergyStored())/((double) getMaxEnergyStored());
        return Math.min(1.0, Math.max(0.0, percentage));
    }
}
