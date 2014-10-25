package edu.ccsu.breakrsademo;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

/**
 * 
 * @author Jason
 * 要分解因子的数字的抽象
 * 
 */

public class BigNumber {
	
	private BigDecimal decimalValue; //这个数字的分数形式
	private BigInteger integerValue; //这个数字的整数形式	
	
	private BigDecimal initRoot;    //初始估计的平方根值,小于实际的平方根值
	private BigDecimal decimalRoot; //较精确的平方根值，精确到小数点后两位
	
	//平方根值的整数部分，小数部分4舍5入, q = integerRoot + x, p = integerRoot - x
	private BigInteger integerRoot; 
	
	private BigInteger p,q;	        //将这个数分解后得到的两个大的素数, n = p*q  
	private BigInteger a;			// a = (p+q)/2
	
	private static final BigDecimal SQRT_DIG = new BigDecimal(2);  //精确到小数点后的位数
	private static final BigDecimal SQRT_PRE = new BigDecimal(10).pow(SQRT_DIG.intValue());
	
	private BigDecimal precision = new BigDecimal(1).divide(SQRT_PRE);  //精度  
	
	
	public BigNumber(String value, String root) {
		decimalValue = new BigDecimal(value);
		integerValue = new BigInteger(value);
		initRoot = new BigDecimal(root);	
	}
	
	public BigNumber() {}
	
	public void guess2() {
		
		double base = 2,  exponent = 20;
		int bound = (int) Math.pow(base, exponent);		
		for(int i = 0; i < bound; i++){
			if( i%1000 == 0)
			System.out.println("i = " + i);
			int result = guess();
			if(result == 1)
				break;
			a = a.add(BigInteger.ONE);			
		}
		System.out.println("guess2 completed!");
	}
	
	public int guess() {
		BigInteger exp= new BigInteger("200");       
		BigInteger step = new BigInteger("10").pow(exp.intValue()); //步长值
		BigInteger x = step;  //搜索的初始距离
		q = a.add(x);
		p = a.subtract(x);
		BigInteger pmulq = p.multiply(q);		
		while(!pmulq.equals(integerValue)) {
			
			//如果p乘以q大于n，表示p和q值之间靠得太近
			if(pmulq.compareTo(integerValue) == 1) {
				x = x.add(step);
				q = a.add(x);
				p = a.subtract(x);
				pmulq = p.multiply(q);				
			}
			
			//如果p乘以q小于n,表示p和q值之间离得太远，将步长step减半，缩短p和q值之间的距离
			else{
				//如果步长值等于1，则表示搜索失败
				if(step.equals(BigInteger.ONE)){
					//System.out.println("Guess failed  when A = " + a.toString());	
					return -1;
				}				
				step = step.divide(new BigInteger("2"));
				x = x.subtract(step);
				q = a.add(x);
				p = a.subtract(x);
				pmulq = p.multiply(q);
				//System.out.println("p * q = " + pmulq);
			}
			//System.out.println("p * q = " + pmulq);
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
		    BigDecimal currentPrecision = currentSquare.subtract(decimalValue);
		    currentPrecision = currentPrecision.abs();

		    //误差大于规定的精度,继续循环，否则(小于规定的精度)跳出循环
		    while(currentPrecision.compareTo(precision)>=0){
		    	fx = xn.pow(2).add(decimalValue.negate());
		    	fpx = xn.multiply(new BigDecimal(2));
		    	xn1 = fx.divide(fpx,2*SQRT_DIG.intValue(),RoundingMode.HALF_DOWN); //第2个参数表示取小数点的位数
		    	xn1 = xn.add(xn1.negate());	    	
		    	currentSquare = xn1.pow(2);
			    currentPrecision = currentSquare.subtract(decimalValue);
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
		    System.out.println("BigNumber:Root = " + integerRoot.toString());
		    //System.out.println("root^2    = " + decimalRoot.pow(2));
		    //System.out.println("BigNumber = " + integerValue.toString() );
		    //System.out.println("BigNumber equal root^2? " + decimalRoot.pow(2).equals(decimalValue));
	    	return;
		}
	
	
	
	
	

}
