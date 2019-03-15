package mekanism.client.gui;

import mekanism.client.gui.element.GuiGauge.Type;
import mekanism.client.gui.element.GuiProgress.ProgressBar;
import mekanism.client.gui.element.GuiSlot.SlotOverlay;
import mekanism.client.gui.element.GuiSlot.SlotType;
import mekanism.common.inventory.container.ContainerSolarNeutronActivator;
import mekanism.common.tile.TileEntitySolarNeutronActivator;
import mekanism.common.util.LangUtils;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.MekanismUtils.ResourceType;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiSolarNeutronActivator extends GuiMekanism {

    public TileEntitySolarNeutronActivator tileEntity;

    public GuiSolarNeutronActivator(InventoryPlayer inventory, TileEntitySolarNeutronActivator tentity) {
        super(tentity, new ContainerSolarNeutronActivator(inventory, tentity));
        tileEntity = tentity;

        guiElements.addAll(
              new ElementBuilder(tileEntity, this, "GuiBlank.png")
                    .addSecurity()
                    .addRedstone()
                    .addUpgrade()
                    .addSlot(SlotType.NORMAL, SlotOverlay.MINUS, 4, 55)
                    .addSlot(SlotType.NORMAL, SlotOverlay.PLUS, 154, 55)
                    .addGasGauge(() -> tileEntity.inputTank, Type.STANDARD, 25, 13)
                    .addGasGauge(() -> tileEntity.outputTank, Type.STANDARD, 133, 13)
                    .addProgress(() -> tileEntity.isActive ? 1 : 0, ProgressBar.LARGE_RIGHT, 62, 38)
                    .build()
        );
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRenderer.drawString(tileEntity.getName(), 26, 4, 0x404040);
        fontRenderer.drawString(LangUtils.localize("container.inventory"), 8, (ySize - 96) + 4, 0x404040);

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
}
