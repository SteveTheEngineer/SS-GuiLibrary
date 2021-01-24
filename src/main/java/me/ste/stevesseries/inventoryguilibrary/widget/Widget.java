package me.ste.stevesseries.inventoryguilibrary.widget;

import me.ste.stevesseries.inventoryguilibrary.GUIManager;
import me.ste.stevesseries.inventoryguilibrary.inventory.GridInventory;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.DragType;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public abstract class Widget {
    private int x;
    private int y;
    private final int width;
    private final int height;

    private boolean changed = false;

    private final List<Widget> children = new ArrayList<>();
    private Widget parent;
    private Player player;

    public Widget(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Get the x offset of the widget
     * @return x offset
     */
    public int getX() {
        return this.x;
    }

    /**
     * Get the y offset of the widget
     * @return y offset
     */
    public int getY() {
        return this.y;
    }

    /**
     * Set the x offset of the widget
     * @param x x offset
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Set the y offset of the widget
     * @param y y offset
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Get the x position of the widget
     * @return x position
     */
    public int getRealX() {
        return this.parent != null ? this.parent.getRealX() + this.x : this.x;
    }

    /**
     * Get the y position of the widget
     * @return y position
     */
    public int getRealY() {
        return this.parent != null ? this.parent.getRealY() + this.y : this.y;
    }

    /**
     * Get the width of the widget
     * @return width
     */
    public int getWidth() {
        return this.width;
    }

    /**
     * Get the height of the widget
     * @return height
     */
    public int getHeight() {
        return this.height;
    }

    /**
     * Add a child to the widget
     * @param child child
     */
    protected final void addChild(Widget child) {
        this.children.add(child);
        child.setParent(this);
    }

    /**
     * Get the children of the widget. The list is modifiable
     * @return children
     */
    public final List<Widget> getChildren() {
        return this.children;
    }

    /**
     * Set the parent of the widget
     * @deprecated should not be used under normal circumstances, the parent is set automatically
     * @param parent parent
     */
    @Deprecated
    public final void setParent(Widget parent) {
        this.parent = parent;
        if(parent != null) {
            this.player = parent.getPlayer();
        }
    }

    /**
     * Get the parent of the widget
     * @return parent
     */
    public final Widget getParent() {
        return this.parent;
    }

    /**
     * Set the player who currently sees this widget
     * @deprecated should not be used under normal circumstances, the player is set automatically
     * @param player player
     */
    @Deprecated
    public final void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * Get the player who currently sees this widget
     * @return player
     */
    public final Player getPlayer() {
        return this.player;
    }

    /**
     * Re-render the whole GUI. Must be called whenever something has changed and needs updating on the screen
     */
    public final void rerender() {
        Widget openGui = GUIManager.getGui(this.player);
        if(openGui == this) {
            if(this.player != null) {
                GUIManager.rerenderGui(this.player);
            }
        } else {
            if(this.parent != null) {
                this.parent.rerender();
            }
        }
    }

    /**
     * Run the given modifier on the hierarchy of this widget. This includes the widget itself
     * @param modifier modifier
     */
    public final void recurse(Consumer<Widget> modifier) {
        modifier.accept(this);
        for(Widget child : this.children) {
            child.recurse(modifier);
        }
    }

    /**
     * Render the widget on the given grid
     * @param grid inventory
     */
    public abstract void render(GridInventory grid);

    /**
     * Called whenever a player clicks the given slot. This does not include number keys, {@link Widget#onNumberKey(int, int, int, GridInventory)} is used for that purpose. <strong>You will generally want to ignore double clicks ({@link ClickType#DOUBLE_CLICK}, or handle them separately</strong>
     * @param x x position
     * @param y y position
     * @param type type of the click
     * @param grid clicked inventory
     */
    public void onClick(int x, int y, ClickType type, GridInventory grid) {}

    /**
     * Called whenever a player presses a number key on their keyboard over an item
     * @param x x position
     * @param y y position
     * @param hotbarButton button number
     * @param grid inventory
     */
    public void onNumberKey(int x, int y, int hotbarButton, GridInventory grid) {}

    /**
     * Called whenever a player drags an item stack across the inventory
     * @param newItems items that were going to be added to the inventory, the key is a 2 element array, first element being the x position and second being the y
     * @param type type of the drag
     * @param newCursor remaining items after the drag
     * @param grid inventory
     */
    public void onDrag(Map<int[], ItemStack> newItems, DragType type, ItemStack newCursor, GridInventory grid) {}

    /**
     * Called when the widget has been open in a GUI
     * @param grid inventory
     */
    public void onCreation(GridInventory grid) {}

    /**
     * Called when the GUI has been closed
     * @param grid inventory
     */
    public void onDestruction(GridInventory grid) {}

    /**
     * Create the inventory. Called when the widget is open as a GUI using {@link GUIManager#open(Player, Widget)}. The default implementation creates an inventory with size matching the widget size, and makes the title the name of the class. <strong>If your widget is intended to be used as GUI, you will want to override this method</strong>
     * @return inventory
     */
    public Inventory createInventory() {
        Inventory inventory;
        if(this.width == 9) {
            inventory = Bukkit.createInventory(null, 9 * height, this.getClass().getSimpleName());
        } else if(this.width == 5 && this.height == 1) {
            inventory = Bukkit.createInventory(null, InventoryType.HOPPER, this.getClass().getSimpleName());
        } else if(this.width == 3 && this.height == 3) {
            inventory = Bukkit.createInventory(null, InventoryType.DROPPER, this.getClass().getSimpleName());
        } else {
            throw new IllegalStateException("Unsupported inventory size: " + this.width + "x" + this.height);
        }
        return inventory;
    }
}