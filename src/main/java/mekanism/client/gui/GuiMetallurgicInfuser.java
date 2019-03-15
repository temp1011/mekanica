package mekanism.client.gui;

import java.io.IOException;
import mekanism.api.Coord4D;
import mekanism.client.gui.element.GuiProgress.ProgressBar;
import mekanism.client.gui.element.GuiSlot.SlotOverlay;
import mekanism.client.gui.element.GuiSlot.SlotType;
import mekanism.client.render.MekanismRenderer;
import mekanism.client.sound.SoundHandler;
import mekanism.common.Mekanism;
import mekanism.common.base.TileNetworkList;
import mekanism.common.inventory.container.ContainerMetallurgicInfuser;
import mekanism.common.network.PacketTileEntity.TileEntityMessage;
import mekanism.common.tile.TileEntityMetallurgicInfuser;
import mekanism.common.util.LangUtils;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.MekanismUtils.ResourceType;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiMetallurgicInfuser extends GuiMekanismBase {

    public TileEntityMetallurgicInfuser tileEntity;

    public GuiMetallurgicInfuser(InventoryPlayer inventory, TileEntityMetallurgicInfuser tentity) {
        super(tentity, new ContainerMetallurgicInfuser(inventory, tentity), "GuiMetallurgicInfuser.png", tentity.energyPerTick);
        tileEntity = tentity;

        guiElements.addAll(
              new ElementBuilderPowered(tileEntity, this, "GuiMetallurgicInfuser.png")
                    .addPowerBar(164, 15)
                    .addSideConfiguration()
                    .addTransporter()
                    .addSlotNoOverlay(SlotType.EXTRA, 16, 34)
                    .addSlot(SlotType.INPUT, SlotOverlay.INPUT, 50, 42)
                    .addSlotPower(142, 34)
                    .addSlot(SlotType.OUTPUT, SlotOverlay.OUTPUT, 108, 42)
                    .addProgress(tileEntity::getScaledProgress, ProgressBar.MEDIUM, 70, 46)
                    .build()
        );
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        int xAxis = (mouseX - (width - xSize) / 2);
        int yAxis = (mouseY - (height - ySize) / 2);

        fontRenderer.drawString(tileEntity.getName(), 45, 6, 0x404040);
        fontRenderer.drawString(LangUtils.localize("container.inventory"), 8, (ySize - 96) + 2, 0x404040);

        if (xAxis >= 7 && xAxis <= 11 && yAxis >= 17 && yAxis <= 69) {
            drawHoveringText(
                  tileEntity.infuseStored.type != null ? tileEntity.infuseStored.type.getLocalizedName() + ": "
                        + tileEntity.infuseStored.amount : LangUtils.localize("gui.empty"), xAxis, yAxis);
        }

        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTick, int mouseX, int mouseY) {
        mc.renderEngine.bindTexture(MekanismUtils.getResource(ResourceType.GUI, "GuiMetallurgicInfuser.png"));
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int guiWidth = (width - xSize) / 2;
        int guiHeight = (height - ySize) / 2;
        drawTexturedModalRect(guiWidth, guiHeight, 0, 0, xSize, ySize);

        if (tileEntity.infuseStored.type != null) {
            mc.renderEngine.bindTexture(MekanismRenderer.getBlocksTexture());
            int displayInt = tileEntity.getScaledInfuseLevel(52);
            drawTexturedRectFromIcon(guiWidth + 7, guiHeight + 17 + 52 - displayInt,
                  tileEntity.infuseStored.type.sprite, 4, displayInt);
        }

        super.drawGuiContainerBackgroundLayer(partialTick, mouseX, mouseY);
    }

    @Override
    protected void mouseClicked(int x, int y, int button) throws IOException {
        super.mouseClicked(x, y, button);

        if (button == 0) {
            int xAxis = (x - (width - xSize) / 2);
            int yAxis = (y - (height - ySize) / 2);

            if (xAxis > 148 && xAxis < 168 && yAxis > 73 && yAxis < 82) {
                TileNetworkList data = TileNetworkList.withContents(0);

                Mekanism.packetHandler.sendToServer(new TileEntityMessage(Coord4D.get(tileEntity), data));
                SoundHandler.playSound(SoundEvents.UI_BUTTON_CLICK);
            }
        }
    }
}
