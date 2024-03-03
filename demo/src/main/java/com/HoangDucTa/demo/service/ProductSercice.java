package com.HoangDucTa.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.HoangDucTa.demo.dto.ForgotPasswordDTO;
import com.HoangDucTa.demo.dto.LoginDTO;
import com.HoangDucTa.demo.entity.UserInfo;
import com.HoangDucTa.demo.repository.UserInfoRepository;
import com.HoangDucTa.demo.security.jwt.TokenProvider;

import antlr.Token;


@Service
public class ProductSercice {

	
	@Autowired
	private UserInfoRepository repository;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	public OTPService otpService;
	
	@Autowired
	public TokenProvider tokenProvider;
	

	
	//thêm mới user
	public String addUser(UserInfo userInfo) {
		if(repository.existsByEmail(userInfo.getEmail()) || repository.existsByUsername(userInfo.getUsername())) {
			//trả về text 
			return "user hoac email da su dung";
		}
		//mã hóa password
		userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
		repository.save(userInfo);
		return "them moi user thanh cong";
	}
	
	// check username passwod 
	public ResponseEntity<String> login(LoginDTO loginDTO) {
		Authentication authentication = null;
		
		try {
			//check username va password lay tu FE so sanh trong DB
			authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword()));
			
		} catch (Exception e) {
			//exception tra ve loi 400
	        return new ResponseEntity<>("Username hoặc Password sai", HttpStatus.FORBIDDEN);
		}
		//Lớp này lưu trữ hiện trạng bảo mật hiện tại
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		int otp = otpService.generateOTP(loginDTO.getUsername());
        return new ResponseEntity<>("Username và Password đúng!     " + "OTP" + "  "+ otp , HttpStatus.OK );
	}
	
	//đổi mật khẩu
	
//	public ResponseEntity<String> forgotPassword(ForgotPasswordDTO forgotPasswordDTO) {
//		String username = null;
//		String jwtToken = null;
//		String newpassword = forgotPasswordDTO.getNewpassword();
////		username = tokenProvider.getUsernameFromToken(jwtToken);
////		System.out.println(username);
//		if (!newpassword.isEmpty()) {
//			try {
////				Authentication authentication = null;
////				authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(forgotPasswordDTO.getUsername(), forgotPasswordDTO.getPassword()));
//				
//				UserInfo userInfo = null ;
//				changeUserPassword(userInfo, newpassword);;
//				
//			} catch (Exception e) {
//				//exception tra ve loi 400
//		        return new ResponseEntity<>("Password sai", HttpStatus.FORBIDDEN);
//			}
//			return new ResponseEntity<>("Password đã được đổi! " , HttpStatus.OK );
//		}
//		return new ResponseEntity<>("hãy nhập password! " , HttpStatus.OK );
//	}
	
	public String changeUserPassword(UserInfo userInfo, ForgotPasswordDTO forgotPasswordDTO) {
		userInfo.setPassword(passwordEncoder.encode(forgotPasswordDTO.getNewpassword()));
	    repository.save(userInfo);
		return "Password đã được đổi! ";
	}
	
	public Optional<UserInfo> findByUsername(String username) {
		return repository.findByUsername(username);
	}
	
}
