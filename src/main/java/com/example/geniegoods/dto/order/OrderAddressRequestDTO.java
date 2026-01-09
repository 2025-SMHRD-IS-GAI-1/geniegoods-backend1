// com.example.geniegoods.dto.order.OrderRequestDto.java

package com.example.geniegoods.dto.order;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class OrderAddressRequestDTO {

    private String zipcode;

    private String address;

    private String detailAddress;
}