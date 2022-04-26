package me.ste.stevesseries.guilib.api.canvas

import me.ste.stevesseries.guilib.api.canvas.item.CanvasItem
import me.ste.stevesseries.guilib.api.canvas.item.CanvasItemBuilder
import java.util.function.BiPredicate
import kotlin.math.max
import kotlin.math.min

open class ItemCanvas(val width: Int, val height: Int) {
    init {
        if (width < 0) {
            throw IllegalArgumentException("Width cannot be negative")
        }
        if (height < 0) {
            throw IllegalArgumentException("Height cannot be negative")
        }
    }

    val items = arrayOfNulls<CanvasItem>(this.width * this.height)

    /**
     * Get the slot number from the provided [x] and [y].
     */
    fun getSlotIndex(x: Int, y: Int) = this.width * y + x

    /**
     * @return true, if the position is inside the canvas area.
     */
    fun isInBounds(x: Int, y: Int) = x >= 0 && y >= 0 && x < this.width && y < this.height

    /**
     * @throws IllegalArgumentException if the position is outside of the canvas area.
     */
    private fun checkPosition(x: Int, y: Int) {
        if (!this.isInBounds(x, y)) {
            throw IllegalArgumentException("Position out of bounds: x $x, y $y.")
        }
    }

    /**
     * Sets the item at [x], [y] to [item].
     */
    operator fun set(x: Int, y: Int, item: CanvasItem?) {
        this.checkPosition(x, y)

        val index = this.getSlotIndex(x, y)
        this.items[index] = item
    }

    /**
     * @return the item at [x], [y]
     */
    operator fun get(x: Int, y: Int): CanvasItem? {
        this.checkPosition(x, y)

        val index = this.getSlotIndex(x, y)
        return this.items[index]
    }

    /**
     * Draws a rectangle of [item] starting at [startX], [startY] with the provided [width] and [height].
     * @param positionFilter Allows for more complex patterns, like [outline].
     */
    fun fill(
        startX: Int,
        startY: Int,
        width: Int,
        height: Int,
        item: CanvasItem?,
        positionFilter: BiPredicate<Int, Int> = BiPredicate { _, _ -> true }
    ) {
        if (!this.isInBounds(startX, startY)) {
            throw IllegalArgumentException("Fill start position out of bounds: x $startX, y $startY.")
        }

        if (width == 0 || height == 0) {
            return
        }

        val endX = startX + width - 1
        val endY = startY + height - 1

        if (!this.isInBounds(endX, endY)) {
            throw IllegalArgumentException("Fill end position out of bounds: x $endX (startX $startX + width $width), y $endY (startY $startY + height $height).")
        }

        val minX = min(startX, endX)
        val minY = min(startY, endY)
        val maxX = max(startX, endX)
        val maxY = max(startY, endY)

        for (x in (minX..maxX)) {
            for (y in (minY..maxY)) {
                if (!positionFilter.test(x, y)) {
                    continue
                }

                val index = this.getSlotIndex(x, y)
                this.items[index] = item
            }
        }
    }

    /**
     * Shortcut to calling [fill] with a null item.
     */
    fun clear(startX: Int, startY: Int, width: Int, height: Int) {
        this.fill(startX, startY, width, height, null)
    }

    /**
     * Fills the entire are with the provided [item].
     */
    fun fill(item: CanvasItem?, positionFilter: BiPredicate<Int, Int> = BiPredicate { _, _ -> true }) {
        this.fill(0, 0, this.width, this.height, item, positionFilter)
    }

    /**
     * Clears the whole canvas.
     */
    fun clear() {
        this.fill(null)
    }

    /**
     * Draws an outline rectangle of [item] starting at [startX], [startY] with the provided [width] and [height].
     */
    fun outline(startX: Int, startY: Int, width: Int, height: Int, item: CanvasItem?) {
        val endX = startX + width - 1
        val endY = startY + height - 1

        this.fill(startX, startY, width, height, item) { x, y -> x == startX || y == startY || x == endX || y == endY }
    }

    // Build
    /**
     * Sets the item at [x], [y] to the item provided by the [CanvasItemBuilder] initialized by the [init] function.
     */
    fun item(x: Int, y: Int, init: CanvasItemBuilder.() -> Unit) {
        val item = CanvasItemBuilder.build(init)
        this[x, y] = item
    }

    /**
     * Fills the provided area with the item provided by the [CanvasItemBuilder] initialized by the [init] function.
     */
    fun fill(x: Int, y: Int, width: Int, height: Int, init: CanvasItemBuilder.() -> Unit) {
        val item = CanvasItemBuilder.build(init)
        this.fill(x, y, width, height, item)
    }

    // Copy
    /**
     * Copies all items from the provided [canvas] to the [startX], [startY] position of this canvas.
     */
    fun copyFrom(startX: Int, startY: Int, canvas: ItemCanvas) {
        if (!this.isInBounds(startX, startY)) {
            throw IllegalArgumentException("Copy start position out of bounds: x $startX, y $startY")
        }

        if (canvas.width == 0 || canvas.height == 0) {
            return
        }

        val endX = startX + canvas.width - 1
        val endY = startY + canvas.height - 1

        if (!this.isInBounds(endX, endY)) {
            throw IllegalArgumentException("Copy end position out of bounds: x $endX (startX $startX + width ${canvas.width}), y $endY (startY $startY + height ${canvas.height}).")
        }

        val items = canvas.items

        for (x in 0 until canvas.width) {
            for (y in 0 until canvas.height) {
                val item = items[canvas.height * y + x]

                val targetX = startX + x
                val targetY = startY + y

                this.items[this.height * targetY + targetX] = item
            }
        }
    }
}
