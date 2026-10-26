package group4.gym.management.util;

import java.util.List;

/**
 * One page of a list paginated in SQL with OFFSET ... FETCH (CL-09; shared utility, owner TV2).
 * Used by the member list (CN-11) and the registration list (CN-24), 20 rows per page.
 *
 * @param <T> row type
 */
public class Page<T> {

    /** Rows of the current page */
    private final List<T> items;

    /** Current page number, starting at 1 */
    private final int page;

    /** Rows per page */
    private final int pageSize;

    /** Total rows matching the filter (all pages) */
    private final int totalItems;

    public Page(List<T> items, int page, int pageSize, int totalItems) {
        this.items = items;
        this.page = page;
        this.pageSize = pageSize;
        this.totalItems = totalItems;
    }

    /**
     * OFFSET value for a page number.
     */
    public static int offsetOf(int page, int pageSize) {
        // TODO: (page - 1) * pageSize; page < 1 is treated as 1 (HL-07).
        // Output: the number of rows to skip.
        return 0;
    }

    public List<T> getItems() {
        return items;
    }

    public int getPage() {
        return page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getTotalItems() {
        return totalItems;
    }

    /**
     * Number of pages (at least 1, so an empty result still renders "page 1 of 1").
     */
    public int getTotalPages() {
        // TODO: ceil(totalItems / pageSize), minimum 1.
        // Output: the total number of pages.
        return 1;
    }

    public boolean isHasPrevious() {
        return page > 1;
    }

    public boolean isHasNext() {
        return page < getTotalPages();
    }
}
