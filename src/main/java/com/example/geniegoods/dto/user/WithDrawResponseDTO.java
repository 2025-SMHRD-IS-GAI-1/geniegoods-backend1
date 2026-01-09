package com.example.geniegoods.dto.user;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class WithDrawResponseDTO {
    private String status;
    private String message;
}
