package com.kakaopay.membership.domain.Point;

import com.sun.istack.NotNull;
import lombok.*;
import org.hibernate.annotations.Generated;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name="tb_point",schema ="membership")
public class Point {

    @Id
    @NotNull
    private String category;

    @NotNull
    private int point;

    @NotNull
    private String barcode;

}
