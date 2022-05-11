package batu.springframework.exchangeapp.data.models;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Conversion implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "source_amount")
	private Float sourceAmount;
	@Column(name = "target_amount")
	private Float targetAmount;
	@Column(name = "source")
	private String source;
	@Column(name = "target")
	private String target;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "creation_date")
	private LocalDateTime creationDate;
	
	@PrePersist
	private void onCreate() {
		creationDate = LocalDateTime.now();
		
	}

	public Long getId() {
		return id;
	}

	public Float getSourceAmount() {
		return sourceAmount;
	}

	public void setSourceAmount(Float sourceAmount) {
		this.sourceAmount = sourceAmount;
	}

	public Float getTargetAmount() {
		return targetAmount;
	}

	public void setTargetAmount(Float targetAmount) {
		this.targetAmount = targetAmount;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public LocalDateTime getCreationDate() {
		return creationDate;
	}
	
}
