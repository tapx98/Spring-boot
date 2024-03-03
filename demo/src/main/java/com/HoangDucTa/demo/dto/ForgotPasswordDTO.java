package com.HoangDucTa.demo.dto;

import lombok.Data;

@Data
public class ForgotPasswordDTO {
	
	private String username;
	
	private String password;

    private  String token;

    private String newpassword;
}
