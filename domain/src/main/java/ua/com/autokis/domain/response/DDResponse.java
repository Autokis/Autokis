package ua.com.autokis.domain.response;

import lombok.Data;
import ua.com.autokis.domain.product.dd.DDProduct;

import java.math.BigDecimal;
import java.util.List;

@Data
public class DDResponse {
    private String status;

    private BigDecimal time;

    private Integer total;

    private Integer totalResults;

    private Integer limit;

    private Integer offset;

    private List<DDProduct> data;
}
