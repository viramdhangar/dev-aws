package com.viram.dev.repository.mat;

import java.io.Serializable;
import java.util.List;

import com.viram.dev.dto.Address;
import com.viram.dev.dto.DAOUser;
import com.viram.dev.dto.mat.AboutDetails;
import com.viram.dev.dto.mat.BasicDetails;
import com.viram.dev.dto.mat.MatImageModel;
import com.viram.dev.dto.mat.PersonalDetails;
import com.viram.dev.dto.mat.ProfessionalDetails;
import com.viram.dev.dto.mat.ReligionDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MatrimonyRegistration implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BasicDetails basicDetails;
	private PersonalDetails personalDetails;
	private ReligionDetails religionDetails;
	private ProfessionalDetails professionalDetails;
	private AboutDetails aboutDetails;
	private List<MatImageModel> matImageModel;
	private DAOUser user;
}
