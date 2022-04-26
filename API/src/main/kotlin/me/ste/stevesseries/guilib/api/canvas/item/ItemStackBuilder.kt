package me.ste.stevesseries.guilib.api.canvas.item

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

class ItemStackBuilder(
    private val stack: ItemStack = ItemStack(Material.STONE, 1)
) {
    companion object {
        /**
         * Shortcut for ItemStackBuilder(init).build().
         */
        fun build(init: ItemStackBuilder.() -> Unit) = ItemStackBuilder(init).build()
    }

    constructor(init: ItemStackBuilder.() -> Unit) : this() {
        this.init()
    }

    /**
     * Perform operations on the [ItemStack].
     */
    fun stack(init: ItemStack.() -> Unit): ItemStackBuilder {
        this.stack.init()
        return this
    }

    /**
     * Perform operations on the [ItemStack]'s [ItemMeta].
     *
     * @throws IllegalArgumentException if the item's meta is not an instance of [clazz].
     * @param clazz The type of the item meta.
     */
    fun <T : ItemMeta> meta(clazz: Class<T>, init: T.() -> Unit): ItemStackBuilder {
        val meta = this.stack.itemMeta ?: throw IllegalStateException("Item meta is null.")

        if (!clazz.isInstance(meta)) {
            throw IllegalArgumentException("The item stack meta (${meta.javaClass}) is not an instance of $clazz.")
        }

        val castMeta = clazz.cast(meta)
        castMeta.init()

        this.stack.itemMeta = castMeta

        return this
    }

    /**
     * Like the other method, but the class does not need to be provided.
     */
    inline fun <reified T : ItemMeta> meta(noinline init: T.() -> Unit) =
        this.meta(T::class.java, init)

    fun build() = this.stack
}