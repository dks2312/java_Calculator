import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;

/*
 * �����ؾߵ� ����
 * 1 String -> StringBulider�� ��ȯ : String�� �Һ����̹Ƿ� �ѱ��ھ� ���� �ޱ⿣ �������� ����
 * 2 BigDecimal�� 0 �Ǵ� ������ ���� �ʵ��� ó�� : ���� �߻�
 * 3 �ϴ� ������ ������ ����
 * 
 * */
public class CalculatorMain extends JFrame{
	private JTextField textField;
	private JLabel textHoldField;
	private Calculator fieldNAN = new Calculator();
	
	Color frameColor = new Color(0x00E0E0E0);
	Color ggrayColor = new Color(0x00FAFAFA);
	Color resultColor = new Color(0x008DC3F9);
	
	CalculatorMain(){
		JFrame frame = new JFrame("����");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		frame.setPreferredSize(new Dimension(320, 480));
		frame.setResizable(false);
		frame.getContentPane().setBackground(frameColor);
		
		ImageIcon topImage = new ImageIcon("images/topImage.png");
		JLabel topLabel = new JLabel(topImage);
		topLabel.setPreferredSize(new Dimension(frame.getPreferredSize().width, 40));
		
		JPanel textFieldPanel = new JPanel();
		textFieldPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		textFieldPanel.setPreferredSize(new Dimension(frame.getPreferredSize().width, 100));
		
		textField = new JTextField("0");
		textField.setEditable(false);
		textField.setFont(new Font("���ü", Font.BOLD, 50));
		textField.setPreferredSize(new Dimension(textFieldPanel.getPreferredSize().width - 20, 60));
		textField.setHorizontalAlignment(JLabel.RIGHT);
		textField.setBorder(new LineBorder(new Color(0,0,0,0), 10));

		textHoldField = new JLabel("");
		textHoldField.setPreferredSize(new Dimension(textFieldPanel.getPreferredSize().width - 40, 20));
		textHoldField.setHorizontalAlignment(JLabel.RIGHT);
		textHoldField.setForeground(Color.gray); 
		
		
		JPanel inputPanel = new JPanel();	
		inputPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 3,0));
		inputPanel.setPreferredSize(new Dimension(frame.getPreferredSize().width-20, 300));
		
		JPanel KeyPanel = new JPanel();
		KeyPanel.setLayout(new GridLayout( 6, 4, 3,3));
		KeyPanel.setPreferredSize(new Dimension(inputPanel.getPreferredSize().width, inputPanel.getPreferredSize().height));
		KeyPanel.setOpaque(false);
		
		// ��ư
		String[] keyPadName = new String[]{ "%", 	"CE", 	"C", 	"��",
											"1/x", 	"x��",	"2��x", 	"��",
											"7", 	"8", 	"9", 	"��",
											"4", 	"5", 	"6", 	"��",
											"1", 	"2", 	"3", 	"��",
											"��", 	"0", 	".", 	"="};
		KeyBtn[] keyPad = new KeyBtn[keyPadName.length];
		
		for(int i = 0; i < keyPadName.length; i++) {
			keyPad[i] = new KeyBtn(keyPadName[i]);
			
			switch(keyPadName[i]) {
				case "1": case "2": case "3": case "4": case "5": case "6": case "7": case "8": case "9": case "0": 
					keyPad[i].addMouseListener(new number());
					keyPad[i].setFont(new Font("���ü", Font.BOLD, 20));
					break;
					
				case "��": case "��": case "��": case "��": 
					keyPad[i].addMouseListener(new arithmetic());
					keyPad[i].setBackground(ggrayColor);
					break;
					
				case "CE": case "C":  case "%": case "1/x": case "x��": case "2��x": case "��":
					keyPad[i].setBackground(ggrayColor);
				case "��": case ".":	
					keyPad[i].addMouseListener(new single());
					break;
					
				default:
					keyPad[i].addMouseListener(new result());
					keyPad[i].setBackground(resultColor);
			}
			
			KeyPanel.add(keyPad[i]);
		}
		
		inputPanel.add(KeyPanel);
		textFieldPanel.add(textHoldField);
		textFieldPanel.add(textField);
		
		frame.add(topLabel);
		frame.add(textFieldPanel);
		frame.add(inputPanel);
		
		frame.setVisible(true);
		frame.pack();
	}
	
	
	// ���ڹ�ư�� ������ �� �Ŀ� ����
	class number extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			JButton b = (JButton)e.getSource();
			fieldNAN.add(b.getText());
			textField.setText(fieldNAN.getBufferStr());
		}
	}
	
	// �����ư�� ������ �� �Ŀ� ����
	class arithmetic extends MouseAdapter {
		public void mousePressed(MouseEvent e) {			
			JButton b = (JButton)e.getSource();
			
			switch(b.getText()) {
				case "��": 	fieldNAN.add("+"); break;
				case "��": 	fieldNAN.add("-"); break;
				case "��": 	fieldNAN.add("*"); break;
				default: 	fieldNAN.add("/");
			}
			
			holdFiledUpdate();
		}
	}
	

	class single extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			JButton b = (JButton)e.getSource();
			
			if(textField.getText().equals("0") || textField.getText().equals("")) 
				return;
			
			switch(b.getText()) {
				case "CE": 	fieldNAN.deleteButter();break;
				case "C": 	fieldNAN.deleteAll(); 	break;
				case "��": 	fieldNAN.deleteEnd(); 	break;
				
				case "%": 	fieldNAN.percent(); 	break;
				case "1/x": fieldNAN.molu(); 		break;
				case "x��": 	fieldNAN.squared(); 	break;
				case "2��x": fieldNAN.squareRoot(); 	break;
				case "��": 	fieldNAN.reverse(); 	break;
				default: 	fieldNAN.decimalPoint();
			}
			
			holdFiledUpdate();
			textField.setText(fieldNAN.getBufferStr());
		}
	}	
	
	// = ��ư ��� : �Էµ� ���� ����Ѵ�
	class result extends MouseAdapter{
		@Override
		public void mousePressed(MouseEvent e) {
			textField.setText(fieldNAN.getResult());
			holdFiledUpdate();
		}
	}	
	
	
	private void holdFiledUpdate(){
		textHoldField.setText(fieldNAN.getFormula());
	}
	
	// ���� ������(�޸�)�� ��� �Լ� 
	// �޸� ���� ����ϴ°� ������ �� ���� 
//	public void StringFormat(String text) {
//		String format = new String();
//		
//		int comma = 0;
//		
//		if(text.charAt(0) == 1)
//		
//		for(int i = text.length()-1; i >= 0; i--) {	
//			format += text.charAt(text.length()-1-i);
//			if(i%3== 0 && i != 0) format += ",";
//		}
//		textField.setText(format);
//		
//		textField.setText(text);
//	}

	public static void main(String[] args) {
		new CalculatorMain();
	}
}
