package mekanism.client.gui;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import mekanism.client.gui.element.GuiElement;
import mekanism.client.gui.element.GuiFluidGauge;
import mekanism.client.gui.element.GuiGasGauge;
import mekanism.client.gui.element.GuiGauge;
import mekanism.client.gui.element.GuiHeatInfo;
import mekanism.client.gui.element.GuiProgress;
import mekanism.client.gui.element.GuiRedstoneControl;
import mekanism.client.gui.element.GuiSecurityTab;
import mekanism.client.gui.element.GuiSideConfigurationTab;
import mekanism.client.gui.element.GuiSlot;
import mekanism.client.gui.element.GuiSlot.SlotOverlay;
import mekanism.client.gui.element.GuiSlot.SlotType;
import mekanism.client.gui.element.GuiTransporterConfigTab;
import mekanism.client.gui.element.GuiUpgradeTab;
import mekanism.common.config.MekanismConfig.general;
import mekanism.common.util.LangUtils;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.MekanismUtils.ResourceType;
import mekanism.common.util.UnitDisplayUtils;
import mekanism.common.util.UnitDisplayUtils.TemperatureUnit;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class ElementBuilder {

    protected IGuiWrapper gui;
    protected ResourceLocation def;
    private TileEntity tile;
    protected Set<GuiElement> elements = new HashSet<>();

    public ElementBuilder(TileEntity tile, IGuiWrapper gui, String textureName) {
        this(tile, gui, MekanismUtils.getResource(ResourceType.GUI, textureName));
    }

    public ElementBuilder(TileEntity tile, IGuiWrapper gui, ResourceLocation def) {
        this.tile = tile;
        this.gui = gui;
        this.def = def;
    }

    public ElementBuilder addSlot(SlotType type, SlotOverlay overlay, int x, int y) {
        elements.add(new GuiSlot(type, gui, def, x, y)
              .with(overlay));
        return this;
    }

    //maybe there should be SlotOverlay.NONE for this?
    public ElementBuilder addSlotNoOverlay(SlotType type, int x, int y) {
        elements.add(new GuiSlot(type, gui, def, x, y));
        return this;
    }

    public ElementBuilder addSlotPower(int x, int y) {
        return this.addSlot(SlotType.POWER, SlotOverlay.POWER, x, y);
    }

    public ElementBuilder addSideConfiguration() {
        elements.add(new GuiSideConfigurationTab(gui, tile, def));
        return this;
    }

    public ElementBuilder addTransporter() {
        elements.add(new GuiTransporterConfigTab(gui, 34, tile, def));  //always seems to be called with 34
        return this;
    }

    public ElementBuilder addGasGauge(GuiGasGauge.IGasInfoHandler handler, GuiGauge.Type type, int x, int y) {
        elements.add(new GuiGasGauge(handler, type, gui, def, x, y));
        return this;
    }

    public ElementBuilder addFluidGauge(GuiFluidGauge.IFluidInfoHandler handler, GuiGauge.Type type, int x, int y) {
        elements.add(new GuiFluidGauge(handler, type, gui, def, x, y));
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

    //this seems to be the standard heat builder
    public ElementBuilder addHeatInfo(double loss) {
        elements.add(new GuiHeatInfo(() ->
        {
            TemperatureUnit unit = TemperatureUnit.values()[general.tempUnit.ordinal()];
            String environment = UnitDisplayUtils.getDisplayShort(loss * unit.intervalSize, false, unit);
            return Collections.singletonList(LangUtils.localize("gui.dissipated") + ": " + environment + "/t");
        }, gui, def));
        return this;
    }

    public Set<GuiElement> build() {
        return elements;
    }

}