import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;

public 	class KeyBtn extends JButton{
	private Font font = new Font("고딕체", Font.PLAIN, 20);
	private Color color = new Color(0x00ffffff);
	
	KeyBtn(String text){
		super(text);
		setFont(font);
		setBackground(color);
		setBorder(null);
		addMouseListener(new MyMouseListener());
	}
	
	class MyMouseListener extends MouseAdapter{
		int orginColor;
		LineBorder lb = new LineBorder(new Color(0,0,0,50), 1);

	    @Override//마우스가 버튼 안으로 들어오면 빨간색으로 바뀜
	    public void mouseEntered(MouseEvent e) {
	    	KeyBtn b = (KeyBtn)e.getSource();
	        b.setBorder(lb);
	    }

	    @Override//마우스가 버튼 밖으로 나가면 노란색으로 바뀜
	    public void mouseExited(MouseEvent e) {
	    	KeyBtn b = (KeyBtn)e.getSource();
	    	b.setBorder(null);
	    }
	}
}