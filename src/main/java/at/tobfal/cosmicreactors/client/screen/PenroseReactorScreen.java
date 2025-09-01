package at.tobfal.cosmicreactors.client.screen;

import at.tobfal.cosmicreactors.CosmicReactors;
import at.tobfal.cosmicreactors.menu.PenroseReactorMenu;
import at.tobfal.cosmicreactors.util.ModUnitsUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class PenroseReactorScreen extends AbstractContainerScreen<PenroseReactorMenu> {
    private static final ResourceLocation TEXTURE = CosmicReactors.getResourceLocation("textures/gui/penrose_reactor_gui.png");

    public PenroseReactorScreen(PenroseReactorMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x, y, 0, 0, imageWidth, imageHeight, 256, 256);

        float energyLevel = (float) menu.getEnergyLevel();
        int energyTicks = Math.round(64 * energyLevel);
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x + 156, y + 11 + 64 - energyTicks,
                176, 64 - energyTicks, 8, energyTicks,
                256, 256
        );
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        super.renderLabels(guiGraphics, mouseX, mouseY);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        if (isOverEnergyBar(mouseX, mouseY)) {
            int energyStored = menu.getEnergyStored();
            int maxEnergyStored = menu.getMaxEnergyStored();

            Component tooltip = Component.literal(String.format("%s / %s", ModUnitsUtil.format(energyStored, "FE"), ModUnitsUtil.format(maxEnergyStored, "FE")));

            guiGraphics.setTooltipForNextFrame(this.font, tooltip, mouseX, mouseY);
        }
    }

    private boolean isOverEnergyBar(int mouseX, int mouseY) {
        int x = mouseX - (width - imageWidth) / 2;
        int y = mouseY - (height - imageHeight) / 2;
        return x >= 155 && x < 164 && y >= 10 && y < 75;
    }
}
