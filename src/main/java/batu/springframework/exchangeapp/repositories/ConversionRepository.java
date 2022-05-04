package batu.springframework.exchangeapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import batu.springframework.exchangeapp.models.Conversion;

public interface ConversionRepository extends JpaRepository<Conversion, Long>{

}
