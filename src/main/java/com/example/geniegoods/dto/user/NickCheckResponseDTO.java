package com.example.geniegoods.dto.user;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class NickCheckResponseDTO {
    private boolean available;
    private String message;
}
