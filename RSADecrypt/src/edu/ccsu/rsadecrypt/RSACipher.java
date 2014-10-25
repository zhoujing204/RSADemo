package edu.ccsu.rsadecrypt;

import java.math.BigInteger;

public class RSACipher {
	
	private BigInteger N;
	private BigInteger p;
	private BigInteger q;
	private BigInteger e;
	private BigInteger d;
	private BigInteger fi;
	private BigInteger cipher;
	private BigInteger plaintext;
	private String hexPlaintext;
	
	final protected static char[] hexArray = "0123456789abcdef".toCharArray();
	public static String bytesToHex(byte[] bytes) {
	    char[] hexChars = new char[bytes.length * 2];
	    for ( int j = 0; j < bytes.length; j++ ) {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 2] = hexArray[v >>> 4];
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
	    return new String(hexChars);
	}
	
	public RSACipher(BigInteger mN, BigInteger mp, BigInteger mq, BigInteger mC, BigInteger me) {
		N = mN;
		p = mp;
		q = mq;
		cipher = mC;
		e = me;
		fi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));  // fi = (p-1)(q-1)
		d = e.modInverse(fi);		 // d = (1/e) mod fi
		BigInteger x = p.multiply(q).mod(N);
		System.out.println("x = " + x.toString());
	}
	
	public void decrypt() {
		plaintext = cipher.modPow(d, N);
		
		//BigInteger c1 = plaintext.modPow(e, N);
		//System.out.println("c1 = " + c1.toString());
		
		
		System.out.println("plaintext = " + plaintext.toString());
		byte[] bytes = plaintext.toByteArray();
		
		System.out.println("length = " + plaintext.bitLength());
		hexPlaintext = bytesToHex(bytes);
		System.out.println("plaintext = " + hexPlaintext);
	}
	
	

}
