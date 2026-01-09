package com.example.geniegoods.entity;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TB_UPLOAD_IMG")
@EntityListeners(AuditingEntityListener.class)
@ToString
@Builder
public class UploadImgEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long uploadId;

    @Column(nullable = false)
    private String uploadImgUrl;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime uploadedAt;

    @Column(nullable = false)
    private Long uploadImgSize;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "upload_group_id", nullable = false)
    private UploadImgGroupEntity uploadImgGroup;
}
