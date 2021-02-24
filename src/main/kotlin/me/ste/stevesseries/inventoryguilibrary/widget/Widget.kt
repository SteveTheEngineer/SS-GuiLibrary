package me.ste.stevesseries.inventoryguilibrary.widget

import me.ste.stevesseries.inventoryguilibrary.PlayerGUIS
import me.ste.stevesseries.inventoryguilibrary.inventory.ItemCanvas
import me.ste.stevesseries.inventoryguilibrary.inventory.Position2I
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.DragType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import java.util.*
import java.util.function.Consumer

abstract class Widget(
    /**
     * The X position of the widget
     */
    var x: Int,
    /**
     * The Y position of the widget
     */
    var y: Int,
    /**
     * The width of the widget
     */
    var width: Int,
    /**
     * The height of the widget
     */
    var height: Int
) {
    /**
     * The parent of the widget
     */
    var parent: Widget? = null

    /**
     * The player who currently sees the widget
     */
    lateinit var player: Player

    /**
     * The child widgets of the widget
     */
    val children: MutableList<Widget> = LinkedList()

    /**
     * The X position relative to the root element
     */
    val rootX: Int
        get() = ((this.parent?.rootX) ?: 0) + this.x

    /**
     * The Y position relative to the root element
     */
    val rootY: Int
        get() = ((this.parent?.rootY) ?: 0) + this.y

    /**
     * Add a child widget
     * @param widget widget to add
     */
    protected fun addChild(widget: Widget) {
        this.children += widget
        widget.parent = this
    }

    /**
     * Add a child widget
     * @param widget widget to add
     */
    protected operator fun plusAssign(value: Widget) = this.addChild(value)

    /**
     * Re-render the root widget. Must be called whenever something has changed and needs updating on the screen
     */
    fun rerender() {
        PlayerGUIS.rerender(this.player)
    }

    /**
     * Run the specified modifier on the whole hierarchy of the widgets. This also includes the specified widget
     * @param modifier the modifier
     */
    fun recurse(modifier: Consumer<Widget>) {
        modifier.accept(this)
        for (child in ArrayList(this.children)) {
            child.recurse(modifier)
        }
    }

    /**
     * Render the widget on the specified canvas
     * @param canvas the canvas
     */
    abstract fun render(canvas: ItemCanvas)

    /**
     * Create an inventory to render this widget as a GUI. Called when the widget is open as a GUI using [PlayerGUIS.set] or [PlayerGUIS.openGui]
     * @return the inventory
     */
    open fun createInventory(): Inventory? = null

    /**
     * Called whenever a player clicks the specified slot. This does not include number keys, [Widget.numberKey] is used for that purpose. **You will generally want to ignore double clicks ([ClickType.DOUBLE_CLICK]), or handle them separately**
     * @param x x position
     * @param y y position
     * @param type click type
     */
    open fun click(x: Int, y: Int, type: ClickType) = Unit

    /**
     * Called whenever a player presses a number key on their keyboard over an item
     * @param x x position
     * @param y y position
     * @param hotbarButton hotbar button
     */
    open fun numberKey(x: Int, y: Int, hotbarButton: Int) = Unit

    /**
     * Called whenever a player drags an item stack across the inventory
     * @param newItems items that were going to be added to the inventory
     * @param type type of the drag
     * @param newCursor remaining items after the drag
     */
    open fun drag(newItems: Map<Position2I, ItemStack>, newCursor: ItemStack?, type: DragType) = Unit

    /**
     * Called when the widget has been open in a GUI
     */
    open fun create() = Unit

    /**
     * Called when the GUI has been closed
     */
    open fun destroy() = Unit
}