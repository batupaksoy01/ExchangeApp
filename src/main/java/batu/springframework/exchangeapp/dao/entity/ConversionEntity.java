package batu.springframework.exchangeapp.dao.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;

import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class ConversionEntity implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "source_amount")
	private Double sourceAmount;
	@Column(name = "target_amount")
	private Double targetAmount;
	@Column(name = "source")
	private String source;
	@Column(name = "target")
	private String target;
	@CreationTimestamp
	@Column(name = "creation_date")
	private Date creationDate;
	
	public ConversionEntity(Double sourceAmount, Double targetAmount,
			String source, String target) {
		this.sourceAmount = sourceAmount;
		this.targetAmount = targetAmount;
		this.source = source;
		this.target = target;
	}

	@Override
	public String toString() {
		return "ConversionEntity [id=" + id + ", sourceAmount=" + sourceAmount + ", targetAmount=" + targetAmount
				+ ", source=" + source + ", target=" + target + ", creationDate=" + creationDate + "]";
	}

	public Long getId() {
		return id;
	}

	public Double getSourceAmount() {
		return sourceAmount;
	}

	public void setSourceAmount(Double sourceAmount) {
		this.sourceAmount = sourceAmount;
	}

	public Double getTargetAmount() {
		return targetAmount;
	}

	public void setTargetAmount(Double targetAmount) {
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
