package com.fvthree.CategoryService.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="category", 
		uniqueConstraints = {
				@UniqueConstraint(columnNames="name"),
})	
public class Category implements Serializable {

	private static final long serialVersionUID = 465584083645732662L;
	
	@Id
	@SequenceGenerator(
			name="category_sequence",
			sequenceName="category_sequence",
			allocationSize=1
	)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="category_sequence")
	@Column(name="category_id")
	private Long id;
	
	@Column(name="name", nullable=false)
	private String name;
	
	@Column(name="slug", nullable=false)
	private String slug;
	
	@DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME)
	@JsonSerialize(using=LocalDateTimeSerializer.class)
	@JsonDeserialize(using=LocalDateTimeDeserializer.class)
	@Column(updatable=false,name="date_created")
	private LocalDateTime dateCreated;
	
	@DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME)
	@JsonSerialize(using=LocalDateTimeSerializer.class)
	@JsonDeserialize(using=LocalDateTimeDeserializer.class)
	@Column(name="last_updated")
	private LocalDateTime lastUpdated;
	
	@PrePersist
	public void setDateCreated() {
		this.dateCreated = LocalDateTime.now();
	}
	
	@PreUpdate
	public void setLastUpdated() {
		this.lastUpdated = LocalDateTime.now();
	}
}
