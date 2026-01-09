package com.example.geniegoods.entity;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TB_UPLOAD_IMG_GROUP")
@EntityListeners(AuditingEntityListener.class)
@ToString
@Builder
public class UploadImgGroupEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long uploadGroupId;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "uploadImgGroup", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<UploadImgEntity> uploadImgList = new ArrayList<>();

}
