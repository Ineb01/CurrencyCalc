import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import org.json.*;



public class CurrencyCalc extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel currencyPanel;
	private JTextField left;
	private JTextField right;
	private JButton leftBtn;
	private JButton rightBtn;
	private float course;
	
	public CurrencyCalc(String leftSymbol, String rightSymbol, float course){
		super("CurrencyCalc");
		
		this.course = course;
		
		currencyPanel = new JPanel();
		left = new JTextField();
		right = new JTextField();
		leftBtn = new JButton(leftSymbol);
		rightBtn = new JButton(rightSymbol);
		
		Calculate calcAction = new Calculate();
		
		leftBtn.addActionListener(calcAction);
		rightBtn.addActionListener(calcAction);
		left.addActionListener(calcAction);
		right.addActionListener(calcAction);
		
		currencyPanel.setLayout(new GridLayout(2,2));
		currencyPanel.add(left);
		currencyPanel.add(right);
		currencyPanel.add(leftBtn);
		currencyPanel.add(rightBtn);
		
		this.add(currencyPanel);
		
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(500,90);
		this.setVisible(true);
	}
	
	private class Calculate implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			try {
				if(e.getSource() == leftBtn || e.getSource() == left) {
					right.setText("" + (Float.parseFloat(left.getText()) * course));
				}
				else if(e.getSource() == rightBtn || e.getSource() == right) {
					left.setText("" + (Float.parseFloat(right.getText()) / course));
				}
			} catch(Exception exception) {
				JOptionPane.showMessageDialog(currencyPanel, "Only valid floats!!");
			}
		}
	}
	
	public static void main(String [] args){
		new CurrencyCalc("EUR","USD", (float)1.23);
	}
	
}