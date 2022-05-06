package batu.springframework.exchangeapp.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import batu.springframework.exchangeapp.data.model.Conversion;

public interface ConversionRepository extends JpaRepository<Conversion, Long>{

}
