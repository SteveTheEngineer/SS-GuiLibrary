package me.ste.stevesseries.inventoryguilibrary;

import me.ste.stevesseries.inventoryguilibrary.inventory.GridInventory;
import me.ste.stevesseries.inventoryguilibrary.widget.Widget;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * @deprecated the class is left for legacy support. <strong>If you still use this class, please switch to the new system as soon as possible!</strong>
 * @see Widget
 */
@Deprecated
public abstract class GUI extends Widget {
    private final InventoryType inventoryType;
    private final int size;
    private final String title;

    public GUI(Player player, InventoryType inventoryType, int size, String title) {
        super(0, 0, size, 1);
        this.setPlayer(player);
        this.inventoryType = inventoryType;
        this.size = size;
        this.title = title;
    }

    public GUI(Player player, int size, String title)  {
        this(player, InventoryType.CHEST, size, title);
    }

    public final InventoryType getInventoryType() {
        return this.inventoryType;
    }

    public final int getSize() {
        return this.size;
    }

    public final String getTitle() {
        return this.title;
    }

    public void refresh() {
        this.rerender();
    }

    public abstract void updateInventory(Inventory inventory);

    public void handleClick(ItemStack stack, ClickType clickType, int slot, Inventory inventory) {}

    public void handleOpening(Inventory inventory) {}

    public void handleClosing(Inventory inventory) {}

    public static int getGridPositionIndex(int x, int y) {
        return y * 9 + x;
    }

    public static void fillItems(Inventory inventory, int x, int y, int w, int h, ItemStack fill) {
        for(int cx = x; cx < x + w; cx++) {
            for(int cy = y; cy < y + h; cy++) {
                inventory.setItem(GUI.getGridPositionIndex(cx, cy), fill);
            }
        }
    }

    @Override
    public void render(GridInventory grid) {
        this.updateInventory(grid.getHandle());
    }

    @Override
    public void onClick(int x, int y, ClickType type, GridInventory grid) {
        this.handleClick(grid.get(x, y), type, x, grid.getHandle());
    }

    @Override
    public void onCreation(GridInventory grid) {
        this.handleOpening(grid.getHandle());
    }

    @Override
    public void onDestruction(GridInventory grid) {
        this.handleClosing(grid.getHandle());
    }

    @Override
    public Inventory createInventory() {
        if(this.inventoryType == InventoryType.CHEST) {
            return Bukkit.createInventory(null, this.size, this.title);
        } else {
            return Bukkit.createInventory(null, this.inventoryType, this.title);
        }
    }
}