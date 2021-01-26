package me.ste.stevesseries.inventoryguilibrary.widget.builtin;

import org.bukkit.inventory.ItemStack;

/**
 * Same as {@link PaginationBarWidget}, but it retrieves the page information from the specified {@link PaginationWidget}
 */
public abstract class PaginationWidgetBarWidget extends PaginationBarWidget {
    private final PaginationWidget paginationWidget;

    /**
     * @param backgroundItem the item used for the background. null for no background
     */
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