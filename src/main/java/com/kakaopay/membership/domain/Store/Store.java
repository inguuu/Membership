package com.kakaopay.membership.domain.Store;

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
@Table(name="tb_store",schema ="membership")
public class Store {
    @Id
    @NotNull
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="store_id")
    private int storeId;

    @NotNull
    @Column(name="partner_name")
    private String partnerName;

    @NotNull
    private String category;
}
