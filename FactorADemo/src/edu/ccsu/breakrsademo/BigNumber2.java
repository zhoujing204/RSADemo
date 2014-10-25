package edu.ccsu.breakrsademo;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class BigNumber2 extends BigNumber{
	
	private BigDecimal decimalValue; //������ֵķ�����ʽ
	private BigDecimal dn6;          //6*n
	private BigInteger integerValue; //������ֵ�������ʽ

	
	private BigDecimal initRoot;    //��ʼ���Ƶ�ƽ����ֵ,С��ʵ�ʵ�ƽ����ֵ
	private BigDecimal decimalRoot; //�Ͼ�ȷ��ƽ����ֵ����ȷ��С�������λ
	
	//6*n��ƽ������С������4��5��
	private BigInteger integerRoot; 
	
	private BigInteger p,q;	        //��������ֽ��õ��������������, n = p*q  
	private BigInteger a;			// a = (3p+2q)/2
	private BigInteger a2;			// a2 = a * 2;
	private BigInteger a3;          // a3 = a/3, ��p=a3ʱ��p*q����ȡ������ֵ
	
	private static final BigDecimal SQRT_DIG = new BigDecimal(2);  //��ȷ��С������λ��
	private static final BigDecimal SQRT_PRE = new BigDecimal(10).pow(SQRT_DIG.intValue());
	private BigDecimal precision = new BigDecimal(1).divide(SQRT_PRE);  //����  
	
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
		BigInteger step = new BigInteger("2").pow(exp.intValue()); //����ֵ		
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
			
			//���p����q����n����Сp��ֵ
			if(pmulq.compareTo(integerValue) == 1) {
				p = p.subtract(step);
				q = getQFromP();
				pmulq = p.multiply(q);				
			}
			
			//���p����qС��n,����p��ֵ
			else{
				//�������ֵ����1�����ʾ����ʧ��
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
		BigInteger step = new BigInteger("2").pow(exp.intValue()); //����ֵ		
		p = a3;
		q = getQFromP();		
		BigInteger pmulq = p.multiply(q);		
		while(!pmulq.equals(integerValue)) {
			
			guessCount ++;
			if(guessCount % 10000000 == 0) {
				System.out.println("guessCount = " + guessCount);
				System.out.println("p*q - N = "+ pmulq.subtract(integerValue).toString());
			}
			
			//���p����q����n������p��ֵ
			if(pmulq.compareTo(integerValue) == 1) {
				p = p.add(step);
				q = getQFromP();
				pmulq = p.multiply(q);				
			}
			
			//���p����qС��n,����p��ֵ
			else{
				
				//System.out.println("step is too big. step = " + step.toString());
				
				//�������ֵ����1�����ʾ����ʧ��
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
	
	

	//�ǵݹ���㷨,����ƽ���������С�ڲ���precision��ȡֵС�������λ
	public void sqrtNewtonRaphson2(){
		    BigDecimal fx;
		    BigDecimal fpx;
		    BigDecimal xn = initRoot;
		    BigDecimal xn1 = initRoot;	    
		    BigDecimal currentSquare = xn1.pow(2);
		    BigDecimal currentPrecision = currentSquare.subtract(dn6);
		    currentPrecision = currentPrecision.abs();

		    //�����ڹ涨�ľ���,����ѭ��������(С�ڹ涨�ľ���)����ѭ��
		    while(currentPrecision.compareTo(precision)>=0){
		    	fx = xn.pow(2).add(dn6.negate());
		    	fpx = xn.multiply(new BigDecimal(2));
		    	xn1 = fx.divide(fpx,2*SQRT_DIG.intValue(),RoundingMode.HALF_DOWN); //��2��������ʾȡС�����λ��
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
