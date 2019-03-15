package mekanism.client.gui;

import mekanism.client.gui.element.GuiProgress.ProgressBar;
import mekanism.client.gui.element.GuiSlot.SlotOverlay;
import mekanism.client.gui.element.GuiSlot.SlotType;
import mekanism.common.inventory.container.ContainerDoubleElectricMachine;
import mekanism.common.tile.prefab.TileEntityDoubleElectricMachine;
import mekanism.common.util.LangUtils;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiDoubleElectricMachine extends GuiMekanismPlus {

    public TileEntityDoubleElectricMachine tileEntity;

    public GuiDoubleElectricMachine(InventoryPlayer inventory, TileEntityDoubleElectricMachine tentity) {
        super(tentity, new ContainerDoubleElectricMachine(inventory, tentity), tentity.guiLocation, tentity.energyPerTick);
        tileEntity = tentity;

        guiElements.addAll(
              new ElementBuilderPowered(tileEntity, this, tileEntity.guiLocation)
                    .addPowerBar(164, 15)
                    .addSideConfiguration()
                    .addTransporter()
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
        fontRenderer
              .drawString(tileEntity.getName(), (xSize / 2) - (fontRenderer.getStringWidth(tileEntity.getName()) / 2),
                    6, 0x404040);
        fontRenderer.drawString(LangUtils.localize("container.inventory"), 8, (ySize - 96) + 2, 0x404040);

        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTick, int mouseX, int mouseY) {
        mc.renderEngine.bindTexture(tileEntity.guiLocation);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int guiWidth = (width - xSize) / 2;
        int guiHeight = (height - ySize) / 2;
        drawTexturedModalRect(guiWidth, guiHeight, 0, 0, xSize, ySize);

        super.drawGuiContainerBackgroundLayer(partialTick, mouseX, mouseY);
    }
}
