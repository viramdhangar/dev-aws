package com.viram.dev.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserPost {
	private DAOUser user;
	private Post post;
	private String comment;
}
