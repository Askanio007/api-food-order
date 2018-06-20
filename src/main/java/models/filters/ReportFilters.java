package models.filters;

import lombok.Getter;
import lombok.Setter;
import utils.PaginationFilter;

import java.sql.Date;

@Setter
@Getter
public class ReportFilters {

    private Date from;
    private Date to;
    private String name;
    private PaginationFilter paginationFilter;

    private ReportFilters() {

    }

    public ReportFilters(PaginationFilter paginationFilter) {
        this.paginationFilter = paginationFilter;
    }


}
