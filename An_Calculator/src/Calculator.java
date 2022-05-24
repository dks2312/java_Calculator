import java.math.BigDecimal;
import java.math.MathContext;

class Calculator{
	private String[] NAN = new String[3];
	private int index = 0;
	
	private String result;
	
	private boolean isCalcul;
	private boolean isDecimal;	
	
	private void init() {
		NAN[0] = "";
		NAN[1] = "";
		NAN[2] = "";
		index = 0;
		isDecimal = false;
	}
	
	Calculator(){
		init();
	}
	
	// 현재 입력된 값이 숫자인지 연산자인지 판별
	private boolean isNumber(String str) {
		if(str.equals("+") || str.equals("/") || str.equals("*") || str.equals("-")) 
			return false;
		return true;
	}
	
	private void calculNext() {
		init();
		NAN[index] = result;
		index++;
	}
	
	// 계산 결과 출력
	private String Calcul() {
		// 특수상황1 - '='버튼을 계속 누를 때 
		if(isCalcul) {
			isCalcul = false;
			String temp = getResult();
			NAN[0] = temp;
		}
		
		isCalcul = true;
		
		BigDecimal numA, numB;
		
		// 숫자, 1개만 입력 받았을 때 +값이 들어있는지도 검사
		if(index == 0) {
			if("".equals(NAN[index])) result =  "0";
			else result = NAN[0];
							
			return result;
		}
		// 숫자+연산자, 2개만 입력 받았을 때
		if(index == 1) {
			NAN[2] = NAN[0];
		}
		// 숫자+연산자+숫자, 3개 온전히 입력 받았을 때
		numA = new BigDecimal(NAN[0]);
		numB = new BigDecimal(NAN[2]);
		
		switch(NAN[1].toString()) {
			case "+": result = numA.add(numB).toString(); break;
			case "-": result = numA.subtract(numB).toString(); break;
			case "*": result = numA.multiply(numB, MathContext.DECIMAL128).toString(); break;
			default : result = numA.divide(numB, MathContext.DECIMAL128).toString();
		}		
		
		index = 2;
		return result;
	}
	
	// 입력된 값에 맞게 index에 변화를 줌
	// 유효성 검사도 같이 실행한다.
	private void nextIndex(String a) {	
		boolean chang = false;
		
		isCalcul = false; 
		
		// 1. 피연산자 입력(index=0)일 때 연산자을 만나면 다음 인덱스로 넘어감
		// 2. 연산자 입력(index=1)일 때 피연산자를 만나면 다음 인덱스로 넘어감
		if((index == 0 && !isNumber(a)) || (index == 1 && isNumber(a)))
			chang = true;
		// 마지막 입력(index=2)일 때 연산자를 만나면 값을 받지 않음
		else if(index == 2 && !isNumber(a))
			return;
		
		if(chang) {
			index++;
			isDecimal = false;
		}
	}
	
	public void add(String str) {
		// 결과 값을 받고 새로운 값을 받을 때
		if(isCalcul) {
			init();
			
			// 새로운 숫자가 아닌 값을 이어간다는 의미의 연산자라면 첫번째 값에 결과를 저장함
			if(!isNumber(str)){
				calculNext();
			}
			
			isCalcul = false;
		}
		
		nextIndex(str);
		
		if(index == 1) NAN[index] = str;
		else NAN[index] += str;
	}
	
	public String FiledtoString() {
		String ret = "";
		
		for(int i = 0; i < NAN.length; i++) {
			if("".equals(NAN[i])) break;
			
			ret += NAN[i] + " ";
		}
		
		if(index == 2 && !NAN[2].equals("") || index == 0 && !NAN[0].equals("")) ret += "= ";
		
		return ret;
	}
	
	public void reverse() {
		String num;
		
		if(isCalcul) {
			num = result;
			isCalcul = false;
		}
		else if("".equals(NAN[index])) 
			return;
		else 
			num = NAN[index];
		
		BigDecimal numA = new BigDecimal(num);
		BigDecimal numB = new BigDecimal("-1");
		
		NAN[index] = numA.multiply(numB, MathContext.DECIMAL128).toString();
	}
	
	
	// 제곱
	public void squared() {
		BigDecimal num = new BigDecimal(NAN[index]);
		NAN[index] = num.multiply(num, MathContext.DECIMAL128).toString();
	}
	
	// 제곱근
	public void squareRoot() {
		double num = new BigDecimal(NAN[index]).doubleValue();
		NAN[index] = Double.toString(Math.sqrt(num));
		
		if(!NAN[index].equals(Double.toString(num)))
			System.out.println("값이 손실됐습니다");
	}
	
	// 퍼센트
	public void percent() {
		BigDecimal numA = new BigDecimal(NAN[index]);
		BigDecimal numB = new BigDecimal("100");
		
		NAN[index] = numA.divide(numB, MathContext.DECIMAL128).toString();		
	}
	
	// 몰루 -> 1/x
	public void molu() {
		BigDecimal numA = BigDecimal.ONE;
		BigDecimal numB = new BigDecimal(NAN[index]);
		
		NAN[index] = numA.divide(numB, MathContext.DECIMAL128).toString();
	}
	
	// 소수점
	public void decimalPoint() {
		if(isDecimal) return;
		
		isDecimal = true;
		NAN[index] += ".";
	}		
	
	public void deleteEnd() {
		if(NAN[index].length() > 1) {
			if(isCalcul) {
				NAN[index] = result;
				isCalcul = false;
			}
			
			NAN[index] = NAN[index].substring(0, NAN[index].length()-1);
		}
		else 
			NAN[index] ="0";
	}
	
	public void deleteAll() { 
		init();
	}
	
	public void deleteButter() { 
		NAN[index] = ""; 
	}
	
	
	public String getFormula() {
		return FiledtoString();
	}
	
	public String getResult() {
		return Calcul();
	}
	
	public String getBufferStr() {
		return NAN[index];
	}
}


