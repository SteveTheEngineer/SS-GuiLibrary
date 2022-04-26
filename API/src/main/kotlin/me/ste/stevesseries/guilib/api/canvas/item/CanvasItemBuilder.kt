package me.ste.stevesseries.guilib.api.canvas.item

import me.ste.stevesseries.guilib.api.canvas.handler.ItemClickHandler
import me.ste.stevesseries.guilib.api.canvas.handler.ItemClickHandlerWithResult
import me.ste.stevesseries.guilib.api.canvas.item.ItemStackBuilder.Companion.build
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

class CanvasItemBuilder {
    companion object {
        /**
         * Shortcut for CanvasItemBuilder(init).build().
         */
        fun build(init: CanvasItemBuilder.() -> Unit) = CanvasItemBuilder(init).build()
    }

    // Public values
    /**
     * Makes the item null. An alternative is to set the item type to [org.bukkit.Material.AIR].
     */
    var isEmpty = false

    // Internal values
    private val stackBuilder = ItemStackBuilder()
    private val handlers = mutableListOf<ItemClickHandlerWithResult>()

    constructor(init: CanvasItemBuilder.() -> Unit) {
        this.init()
    }

    /**
     * Perform operations directly on the [ItemStackBuilder].
     */
    fun item(init: ItemStackBuilder.() -> Unit) {
        this.stackBuilder.init()
    }

    /**
     * Shortcut for the [ItemStackBuilder.stack] method of the [ItemStackBuilder].
     */
    fun stack(init: ItemStack.() -> Unit) = this.stackBuilder.stack(init)

    /**
     * Shortcut for the [ItemStackBuilder.meta] method of the [ItemStackBuilder].
     */
    fun <T : ItemMeta> meta(clazz: Class<T>, init: T.() -> Unit) = this.stackBuilder.meta(clazz, init)

    /**
     * Like the other method, but the class does not need to be provided.
     */
    inline fun <reified T : ItemMeta> meta(noinline init: T.() -> Unit) = this.meta(T::class.java, init)

    // Click handling
    /**
     * The provided [handler] will receive all click types.
     */
    fun click(handler: ItemClickHandlerWithResult): CanvasItemBuilder {
        this.handlers += handler
        return this
    }

    /**
     * The provided [handler] will only receive clicks of type [acceptType].
     */
    fun click(acceptType: ClickType, handler: ItemClickHandler): CanvasItemBuilder {
        this.handlers += ItemClickHandlerWithResult { x, y, type, numberKey ->
            if (type != acceptType) {
                return@ItemClickHandlerWithResult false
            }

            handler.handle(x, y, type, numberKey)

            true
        }

        return this
    }

    /**
     * The provided [handler] will only receive clicks, if [ClickType.isLeftClick] returns true, and it is not [ClickType.DOUBLE_CLICK].
     */
    fun leftClick(handler: ItemClickHandler): CanvasItemBuilder {
        this.handlers += ItemClickHandlerWithResult { x, y, type, numberKey ->
            if (!type.isLeftClick || type == ClickType.DOUBLE_CLICK) {
                return@ItemClickHandlerWithResult false
            }

            handler.handle(x, y, type, numberKey)

            true
        }

        return this
    }

    /**
     * The provided [handler] will only receive clicks, if [ClickType.isRightClick] returns true.
     */
    fun rightClick(handler: ItemClickHandler): CanvasItemBuilder {
        this.handlers += ItemClickHandlerWithResult { x, y, type, numberKey ->
            if (!type.isRightClick) {
                return@ItemClickHandlerWithResult false
            }

            handler.handle(x, y, type, numberKey)

            true
        }

        return this
    }

    fun build(): CanvasItem {
        val clickHandler = ItemClickHandler { x, y, type, numberKey ->
            for (handler in this.handlers) {
                if (handler.handle(x, y, type, numberKey)) {
                    return@ItemClickHandler
                } else {
                    continue
                }
            }
        }

        val item = if (!this.isEmpty) {
            this.stackBuilder.build()
        } else {
            null
        }

        return CanvasItem(item, clickHandler)
    }
}