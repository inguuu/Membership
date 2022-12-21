package com.kakaopay.membership.dto;

import com.sun.istack.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResponseHistory {
    private String approvedAt;
    private String type;
    private String category;
    private String partnerName;
}
