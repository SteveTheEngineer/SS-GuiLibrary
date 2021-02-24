package me.ste.stevesseries.inventoryguilibrary.widget.builtin

import me.ste.stevesseries.inventoryguilibrary.inventory.ItemCanvas
import me.ste.stevesseries.inventoryguilibrary.widget.Widget
import org.bukkit.inventory.ItemStack

/**
 * This widget represents a horizontal bar with next and previous page buttons
 */
class PaginationBar(
    x: Int,
    y: Int,
    width: Int,
    height: Int,
    pagination: Pagination,
    previousPageFunction: ItemSupplier,
    nextPageFunction: ItemSupplier
) : Widget(x, y, width, height) {
    fun interface ItemSupplier {
        fun get(displayPage: Int, displayPages: Int): ItemStack
    }

    init {
        this += DynamicButton(
            0,
            0,
            1,
            this.height,
            { previousPageFunction.get(pagination.page + 1, 1.coerceAtLeast(pagination.pages)) },
            {
                if (pagination.page > 0) {
                    pagination.page--
                    this@PaginationBar.rerender()
                }
            })
        this += DynamicButton(
            this.width - 1,
            0,
            1,
            this.height,
            { nextPageFunction.get(pagination.page + 1, 1.coerceAtLeast(pagination.pages)) },
            {
                if (pagination.page < pagination.pages - 1) {
                    pagination.page++
                    this@PaginationBar.rerender()
                }
            })
    }

    override fun render(canvas: ItemCanvas) = Unit
}