package com.viram.dev.dto.mat;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "mat_images")
public class MatImageModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public MatImageModel() {
		super();
	}

	public MatImageModel(String name, String type, byte[] picByte) {
		this.name = name;
		this.type = type;
		this.picByte = picByte;
	}

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "name")
	private String name;

	@Column(name = "type")
	private String type;

    //image bytes can have large lengths so we specify a value
    //which is more than the default length for picByte column
	@Lob
	@Column(name = "picByte", length=1000)
	private byte[] picByte;
	
	@Transient
	private String decodedBase64;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "profile_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private BasicDetails basicDetail;
	
	@Transient
	private String image;
	
}
