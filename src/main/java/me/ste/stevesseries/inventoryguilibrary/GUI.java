package me.ste.stevesseries.inventoryguilibrary;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

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

    /**
     * Get the player who has the GUI open
     * @return the player
     */
    public final Player getPlayer() {
        return this.player;
    }

    /**
     * Get the inventory type of the GUI
     * @return the inventory type
     */
    public final InventoryType getInventoryType() {
        return this.inventoryType;
    }

    /**
     * Get the size of the GUI
     * @return the size
     */
    public final int getSize() {
        return this.size;
    }

    /**
     * Get the title of the GUI
     * @return the title
     */
    public final String getTitle() {
        return this.title;
    }

    /**
     * Refresh the GUI
     */
    public final void refresh() {
        InventoryGUILibrary.getInstance().refreshGUI(this.player);
    }

    /**
     * Update the specified inventory with the GUI's content
     * @param inventory target inventory
     */
    public abstract void updateInventory(Inventory inventory);

    /**
     * Handle specific inventory click
     * @param stack clicked item
     * @param clickType type of the click
     * @param slot clicked slot
     * @param inventory clicked inventory
     */
    public void handleClick(ItemStack stack, ClickType clickType, int slot, Inventory inventory) {}

    /**
     * Handle the opening of the specified inventory
     * @param inventory opened inventory
     */
    public void handleOpening(Inventory inventory) {}

    /**
     * Handle the closing of the specified inventory
     * @param inventory closed inventory
     */
    public void handleClosing(Inventory inventory) {}

    /**
     * Get chest slot index by it's x and y coordinates
     * @param x x coordinate
     * @param y y coordinate
     * @return slot index
     */
    public static int getGridPositionIndex(int x, int y) {
        return y * 9 + x;
    }

    /**
     * Fill slots in the specified area with the specified item
     * @param inventory target inventory
     * @param x start x
     * @param y start y
     * @param w width
     * @param h height
     * @param fill the item to set
     */
    public static void fillItems(Inventory inventory, int x, int y, int w, int h, ItemStack fill) {
        for(int cx = x; cx < x + w; cx++) {
            for(int cy = y; cy < y + h; cy++) {
                inventory.setItem(GUI.getGridPositionIndex(cx, cy), fill);
            }
        }
    }
}