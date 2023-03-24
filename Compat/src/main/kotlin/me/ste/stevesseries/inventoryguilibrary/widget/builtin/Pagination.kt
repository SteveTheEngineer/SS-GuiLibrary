package me.ste.stevesseries.inventoryguilibrary.widget.builtin

import me.ste.stevesseries.inventoryguilibrary.inventory.ItemCanvas
import me.ste.stevesseries.inventoryguilibrary.widget.Widget
import java.util.function.Supplier
import kotlin.math.ceil

@Deprecated("For backwards compatibility only.")
class Pagination(x: Int, y: Int, width: Int, height: Int, private val itemsSupplier: Supplier<List<Widget>>) :
    Widget(x, y, width, height) {
    var page = 0
    var pages = 0
        private set

    override fun render(canvas: ItemCanvas) {
        val items = this.itemsSupplier.get()
        this.pages = ceil((items.size.toFloat() / (this.width * this.height).toFloat()).toDouble()).toInt()
        if (this.page > 0 && this.page >= this.pages) {
            this.page = this.pages - 1
        }

        this.children.clear()
        for ((i, widget) in items.subList(
            this.page * this.width * this.height,
            items.size.coerceAtMost((this.page + 1) * this.width * this.height)
        ).withIndex()) {
            val position = ItemCanvas.getPositionByIndex(i, this.width)
            widget.x = position.x
            widget.y = position.y
            widget.width = 1
            widget.height = 1
            widget.player = this.player
            this += widget
        }
    }
}