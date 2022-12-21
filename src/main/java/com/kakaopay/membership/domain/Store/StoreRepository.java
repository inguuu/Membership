package com.kakaopay.membership.domain.Store;

import com.kakaopay.membership.domain.Store.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<Store,String> {
    Store findByStoreId(int storeId);
}
