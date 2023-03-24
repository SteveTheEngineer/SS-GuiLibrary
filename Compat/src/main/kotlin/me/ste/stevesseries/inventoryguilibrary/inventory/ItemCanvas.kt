package me.ste.stevesseries.inventoryguilibrary.inventory

import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import kotlin.math.floor

@Deprecated("For backwards compatibility only.")
class ItemCanvas(
    val width: Int,
    val height: Int
) {
    private val buffer: Array<ItemStack?> = arrayOfNulls(this.width * this.height)

    operator fun get(x: Int, y: Int): ItemStack? =
        if (x < 0 || x < 0 || x >= this.width || y >= this.height)
            null
        else
            this.buffer[y * this.width + x]

    operator fun set(x: Int, y: Int, stack: ItemStack?) {
        if (x >= 0 && y >= 0 && x < this.width && y < this.height) {
            this.buffer[y * this.width + x] =
                if (stack == null || !stack.type.isAir || stack.amount > 0) stack else null
        }
    }

    fun fill(x: Int, y: Int, width: Int, height: Int, stack: ItemStack?) {
        for (cx in x until x + width) {
            for (cy in y until y + height) {
                this[cx, cy] = stack
            }
        }
    }

    fun clear() {
        this.fill(0, 0, this.width, this.height, null)
    }

    fun drawOn(canvas: ItemCanvas, x: Int, y: Int) {
        for (cx in 0 until this.width) {
            for (cy in 0 until this.height) {
                canvas[x + cx, y + cy] = this[cx, cy]
            }
        }
    }

    fun drawOnIgnoreNull(canvas: ItemCanvas, x: Int, y: Int) {
        for (cx in 0 until this.width) {
            for (cy in 0 until this.height) {
                canvas[x + cx, y + cy] = this[cx, cy] ?: continue
            }
        }
    }

    fun drawOn(inventory: Inventory, x: Int, y: Int) {
        for (cx in 0 until this.width) {
            for (cy in 0 until this.height) {
                inventory.setItem((y + cy) * this.width + x + cx, this[cx, cy])
            }
        }
    }

    companion object {
        @JvmStatic
        fun isInside(x: Int, y: Int, ax: Int, ay: Int, w: Int, h: Int) = x >= ax && y >= ay && x < ax + w && y < ay + h

        @JvmStatic
        fun getPositionByIndex(index: Int, width: Int) =
            Position2I(index % width, floor(index.toDouble() / width.toDouble()).toInt())
    }
}