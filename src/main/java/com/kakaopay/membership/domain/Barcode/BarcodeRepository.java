package com.kakaopay.membership.domain.Barcode;

import com.kakaopay.membership.dto.SerialNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BarcodeRepository extends JpaRepository<Barcode,String> {
    Barcode findByUserId(int userId);
    Barcode findByBarcode(String barCode);
    Barcode save(Barcode barCode);

    @Procedure(name = "sp_get_serial")
    List<SerialNumber> sp_get_serial();


}
