package me.ste.stevesseries.inventoryguilibrary;

import me.ste.stevesseries.inventoryguilibrary.inventory.GridInventory;
import me.ste.stevesseries.inventoryguilibrary.widget.Widget;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * @deprecated legacy support. Returned by ({@link InventoryGUILibrary#getPlayerGUI(Player)} if the GUI open uses the newer widget API
 */
@Deprecated
public class GUIWidgetWrapper extends GUI {
    private final Widget widget;

    public GUIWidgetWrapper(Widget widget) {
        super(widget.getPlayer(), InventoryType.CHEST, widget.getWidth() * widget.getHeight(), widget.getClass().getSimpleName());
        this.widget = widget;
    }

    @Override
    public void updateInventory(Inventory inventory) {
        this.widget.render(new GridInventory(inventory, this.widget.getWidth(), this.widget.getHeight()));
    }

    @Override
    public void refresh() {
        this.widget.rerender();
    }

    @Override
    public void handleClick(ItemStack stack, ClickType clickType, int slot, Inventory inventory) {
        int[] position = GridInventory.getPositionByIndex(slot, this.widget.getWidth());
        this.widget.onClick(position[0], position[1], clickType, new GridInventory(inventory, this.widget.getWidth(), this.widget.getHeight()));
    }

    @Override
    public void handleOpening(Inventory inventory) {
        this.widget.onCreation(new GridInventory(inventory, this.widget.getWidth(), this.widget.getHeight()));
    }

    @Override
    public void handleClosing(Inventory inventory) {
        this.widget.onDestruction(new GridInventory(inventory, this.widget.getWidth(), this.widget.getHeight()));
    }
}