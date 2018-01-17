import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.*;
import java.net.*;
import java.nio.charset.Charset;

import javax.swing.*;

import org.json.*;



public class CurrencyCalc extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel currencyPanel;
	private JTextField left;
	private JTextField right;
	private JButton leftBtn;
	private JButton rightBtn;
	private JComboBox<String> currencySelector;
	
	private JSONObject rates;
	private double selectedRate;
	private String selectedCurrency;
	private String base;
	private String date;
	private String[] currencies;
	
	public CurrencyCalc(String leftSymbol, String rightSymbol){
		super("CurrencyCalc");

		updateRates();
		try {
			selectedRate = rates.getDouble("USD");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		System.out.println(currencies[0]);
		
		currencySelector = new JComboBox<>(currencies);
		currencySelector.setSelectedIndex(22);
		
		ChooseCurrency chooseAction = new ChooseCurrency();
		currencySelector.addActionListener(chooseAction);
		
		currencyPanel = new JPanel();
		left = new JTextField();
		right = new JTextField();
		leftBtn = new JButton(this.base);
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
		
		getContentPane().add(currencyPanel, BorderLayout.CENTER);
		getContentPane().add(currencySelector, BorderLayout.EAST);
		
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(500,100);
		this.setVisible(true);
	}
	
	private class Calculate implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			try {
				if(e.getSource() == leftBtn || e.getSource() == left) {
					right.setText("" + (Float.parseFloat(left.getText()))*selectedRate);
				}
				else if(e.getSource() == rightBtn || e.getSource() == right) {
					left.setText("" + (Float.parseFloat(right.getText()))/selectedRate);
				}
			} catch(Exception exception) {
				JOptionPane.showMessageDialog(currencyPanel, "Only valid floats!!");
			}
		}
	}
	
	private class ChooseCurrency implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == currencySelector) {
				selectedCurrency = (String) currencySelector.getSelectedItem();
				try {
					selectedRate = rates.getDouble(selectedCurrency);
				} catch (JSONException e1) {
					JOptionPane.showMessageDialog(currencyPanel, "This Currency is not available!");
				}
				try{
					right.setText("" + (Float.parseFloat(left.getText()))*selectedRate);
				} catch (Exception exception) {
				}
				rightBtn.setText(selectedCurrency);
			}
		}
	}
	
	public static void main(String [] args){
		new CurrencyCalc("EUR","USD");
	}
	
	private void updateRates() {
		try{
			InputStream is = new URL("https://api.fixer.io/latest").openStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			StringBuilder jsonText = new StringBuilder();
			String buff;
			while ((buff = rd.readLine()) != null) {
			      jsonText.append(buff);
			}
		    JSONObject json = new JSONObject(jsonText.toString());
		    this.base = json.getString("base");
		    this.date = json.getString("date");
		    this.rates = json.getJSONObject("rates");
		    JSONArray jsonNames = this.rates.names();
		    currencies = new String[jsonNames.length()];
		    for(int i =0;i<jsonNames.length();i++) {
		    	currencies[i] = jsonNames.getString(i);
		    }
		} catch (Exception e) {
			JOptionPane.showMessageDialog(currencyPanel, "You are offline!");
		}
	}
	
}