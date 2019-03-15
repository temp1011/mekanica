package mekanism.client.gui;

import mekanism.client.gui.element.GuiBucketIO;
import mekanism.client.gui.element.GuiGauge.Type;
import mekanism.client.gui.element.GuiProgress.ProgressBar;
import mekanism.client.gui.element.GuiSlot.SlotOverlay;
import mekanism.client.gui.element.GuiSlot.SlotType;
import mekanism.common.inventory.container.ContainerChemicalWasher;
import mekanism.common.tile.TileEntityChemicalWasher;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.MekanismUtils.ResourceType;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiChemicalWasher extends GuiMekanismBase {

    public TileEntityChemicalWasher tileEntity;

    public GuiChemicalWasher(InventoryPlayer inventory, TileEntityChemicalWasher tentity) {
        super(tentity, new ContainerChemicalWasher(inventory, tentity), "GuiChemicalWasher.png", tentity.clientEnergyUsed);
        tileEntity = tentity;

        guiElements.addAll(
              new ElementBuilder(tileEntity, this, "GuiChemicalWasher.png")
                    .addGasGauge(() -> tileEntity.inputTank, Type.STANDARD, 26 , 13)
                    .addGasGauge(() -> tileEntity.outputTank, Type.STANDARD, 133, 13)
                    .addFluidGauge(() -> tileEntity.fluidTank, Type.STANDARD, 5, 4)
                    .addSlotPower(154, 4)
                    .addSlot(SlotType.NORMAL, SlotOverlay.MINUS, 154, 55)
                    .addProgress(() -> tileEntity.isActive ? 1 : 0, ProgressBar.LARGE_RIGHT, 62, 38)
                    .build()
        );

        guiElements.add(new GuiBucketIO(this, MekanismUtils.getResource(ResourceType.GUI, "GuiChemicalWasher.png")));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        int xAxis = (mouseX - (width - xSize) / 2);
        int yAxis = (mouseY - (height - ySize) / 2);

        fontRenderer.drawString(tileEntity.getName(), 45, 4, 0x404040);

        if (xAxis >= 116 && xAxis <= 168 && yAxis >= 76 && yAxis <= 80) {
            drawHoveringText(MekanismUtils.getEnergyDisplay(tileEntity.getEnergy(), tileEntity.getMaxEnergy()), xAxis,
                  yAxis);
        }

        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTick, int mouseX, int mouseY) {
        mc.renderEngine.bindTexture(MekanismUtils.getResource(ResourceType.GUI, "GuiChemicalWasher.png"));
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
