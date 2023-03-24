package me.ste.stevesseries.inventoryguilibrary.widget.builtin;

import org.bukkit.inventory.ItemStack;

/**
 * @deprecated For backwards compatibility only.
 */
@Deprecated
public abstract class PaginationWidgetBarWidget extends PaginationBarWidget {
    private final PaginationWidget paginationWidget;

    public PaginationWidgetBarWidget(int x, int y, int width, int height, ItemStack backgroundItem, PaginationWidget paginationWidget) {
        super(x, y, width, height, backgroundItem);
        this.paginationWidget = paginationWidget;
    }

    @Override
    public int getPage() {
        return this.paginationWidget.getPage();
    }

    @Override
    public int getPages() {
        return this.paginationWidget.getPages();
    }

    @Override
    public void setPage(int page) {
        this.paginationWidget.setPage(page);
    }
}
