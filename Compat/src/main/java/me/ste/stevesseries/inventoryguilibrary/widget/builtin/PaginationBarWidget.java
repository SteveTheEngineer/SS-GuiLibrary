package me.ste.stevesseries.inventoryguilibrary.widget.builtin;

import me.ste.stevesseries.inventoryguilibrary.inventory.GridInventory;
import me.ste.stevesseries.inventoryguilibrary.widget.Widget;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * @deprecated For backwards compatibility only.
 */
@Deprecated
public abstract class PaginationBarWidget extends Widget {
    private final ItemStack backgroundItem;

    public PaginationBarWidget(int x, int y, int width, int height, ItemStack backgroundItem) {
        super(x, y, width, height);
        this.backgroundItem = backgroundItem;

        PaginationBarWidget self = this;
        this.addChild(new ButtonWidget(0, 0, 1, this.getHeight()) {
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
        this.addChild(new ButtonWidget(this.getWidth() - 1, 0, 1, this.getHeight()) {
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

    protected abstract int getPage();

    protected abstract int getPages();

    protected abstract void setPage(int page);

    protected abstract ItemStack getNextPageItem(int displayPage, int displayPages);

    protected abstract ItemStack getPreviousPageItem(int displayPage, int displayPages);
}

