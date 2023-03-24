package me.ste.stevesseries.inventoryguilibrary.inventory;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * @deprecated For backwards compatibility only.
 */
@Deprecated
public class GridInventory {
    public static GridInventory DUMMY = new GridInventory(new ItemCanvas(0, 0), 0, 0, 0, 0);

    private final ItemCanvas canvas;
    private final int offsetX;
    private final int offsetY;
    private final int width;
    private final int height;

    public GridInventory(Inventory handle, int width, int height) {
        throw new UnsupportedOperationException("Cannot create a compatibility version GridInventory using an Inventory handle!");
    }

    public GridInventory(ItemCanvas canvas, int offsetX, int offsetY, int width, int height) {
        this.canvas = canvas;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.width = width;
        this.height = height;
    }

    public void set(int x, int y, ItemStack stack) {
        this.canvas.set(x + this.offsetX, y + this.offsetY, stack);
    }

    public ItemStack get(int x, int y) {
        return this.canvas.get(x + this.offsetX, y + this.offsetY);
    }

    public void fill(int x, int y, int w, int h, ItemStack stack) {
        this.canvas.fill(x + this.offsetX, y + this.offsetY, w, h, stack);
    }

    public void clear() {
        this.canvas.clear();
    }

    public Inventory getHandle() {
        throw new UnsupportedOperationException("Cannot get the inventory handle of a compatibility version GridInventory!");
    }

    public static boolean isInside(int x, int y, int ax, int ay, int w, int h) {
        return x >= ax && y >= ay && x < ax + w && y < ay + h;
    }

    public static int[] getPositionByIndex(int index, int width) {
        return new int[] {index % width, (int) Math.floor((double) index / (double) width)};
    }
}
