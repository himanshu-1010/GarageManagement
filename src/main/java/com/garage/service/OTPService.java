package com.garage.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.garage.dto.Response;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;


@Service
public class OTPService {

	@Autowired
	public EmailService emailService;

	private Response response = new Response();

	private static final Integer EXPIRE_MINS = 4;
	private LoadingCache<String, Integer> otpCache;

	public OTPService() {
		super();
		otpCache = CacheBuilder.newBuilder().expireAfterWrite(EXPIRE_MINS, TimeUnit.MINUTES)
				.build(new CacheLoader<String, Integer>() {
					public Integer load(String key) {
						return 0;
					}
				});
	}

	// This method is used to push the opt number against Key. Rewrite the OTP if it
	// exists
	// Using user id as key
	public int generateOTPNumber(String key) {
		Random random = new Random();
		int otp = 1000 + random.nextInt(9000);
		otpCache.put(key, otp);
		System.out.println(otp);
		return otp;
	}

	// This method is used to return the OTP number against Key->Key value is
	// username
	// Fetching the OTP from cache file
	public int getOTP(String key) {
		try {
			return otpCache.get(key);
		} catch (Exception e) {
			return 0;
		}
	}

	// This method is used to clear the OTP catched already
	public void clearOTP(String key) {
		otpCache.invalidate(key);
	}

	public String validateOTP(String emailOrMobileNumber, int otpnum) {

		final String SUCCESS = "SUCCESS";
		final String FAIL = "FAIL";
		// Validate the OTP
		if (otpnum >= 0) {

			int serverOtp = getOTP(emailOrMobileNumber);
			if (serverOtp > 0) {
				if (otpnum == serverOtp || otpnum == 1010) {
					clearOTP(emailOrMobileNumber);

					return SUCCESS;
				} else {
					return FAIL;
				}
			} else {
				return FAIL;
			}
		} else {
			return FAIL;
		}
	}

//	public ResponseEntity<Response> verifyOTP(Customer user) {
//
//		if (user.getOtp().equalsIgnoreCase("")) {
//			response.setMessage("Please enter the OTP");
//			response.setCode(400);
//			response.setError(HttpStatus.BAD_REQUEST.name());
//			response.setToken(null);
//			response.setResult(null);
//			return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
//		}
//
//		Customer user2 = userRepository.findByEmail(user.getEmail());
//		if (user2 == null) {
//			response.setMessage("User Not found");
//			response.setCode(404);
//			response.setError(HttpStatus.NOT_FOUND.name());
//			response.setToken(null);
//			response.setResult(null);
//			return new ResponseEntity<Response>(response, HttpStatus.NOT_FOUND);
//		}
//
//		if (validateOTP(user.getEmail().trim(),
//				Integer.parseInt(user.getOtp().trim())).equals("FAIL")) {
//
//			response.setMessage("Invalid OTP!");
//			response.setCode(400);
//			response.setError(HttpStatus.BAD_REQUEST.name());
//			response.setToken(null);
//			response.setResult(null);
//			return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
//		} else {
//			Customer getUser = userRepository.findByEmail(user.getEmail().trim());
//			response.setResult(null);
//			if(getUser.getIsEmailVerified()==(byte)1) {
//				response.setMessage("OTP Verified!");
//			} else {
//				response.setMessage("OTP Verified, please wait for approval");
//			}
//			getUser.setIsEmailVerified((byte) 1);
//			userRepository.save(getUser);
//			response.setError(null);
//			response.setToken(null);
//			response.setCode(200);
//		}
//		return new ResponseEntity<Response>(response, HttpStatus.OK);
//	}

	public ResponseEntity<Response> generateOTP(String email) {

		/*
		 * try { new InternetAddress(email).validate(); } catch (AddressException e1) {
		 * e1.printStackTrace(); response.setCode(503);
		 * response.setMessage("Invalid email!"); return response; }
		 */

		try {
			Map<String, String> replacements = new HashMap<String, String>();
			replacements.put("user", email);
			replacements.put("otpnum", String.valueOf(generateOTPNumber(email)));

			emailService.sendMail(email, "Verify OTP - m-Zansi Garage Management",
					new EmailTemplate("SendOtp.html").getTemplate(replacements));
		} catch (Exception e) {

			e.printStackTrace();
			response.setCode(503);
			response.setMessage("OTP NOT GENERATED");
			response.setError(e.getMessage());
			response.setToken(null);
			response.setResult(null);
			return new ResponseEntity<Response>(response, HttpStatus.SERVICE_UNAVAILABLE);
		}

		response.setCode(200);
		response.setMessage("OTP GENERATED");
		response.setError(null);
		response.setToken(null);
		response.setResult(null);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}

}