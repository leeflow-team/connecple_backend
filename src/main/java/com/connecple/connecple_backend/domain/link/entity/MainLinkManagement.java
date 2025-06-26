package com.connecple.connecple_backend.domain.link.entity;

import com.connecple.connecple_backend.domain.base.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "main_link_management")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class MainLinkManagement extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String linkPath;

    public void updateLinkPath(String linkPath) {
        this.linkPath = linkPath;
    }
}
