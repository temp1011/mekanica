package mekanism.client.gui;

import mekanism.api.gas.GasStack;
import mekanism.client.gui.element.GuiProgress.ProgressBar;
import mekanism.client.gui.element.GuiSlot.SlotOverlay;
import mekanism.client.gui.element.GuiSlot.SlotType;
import mekanism.client.render.MekanismRenderer;
import mekanism.common.inventory.container.ContainerAdvancedElectricMachine;
import mekanism.common.tile.prefab.TileEntityAdvancedElectricMachine;
import mekanism.common.util.LangUtils;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiAdvancedElectricMachine extends GuiMekanismPlus {

    public TileEntityAdvancedElectricMachine tileEntity;

    public GuiAdvancedElectricMachine(InventoryPlayer inventory, TileEntityAdvancedElectricMachine tentity) {
        super(tentity, new ContainerAdvancedElectricMachine(inventory, tentity), tentity.guiLocation, tentity.energyPerTick);
        tileEntity = tentity;

        guiElements.addAll(
              new ElementBuilder(tileEntity, this, tileEntity.guiLocation)
                    .addSideConfiguration()
                    .addTransporter()
                    .addPowerBar(164, 15)
                    .addSlot(SlotType.INPUT, SlotOverlay.INPUT, 55, 16)
                    .addSlotPower(30, 34)
                    .addSlotNoOverlay(SlotType.EXTRA, 55, 52)
                    .addSlot(SlotType.OUTPUT_LARGE, SlotOverlay.OUTPUT, 111, 30)
                    .addProgress(() -> tileEntity.getScaledProgress(), getProgressType(), 77, 37)
                    .build()
        );
    }

    public ProgressBar getProgressType() {
        return ProgressBar.BLUE;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        int xAxis = (mouseX - (width - xSize) / 2);
        int yAxis = (mouseY - (height - ySize) / 2);

        fontRenderer
              .drawString(tileEntity.getName(), (xSize / 2) - (fontRenderer.getStringWidth(tileEntity.getName()) / 2),
                    6, 0x404040);
        fontRenderer.drawString(LangUtils.localize("container.inventory"), 8, (ySize - 96) + 2, 0x404040);

        if (xAxis >= 61 && xAxis <= 67 && yAxis >= 37 && yAxis <= 49) {
            drawHoveringText(
                  tileEntity.gasTank.getGas() != null ? tileEntity.gasTank.getGas().getGas().getLocalizedName() + ": "
                        + tileEntity.gasTank.getStored() : LangUtils.localize("gui.none"), xAxis, yAxis);
        }

        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTick, int mouseX, int mouseY) {
        mc.renderEngine.bindTexture(tileEntity.guiLocation);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int guiWidth = (width - xSize) / 2;
        int guiHeight = (height - ySize) / 2;
        drawTexturedModalRect(guiWidth, guiHeight, 0, 0, xSize, ySize);

        int displayInt;

        if (tileEntity.getScaledGasLevel(12) > 0) {
            displayInt = tileEntity.getScaledGasLevel(12);
            displayGauge(61, 37 + 12 - displayInt, 6, displayInt, tileEntity.gasTank.getGas());
        }

        super.drawGuiContainerBackgroundLayer(partialTick, mouseX, mouseY);
    }

    public void displayGauge(int xPos, int yPos, int sizeX, int sizeY, GasStack gas) {
        if (gas == null) {
            return;
        }

        int guiWidth = (width - xSize) / 2;
        int guiHeight = (height - ySize) / 2;

        mc.renderEngine.bindTexture(MekanismRenderer.getBlocksTexture());
        //TODO: Use GuiGasGauge?
        int tint = gas.getGas().getTint();
        if (tint != -1) {
            MekanismRenderer.color(tint);
        }
        drawTexturedModalRect(guiWidth + xPos, guiHeight + yPos, gas.getGas().getSprite(), sizeX, sizeY);
        MekanismRenderer.resetColor();
    }
}
