package com.kakaopay.membership.domain.Point;

import com.kakaopay.membership.domain.Point.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PointReopsitory extends JpaRepository<Point,String> {
    Point findByBarcode(String barcode);

    Point save(Point point);

    @Modifying
    @Query(value =
            "UPDATE tb_point " +
                    "SET point = ?1 " +
                    "WHERE barcode = ?2 AND category = ?3",
            nativeQuery = true)
    void updateByBarcodeAndCategory(int point, String barcode,String category);

}
