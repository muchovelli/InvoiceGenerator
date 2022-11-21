package com.invoicegenerator.invoicegenerator.services;

import com.invoicegenerator.invoicegenerator.model.PrivatePurchaser;
import com.invoicegenerator.invoicegenerator.repository.PrivatePurchaserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrivatePurchaserServiceImpl implements PrivatePurchaserService {

    @Autowired
    private PrivatePurchaserRepository privatePurchaserRepository;

    @Override
    public PrivatePurchaser save(PrivatePurchaser privatePurchaser) {

        return privatePurchaserRepository.save(privatePurchaser);
    }

    @Override
    public PrivatePurchaser findById(Long id) {
        return privatePurchaserRepository.findAll().stream().filter(privatePurchaser -> privatePurchaser.getId().equals(id)).findFirst().get();
    }

    @Override
    public List<PrivatePurchaser> findAll() {
        return privatePurchaserRepository.findAll();
    }
}
