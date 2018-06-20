package utils;

public class PaginationFilter {

    private int limit;

    private int offset;

    private static final int DEFAULT_LIMIT = 10;
    private static final int DEFAULT_OFFSET = 1;

    public PaginationFilter(int currentPage, int limit) {
        if (currentPage == 0)
            currentPage++;
        this.offset = ((currentPage - 1) * limit);
        this.limit = limit;
    }

    public PaginationFilter() {}

    public void setOffset(int offset) {
        if (offset == 0)
            offset++;
        this.offset = ((offset - 1) * DEFAULT_LIMIT);
    }

    public int getLimit() {
        return limit;
    }

    public int getOffset() {
        return offset;
    }

    public static  PaginationFilter defaultPagination(){
        return new PaginationFilter(DEFAULT_OFFSET, DEFAULT_LIMIT);
    }

}
