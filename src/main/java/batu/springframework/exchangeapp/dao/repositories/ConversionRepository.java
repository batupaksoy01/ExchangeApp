package batu.springframework.exchangeapp.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import batu.springframework.exchangeapp.dao.entities.Conversion;

public interface ConversionRepository extends JpaRepository<Conversion, Long>{

}
