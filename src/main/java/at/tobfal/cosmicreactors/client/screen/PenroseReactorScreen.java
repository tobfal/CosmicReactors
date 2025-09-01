package at.tobfal.cosmicreactors.client.screen;

import at.tobfal.cosmicreactors.CosmicReactors;
import at.tobfal.cosmicreactors.menu.PenroseReactorMenu;
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
    }
}
