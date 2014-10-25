package edu.ccsu.breakrsademo;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MainClass {
	
	//要分解的数n1
	private String sn1 ="17976931348623159077293051907890247336179769789423065727343008115"+
					   "77326758055056206869853794492129829595855013875371640157101398586" +
					   "47833778606925583497541085196591615128057575940752635007475935288" +
					   "71082364994994077189561705436114947486504671101510156394068052754" +
					   "0071584560878577663743040086340742855278549092581";
	
	private String sn2 = "6484558428080716696628242653467722787263437207069762630604390703787" +
		    			"9730861808111646271401527606141756919558732184025452065542490671989" +
		    			"2428844841839353281972988531310511738648965962582821502504990264452" + 
		    			"1008852816733037111422964210278402893076574586452336833570778346897" +
		    			"15838646088239640236866252211790085787877";
	
	private String sn3 = "72006226374735042527956443552558373833808445147399984182665305798191" +
		    			"63556901883377904234086641876639384851752649940178970835240791356868" +
		    			"77441155132015188279331812309091996246361896836573643119174094961348" +
		    			"52463970788523879939683923036467667022162701835329944324119217381272" +
		    			"9276147530748597302192751375739387929";
	

	
	
	
	//字符串s1包含的数字是大略估计小于字符串sn1平方根的数字
	private String s1 = "37976931348623159077293051907890247336179769789423065727343008115"+
			   			"77326758055056206869853794492129829595855013875371640157101398586" +
			   			"47833778606925583497541085196591615128057575940752635007475935288" +
			   			"71082364994994077189561705436114947486504671101510156394068052754" +
			   			"007158456087857766374304008634074285527854909250";
	
	private String s2=  "804558428080716696628242653467722787263437207069762630604390703787" +
						"9730861808111646271401527606141756919558732184025452065542490671989" +
						"2428844841839353281972988531310511738648965962582821502504990264452" + 
						"1008852816733037111422964210278402893076574586452336833570778346897" +
						"15838646088239640236866252211790085787877";
	//s3小于但是接近6*sn3的平方根
	private String s3 = "12006226374735042527956443552558373833808445147399984182665305798191" +
						"63556901883377904234086641876639384851752649940178970835240791356868" +
						"77441155132015188279331812309091996246361896836573643119174094961348" +
						"52463970788523879939683923036467667022162701835329944324119217381272" +
						"9276147530748597302192751375739387929";	

	public static void main(String[] args) {		
		MainClass main = new MainClass();
		main.startDemo();
	}
	
	public void startDemo() {
		
		//BigNumber n1 = new BigNumber(sn1,s1);
		//n1.sqrtNewtonRaphson2();
		//n1.guess();
		//BigNumber n2 = new BigNumber(sn2,s2);
		//n2.sqrtNewtonRaphson2();
		//n2.guess2();
		//n2.guess();
		BigNumber2 n3 = new BigNumber2(sn3, s3);
		n3.sqrtNewtonRaphson2();
		n3.guessDownwards();
		n3.guessUpwards();
		
	
	}
	
	
	private static final BigDecimal SQRT_DIG = new BigDecimal(150);
	private static final BigDecimal SQRT_PRE = new BigDecimal(10).pow(SQRT_DIG.intValue());

	/**
	 * Private utility method used to compute the square root of a BigDecimal.
	 * 
	 * @author Luciano Culacciatti 
	 * @url http://www.codeproject.com/Tips/257031/Implementing-SqrtRoot-in-BigDecimal
	 */
	private static BigDecimal sqrtNewtonRaphson(BigDecimal c, BigDecimal xn, BigDecimal precision){
	    BigDecimal fx = xn.pow(2).add(c.negate());
	    BigDecimal fpx = xn.multiply(new BigDecimal(2));
	    BigDecimal xn1 = fx.divide(fpx,2*SQRT_DIG.intValue(),RoundingMode.HALF_DOWN);
	    xn1 = xn.add(xn1.negate());
	    BigDecimal currentSquare = xn1.pow(2);
	    BigDecimal currentPrecision = currentSquare.subtract(c);
	    currentPrecision = currentPrecision.abs();
	    if (currentPrecision.compareTo(precision) <= -1){
	        return xn1;
	    }	    
	    return sqrtNewtonRaphson(c, xn1, precision);
	}
	
	/**
	 * Uses Newton Raphson to compute the square root of a BigDecimal.
	 * 
	 * @author Luciano Culacciatti 
	 * @url http://www.codeproject.com/Tips/257031/Implementing-SqrtRoot-in-BigDecimal
	 */
	public static BigDecimal bigSqrt(BigDecimal c){
	    return sqrtNewtonRaphson(c,new BigDecimal(1),new BigDecimal(1).divide(SQRT_PRE));
	}	


}
