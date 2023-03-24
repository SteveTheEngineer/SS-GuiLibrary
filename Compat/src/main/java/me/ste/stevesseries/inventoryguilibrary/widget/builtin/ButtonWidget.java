package me.ste.stevesseries.inventoryguilibrary.widget.builtin;

import me.ste.stevesseries.inventoryguilibrary.inventory.GridInventory;
import me.ste.stevesseries.inventoryguilibrary.widget.Widget;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * @deprecated For backwards compatibility only.
 */
@Deprecated
public abstract class ButtonWidget extends Widget {
    public ButtonWidget(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void render(GridInventory grid) {
        grid.fill(this.getRealX(), this.getRealY(), this.getWidth(), this.getHeight(), this.getItem());
    }

    @Override
    public void onClick(int x, int y, ClickType type, GridInventory grid) {
        if(type == ClickType.DOUBLE_CLICK) {
            return;
        }

        if(GridInventory.isInside(x, y, this.getRealX(), this.getRealY(), this.getWidth(), this.getHeight())) {
            this.click(type);
            this.rerender();
        }
    }

    protected abstract ItemStack getItem();

    protected void click(ClickType type) {
        this.click();
    }
    protected void click() {}
}
