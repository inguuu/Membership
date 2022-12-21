package com.kakaopay.membership.domain.History;

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
@Table(name="tb_history",schema ="membership")
public class History {
    @Id
    @NotNull
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int idx;

    @NotNull
    @Column(name="approved_at")
    private String approvedAt;

    @NotNull
    private String type;

    @NotNull
    private String category;

    @NotNull
    @Column(name="partner_name")
    private String partnerName;

    @NotNull
    private String barcode;

    public History(String approvedAt, String type, String category,String partnerName,String barcode){
        this.approvedAt = approvedAt;
        this.type = type;
        this.category = category;
        this.partnerName = partnerName;
        this.barcode = barcode;
    }
}
