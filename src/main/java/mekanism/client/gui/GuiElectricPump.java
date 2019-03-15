package mekanism.client.gui;

import mekanism.client.gui.element.GuiEnergyInfo;
import mekanism.client.gui.element.GuiGauge.Type;
import mekanism.client.gui.element.GuiPowerBar;
import mekanism.client.gui.element.GuiSlot.SlotType;
import mekanism.common.inventory.container.ContainerElectricPump;
import mekanism.common.tile.TileEntityElectricPump;
import mekanism.common.util.LangUtils;
import mekanism.common.util.ListUtils;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.MekanismUtils.ResourceType;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiElectricPump extends GuiMekanism {

    public TileEntityElectricPump tileEntity;

    public ResourceLocation guiLocation = MekanismUtils.getResource(ResourceType.GUI, "GuiElectricPump.png");

    public GuiElectricPump(InventoryPlayer inventory, TileEntityElectricPump tentity) {
        super(tentity, new ContainerElectricPump(inventory, tentity));
        tileEntity = tentity;

        guiElements.addAll(
              new ElementBuilder(tileEntity, this, guiLocation)
                    .addRedstone()
                    .addSecurity()
                    .addUpgrade()
                    .addFluidGauge(() -> tileEntity.fluidTank, Type.STANDARD, 6, 13)
                    .addSlotNoOverlay(SlotType.NORMAL, 27, 19)
                    .addSlotNoOverlay(SlotType.NORMAL, 27, 50)
                    .addSlotPower(142, 34)
                    .build()
        );
        guiElements.add(new GuiPowerBar(this, tileEntity, guiLocation, 164, 15));
        guiElements.add(new GuiEnergyInfo(() ->
        {
            String multiplier = MekanismUtils.getEnergyDisplay(tileEntity.energyPerTick);
            return ListUtils.asList(LangUtils.localize("gui.using") + ": " + multiplier + "/t",
                  LangUtils.localize("gui.needed") + ": " + MekanismUtils
                        .getEnergyDisplay(tileEntity.getMaxEnergy() - tileEntity.getEnergy()));
        }, this, guiLocation));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRenderer
              .drawString(tileEntity.getName(), (xSize / 2) - (fontRenderer.getStringWidth(tileEntity.getName()) / 2),
                    6, 0x404040);
        fontRenderer.drawString(LangUtils.localize("container.inventory"), 8, (ySize - 94) + 2, 0x404040);
        fontRenderer
              .drawString(MekanismUtils.getEnergyDisplay(tileEntity.getEnergy(), tileEntity.getMaxEnergy()), 51, 26,
                    0x00CD00);

        String text =
              tileEntity.fluidTank.getFluid() != null ? LangUtils.localizeFluidStack(tileEntity.fluidTank.getFluid())
                    + ": " + tileEntity.fluidTank.getFluid().amount : LangUtils.localize("gui.noFluid");
        renderScaledText(text, 51, 35, 0x00CD00, 74);

        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTick, int mouseX, int mouseY) {
        mc.renderEngine.bindTexture(guiLocation);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int guiWidth = (width - xSize) / 2;
        int guiHeight = (height - ySize) / 2;
        drawTexturedModalRect(guiWidth, guiHeight, 0, 0, xSize, ySize);

        super.drawGuiContainerBackgroundLayer(partialTick, mouseX, mouseY);
    }
}
