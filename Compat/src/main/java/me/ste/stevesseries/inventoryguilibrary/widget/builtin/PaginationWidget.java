package me.ste.stevesseries.inventoryguilibrary.widget.builtin;

import me.ste.stevesseries.inventoryguilibrary.inventory.GridInventory;
import me.ste.stevesseries.inventoryguilibrary.widget.Widget;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;

/**
 * @deprecated For backwards compatibility only.
 */
@Deprecated
public class PaginationWidget extends Widget {
    private final ItemStack backgroundItem;
    private int page;
    private int pages;

    private List<Widget> items = Collections.emptyList();

    public PaginationWidget(int x, int y, int width, int height, ItemStack backgroundItem) {
        super(x, y, width, height);
        this.backgroundItem = backgroundItem;
    }

    public void setItems(List<Widget> items) {
        this.items = items;
        this.pages = (int) Math.ceil((float) items.size() / (float) (this.getWidth() * this.getHeight()));
    }

    public List<Widget> getItems() {
        return this.items;
    }

    public void setPage(int page) {
        this.page = page;
        this.rerender();
    }

    public int getPage() {
        return this.page;
    }

    public int getPages() {
        return this.pages;
    }

    @Override
    public void render(GridInventory grid) {
        if(this.backgroundItem != null) {
            grid.fill(this.getRealX(), this.getRealY(), this.getWidth(), this.getHeight(), this.backgroundItem);
        }

        int i = 0;
        this.getChildren().clear();
        for(Widget widget : this.items.subList(this.page * this.getWidth() * this.getHeight(), Math.min(this.items.size(), (this.page + 1) * this.getWidth() * this.getHeight()))) {
            int[] position = GridInventory.getPositionByIndex(i++, this.getWidth());
            widget.setX(position[0]);
            widget.setY(position[1]);
            this.addChild(widget);
        }
    }
}
