package mekanism.client.gui;

import java.util.Arrays;
import mekanism.client.gui.element.GuiEnergyInfo;
import mekanism.common.tile.prefab.TileEntityElectricBlock;
import mekanism.common.util.LangUtils;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.MekanismUtils.ResourceType;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

//needs better name but for now just try and dedupe code
public class GuiMekanismBase extends GuiMekanism {

    public GuiMekanismBase(TileEntityElectricBlock tileEntity, Container container, String imageName, double energy) {
        this(tileEntity, container, MekanismUtils.getResource(ResourceType.GUI, imageName), energy);
    }

    public GuiMekanismBase(TileEntityElectricBlock tileEntity, Container container, ResourceLocation def, double energy) {
        super(container);

        guiElements.addAll(
              new ElementBuilder(tileEntity, this, def)
              .addSecurity()
              .addRedstone()
              .addUpgrade()
              .build()
        );

        guiElements.add(new GuiEnergyInfo(() ->
        {
            String usage = MekanismUtils.getEnergyDisplay(energy);
            return Arrays.asList(LangUtils.localize("gui.using") + ": " + usage + "/t",
                  LangUtils.localize("gui.needed") + ": " + MekanismUtils
                        .getEnergyDisplay(tileEntity.getMaxEnergy() - tileEntity.getEnergy()));
        }, this, def));
    }
}
