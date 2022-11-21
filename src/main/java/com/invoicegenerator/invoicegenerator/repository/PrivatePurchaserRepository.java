package com.invoicegenerator.invoicegenerator.repository;

import com.invoicegenerator.invoicegenerator.model.PrivatePurchaser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivatePurchaserRepository extends JpaRepository<PrivatePurchaser,Long> {

}
