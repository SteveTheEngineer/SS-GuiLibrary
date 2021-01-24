package me.ste.stevesseries.inventoryguilibrary.inventory;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GridInventory {
    private final Inventory handle;
    private final int width;
    private final int height;

    public GridInventory(Inventory handle, int width, int height) {
        this.handle = handle;
        this.width = width;
        this.height = height;
    }

    /**
     * Set the item stack at the given position
     * @param x x position
     * @param y y position
     * @param stack item stack
     */
    public void set(int x, int y, ItemStack stack) {
        this.handle.setItem(y * this.width + x, stack);
    }

    /**
     * Get the item stack at the given position
     * @param x x position
     * @param y y position
     * @return item stack
     */
    public ItemStack get(int x, int y) {
        return this.handle.getItem(y * this.width + x);
    }

    /**
     * Fill the items within the given area
     * @param x x position
     * @param y y position
     * @param w width
     * @param h height
     * @param stack item stack
     */
    public void fill(int x, int y, int w, int h, ItemStack stack) {
        for(int cx = x; cx < x + w; cx++) {
            for(int cy = y; cy < y + h; cy++) {
                this.set(cx, cy, stack);
            }
        }
    }

    /**
     * Clear the inventory
     */
    public void clear() {
        this.handle.clear();
    }

    /**
     * Get the bukkit inventory
     * @return the bukkit inventory
     */
    public Inventory getHandle() {
        return this.handle;
    }

    /**
     * Check whether the position is inside the given bounds
     * @param x x position
     * @param y y position
     * @param ax start x
     * @param ay start y
     * @param w area width
     * @param h area height
     * @return true, if the position is inside the given bounds
     */
    public static boolean isInside(int x, int y, int ax, int ay, int w, int h) {
        return x >= ax && y >= ay && x < ax + w && y < ay + h;
    }

    /**
     * Get the position in the inventory by it's slot index
     * @param index slot index
     * @param width the width of the inventory
     * @return 2 element array, with the first being the x position and the second being the y
     */
    public static int[] getPositionByIndex(int index, int width) {
        return new int[] {index % width, (int) Math.floor((double) index / (double) width)};
    }
}