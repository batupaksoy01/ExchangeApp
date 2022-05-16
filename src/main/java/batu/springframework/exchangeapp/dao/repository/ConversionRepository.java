package batu.springframework.exchangeapp.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import batu.springframework.exchangeapp.dao.entity.ConversionEntity;

public interface ConversionRepository extends JpaRepository<ConversionEntity, Long>{

}
