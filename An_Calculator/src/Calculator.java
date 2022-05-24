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
	
	// ���� �Էµ� ���� �������� ���������� �Ǻ�
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
	
	// ��� ��� ���
	private String Calcul() {
		// Ư����Ȳ1 - '='��ư�� ��� ���� �� 
		if(isCalcul) {
			isCalcul = false;
			String temp = getResult();
			NAN[0] = temp;
		}
		
		isCalcul = true;
		
		BigDecimal numA, numB;
		
		// ����, 1���� �Է� �޾��� �� +���� ����ִ����� �˻�
		if(index == 0) {
			if("".equals(NAN[index])) result =  "0";
			else result = NAN[0];
							
			return result;
		}
		// ����+������, 2���� �Է� �޾��� ��
		if(index == 1) {
			NAN[2] = NAN[0];
		}
		// ����+������+����, 3�� ������ �Է� �޾��� ��
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
	
	// �Էµ� ���� �°� index�� ��ȭ�� ��
	// ��ȿ�� �˻絵 ���� �����Ѵ�.
	private void nextIndex(String a) {	
		boolean chang = false;
		
		isCalcul = false; 
		
		// 1. �ǿ����� �Է�(index=0)�� �� �������� ������ ���� �ε����� �Ѿ
		// 2. ������ �Է�(index=1)�� �� �ǿ����ڸ� ������ ���� �ε����� �Ѿ
		if((index == 0 && !isNumber(a)) || (index == 1 && isNumber(a)))
			chang = true;
		// ������ �Է�(index=2)�� �� �����ڸ� ������ ���� ���� ����
		else if(index == 2 && !isNumber(a))
			return;
		
		if(chang) {
			index++;
			isDecimal = false;
		}
	}
	
	public void add(String str) {
		// ��� ���� �ް� ���ο� ���� ���� ��
		if(isCalcul) {
			init();
			
			// ���ο� ���ڰ� �ƴ� ���� �̾�ٴ� �ǹ��� �����ڶ�� ù��° ���� ����� ������
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
	
	
	// ����
	public void squared() {
		BigDecimal num = new BigDecimal(NAN[index]);
		NAN[index] = num.multiply(num, MathContext.DECIMAL128).toString();
	}
	
	// ������
	public void squareRoot() {
		double num = new BigDecimal(NAN[index]).doubleValue();
		NAN[index] = Double.toString(Math.sqrt(num));
		
		if(!NAN[index].equals(Double.toString(num)))
			System.out.println("���� �սǵƽ��ϴ�");
	}
	
	// �ۼ�Ʈ
	public void percent() {
		BigDecimal numA = new BigDecimal(NAN[index]);
		BigDecimal numB = new BigDecimal("100");
		
		NAN[index] = numA.divide(numB, MathContext.DECIMAL128).toString();		
	}
	
	// ���� -> 1/x
	public void molu() {
		BigDecimal numA = BigDecimal.ONE;
		BigDecimal numB = new BigDecimal(NAN[index]);
		
		NAN[index] = numA.divide(numB, MathContext.DECIMAL128).toString();
	}
	
	// �Ҽ���
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


