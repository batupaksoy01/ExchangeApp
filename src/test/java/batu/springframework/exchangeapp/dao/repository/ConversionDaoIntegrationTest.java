package batu.springframework.exchangeapp.dao.repository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import batu.springframework.exchangeapp.dao.entity.ConversionEntity;

@AutoConfigureTestDatabase
@DataJpaTest
public class ConversionDaoIntegrationTest {
	
	@Autowired
	private ConversionRepository repo;
	

	@Test
	public void postConversion_InputValid_SavedToDb() {
		repo.save(new ConversionEntity(5.0,7.5,"EUR","USD"));
		
		List<ConversionEntity> repoContent = repo.findAll();
		
		assertEquals(repoContent.size(),1);
		ConversionEntity savedEntity = repoContent.get(0);
		assertEquals("EUR", savedEntity.getSource());
		assertEquals("USD", savedEntity.getTarget());
		assertEquals(5.0, savedEntity.getSourceAmount());
		assertEquals(7.5, savedEntity.getTargetAmount());
		assertNotNull(savedEntity.getId());
		assertNotNull(savedEntity.getCreationDate());
	}
	
	@Test
	public void getConversions_PageableGiven_ListReturned() {
		for(int i = 0; i < 4; i++) {
			repo.save(new ConversionEntity(5.0,7.5,"EUR","USD"));
		}
		
		List<ConversionEntity> repoContent = repo.findAll(PageRequest.of(0, 3, Sort.by("id").descending())).getContent();
		assertNotNull(repoContent);
		assertEquals(3,repoContent.size());
	}
}
