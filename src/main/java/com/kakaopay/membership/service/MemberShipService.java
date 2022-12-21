package com.kakaopay.membership.service;


import com.kakaopay.membership.domain.Barcode.Barcode;
import com.kakaopay.membership.domain.Barcode.BarcodeRepository;
import com.kakaopay.membership.domain.History.History;
import com.kakaopay.membership.domain.History.HistoryRepository;
import com.kakaopay.membership.domain.History.HistoryResponseInterface;
import com.kakaopay.membership.domain.Point.Point;
import com.kakaopay.membership.domain.Point.PointReopsitory;
import com.kakaopay.membership.domain.Store.Store;
import com.kakaopay.membership.domain.Store.StoreRepository;
import com.kakaopay.membership.dto.ResponseHistory;
import com.kakaopay.membership.dto.SerialNumber;
import com.kakaopay.membership.utils.CommonUtils;
import com.kakaopay.membership.utils.DefaultRes;
import com.kakaopay.membership.utils.ResponseMessage;
import com.kakaopay.membership.utils.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberShipService {

    private final BarcodeRepository barcodeRepository;
    private final PointReopsitory pointReopsitory;
    private final StoreRepository storeReopsitory;
    private final HistoryRepository historyReopsitory;


    @Transactional
    public DefaultRes getBarCode(int userId) {
        Optional<Barcode> barCode = Optional.ofNullable(barcodeRepository.findByUserId(userId));

        if (!barCode.isPresent()) {// 바코드 존재 안함
            try {
                //추가 생성
                List<SerialNumber> serialNumbers = barcodeRepository.sp_get_serial();
                Barcode newBarcode = new Barcode(userId, serialNumbers.get(0).getSerialNumber(),CommonUtils.getDate());
                barcodeRepository.save(newBarcode);
                return DefaultRes.res(StatusCode.OK, ResponseMessage.BARCODE_CREATED_SUCCESS, newBarcode.getBarcode());
            }catch (Exception e){
                return DefaultRes.res(StatusCode.DB_ERROR,ResponseMessage.DB_ERROR);
            }

        }else{
            return DefaultRes.res(StatusCode.OK, ResponseMessage.BARCODE_ISSUANCE_SUCCESS,barCode.get().getBarcode());
        }
    }

    @Transactional
    public DefaultRes insertPoint(Map<String,String> map) {
        int storeId = Integer.parseInt(map.get("storeId"));
        int newPoint = Integer.parseInt(map.get("point"));
        String barcode = map.get("barcode");
        String category = "";
        String partnerName = "";

        Optional<Barcode> barCodeObj = Optional.ofNullable(barcodeRepository.findByBarcode(barcode));
        if (!barCodeObj.isPresent()) {
            return DefaultRes.res(StatusCode.OK, ResponseMessage.NOT_REGISTERED_BARCODE);
        }

        Optional<Store> store = Optional.ofNullable(storeReopsitory.findByStoreId(storeId));
        if (!store.isPresent()) {// 상점 존재 안함
            return DefaultRes.res(StatusCode.OK, ResponseMessage.NOT_FOUND_STORE);
        }else{
            try {
                Optional<Point> point = Optional.ofNullable(pointReopsitory.findByBarcode(map.get("barcode")));
                category = store.get().getCategory();
                partnerName = store.get().getPartnerName();
                if (!point.isPresent()) {// 포인트 존재 안함, 포인트 등록
                    Point pointObj = new Point(category,newPoint,barcode);
                    pointReopsitory.save(pointObj);
                }else {
                    newPoint = newPoint + point.get().getPoint();
                    pointReopsitory.updateByBarcodeAndCategory(newPoint,barcode,category);
                }

                History history = new History(CommonUtils.getDate(),"earn",category,partnerName,barcode);
                historyReopsitory.save(history);

                return DefaultRes.res(StatusCode.OK, ResponseMessage.EARN_POINT_SUCCESS);
            } catch (Exception e) {
                System.out.println(e.toString());
                return DefaultRes.res(StatusCode.DB_ERROR,ResponseMessage.DB_ERROR);
            }

        }
    }

    @Transactional
    public DefaultRes updatePoint(Map<String,String> map) {
        int storeId = Integer.parseInt(map.get("storeId"));
        int newPoint = Integer.parseInt(map.get("point"));
        String barcode = map.get("barcode");
        String category = "";
        String partnerName = "";

        Optional<Store> store = Optional.ofNullable(storeReopsitory.findByStoreId(storeId));
        if (!store.isPresent()) {// 상점 존재 안함
            return DefaultRes.res(StatusCode.OK, ResponseMessage.NOT_FOUND_STORE);
        }else{
            Optional<Point> point = Optional.ofNullable(pointReopsitory.findByBarcode(map.get("barcode")));
            if (!point.isPresent()) {// 포잊트에 맞는 바코드 존재 안함
                return DefaultRes.res(StatusCode.OK, ResponseMessage.NOT_REGISTERED_BARCODE);
            }else {
                if(point.get().getPoint() - newPoint<0){// 금액 초과
                    return DefaultRes.res(StatusCode.OK, ResponseMessage.LACK_OF_POINT);
                }else{
                    try {
                        newPoint = point.get().getPoint()-newPoint;
                        category = store.get().getCategory();
                        partnerName = store.get().getPartnerName();
                        pointReopsitory.updateByBarcodeAndCategory(newPoint,barcode,category);

                        History history = new History(CommonUtils.getDate(),"use",category,partnerName,barcode);
                        historyReopsitory.save(history);
                        return DefaultRes.res(StatusCode.OK, ResponseMessage.USE_POINT_SUCCESS);
                    } catch (Exception e) {
                        return DefaultRes.res(StatusCode.DB_ERROR,ResponseMessage.DB_ERROR);
                    }
                }
            }
        }
    }

    public DefaultRes getBarCodeHistory(String barcode, String startDate, String endDate) {
        Optional<Barcode> barCodeObj = Optional.ofNullable(barcodeRepository.findByBarcode(barcode));
        if (!barCodeObj.isPresent()) {// 바코드 존재 안함
            return DefaultRes.res(StatusCode.OK, ResponseMessage.NOT_REGISTERED_BARCODE);
        }else{
            try{
                List<HistoryResponseInterface> history = historyReopsitory.findByBarCodeAndStartDateAndEndDate(barcode,startDate,endDate);
                return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_BARCODE_HISTORY,history);
            }catch (Exception e){
                System.out.println(e.toString());
            }
            return  null;

        }
    }
}
