package mekanism.client.gui;

import mekanism.client.gui.element.GuiGauge.Type;
import mekanism.client.gui.element.GuiProgress.ProgressBar;
import mekanism.client.gui.element.GuiSlot.SlotOverlay;
import mekanism.client.gui.element.GuiSlot.SlotType;
import mekanism.common.inventory.container.ContainerChemicalOxidizer;
import mekanism.common.tile.TileEntityChemicalOxidizer;
import mekanism.common.util.LangUtils;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.MekanismUtils.ResourceType;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiChemicalOxidizer extends GuiMekanismBase {

    private TileEntityChemicalOxidizer tileEntity;

    public GuiChemicalOxidizer(InventoryPlayer inventory, TileEntityChemicalOxidizer tentity) {
        super(tentity, new ContainerChemicalOxidizer(inventory, tentity), "GuiChemicalOxidizer.png", tentity.energyPerTick);
        tileEntity = tentity;

        guiElements.addAll(
              new ElementBuilder(tileEntity, this, "GuiChemicalOxidizer.png")
                    .addGasGauge(() -> tileEntity.gasTank, Type.STANDARD, 133, 13)
                    .addSlotPower(154, 4)
                    .addSlotNoOverlay(SlotType.NORMAL, 25, 35)
                    .addSlot(SlotType.NORMAL, SlotOverlay.PLUS, 154, 24)
                    .addProgress(() -> tileEntity.getScaledProgress(), ProgressBar.LARGE_RIGHT, 62, 39)
                    .build()
        );
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        int xAxis = (mouseX - (width - xSize) / 2);
        int yAxis = (mouseY - (height - ySize) / 2);

        fontRenderer.drawString(tileEntity.getName(), 45, 6, 0x404040);
        fontRenderer.drawString(LangUtils.localize("container.inventory"), 8, (ySize - 96) + 2, 0x404040);

        if (xAxis >= 116 && xAxis <= 168 && yAxis >= 76 && yAxis <= 80) {
            drawHoveringText(MekanismUtils.getEnergyDisplay(tileEntity.getEnergy(), tileEntity.getMaxEnergy()), xAxis,
                  yAxis);
        }

        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTick, int mouseX, int mouseY) {
        mc.renderEngine.bindTexture(MekanismUtils.getResource(ResourceType.GUI, "GuiChemicalOxidizer.png"));
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int guiWidth = (width - xSize) / 2;
        int guiHeight = (height - ySize) / 2;
        drawTexturedModalRect(guiWidth, guiHeight, 0, 0, xSize, ySize);

        drawTexturedModalRect(guiWidth + 116, guiHeight + 76, 176, 0, tileEntity.getScaledEnergyLevel(52), 4);

        super.drawGuiContainerBackgroundLayer(partialTick, mouseX, mouseY);
    }
}
