package me.ste.stevesseries.guilib.api.component

import me.ste.stevesseries.guilib.api.component.render.ComponentCanvas
import me.ste.stevesseries.guilib.api.component.render.ComponentWithChildren
import me.ste.stevesseries.guilib.api.component.render.ComponentWithPosition
import me.ste.stevesseries.guilib.api.gui.inventory.ComponentGui
import org.bukkit.entity.Player
import java.util.function.Supplier

abstract class GuiComponent {
    // Internal managed values
    lateinit var gui: ComponentGui
    private val currentState = mutableListOf<Int>()
    private var watchedValues = emptyList<Supplier<Int>>()

    // Abstract methods
    /**
     * Renders the component to the [ComponentCanvas].
     */
    abstract fun ComponentCanvas.render()

    // Overridable methods
    /**
     * Called when the component is added to the view.
     */
    open fun create() {}

    /**
     * Called when the component is removed from the view.
     */
    open fun destroy() {}

    // Internal logic
    /**
     * Renders the component to the [canvas].
     */
    fun renderTo(canvas: ComponentCanvas) {
        canvas.render()
    }

    /**
     * Renders ONLY the components and ignores the items.
     */
    fun renderComponents(x: Int, y: Int, width: Int, height: Int): ComponentWithChildren {
        val componentCanvas = ComponentCanvas(width, height)
        this.renderTo(componentCanvas)

        val children = mutableListOf<ComponentWithChildren>()

        for (component in componentCanvas.components) {
            component.component.gui = this.gui
            component.component.create()
            children += component.component.renderComponents(component.x, component.y, component.width, component.height)
        }

        this.watchedValues = componentCanvas.watchedValues
        this.resetState()

        return ComponentWithChildren(
            ComponentWithPosition(x, y, width, height, this),
            children
        )
    }

    // State
    /**
     * @return true, if the state has changed since the last reset.
     */
    open fun hasChanged(): Boolean {
        val state = this.getState()

        if (state.size != this.currentState.size) {
            return true
        }

        for ((index, newValue) in state.withIndex()) {
            val oldValue = this.currentState[index]

            if (oldValue != newValue) {
                return true
            }
        }

        return false
    }

    /**
     * Resets the current state to the state from the [canvas].
     */
    open fun resetState() {
        this.currentState.clear()
        this.currentState += this.getState()
    }

    /**
     * @return the current state.
     */
    open fun getState(): List<Int> {
        val state = mutableListOf<Int>()

        for (supplier in this.watchedValues) {
            state += supplier.get()
        }

        return state
    }
}