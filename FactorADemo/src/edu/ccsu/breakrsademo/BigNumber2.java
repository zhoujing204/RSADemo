package edu.ccsu.breakrsademo;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class BigNumber2 extends BigNumber{
	
	private BigDecimal decimalValue; //这个数字的分数形式
	private BigDecimal dn6;          //6*n
	private BigInteger integerValue; //这个数字的整数形式

	
	private BigDecimal initRoot;    //初始估计的平方根值,小于实际的平方根值
	private BigDecimal decimalRoot; //较精确的平方根值，精确到小数点后两位
	
	//6*n的平方根，小数部分4舍5入
	private BigInteger integerRoot; 
	
	private BigInteger p,q;	        //将这个数分解后得到的两个大的素数, n = p*q  
	private BigInteger a;			// a = (3p+2q)/2
	private BigInteger a2;			// a2 = a * 2;
	private BigInteger a3;          // a3 = a/3, 当p=a3时，p*q可以取到极大值
	
	private static final BigDecimal SQRT_DIG = new BigDecimal(2);  //精确到小数点后的位数
	private static final BigDecimal SQRT_PRE = new BigDecimal(10).pow(SQRT_DIG.intValue());
	private BigDecimal precision = new BigDecimal(1).divide(SQRT_PRE);  //精度  
	
	private int guessCount = 0;
	
	
	public BigNumber2(String value, String root) {
		decimalValue = new BigDecimal(value);
		BigDecimal bdSix = new BigDecimal("6"); 
		dn6 = decimalValue.multiply(bdSix);		
		integerValue = new BigInteger(value);
		initRoot = new BigDecimal(root);		
	}
	
	private BigInteger getQFromP() {
		BigInteger p3 = p.multiply(new BigInteger("3"));
		BigInteger q2 = a2.subtract(p3);
		return q2.divide(new BigInteger("2"));		
	}
	
	public int guessDownwards() {
		BigInteger exp= new BigInteger("800");       
		BigInteger step = new BigInteger("2").pow(exp.intValue()); //步长值		
		p = a3;
		q = getQFromP();		
		BigInteger pmulq = p.multiply(q);
		System.out.println("p*q = " + pmulq.toString());
		System.out.println("p*q - N=" + pmulq.subtract(integerValue).toString());
		
		while(!pmulq.equals(integerValue)) {
			guessCount ++;
			if(guessCount % 10000000 == 0) {
				System.out.println("guessCount = " + guessCount);
				System.out.println("p*q - N = "+ pmulq.subtract(integerValue).toString());
			}
			
			//如果p乘以q大于n，减小p的值
			if(pmulq.compareTo(integerValue) == 1) {
				p = p.subtract(step);
				q = getQFromP();
				pmulq = p.multiply(q);				
			}
			
			//如果p乘以q小于n,增加p的值
			else{
				//如果步长值等于1，则表示搜索失败
				if(step.equals(BigInteger.ONE)){
					System.out.println("Guessdown failed  when pmulq = " + pmulq.toString());	
					return -1;
				}				
				step = step.divide(new BigInteger("2"));				
				p = p.add(step);
				q = getQFromP();
				pmulq = p.multiply(q);				
			}			
		}
		System.out.println("q = " + q.toString());
		System.out.println("p = " + p.toString());
		System.out.println("p*q == N ? " +p.multiply(q).equals(integerValue));
		return 1;		
	}
	
	public int guessUpwards() {
		BigInteger exp= new BigInteger("800");       
		BigInteger step = new BigInteger("2").pow(exp.intValue()); //步长值		
		p = a3;
		q = getQFromP();		
		BigInteger pmulq = p.multiply(q);		
		while(!pmulq.equals(integerValue)) {
			
			guessCount ++;
			if(guessCount % 10000000 == 0) {
				System.out.println("guessCount = " + guessCount);
				System.out.println("p*q - N = "+ pmulq.subtract(integerValue).toString());
			}
			
			//如果p乘以q大于n，增加p的值
			if(pmulq.compareTo(integerValue) == 1) {
				p = p.add(step);
				q = getQFromP();
				pmulq = p.multiply(q);				
			}
			
			//如果p乘以q小于n,增加p的值
			else{
				
				//System.out.println("step is too big. step = " + step.toString());
				
				//如果步长值等于1，则表示搜索失败
				if(step.equals(BigInteger.ONE)){
					System.out.println("Guessupwards failed when p*q = " + pmulq.toString());	
					return -1;
				}				
				step = step.divide(new BigInteger("2"));				
				p = p.subtract(step);
				q = getQFromP();
				pmulq = p.multiply(q);				
			}			
		}
		System.out.println("q = " + q.toString());
		System.out.println("p = " + p.toString());
		return 1;		
	}
	
	

	//非递归的算法,计算平方根，误差小于参数precision，取值小数点后两位
	public void sqrtNewtonRaphson2(){
		    BigDecimal fx;
		    BigDecimal fpx;
		    BigDecimal xn = initRoot;
		    BigDecimal xn1 = initRoot;	    
		    BigDecimal currentSquare = xn1.pow(2);
		    BigDecimal currentPrecision = currentSquare.subtract(dn6);
		    currentPrecision = currentPrecision.abs();

		    //误差大于规定的精度,继续循环，否则(小于规定的精度)跳出循环
		    while(currentPrecision.compareTo(precision)>=0){
		    	fx = xn.pow(2).add(dn6.negate());
		    	fpx = xn.multiply(new BigDecimal(2));
		    	xn1 = fx.divide(fpx,2*SQRT_DIG.intValue(),RoundingMode.HALF_DOWN); //第2个参数表示取小数点的位数
		    	xn1 = xn.add(xn1.negate());	    	
		    	currentSquare = xn1.pow(2);
			    currentPrecision = currentSquare.subtract(dn6);
			    currentPrecision = currentPrecision.abs();
			    
			    //System.out.println("currentPrecision = " + currentPrecision.toString());
			    //System.out.println("currentSquare = " + currentSquare.toString());
			    if(xn.equals(xn1)){
			    	//System.out.println("endless loop");
			    	break;
			    }
		    	xn = xn1;
		    	//System.out.println("BigNumber: xn1 = " + xn1.toString());
		    }
		    decimalRoot = xn1;
		    integerRoot = decimalRoot.setScale(0, RoundingMode.HALF_DOWN).toBigInteger();
		    a = integerRoot;
		    System.out.println("root = " + decimalRoot.toString());
		    BigDecimal da2 = decimalRoot.multiply(new BigDecimal("2"));
		    BigDecimal da3 = decimalRoot.divide(new BigDecimal("3"));
		    System.out.println("da3   = " + da3.toString());
		    a2 = da2.setScale(0, RoundingMode.HALF_DOWN).toBigInteger();
		    a3 = da3.setScale(0, RoundingMode.CEILING).toBigInteger();
		    System.out.println("a2   = " + a2.toString());
		    System.out.println("a3   = " + a3.toString());
	    	return;
		}
	

}
