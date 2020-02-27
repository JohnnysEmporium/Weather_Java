import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.UIManager;

public class Interface extends JFrame{
	
	
	JPanel[] tabArr = new JPanel[3];
	//keys are labelNames and values are new JLabels that will hold data
	Map<String, JLabel[]> cMap = new HashMap<String, JLabel[]>();
	//keys are labelNames and values are new JLabels that will hold icons
	Map<String, JLabel[]> ciMap = new HashMap<String, JLabel[]>();
	//keys are labelNames and values are arrays of new JLabels that will hold data
	Map<String, JLabel[]> hMap = new HashMap<String, JLabel[]>();
	Map<String, JLabel[]> dMap = new HashMap<String, JLabel[]>();
	//keys are labelNames and values are arrays of new JLabels that will hold icons
	Map<String, JLabel[]> hiMap = new HashMap<String, JLabel[]>();
	Map<String, JLabel[]> diMap = new HashMap<String, JLabel[]>();
	String[] labelNames = {"time", "summ", "temp", "humid", "preass", "precipP", "precipT", "windB", "windS"};
	private static DecimalFormat df = new DecimalFormat("#.#");

	public Interface() {
		super("Weather");
		start();
	}
	
	private JLabel[] insertJLabels(int x){
		JLabel[] temp = new JLabel[x];
		for(int j = 0; j < temp.length; j++) {
			temp[j] = new JLabel();
//			temp[j].setForeground(Color.GRAY);
		}
		return temp;
	}
	
	private JLabel[] insertRotatedJLabels(int x, String[] bearing){
		JLabel[] temp = new JLabel[x];
		for(int j = 0; j < temp.length; j++) {
			int angle = Integer.parseInt(bearing[j]);
			temp[j] = rotateWindArrow(angle);
//			temp[j].setForeground(Color.GRAY);
		}
		return temp;
	}
	
	private JLabel rotateWindArrow(int angle) {
		
		JLabel label = new JLabel(IconLoader.wind_N) {
			protected void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D)g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
										RenderingHints.VALUE_ANTIALIAS_ON);
				AffineTransform aT = g2.getTransform();
				double x = getWidth()/2.0;
				double y = getHeight()/2.0;
				aT.rotate(Math.toRadians(angle), x, y);
				g2.setTransform(aT);
				super.paintComponent(g);
			    }
			};
		return label;
	}
	
	
	//Creating arrays of JLabels that will hold icons and data
	//Holds JLabels for data
	private void putJLabelsIntoMaps(){
		//Calling methods of 2 helping classes that will update their contents
		Data.getWeatherForLocation(new String[] {"Krakow Poland"});
		IconLoader.getIcons();
		for(int i = 0; i < labelNames.length; i++) {
			if(labelNames[i] != "windB") {
				hMap.put(labelNames[i],insertJLabels(49));
				//Holds JLabels for icons
				hiMap.put(labelNames[i],insertJLabels(49));
				dMap.put(labelNames[i],insertJLabels(8));
				diMap.put(labelNames[i],insertJLabels(8));
				cMap.put(labelNames[i],insertJLabels(1));
				ciMap.put(labelNames[i],insertJLabels(1));
			} else {
				hiMap.put(labelNames[i],insertJLabels(49));
				diMap.put(labelNames[i],insertJLabels(8));
				ciMap.put(labelNames[i],insertJLabels(1));
				hMap.put(labelNames[i],insertRotatedJLabels(49, Data.hWindBearing));
				dMap.put(labelNames[i],insertRotatedJLabels(8, Data.dWindBearing));
				cMap.put(labelNames[i], new JLabel[] {rotateWindArrow(Integer.parseInt(Data.cWindBearing))});
			}
		}
	}
	
	private void setInterface(){
		
		//Creating JFrame window
		setSize(1000,1000);
		getContentPane().setBackground(Color.BLACK);

		
		//Setting layout and components for main frame
		setContentPane(new JLabel(IconLoader.background));
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		
		//Search field and "Find" button
		JTextField locationTextField = new JTextField(15);
		JButton locationButton = new JButton("Find");
		//Adding action listener to button
		locationButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String text = locationTextField.getText();
				if(text.length() > 1) {
					String[] location = text.split(" ");
					System.out.println(location);
					Data.getWeatherForLocation(location);
					setData();
					setIcons();
					locationTextField.setText(Data.country + ", " + Data.city);
				}
			}
		});
		
		locationTextField.setText(Data.country + ", " + Data.city);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1.0;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbc.insets = new Insets(10,5,0,10);
		add(locationTextField, gbc);
		

		locationButton.setBackground(Color.BLACK);
		locationButton.setForeground(Color.WHITE);
		gbc.gridx = 1;
		gbc.fill = GridBagConstraints.NONE;
		add(locationButton, gbc);
		
		//Tab container, adding to main frame, below search bar\
		UIManager.put("TabbedPane.contentOpaque", false);
		JTabbedPane tabs = new JTabbedPane();
//		tabs.setBackground(Color.BLACK);
//		tabs.setForeground(Color.WHITE);
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		gbc.weighty = 1.0;
		gbc.weightx = 0;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(10,0,0,0);
		gbc.anchor = GridBagConstraints.PAGE_END;
		add(tabs, gbc);
		
		//Setting scrollable Panes as tabs main containers
		JPanel currentlyTab = new JPanel();
		currentlyTab.setOpaque(false);
		JScrollPane currentlyScroll = new JScrollPane(currentlyTab);
		currentlyScroll.setOpaque(false);
		currentlyScroll.getViewport().setOpaque(false);
		tabs.addTab("Currently", currentlyScroll);
		JPanel hourlyTab = new JPanel();
		hourlyTab.setOpaque(false);
		JScrollPane hourlyScroll = new JScrollPane(hourlyTab);
		hourlyScroll.setOpaque(false);
		hourlyScroll.getViewport().setOpaque(false);
		tabs.addTab("Hourly", hourlyScroll);
		JPanel dailyTab = new JPanel();
		dailyTab.setOpaque(false);
		JScrollPane dailyScroll = new JScrollPane(dailyTab);
		dailyScroll.setOpaque(false);
		dailyScroll.getViewport().setOpaque(false);
		tabs.addTab("Daily", dailyScroll);


		
		//Assigning JPanels to array 
		tabArr[0] = currentlyTab;
		tabArr[1] = hourlyTab;
		tabArr[2] = dailyTab;
		
		currentlyTab.setLayout(new GridBagLayout());
		hourlyTab.setLayout(new GridBagLayout());
		dailyTab.setLayout(new GridBagLayout());
		gbc = new GridBagConstraints();
		
		//This nested loop puts JLabels in specific positions in Hourly and Daily tabs
		//use i for rows, j and k for columns
		for(int i = 0; i < labelNames.length; i++) {
			
			//Putting separators inbetween labelNames for ease of readability
			JSeparator[] separators = {new JSeparator(JSeparator.HORIZONTAL), new JSeparator(JSeparator.HORIZONTAL)};
			for(JSeparator x : separators){
				x.setBackground(new Color(0,0,0));
				x.setForeground(new Color(0,0,0));
			}
			gbc.gridy = (3*i)+2;
			gbc.gridx = 0;
			gbc.weightx = 1.0;
	        gbc.fill = GridBagConstraints.HORIZONTAL;
	        gbc.gridwidth = GridBagConstraints.REMAINDER;
	        hourlyTab.add(separators[0], gbc);
	        dailyTab.add(separators[1], gbc);
	        gbc.fill = GridBagConstraints.NONE;
	        gbc.gridwidth = 1;
			
			//X-axis -> displaying data
			//Y-axis -> displaying hours/days
			//			holders for icons
			gbc.gridy = 3*i;
			gbc.weighty = 1.0;
			gbc.weightx = 0.5;
			gbc.insets = new Insets(0,8,0,8);
			
			//Puts icon JLabels
			//Hourly tab
			for(int j = 0; j < hMap.get("time").length; j++) {
				gbc.insets = new Insets(2,15,2,15);
				gbc.gridx = j;
				hourlyTab.add(hiMap.get(labelNames[i])[j], gbc);
			}
			//Daily tab
			for(int k = 0; k < dMap.get("time").length; k++) {
				gbc.insets = new Insets(0,8,0,8);
				gbc.gridx = k;
				dailyTab.add(diMap.get(labelNames[i])[k], gbc);
			}
			
			gbc.gridy = (3*i)+1;
			
			//Puts info JLabels
			//Hourly tab
			for(int j = 0; j < hMap.get("time").length; j++) {
				gbc.insets = new Insets(2,15,2,15);
				gbc.gridx = j;
				hourlyTab.add(hMap.get(labelNames[i])[j], gbc);
			}
			//Daily tab
			for(int k = 0; k < dMap.get("time").length; k++) {
				gbc.insets = new Insets(0,8,0,8);
				gbc.gridx = k;
				dailyTab.add(dMap.get(labelNames[i])[k], gbc);
			}
		}
		//Currently Tab
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.2;
		gbc.weighty = 0.2;
		gbc.gridwidth = 4;
		currentlyTab.add(ciMap.get("summ")[0], gbc);
		gbc.gridy = 1;
		currentlyTab.add(cMap.get("summ")[0], gbc);
		gbc.weightx = 0.6;
		gbc.weighty = 0.6;
		gbc.gridwidth = 1;
		for(int i = 1; i < 4; i++) {
			gbc.gridx = 0;
			gbc.gridy = i+1;
			currentlyTab.add(ciMap.get(labelNames[i+1])[0], gbc);
			gbc.gridx = 1;
			currentlyTab.add(cMap.get(labelNames[i+1])[0], gbc);
		}
		for(int i = 1; i < labelNames.length-4; i++) {
			gbc.gridx = 2;
			gbc.gridy = i+1;
			currentlyTab.add(ciMap.get(labelNames[i+4])[0], gbc);
			gbc.gridx = 3;
			currentlyTab.add(cMap.get(labelNames[i+4])[0], gbc);
		}
		
		setFontSize(ciMap, Font.BOLD, 22);
		setFontSize(cMap, Font.PLAIN, 20);
		setFontSize(hiMap, Font.BOLD, 18);
		setFontSize(hMap, Font.PLAIN, 16);
		setFontSize(diMap, Font.BOLD, 18);
		setFontSize(dMap, Font.PLAIN, 16);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	//Sets icons based on given data, positions JLabels in correct way and assigns text value to icon JLabels 
	private void insertIconsToMap(Map<String,JLabel[]> icon, Map<String,JLabel[]> text ) {
				
		for(int i = 0; i < icon.get("time").length; i++) {
			Double temp = Double.parseDouble(text.get("temp")[i].getText().split("°C")[0]);
			Double humid = Double.parseDouble(text.get("humid")[i].getText().split("%")[0]);
			Double precip = Double.parseDouble(text.get("precipP")[i].getText().split("%")[0]);


			icon.get("temp")[i].setText("Temperature");
			icon.get("temp")[i].setVerticalTextPosition(JLabel.TOP);
			icon.get("temp")[i].setHorizontalTextPosition(JLabel.CENTER);
			
			if(temp < 0) {
				icon.get("temp")[i].setIcon(IconLoader.temp0);
			} else if(temp >= 0 && temp < 10) {
				icon.get("temp")[i].setIcon(IconLoader.temp1);
			} else if(temp >= 10 && temp < 20) {
				icon.get("temp")[i].setIcon(IconLoader.temp2);
			} else if(temp >= 20) {
				icon.get("temp")[i].setIcon(IconLoader.temp3);
			}
					
			icon.get("humid")[i].setText("Humidity");
			icon.get("humid")[i].setVerticalTextPosition(JLabel.TOP);
			icon.get("humid")[i].setHorizontalTextPosition(JLabel.CENTER);
			
			if(humid < 0.3 ) {
				if(precip > 0.3) {
					icon.get("humid")[i].setIcon(IconLoader.sun);
				} else {
					icon.get("humid")[i].setIcon(IconLoader.sun_cloud_rain);
				}
			} else if(humid >= 0.3 && humid < 0.7) {
				if(precip >= 0.3) {
					icon.get("humid")[i].setIcon(IconLoader.sun_cloud_rain);
				} else {
					icon.get("humid")[i].setIcon(IconLoader.sun_cloud);
				}
			} else {
				if(precip >= 0.3) {
					icon.get("humid")[i].setIcon(IconLoader.cloud_rain);
				} else {
					icon.get("humid")[i].setIcon(IconLoader.cloud);
				}
			}
			
//			icon.get("summ")[i].setText("Summary");
			icon.get("preass")[i].setText("Preassure");
			icon.get("precipP")[i].setText("Precip chance");
			icon.get("precipT")[i].setText("Precip type");
			//icon.get("windB")[i].setText("Wind direction");
			icon.get("windS")[i].setText("Wind speed");
			
		}
	}
	
	//setData() IS FOR CUSTOMIZING WHAT IS BEEING DISPLAYED IN WHICH HOLDER
	private void setData() {
		//Setting data for Currently tab
		cMap.get("summ")[0].setText(Data.cSummary);
		cMap.get("temp")[0].setText(Data.cTemp + " °C");
		cMap.get("humid")[0].setText(String.valueOf((int)(Double.parseDouble(Data.cHumid) * 100)) + "%");
		cMap.get("preass")[0].setText(Data.cPreassure + " hPa");
		cMap.get("precipP")[0].setText(String.valueOf((int)(Double.parseDouble(Data.cPrecipProb) * 100)) + "%");
		cMap.get("precipT")[0].setText(Data.cPrecipType);
		ciMap.get("windB")[0].setText("Wind Direction");
		cMap.get("windS")[0].setText(df.format((Double.parseDouble(Data.cWindSpeed) * 5) / 18) + " km/h");
				
		String[][] hDataArray = {Data.hTime, Data.hSummary, Data.hTemp, Data.hHumid, Data.hPreassure, Data.hPrecipProb, Data.hPrecipType, Data.hWindBearing, Data.hWindSpeed};
		String[][] dDataArray = {Data.dTime, Data.dSummary, Data.dTemp, Data.dHumid, Data.dPreassure, Data.dPrecipProb, Data.dPrecipType, Data.dWindBearing, Data.dWindSpeed};
		fillHourlyAndDailyJLabels(hDataArray, hMap, hiMap);
		fillHourlyAndDailyJLabels(dDataArray, dMap, diMap);
	}

	
	//This method is putting data in JLabels, loops iterate over maps and JLabels in these maps
	//If condition is added to prevent putting Data.(c/h/d)WindB into text fields. Instead, wind arrow is held in text JLabel and "Wind Direction" text in icon JLabel
	//{"time", "summ", "temp", "humid", "preass", "precipP", "precipT", "windB", "windS"}
	private void fillHourlyAndDailyJLabels(String[][] dataArray, Map<String,JLabel[]> mapData, Map<String,JLabel[]> mapIcons) {
		for(int i = 0; i < mapData.size(); i++) {
			if(labelNames[i] == "windB") {
				JLabel[] currentLabels = mapIcons.get(labelNames[i]);
				for(int j = 0; j < currentLabels.length; j++) {
					currentLabels[j].setText("Wind Direction");
				}
			} else if(labelNames[i] == "temp" ) {
				JLabel[] currentLabels = mapData.get(labelNames[i]);
				for(int j = 0; j < currentLabels.length; j++) {
					currentLabels[j].setText(dataArray[i][j] + " °C");
				}
			} else if(labelNames[i] == "humid"){
				JLabel[] currentLabels = mapData.get(labelNames[i]);
				for(int j = 0; j < currentLabels.length; j++) {
					int temp = (int)(Double.parseDouble(dataArray[i][j]) * 100);
					currentLabels[j].setText(String.valueOf(temp) + "%");
				}
			} else if(labelNames[i] == "preass"){
				JLabel[] currentLabels = mapData.get(labelNames[i]);
				for(int j = 0; j < currentLabels.length; j++) {
					currentLabels[j].setText(dataArray[i][j] + " hPa");
				}
			} else if(labelNames[i] == "precipP"){
				JLabel[] currentLabels = mapData.get(labelNames[i]);
				for(int j = 0; j < currentLabels.length; j++) {
					int temp = (int)(Double.parseDouble(dataArray[i][j]) * 100);
					currentLabels[j].setText(temp + "%");
				}			
			} else if(labelNames[i] == "windS"){
				JLabel[] currentLabels = mapData.get(labelNames[i]);
				for(int j = 0; j < currentLabels.length; j++) {
					currentLabels[j].setText(df.format((Double.parseDouble(Data.cWindSpeed) * 5) / 18) + " km/h");
				}	
			} else {
				JLabel[] currentLabels = mapData.get(labelNames[i]);
				for(int j = 0; j < currentLabels.length; j++) {
					currentLabels[j].setText(dataArray[i][j]);
				}

			}
		}

	}
	
	private void setFontSize(Map<String,JLabel[]> map, int fontStyle, int fontSize) {
		map.forEach((k,v) -> {
			for(int i = 0; i < v.length; i++) {
				v[i].setFont(new Font("Calibri", fontStyle, fontSize));
			}
		});
	}
	
	
	private void setIcons() {
		insertIconsToMap(hiMap, hMap);
		insertIconsToMap(diMap, dMap);
		insertIconsToMap(ciMap, cMap);
		
	}

	private void start() {
		putJLabelsIntoMaps();
		setData();
		setIcons();
		setInterface();
	}
	
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				Interface ifc = new Interface();
			}
		});
	}
}