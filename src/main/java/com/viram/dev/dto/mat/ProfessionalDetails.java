package com.viram.dev.dto.mat;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.viram.dev.dto.DAOUser;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class ProfessionalDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String education;
	private String employedIn;
	private String occupation;
	private String annualIncome;
	private String workLocation;
	private String residingState;
	private String ancestralOrigin;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "profile_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private BasicDetails basicDetail;
}
