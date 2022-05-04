package batu.springframework.exchangeapp.models;

import java.time.LocalDateTime;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Conversion {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private float sourceAmount;
	private float targetAmount;
	private String source;
	private String target;
	private LocalDateTime dateTime;
}
