package at.tobfal.cosmicreactors.menu;

import at.tobfal.cosmicreactors.init.ModMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class PenroseReactorMenu extends AbstractContainerMenu {
    protected final Level level;

    public PenroseReactorMenu(int containerId, Inventory inv, FriendlyByteBuf extraData) {
        this(containerId, inv);
    }

    public PenroseReactorMenu(int containerId, Inventory playerInventory) {
        super(ModMenuTypes.PENROSE_REACTOR_MENU.get(), containerId);
        this.level = playerInventory.player.level();
        addPlayerInventorySlots(playerInventory,8, 84);
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
        for(int i = 0;i < 3;i++)
            for(int j = 0;j < 9;j++)
                addSlot(new Slot(playerInventory, j + i * 9 + 9, x + j * 18, y + i * 18));

        for(int i = 0;i < 9;i++)
            addSlot(new Slot(playerInventory, i, x + i * 18, y + 58));
    }
}
