package it.unisa.backend.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordUtil {

	public static String generateSalt() {
		
		SecureRandom rand = new SecureRandom();
		byte[] salt = new byte[16];
		rand.nextBytes(salt);
		
		return Base64.getEncoder().encodeToString(salt);
	}
	
	public static String hashPassword(String password, String salt) {
		try {
			
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(Base64.getDecoder().decode(salt));
			
			byte[] hashedPassword = md.digest(password.getBytes());
			
			return Base64.getEncoder().encodeToString(hashedPassword);
			
		} catch(NoSuchAlgorithmException e) {
			throw new RuntimeException("Critical Error: SHA-256 algorithm not found.", e);
		}
	}
	
	public static boolean verifyPassword(String passwordInput, String storedHash, String storedSalt) {
		
		String newHash = hashPassword(passwordInput, storedSalt);
		return newHash.equals(storedHash);
	}
	
}
