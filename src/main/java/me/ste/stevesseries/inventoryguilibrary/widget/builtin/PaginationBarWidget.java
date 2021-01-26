package me.ste.stevesseries.inventoryguilibrary.widget.builtin;

import me.ste.stevesseries.inventoryguilibrary.inventory.GridInventory;
import me.ste.stevesseries.inventoryguilibrary.widget.Widget;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * This widget represents a horizontal bar with next and previous page buttons
 */
public abstract class PaginationBarWidget extends Widget {
    private final ItemStack backgroundItem;

    /**
     * @param backgroundItem the item used for the background. null for no background
     */
    public PaginationBarWidget(int x, int y, int width, int height, ItemStack backgroundItem) {
        super(x, y, width, height);
        this.backgroundItem = backgroundItem;

        PaginationBarWidget self = this;
        this.addChild(new ButtonWidget(this.getRealX(), this.getRealY(), 1, this.getHeight()) {
            @Override
            protected ItemStack getItem() {
                return self.getPreviousPageItem(self.getPage() + 1, Math.max(1, self.getPages()));
            }

            @Override
            protected void click(ClickType type) {
                int page = self.getPage();
                if(page > 0) {
                    self.setPage(page - 1);
                    self.rerender();
                }
            }
        });
        this.addChild(new ButtonWidget(this.getRealX() + this.getWidth() - 1, this.getRealY(), 1, this.getHeight()) {
            @Override
            protected ItemStack getItem() {
                return self.getNextPageItem(self.getPage() + 1, Math.max(1, self.getPages()));
            }

            @Override
            protected void click(ClickType type) {
                int page = self.getPage();
                if(page < self.getPages() - 1) {
                    self.setPage(page + 1);
                    self.rerender();
                }
            }
        });
    }

    @Override
    public void render(GridInventory grid) {
        int page = this.getPage();
        int pages = this.getPages();

        if(pages > 0 && page >= pages) {
            page = pages - 1;
            this.setPage(page);
        }

        if(this.backgroundItem != null) {
            grid.fill(this.getRealX() + 1, this.getRealY(), this.getWidth() - 2, this.getHeight(), this.backgroundItem);
        }
    }

    /**
     * Get the current page number
     * @return page number
     */
    protected abstract int getPage();

    /**
     * Get the total amount of pages, 0 is the minimum value
     * @return amount of pages
     */
    protected abstract int getPages();

    /**
     * Set the current page number
     * @param page current page number
     */
    protected abstract void setPage(int page);

    /**
     * Get the item stack to display as the next page button
     * @param displayPage page number to display, 1 is the minimum value
     * @param displayPages pages amount to display, 1 is the minimum value
     * @return item stack
     */
    protected abstract ItemStack getNextPageItem(int displayPage, int displayPages);

    /**
     * Get the item stack to display as the previous page button
     * @param displayPage page number to display, 1 is the minimum value
     * @param displayPages pages amount to display, 1 is the minimum value
     * @return item stack
     */
    protected abstract ItemStack getPreviousPageItem(int displayPage, int displayPages);
}