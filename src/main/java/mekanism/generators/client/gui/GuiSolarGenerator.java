package mekanism.generators.client.gui;

import java.util.Arrays;
import mekanism.client.gui.ElementBuilderPowered;
import mekanism.client.gui.GuiMekanism;
import mekanism.client.gui.element.GuiEnergyInfo;
import mekanism.common.util.LangUtils;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.MekanismUtils.ResourceType;
import mekanism.generators.common.inventory.container.ContainerSolarGenerator;
import mekanism.generators.common.tile.TileEntitySolarGenerator;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiSolarGenerator extends GuiMekanism {

    private TileEntitySolarGenerator tileEntity;

    public GuiSolarGenerator(InventoryPlayer inventory, TileEntitySolarGenerator tentity) {
        super(new ContainerSolarGenerator(inventory, tentity));
        tileEntity = tentity;

        guiElements.addAll(
              new ElementBuilderPowered(tileEntity, this, "GuiSolarGenerator.png")
                    .addPowerBar(164, 15)
                    .addRedstone()
                    .addSecurity()
                    .addSlotPower(142, 34)
                    .build()
        );

        guiElements.add(new GuiEnergyInfo(() -> Arrays.asList(
              LangUtils.localize("gui.producing") + ": " + MekanismUtils
                    .getEnergyDisplay(tileEntity.isActive ? tileEntity.getProduction() : 0) + "/t",
              LangUtils.localize("gui.maxOutput") + ": " + MekanismUtils.getEnergyDisplay(tileEntity.getMaxOutput())
                    + "/t"), this, MekanismUtils.getResource(ResourceType.GUI, "GuiSolarGenerator.png")));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRenderer.drawString(tileEntity.getName(), !tileEntity.fullName.contains("Advanced") ? 45 : 30, 6, 0x404040);
        fontRenderer.drawString(LangUtils.localize("container.inventory"), 8, (ySize - 96) + 2, 0x404040);
        fontRenderer
              .drawString(MekanismUtils.getEnergyDisplay(tileEntity.getEnergy(), tileEntity.getMaxEnergy()), 51, 26,
                    0x00CD00);
        fontRenderer
              .drawString(LangUtils.localize("gui.solarGenerator.sun") + ": " + tileEntity.seesSun, 51, 35, 0x00CD00);
        fontRenderer.drawString(
              LangUtils.localize("gui.out") + ": " + MekanismUtils.getEnergyDisplay(tileEntity.getMaxOutput()) + "/t",
              51, 44, 0x00CD00);

        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTick, int mouseX, int mouseY) {
        mc.renderEngine.bindTexture(MekanismUtils.getResource(ResourceType.GUI, "GuiSolarGenerator.png"));
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int guiWidth = (width - xSize) / 2;
        int guiHeight = (height - ySize) / 2;
        drawTexturedModalRect(guiWidth, guiHeight, 0, 0, xSize, ySize);

        drawTexturedModalRect(guiWidth + 20, guiHeight + 37, 176, (tileEntity.seesSun ? 52 : 64), 12, 12);

        super.drawGuiContainerBackgroundLayer(partialTick, mouseX, mouseY);
    }
}
