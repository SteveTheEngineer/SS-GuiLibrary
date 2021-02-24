package me.ste.stevesseries.inventoryguilibrary.inventory

import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import kotlin.math.floor

class ItemCanvas(
    /**
     * The width of the canvas
     */
    val width: Int,
    /**
     * The height of the canvas
     */
    val height: Int
) {
    private val buffer: Array<ItemStack?> = arrayOfNulls(this.width * this.height)

    /**
     * Get the item stack at the specified position
     * @param x x position
     * @param y y position
     * @return item stack
     */
    operator fun get(x: Int, y: Int): ItemStack? =
        if (x < 0 || x < 0 || x >= this.width || y >= this.height)
            null
        else
            this.buffer[y * this.width + x]

    /**
     * Get the item stack at the given position
     * @param x x position
     * @param y y position
     * @return item stack
     */
    operator fun set(x: Int, y: Int, stack: ItemStack?) {
        if (x >= 0 && y >= 0 && x < this.width && y < this.height) {
            this.buffer[y * this.width + x] =
                if (stack == null || !stack.type.isAir || stack.amount > 0) stack else null
        }
    }

    /**
     * Fill the items within the given area
     * @param x x position
     * @param y y position
     * @param w width
     * @param h height
     * @param stack item stack to fill with
     */
    fun fill(x: Int, y: Int, width: Int, height: Int, stack: ItemStack?) {
        for (cx in x until x + width) {
            for (cy in y until y + height) {
                this[cx, cy] = stack
            }
        }
    }

    /**
     * Clear the canvas
     */
    fun clear() {
        this.fill(0, 0, this.width, this.height, null)
    }

    /**
     * Draw the canvas content on the specified canvas
     * @param canvas canvas to draw on
     * @param x target x
     * @param y target y
     */
    fun drawOn(canvas: ItemCanvas, x: Int, y: Int) {
        for (cx in 0 until this.width) {
            for (cy in 0 until this.height) {
                canvas[x + cx, y + cy] = this[cx, cy]
            }
        }
    }

    /**
     * Draw the canvas content on the specified inventory
     * @param inventory inventory to draw on
     * @param x target x
     * @param y target y
     */
    fun drawOn(inventory: Inventory, x: Int, y: Int) {
        for (cx in 0 until this.width) {
            for (cy in 0 until this.height) {
                inventory.setItem((y + cy) * this.width + x + cx, this[cx, cy])
            }
        }
    }

    companion object {
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
        @JvmStatic
        fun isInside(x: Int, y: Int, ax: Int, ay: Int, w: Int, h: Int) = x >= ax && y >= ay && x < ax + w && y < ay + h

        /**
         * Get the position in an inventory by the index of the slot
         * @param index slot index
         * @param width the width of the inventory
         * @return inventory position
         */
        @JvmStatic
        fun getPositionByIndex(index: Int, width: Int) =
            Position2I(index % width, floor(index.toDouble() / width.toDouble()).toInt())
    }
}