package mekanism.client.gui;

import mekanism.client.gui.element.GuiGauge.Type;
import mekanism.client.gui.element.GuiProgress.ProgressBar;
import mekanism.client.gui.element.GuiSlot.SlotOverlay;
import mekanism.client.gui.element.GuiSlot.SlotType;
import mekanism.common.inventory.container.ContainerPRC;
import mekanism.common.tile.TileEntityPRC;
import mekanism.common.util.LangUtils;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.MekanismUtils.ResourceType;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiPRC extends GuiMekanismBase {

    private TileEntityPRC tileEntity;

    public GuiPRC(InventoryPlayer inventory, TileEntityPRC tentity) {
        super(tentity, new ContainerPRC(inventory, tentity), "GuiBlank.png",
              MekanismUtils.getEnergyPerTick(tentity, tentity.BASE_ENERGY_PER_TICK + (tentity.getRecipe() != null ? tentity.getRecipe().extraEnergy : 0)));
        tileEntity = tentity;

        guiElements.addAll(
              new ElementBuilderPowered(tileEntity, this, "GuiBlank.png")
                    .addPowerBar(164, 15)
                    .addSideConfiguration()
                    .addTransporter()
                    .addGasGauge(() -> tileEntity.inputGasTank, Type.STANDARD_RED, 28, 10)
                    .addGasGauge(() -> tileEntity.outputGasTank, Type.SMALL_BLUE, 140, 40)
                    .addFluidGauge(() -> tileEntity.inputFluidTank, Type.STANDARD_YELLOW, 5, 10)
                    .addSlot(SlotType.INPUT, SlotOverlay.INPUT, 53, 34)
                    .addSlotPower(140, 18)
                    .addSlot(SlotType.OUTPUT, SlotOverlay.OUTPUT, 115, 34)
                    .addProgress(tileEntity::getScaledProgress, getProgressType(), 75, 37)
                    .build()
        );
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRenderer.drawString(tileEntity.getName(), (xSize / 2) - (fontRenderer.getStringWidth(tileEntity.getName()) / 2),
                    6, 0x404040);
        fontRenderer.drawString(LangUtils.localize("container.inventory"), 8, (ySize - 96) + 2, 0x404040);

        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTick, int mouseX, int mouseY) {
        mc.renderEngine.bindTexture(MekanismUtils.getResource(ResourceType.GUI, "GuiBlank.png"));
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int guiWidth = (width - xSize) / 2;
        int guiHeight = (height - ySize) / 2;
        drawTexturedModalRect(guiWidth, guiHeight, 0, 0, xSize, ySize);

        super.drawGuiContainerBackgroundLayer(partialTick, mouseX, mouseY);
    }

    public ProgressBar getProgressType() {
        return ProgressBar.MEDIUM;
    }
}
