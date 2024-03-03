package com.HoangDucTa.demo.dto;

import com.sun.istack.NotNull;

import lombok.Data;

@Data
public class Test {

	
	@NotNull
	private Integer otp;
	private String username;
}
