package mekanism.client.gui;

import mekanism.client.gui.element.GuiGauge.Type;
import mekanism.client.gui.element.GuiProgress.ProgressBar;
import mekanism.client.gui.element.GuiSlot.SlotOverlay;
import mekanism.client.gui.element.GuiSlot.SlotType;
import mekanism.common.inventory.container.ContainerChemicalInfuser;
import mekanism.common.tile.TileEntityChemicalInfuser;
import mekanism.common.util.LangUtils;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.MekanismUtils.ResourceType;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiChemicalInfuser extends GuiMekanismBase {

    private TileEntityChemicalInfuser tileEntity;

    public GuiChemicalInfuser(InventoryPlayer inventory, TileEntityChemicalInfuser tentity) {
        super(tentity, new ContainerChemicalInfuser(inventory, tentity), "GuiChemicalInfuser.png", tentity.clientEnergyUsed);
        tileEntity = tentity;

        guiElements.addAll(
              new ElementBuilder(tileEntity, this, "GuiChemicalInfuser.png")
                    .addGasGauge(() -> tileEntity.leftTank, Type.STANDARD, 25, 13)
                    .addGasGauge(() -> tileEntity.centerTank, Type.STANDARD, 79, 4)
                    .addGasGauge(() -> tileEntity.rightTank, Type.STANDARD, 133, 13)
                    .addSlotPower(154, 4)
                    .addSlot(SlotType.NORMAL, SlotOverlay.MINUS, 154, 55)
                    .addSlot(SlotType.NORMAL, SlotOverlay.MINUS, 4, 55)
                    .addSlot(SlotType.NORMAL, SlotOverlay.PLUS, 79, 64)
                    .addProgress(() -> tileEntity.isActive ? 1 : 0, ProgressBar.SMALL_RIGHT, 45, 38)
                    .addProgress(() -> tileEntity.isActive ? 1 : 0, ProgressBar.SMALL_LEFT, 99, 38)
                    .build()
        );
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        int xAxis = (mouseX - (width - xSize) / 2);
        int yAxis = (mouseY - (height - ySize) / 2);

        fontRenderer.drawString(LangUtils.localize("gui.chemicalInfuser.short"), 5, 5, 0x404040);
        fontRenderer.drawString(LangUtils.localize("container.inventory"), 8, (ySize - 96) + 4, 0x404040);

        if (xAxis >= 116 && xAxis <= 168 && yAxis >= 76 && yAxis <= 80) {
            drawHoveringText(MekanismUtils.getEnergyDisplay(tileEntity.getEnergy(), tileEntity.getMaxEnergy()), xAxis,
                  yAxis);
        }

        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTick, int mouseX, int mouseY) {
        mc.renderEngine.bindTexture(MekanismUtils.getResource(ResourceType.GUI, "GuiChemicalInfuser.png"));
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int guiWidth = (width - xSize) / 2;
        int guiHeight = (height - ySize) / 2;
        drawTexturedModalRect(guiWidth, guiHeight, 0, 0, xSize, ySize);

        int displayInt;

        displayInt = tileEntity.getScaledEnergyLevel(52);
        drawTexturedModalRect(guiWidth + 116, guiHeight + 76, 176, 0, displayInt, 4);

        super.drawGuiContainerBackgroundLayer(partialTick, mouseX, mouseY);
    }
}
