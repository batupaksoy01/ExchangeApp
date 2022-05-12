package batu.springframework.exchangeapp.dao.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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
	private BigDecimal sourceAmount;
	@Column(name = "target_amount")
	private BigDecimal targetAmount;
	@Column(name = "source")
	private String source;
	@Column(name = "target")
	private String target;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "creation_date")
	private Date creationDate;
	
	@PrePersist
	private void onCreate() {
		creationDate = new Date();
		
	}

	public Long getId() {
		return id;
	}

	public BigDecimal getSourceAmount() {
		return sourceAmount;
	}

	public void setSourceAmount(BigDecimal sourceAmount) {
		this.sourceAmount = sourceAmount;
	}

	public BigDecimal getTargetAmount() {
		return targetAmount;
	}

	public void setTargetAmount(BigDecimal targetAmount) {
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

	public Date getCreationDate() {
		return creationDate;
	}
	
}
