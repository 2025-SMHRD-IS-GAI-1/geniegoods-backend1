package com.example.geniegoods.dto.user;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class NickUpdateRequestDTO {
    private String nickname;
}