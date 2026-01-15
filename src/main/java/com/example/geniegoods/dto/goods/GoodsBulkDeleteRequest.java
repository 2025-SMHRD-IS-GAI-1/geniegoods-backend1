package com.example.geniegoods.dto.goods;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class GoodsBulkDeleteRequest {
    private List<Long> goodsIds;
}