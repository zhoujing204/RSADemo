package edu.ccsu.breakrsademo;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

/**
 * 
 * @author Jason
 * Ҫ�ֽ����ӵ����ֵĳ���
 * 
 */

public class BigNumber {
	
	private BigDecimal decimalValue; //������ֵķ�����ʽ
	private BigInteger integerValue; //������ֵ�������ʽ	
	
	private BigDecimal initRoot;    //��ʼ���Ƶ�ƽ����ֵ,С��ʵ�ʵ�ƽ����ֵ
	private BigDecimal decimalRoot; //�Ͼ�ȷ��ƽ����ֵ����ȷ��С�������λ
	
	//ƽ����ֵ���������֣�С������4��5��, q = integerRoot + x, p = integerRoot - x
	private BigInteger integerRoot; 
	
	private BigInteger p,q;	        //��������ֽ��õ��������������, n = p*q  
	private BigInteger a;			// a = (p+q)/2
	
	private static final BigDecimal SQRT_DIG = new BigDecimal(2);  //��ȷ��С������λ��
	private static final BigDecimal SQRT_PRE = new BigDecimal(10).pow(SQRT_DIG.intValue());
	
	private BigDecimal precision = new BigDecimal(1).divide(SQRT_PRE);  //����  
	
	
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
		BigInteger step = new BigInteger("10").pow(exp.intValue()); //����ֵ
		BigInteger x = step;  //�����ĳ�ʼ����
		q = a.add(x);
		p = a.subtract(x);
		BigInteger pmulq = p.multiply(q);		
		while(!pmulq.equals(integerValue)) {
			
			//���p����q����n����ʾp��qֵ֮�俿��̫��
			if(pmulq.compareTo(integerValue) == 1) {
				x = x.add(step);
				q = a.add(x);
				p = a.subtract(x);
				pmulq = p.multiply(q);				
			}
			
			//���p����qС��n,��ʾp��qֵ֮�����̫Զ��������step���룬����p��qֵ֮��ľ���
			else{
				//�������ֵ����1�����ʾ����ʧ��
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

	//�ǵݹ���㷨,����ƽ���������С�ڲ���precision��ȡֵС�������λ
	public void sqrtNewtonRaphson2(){
		    BigDecimal fx;
		    BigDecimal fpx;
		    BigDecimal xn = initRoot;
		    BigDecimal xn1 = initRoot;	    
		    BigDecimal currentSquare = xn1.pow(2);
		    BigDecimal currentPrecision = currentSquare.subtract(decimalValue);
		    currentPrecision = currentPrecision.abs();

		    //�����ڹ涨�ľ���,����ѭ��������(С�ڹ涨�ľ���)����ѭ��
		    while(currentPrecision.compareTo(precision)>=0){
		    	fx = xn.pow(2).add(decimalValue.negate());
		    	fpx = xn.multiply(new BigDecimal(2));
		    	xn1 = fx.divide(fpx,2*SQRT_DIG.intValue(),RoundingMode.HALF_DOWN); //��2��������ʾȡС�����λ��
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
