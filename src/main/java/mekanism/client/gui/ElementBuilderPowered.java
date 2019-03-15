package mekanism.client.gui;

import mekanism.client.gui.element.GuiPowerBar;
import mekanism.common.tile.prefab.TileEntityElectricBlock;
import net.minecraft.util.ResourceLocation;

public class ElementBuilderPowered extends ElementBuilder {

    private TileEntityElectricBlock poweredTile;

    public ElementBuilderPowered(TileEntityElectricBlock poweredTile, IGuiWrapper gui, String textureName) {
        super(poweredTile, gui, textureName);
        this.poweredTile = poweredTile;
    }

    public ElementBuilderPowered(TileEntityElectricBlock poweredTile, IGuiWrapper gui, ResourceLocation def) {
        super(poweredTile, gui, def);
        this.poweredTile = poweredTile;
    }


    /**
     * This must be called first when adding elements so the  types match up
     */
    public ElementBuilder addPowerBar(int x, int y) {
        elements.add(new GuiPowerBar(gui, poweredTile, def, x, y));
        return this;
    }
}