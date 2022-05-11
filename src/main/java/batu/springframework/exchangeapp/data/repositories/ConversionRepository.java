package batu.springframework.exchangeapp.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import batu.springframework.exchangeapp.data.models.Conversion;

public interface ConversionRepository extends JpaRepository<Conversion, Long>{

}
