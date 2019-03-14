package mekanism.client.gui;

import java.util.HashSet;
import java.util.Set;
import mekanism.client.gui.element.GuiElement;
import mekanism.client.gui.element.GuiGasGauge;
import mekanism.client.gui.element.GuiGauge;
import mekanism.client.gui.element.GuiProgress;
import mekanism.client.gui.element.GuiProgress.IProgressInfoHandler;
import mekanism.client.gui.element.GuiProgress.ProgressBar;
import mekanism.client.gui.element.GuiRedstoneControl;
import mekanism.client.gui.element.GuiSecurityTab;
import mekanism.client.gui.element.GuiSideConfigurationTab;
import mekanism.client.gui.element.GuiSlot;
import mekanism.client.gui.element.GuiSlot.SlotOverlay;
import mekanism.client.gui.element.GuiSlot.SlotType;
import mekanism.client.gui.element.GuiTransporterConfigTab;
import mekanism.client.gui.element.GuiUpgradeTab;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.MekanismUtils.ResourceType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class ElementBuilder {

    private IGuiWrapper gui;
    private ResourceLocation def;
    private TileEntity tile;
    private Set<GuiElement> elements = new HashSet<>();

    public ElementBuilder(TileEntity tile, IGuiWrapper gui, String textureName) {
        this.tile = tile;
        this.gui = gui;
        this.def = MekanismUtils.getResource(ResourceType.GUI, textureName);
    }

    public ElementBuilder addSlot(SlotType type, SlotOverlay overlay, int x, int y) {
        elements.add(new GuiSlot(type, gui, def, x, y)
              .with(overlay));
        return this;
    }

    public ElementBuilder addSlotPower(int x, int y) {
        return this.addSlot(SlotType.POWER, SlotOverlay.POWER, x, y);
    }

    public ElementBuilder addSideConfiguration() {
        elements.add(new GuiSideConfigurationTab(gui, tile, def));
        return this;
    }

    //TODO - mekanism really needs a baseline machine, or I maybe can't have this here
    public ElementBuilder addPowerBar(int x, int y) {
//        elements.add(new GuiPowerBar(gui, tile,
//              def, 160, 23));
        return this;
    }

    public ElementBuilder addTransporter(int y) {
        elements.add(new GuiTransporterConfigTab(gui, y, tile, def));
        return this;
    }

    public ElementBuilder addGasGauge(GuiGasGauge.IGasInfoHandler handler, GuiGauge.Type type, int x, int y) {
        elements.add(new GuiGasGauge(handler, type, gui, def, x, y));
        return this;
    }

    public ElementBuilder addSecurity() {
        elements.add(new GuiSecurityTab(gui, tile, def));
        return this;
    }

    public ElementBuilder addRedstone() {
        elements.add(new GuiRedstoneControl(gui, tile, def));
        return this;
    }

    public ElementBuilder addUpgrade() {
        elements.add(new GuiUpgradeTab(gui, tile, def));
        return this;
    }

    public ElementBuilder addProgress(GuiProgress.IProgressInfoHandler handler, GuiProgress.ProgressBar type, int x, int y) {
        elements.add(new GuiProgress(handler, type, gui, def, x, y));
        return this;
    }

    public Set<GuiElement> build() {
        return elements;
    }

}
