package me.ste.stevesseries.inventoryguilibrary;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * @deprecated For backwards compatibility only.
 */
@Deprecated
public abstract class GUI {
    private final Player player;
    private final InventoryType inventoryType;
    private final int size;
    private final String title;

    public GUI(Player player, InventoryType inventoryType, int size, String title) {
        this.player = player;
        this.inventoryType = inventoryType;
        this.size = size;
        this.title = title;
    }

    public GUI(Player player, int size, String title)  {
        this(player, InventoryType.CHEST, size, title);
    }

    public final Player getPlayer() {
        return this.player;
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

    public final void refresh() {
        PlayerGUIS.INSTANCE.rerender(this.player);
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
}
