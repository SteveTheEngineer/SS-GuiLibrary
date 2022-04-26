package me.ste.stevesseries.guilib.api.gui.inventory

import me.ste.stevesseries.guilib.api.canvas.ItemCanvas
import me.ste.stevesseries.guilib.api.component.render.ComponentWithChildren
import me.ste.stevesseries.guilib.api.component.GuiComponent
import me.ste.stevesseries.guilib.api.component.RootComponent
import me.ste.stevesseries.guilib.api.util.Ticking
import org.bukkit.event.inventory.ClickType

/**
 * A GUI that wraps a [root] component.
 */
class ComponentGui(
    private val root: GuiComponent
) : GridInventoryGui(), Ticking {
    lateinit var renderedComponents: ComponentWithChildren

    override fun getSize(): GridInventorySize {
        if (this.root is RootComponent) {
            return this.root.getSize()
        }

        return GridInventorySize.GENERIC_9X6
    }

    override fun getTitle(): String {
        if (this.root is RootComponent) {
            return this.root.getTitle()
        }

        return this::class.simpleName.toString()
    }

    override fun render(canvas: ItemCanvas) {
        this.renderedComponents.renderItems(canvas)
    }

    override fun create() {
        super.create()

        this.root.gui = this
        this.root.create()

        this.renderedComponents = this.root.renderComponents(0, 0, this.currentSize.width, this.currentSize.height)
    }

    override fun destroy() {
        super.destroy()

        this.renderedComponents.destroyHierarchy()
    }
    override fun click(x: Int, y: Int, type: ClickType, numberKey: Int): Boolean {
        if (this.root is RootComponent) {
            return this.root.click(x, y, type, numberKey)
        }

        return false
    }

    private fun processChanges() {
        if (this.renderedComponents.processChanges()) {
            this.changed()
        }
    }

    override fun tick() {
        this.processChanges()
    }
}