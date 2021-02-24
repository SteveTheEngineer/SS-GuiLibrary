package me.ste.stevesseries.inventoryguilibrary.widget.builtin

import me.ste.stevesseries.inventoryguilibrary.inventory.ItemCanvas
import me.ste.stevesseries.inventoryguilibrary.widget.Widget
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.ItemStack
import java.util.function.Consumer
import java.util.function.Supplier

/**
 * This widget represents a rectangle with a click handler
 */
open class DynamicButton(
    x: Int,
    y: Int,
    width: Int,
    height: Int,
    /**
     * Display item supplier
     */
    private val itemSupplier: Supplier<ItemStack>,
    /**
     * Called whenever the button is clicked
     */
    private val clickConsumer: Consumer<ClickType>
) : Widget(x, y, width, height) {
    override fun render(canvas: ItemCanvas) = canvas.fill(0, 0, this.width, this.height, this.itemSupplier.get())

    override fun click(x: Int, y: Int, type: ClickType) = if (type != ClickType.DOUBLE_CLICK) {
        this.clickConsumer.accept(type)
        this.rerender()
    } else Unit
}