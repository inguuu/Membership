package com.kakaopay.membership.controller;

import com.kakaopay.membership.domain.History.History;
import com.kakaopay.membership.domain.History.HistoryResponseInterface;
import com.kakaopay.membership.dto.ResponseHistory;
import com.kakaopay.membership.service.MemberShipService;
import com.kakaopay.membership.utils.DefaultRes;
import com.kakaopay.membership.utils.ResponseMessage;
import com.kakaopay.membership.utils.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/membership")
@RequiredArgsConstructor
public class MembershipController {
    private final MemberShipService memberShipService;

    private static final DefaultRes BAD_REQUEST_DEFAULT_RES = new DefaultRes(StatusCode.BAD_REQUEST, ResponseMessage.BAD_REQUEST);
    private static final DefaultRes SERVER_ERROR_DEFAULT_RES = new DefaultRes(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR);

    /**
     * 통합 바코드 발급 API
     * @Header userId 사용자 ID
     * @return ResponseEntity
     */
    @GetMapping("/barcode")
    public ResponseEntity getBarcode(@RequestHeader int userId) {
        try {
            DefaultRes defaultRes = memberShipService.getBarCode(userId);
            return new ResponseEntity<>(defaultRes, HttpStatus.CREATED);
        }catch (InternalError | Exception e){
            return new ResponseEntity<>(SERVER_ERROR_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 포인트 적립 API
     * @param req { storeId, point, barcode}
     * @return ResponseEntity
     */
    @PostMapping("/point")
    public ResponseEntity earnPoint(@RequestBody @Validated Map<String,String> req) {
        try {
            DefaultRes defaultRes = memberShipService.insertPoint(req);
            return new ResponseEntity<>(defaultRes, HttpStatus.OK);
        }catch (InternalError | Exception e){
            return new ResponseEntity<>(SERVER_ERROR_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 포인트 사용 API
     * @param req { storeId, point, barcode}
     * @return ResponseEntity
     */
    @PutMapping("/point")
    public ResponseEntity usePoint(@RequestBody @Validated Map<String,String> req) {
        try {
            DefaultRes defaultRes = memberShipService.updatePoint(req);
            return new ResponseEntity<>(defaultRes, HttpStatus.OK);
        }catch (InternalError | Exception e){
            return new ResponseEntity<>(SERVER_ERROR_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 내역 조회 API
     * @param startDate 날짜 시작 조건
     * @param endDate 날짜 마침 조건
     * @param barcode 바코드
     * @return ResponseEntity
     */
    @GetMapping("/barcode/history")
    public ResponseEntity getHistory(@RequestParam String startDate,
                                                      @RequestParam String endDate,
                                                      @RequestParam String barcode) {
        try {
            DefaultRes<List<HistoryResponseInterface>> defaultRes
                    = memberShipService.getBarCodeHistory(barcode,startDate,endDate);
            return new ResponseEntity<>(defaultRes, HttpStatus.OK);
        }catch (InternalError | Exception e){
            return new ResponseEntity<>(SERVER_ERROR_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
