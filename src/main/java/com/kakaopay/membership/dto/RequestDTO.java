package com.kakaopay.membership.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RequestDTO {
    private String storeId;
    private String point;
    private String barcode;

}
