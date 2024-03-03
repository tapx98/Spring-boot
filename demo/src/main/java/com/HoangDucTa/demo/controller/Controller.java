package com.HoangDucTa.demo.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.HoangDucTa.demo.dto.ForgotPasswordDTO;
import com.HoangDucTa.demo.dto.LoginDTO;
import com.HoangDucTa.demo.dto.VerifyTokenRequestDTO;
import com.HoangDucTa.demo.entity.UserInfo;
import com.HoangDucTa.demo.security.jwt.JWTToken;
import com.HoangDucTa.demo.security.jwt.TokenProvider;
import com.HoangDucTa.demo.service.OTPService;
import com.HoangDucTa.demo.service.ProductSercice;



@RestController
@RequestMapping("/api/auth/")
public class Controller {

	
	@Autowired
	private ProductSercice productService;
	
	@Autowired
	private OTPService otpService;
	
	@Autowired
    private TokenProvider tokenProvider;
	
	// thêm mới user
	@PostMapping("/adduser")
	public ResponseEntity<?> addNewUser(@RequestBody UserInfo userInfo) {
		
		String a = productService.addUser(userInfo);
		//check khi sercice tra ve text loi
		if(a == "user hoac email da su dung") {
			//khi co loi return 1 ResponseEntity gom text loi va statur ma loi 400
			return new ResponseEntity<String>(a, HttpStatus.BAD_REQUEST);
		}
		//khi ko co loi return text va statur 200
		return new ResponseEntity<String>(a, HttpStatus.OK);
		
	}
	
	//login
	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO ) {
		
		return productService.login(loginDTO);
	}
	
	// verify OTP
	@PostMapping("/verifyOTP")
	public ResponseEntity<JWTToken> verifyOtp(@RequestBody VerifyTokenRequestDTO verifyTokenRequestDTO) {
		
		String username = verifyTokenRequestDTO.getUsername();
		Integer otp = verifyTokenRequestDTO.getOtp();
		
		Boolean isOtpValid = otpService.validateOTP(username, otp);
		
		if (!isOtpValid) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		 String token = tokenProvider.generateToken(username);
	        JWTToken response = new JWTToken(token);
		return new ResponseEntity<JWTToken>(response, HttpStatus.OK );
	}
	
	//quyên mật khẩu
	@PostMapping("/forgotPassword")
	public ResponseEntity<?> forgotPassword (@RequestParam String username) {
		Optional<UserInfo> user = productService.findByUsername(username);
		if (user == null) {
			return ResponseEntity.badRequest().body("user người dùng không tồn tại");
		}
		String resetLink = "http://localhost:8080/api/auth/reset-password";
		return ResponseEntity.ok("link thay đổi password");
		
	}
}
