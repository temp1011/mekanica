package mekanism.client.gui;

import java.io.IOException;
import mekanism.api.Coord4D;
import mekanism.client.gui.element.GuiGauge.Type;
import mekanism.client.gui.element.GuiProgress.ProgressBar;
import mekanism.client.gui.element.GuiSlot.SlotType;
import mekanism.client.sound.SoundHandler;
import mekanism.common.Mekanism;
import mekanism.common.base.TileNetworkList;
import mekanism.common.inventory.container.ContainerElectrolyticSeparator;
import mekanism.common.network.PacketTileEntity.TileEntityMessage;
import mekanism.common.tile.TileEntityElectrolyticSeparator;
import mekanism.common.tile.TileEntityGasTank;
import mekanism.common.util.LangUtils;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.MekanismUtils.ResourceType;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiElectrolyticSeparator extends GuiMekanismPlus {

    public TileEntityElectrolyticSeparator tileEntity;

    public GuiElectrolyticSeparator(InventoryPlayer inventory, TileEntityElectrolyticSeparator tentity) {
        super(tentity, new ContainerElectrolyticSeparator(inventory, tentity), "GuiElectrolyticSeparator.png", tentity.clientEnergyUsed);

        tileEntity = tentity;

        guiElements.addAll(
              new ElementBuilderPowered(tileEntity, this, "GuiElectrolyticSeparator.png")
                    .addPowerBar(164, 15)
                    .addGasGauge(() -> tileEntity.leftTank, Type.SMALL, 58, 18)
                    .addGasGauge(() -> tileEntity.rightTank, Type.SMALL, 100, 18)
                    .addFluidGauge(() -> tileEntity.fluidTank, Type.STANDARD, 5, 10)
                    .addSlotNoOverlay(SlotType.NORMAL, 25, 34)
                    .addSlotNoOverlay(SlotType.NORMAL, 58, 51)
                    .addSlotNoOverlay(SlotType.NORMAL, 100, 51)
                    .addSlotPower(142, 34)
                    .addProgress(() -> tileEntity.isActive ? 1 : 0, ProgressBar.BI, 78, 29)
                    .build()
        );
    }

    @Override
    protected void mouseClicked(int x, int y, int button) throws IOException {
        super.mouseClicked(x, y, button);

        int xAxis = (x - (width - xSize) / 2);
        int yAxis = (y - (height - ySize) / 2);

        if (xAxis > 8 && xAxis < 17 && yAxis > 73 && yAxis < 82) {
            TileNetworkList data = TileNetworkList.withContents((byte) 0);

            Mekanism.packetHandler.sendToServer(new TileEntityMessage(Coord4D.get(tileEntity), data));
            SoundHandler.playSound(SoundEvents.UI_BUTTON_CLICK);
        } else if (xAxis > 160 && xAxis < 169 && yAxis > 73 && yAxis < 82) {
            TileNetworkList data = TileNetworkList.withContents((byte) 1);

            Mekanism.packetHandler.sendToServer(new TileEntityMessage(Coord4D.get(tileEntity), data));
            SoundHandler.playSound(SoundEvents.UI_BUTTON_CLICK);
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRenderer.drawString(tileEntity.getName(), 45, 6, 0x404040);

        String name = chooseByMode(tileEntity.dumpLeft, LangUtils.localize("gui.idle"),
              LangUtils.localize("gui.dumping"), LangUtils.localize("gui.dumping_excess"));
        renderScaledText(name, 21, 73, 0x404040, 66);

        name = chooseByMode(tileEntity.dumpRight, LangUtils.localize("gui.idle"), LangUtils.localize("gui.dumping"),
              LangUtils.localize("gui.dumping_excess"));
        renderScaledText(name, 156 - (int) (fontRenderer.getStringWidth(name) * getNeededScale(name, 66)), 73, 0x404040,
              66);

        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTick, int mouseX, int mouseY) {
        mc.renderEngine.bindTexture(MekanismUtils.getResource(ResourceType.GUI, "GuiElectrolyticSeparator.png"));
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int guiWidth = (width - xSize) / 2;
        int guiHeight = (height - ySize) / 2;
        drawTexturedModalRect(guiWidth, guiHeight, 0, 0, xSize, ySize);

        int displayInt = chooseByMode(tileEntity.dumpLeft, 52, 60, 68);
        drawTexturedModalRect(guiWidth + 8, guiHeight + 73, 176, displayInt, 8, 8);

        displayInt = chooseByMode(tileEntity.dumpRight, 52, 60, 68);
        drawTexturedModalRect(guiWidth + 160, guiHeight + 73, 176, displayInt, 8, 8);

        super.drawGuiContainerBackgroundLayer(partialTick, mouseX, mouseY);
    }

    private <T> T chooseByMode(TileEntityGasTank.GasMode dumping, T idleOption, T dumpingOption,
          T dumpingExcessOption) {
        if (dumping.equals(TileEntityGasTank.GasMode.IDLE)) {
            return idleOption;
        } else if (dumping.equals(TileEntityGasTank.GasMode.DUMPING)) {
            return dumpingOption;
        } else if (dumping.equals(TileEntityGasTank.GasMode.DUMPING_EXCESS)) {
            return dumpingExcessOption;
        }

        return idleOption; //should not happen;
    }
}
