package com.kakaopay.membership.domain.Barcode;

import com.sun.istack.NotNull;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name="tb_barcode",schema ="membership")
public class Barcode {
    @Id
    @NotNull
    @Column(name="user_id")
    private int userId;

    @Column
    @NotNull
    private String barcode;

    @NotNull
    @Column(name="created_at")
    private String createdAt;
}
