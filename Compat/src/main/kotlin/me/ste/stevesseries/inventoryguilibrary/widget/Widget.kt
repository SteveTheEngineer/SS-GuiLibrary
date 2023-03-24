package me.ste.stevesseries.inventoryguilibrary.widget

import me.ste.stevesseries.inventoryguilibrary.PlayerGUIS
import me.ste.stevesseries.inventoryguilibrary.inventory.GridInventory
import me.ste.stevesseries.inventoryguilibrary.inventory.ItemCanvas
import me.ste.stevesseries.inventoryguilibrary.inventory.Position2I
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.DragType
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import java.util.*
import java.util.function.Consumer


@Deprecated("For backwards compatibility only.")
abstract class Widget(
    var x: Int,
    var y: Int,
    var width: Int,
    var height: Int
) {
    var parent: Widget? = null

    lateinit var player: Player

    val children: MutableList<Widget> = LinkedList()

    val rootX: Int
        get() = ((this.parent?.rootX) ?: 0) + this.x

    val rootY: Int
        get() = ((this.parent?.rootY) ?: 0) + this.y

    protected fun addChild(widget: Widget) {
        this.children += widget
        widget.parent = this
    }

    protected operator fun plusAssign(value: Widget) = this.addChild(value)

    fun rerender() {
        PlayerGUIS.rerender(this.player)
    }

    fun recurse(modifier: Consumer<Widget>) {
        modifier.accept(this)
        for (child in ArrayList(this.children)) {
            child.recurse(modifier)
        }
    }

    open fun render(canvas: ItemCanvas) {
        val grid = GridInventory(canvas, -this.rootX, -this.rootY, this.width, this.height)
        this.render(grid)
    }

    open fun createInventory(): Inventory? {
        val inventory = if (width == 9) {
            Bukkit.createInventory(null, 9 * height, this.javaClass.simpleName)
        } else if (width == 5 && height == 1) {
            Bukkit.createInventory(null, InventoryType.HOPPER, this.javaClass.simpleName)
        } else if (width == 3 && height == 3) {
            Bukkit.createInventory(null, InventoryType.DROPPER, this.javaClass.simpleName)
        } else {
            throw IllegalStateException("Unsupported inventory size: " + width + "x" + height)
        }
        return inventory
    }

    open fun click(x: Int, y: Int, type: ClickType) {
        this.onClick(x + this.rootX, y + this.rootY, type, GridInventory.DUMMY)
    }
    open fun numberKey(x: Int, y: Int, hotbarButton: Int) {
        this.onNumberKey(x + this.rootX, y + this.rootY, hotbarButton, GridInventory.DUMMY)
    }
    open fun drag(newItems: Map<Position2I, ItemStack>, newCursor: ItemStack?, type: DragType) {
        this.onDrag(newItems.mapKeys { (key, _) -> intArrayOf(key.x + this.rootX, key.y + this.rootY) }, type, newCursor, GridInventory.DUMMY)
    }
    open fun create() {
        this.onCreation(GridInventory.DUMMY)
    }
    open fun destroy() {
        this.onDestruction(GridInventory.DUMMY)
    }


    // Special Legacy Java Compatibility
    open fun render(grid: GridInventory) {}

    open fun onClick(x: Int, y: Int, type: ClickType, grid: GridInventory) {}
    open fun onNumberKey(x: Int, y: Int, hotbarButton: Int, grid: GridInventory) {}
    open fun onDrag(newItems: Map<IntArray, ItemStack>, type: DragType, newCursor: ItemStack?, grid: GridInventory) {}
    open fun onCreation(grid: GridInventory) {}
    open fun onDestruction(grid: GridInventory) {}

    fun getRealX(): Int = this.rootX
    fun getRealY(): Int = this.rootY
}