import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Iterator;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.OffsetTime;  
import java.time.temporal.ChronoField;  
import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import java.sql.*;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import acm.graphics.*;
import acm.program.GraphicsProgram;
import acm.program.GraphicsProgramInterface;

public class Scouting extends GraphicsProgram {

	/** True after clicking the start button **/
	private boolean gameOn;
	StringBuilder query;
	// data to record
	private String connectionURL = "jdbc:sqlserver://frc3630.database.windows.net:1433;database=scouting;user=Stampede3630@frc3630;password=Whatsaspock?;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;;";
	private String matchNumber = null;
	private Boolean isRed = null;
	private String teamNumber = null;
	private Boolean autoRun = false;
	private Integer autoSwitch = 0;
	private Integer autoWrongSwitch = 0;
	private Integer autoScale = 0;
	private Integer autoWrongScale = 0;
	private Integer autoOppSwitch = 0;
	private Integer autoOppWrongSwitch = 0;
	private Integer autoVault = 0;
	private Integer teleSwitch = 0;
	private Integer teleWrongSwitch = 0;
	private Integer teleScale = 0;
	private Integer teleWrongScale = 0;
	private Integer teleOppSwitch = 0;
	private Integer teleOppWrongSwitch = 0;
	private Integer teleVault = 0;
	private Boolean parked = false;
	private Boolean climb = false;
	private String climbType = "Default";
	private String notes = "Default";
	
	// All the interactors that will be called more than once
	private GCanvas canvas = new GCanvas();
	private JTextField matchNum = new JTextField(5);
	private JTextField red1;
	private JTextField red2;
	private JTextField red3;
	private JTextField blue1;
	private JTextField blue2;
	private JTextField blue3;
	private JComboBox<String> mode;
	private JButton start;
	private JButton reset;
	private JButton submit;
	private JButton blueLine;
	private JButton blueRung;
	private JButton blueVault;
	private JButton bottomScale;
	private JButton redBottomSwitch;
	private JButton blueBottomSwitch;
	private JButton redLine;
	private JButton redRung;
	private JButton redVault;
	private JButton topScale;
	private JButton redTopSwitch;
	private JButton blueTopSwitch;
	private JButton redPark;
	private JButton bluePark;
	private GRect modeRect;

	public static void main(String[] args) {
		(new Scouting()).start(args);
	}

	public void run() {
		
		initiation();
		addFieldComponents();
	}

	/**
	 * Adds all the graphics to canvas in the beginning.
	 **/
	private void initiation() {
		query = new StringBuilder( "INSERT INTO [dbo].[MATCH] ([TEAMNUM],[MATCHNUM],[AUTOLINE],[AUTOVAULT],[AUTOSWITCH],[AUTOSCALE],[VAULT],[SWITCH],[SCALE],[OPPSWITCH],[CLIMBTYPE],[NOTES]) VALUES  ");
		JLabel Red1 = new JLabel("RED 1");
		Red1.setForeground(Color.RED);
		JLabel Red2 = new JLabel("RED 2");
		Red2.setForeground(Color.RED);
		JLabel Red3 = new JLabel("RED 3");
		Red3.setForeground(Color.RED);
		JLabel Blue1 = new JLabel("BLUE 1");
		Blue1.setForeground(Color.BLUE);
		JLabel Blue2 = new JLabel("BLUE 2");
		Blue2.setForeground(Color.BLUE);
		JLabel Blue3 = new JLabel("BLUE 3");
		Blue3.setForeground(Color.BLUE);

		setCanvasSize(1000, 457);
		canvas.setSize(1000, 457);
		add(canvas, 0, 0);

		GImage field = new GImage("res/field.JPG");
		field.setSize(1000, 427);
		canvas.add(field, 0, 30);
		JLabel match = new JLabel("Match");
		match.setFont(new Font("Sans Serif", Font.BOLD | Font.ITALIC, 20));
		JLabel selectMode = new JLabel("Mode");
		selectMode.setFont(new Font("Sans Serif", Font.BOLD, 20));

		canvas.add(selectMode, getWidth() / 2 - 100, 5);
		canvas.add(match, 30, 5);
		canvas.add(Red1, 20, 130);
		canvas.add(Red2, 20, 225);
		canvas.add(Red3, 20, 320);
		canvas.add(Blue3, 890, 130);
		canvas.add(Blue2, 890, 225);
		canvas.add(Blue1, 890, 320);

		addInteractors();
		System.out.println("Initialized...");
	}

	/**
	 * Makes the game elements that can earn points.
	 **/
	private void addFieldComponents() {
		blueLine = new JButton();
		blueLine.setBorder(BorderFactory.createLineBorder(Color.BLUE));
		blueLine.setContentAreaFilled(true);
		blueRung = new JButton();
		blueRung.setBorder(BorderFactory.createLineBorder(Color.BLUE));
		blueRung.setContentAreaFilled(true);
		blueVault = new JButton("Vault");
		blueVault.setBorder(BorderFactory.createLineBorder(Color.BLUE));
		blueVault.setBackground(Color.BLUE);
		bottomScale = new JButton("Scale");
		bottomScale.setBorder(BorderFactory.createLineBorder(Color.BLUE));
		bottomScale.setBackground(Color.BLUE);
		blueBottomSwitch = new JButton("Switch");
		blueBottomSwitch.setBorder(BorderFactory.createLineBorder(Color.BLUE));
		blueBottomSwitch.setOpaque(true);
		blueBottomSwitch.setBackground(Color.BLUE);
		redBottomSwitch = new JButton("Switch");
		redBottomSwitch.setBorder(BorderFactory.createLineBorder(Color.BLUE));
		redBottomSwitch.setOpaque(true);
		redBottomSwitch.setBackground(Color.BLUE);
		redLine = new JButton();
		redLine.setBorder(BorderFactory.createLineBorder(Color.RED));
		redLine.setContentAreaFilled(true);
		redRung = new JButton();
		redRung.setBorder(BorderFactory.createLineBorder(Color.RED));
		redRung.setContentAreaFilled(true);
		redVault = new JButton("Vault");
		redVault.setBorder(BorderFactory.createLineBorder(Color.RED));
		redVault.setBackground(Color.RED);
		topScale = new JButton("Scale");
		topScale.setBorder(BorderFactory.createLineBorder(Color.RED));
		topScale.setBackground(Color.RED);
		blueTopSwitch = new JButton("Switch");
		blueTopSwitch.setBorder(BorderFactory.createLineBorder(Color.RED));
		blueTopSwitch.setOpaque(true);
		blueTopSwitch.setBackground(Color.RED);
		redTopSwitch = new JButton("Switch");
		redTopSwitch.setBorder(BorderFactory.createLineBorder(Color.RED));
		redTopSwitch.setOpaque(true);
		redTopSwitch.setBackground(Color.RED);
		redPark = new JButton("Park");
		redPark.setBorder(BorderFactory.createLineBorder(Color.RED));
		redPark.setOpaque(true);
		redPark.setBackground(Color.RED);
		bluePark = new JButton("Park");
		bluePark.setBorder(BorderFactory.createLineBorder(Color.BLUE));
		bluePark.setOpaque(true);
		bluePark.setBackground(Color.BLUE);

		redPark.setSize(40, 100);
		canvas.add(redPark, 430, 190);
		bluePark.setSize(40, 100);
		canvas.add(bluePark, 535, 190);
		blueLine.setSize(10, 410);
		canvas.add(blueLine, 720, 40);
		blueRung.setSize(20, 30);
		canvas.add(blueRung, 505, 230);
		blueVault.setSize(85, 65);
		canvas.add(blueVault, 810, 255);
		bottomScale.setSize(60, 45);
		canvas.add(bottomScale, 470, 297);
		blueBottomSwitch.setSize(65, 50);
		redBottomSwitch.setSize(65, 50);
		canvas.add(redBottomSwitch, 300, 280);
		canvas.add(blueBottomSwitch, 640, 280);
		redLine.setSize(10, 410);
		canvas.add(redLine, 274, 40);
		redRung.setSize(20, 30);
		canvas.add(redRung, 477, 230);
		redVault.setSize(85, 65);
		canvas.add(redVault, 115, 170);
		topScale.setSize(60, 45);
		canvas.add(topScale, 474, 144);
		blueTopSwitch.setSize(65, 50);
		redTopSwitch.setSize(65, 50);
		canvas.add(redTopSwitch, 300, 156);
		canvas.add(blueTopSwitch, 640, 157);

		addActionListeners();
	}

	/**
	 * Adds the interactors to the screen.
	 **/
	private void addInteractors() {
		start = new JButton("Start");
		reset = new JButton("Reset");
		submit = new JButton("Submit");

		red1 = new JTextField(10);
		red2 = new JTextField(10);
		red3 = new JTextField(10);
		blue1 = new JTextField(10);
		blue2 = new JTextField(10);
		blue3 = new JTextField(10);

		String[] modes = { "Pending", "Autonomous", "Teleop" };
		mode = new JComboBox<String>(modes);
		mode.setForeground(Color.RED);
		mode.setFont(new Font("Serif", Font.BOLD, 16));
		mode.setSelectedIndex(0);
		mode.setOpaque(true);
		mode.setBackground(Color.GRAY);

		canvas.add(matchNum, 100, 10);
		canvas.add(mode, getWidth() / 2, 10);
		mode.setVisible(true);
		canvas.add(red1, 20, 145);
		canvas.add(red2, 20, 240);
		canvas.add(red3, 20, 335);
		canvas.add(blue3, 890, 145);
		canvas.add(blue2, 890, 240);
		canvas.add(blue1, 890, 335);
		canvas.add(start, getWidth() - 300, 10);
		canvas.add(reset, getWidth() - 200, 10);
		canvas.add(submit, getWidth() - 100, 10);

		addActionListeners();
	}

	/**
	 * Listens for and responds to action commands.
	 **/
	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == start) {
			// When the match starts
			if (!matchNum.getText().equals("")) {
				gameOn = true;
				mode.setForeground(Color.ORANGE);
				mode.setSelectedIndex(mode.getSelectedIndex() == 0 ? 1 : 2);
				if (mode.getSelectedIndex() > 0)
					start.setText("Next Mode");
				if (mode.getSelectedIndex() == 2)
					start.setEnabled(false);
				matchNumber = matchNum.getText();
				if (!red1.getText().equals("")) {
					isRed = true;
					teamNumber = red1.getText();
				}
				if (!red2.getText().equals("")) {
					isRed = true;
					teamNumber = red2.getText();
				}
				if (!red3.getText().equals("")) {
					isRed = true;
					teamNumber = red3.getText();
				}
				if (!blue1.getText().equals("")) {
					isRed = false;
					teamNumber = blue1.getText();
				}
				if (!blue2.getText().equals("")) {
					isRed = false;
					teamNumber = blue2.getText();
				}
				if (!blue3.getText().equals("")) {
					isRed = false;
					teamNumber = blue3.getText();
				}
			}
			if (mode.getSelectedIndex() == 2) {
				mode.setForeground(Color.GREEN);
			}
		} else if (event.getSource() == reset) {
			// When the match resets
			gameOn = false;
			mode.setForeground(Color.RED);
			start.setText("Start");
			start.setEnabled(true);
			mode.setSelectedIndex(0);
			matchNum.setText("");
			red1.setText("");
			red2.setText("");
			red3.setText("");
			blue1.setText("");
			blue2.setText("");
			blue3.setText("");
			matchNumber = null;
			isRed = null;
			teamNumber = null;
			autoRun = false;
			autoSwitch = 0;
			autoWrongSwitch = 0;
			autoScale = 0;
			autoWrongScale = 0;
			autoOppSwitch = 0;
			autoOppWrongSwitch = 0;
			autoVault = 0;
			teleSwitch = 0;
			teleWrongSwitch = 0;
			teleScale = 0;
			teleWrongScale = 0;
			teleOppSwitch = 0;
			teleOppWrongSwitch = 0;
			teleVault = 0;
			parked = false;
			climb = false;
			climbType = "Default";
			notes = "Default";
			query = new StringBuilder( "INSERT INTO [dbo].[MATCH] ([TEAMNUM],[MATCHNUM],[AUTOLINE],[AUTOVAULT],[AUTOSWITCH],[AUTOSCALE],[VAULT],[SWITCH],[SCALE],[OPPSWITCH],[CLIMBTYPE],[NOTES]) VALUES  ");
		} else if (event.getSource() == submit) {
			// sends the data over
			if (gameOn && matchNumber != null && teamNumber != null && mode.getSelectedIndex() == 2) {
				gameOn = false;
				mode.setForeground(Color.RED);
				//try {
					writeData();
				//}
				/*catch (IOException e) {
					e.printStackTrace();
				}*/
				mode.setSelectedIndex(0);
				matchNum.setText("");
				red1.setText("");
				red2.setText("");
				red3.setText("");
				blue1.setText("");
				blue2.setText("");
				blue3.setText("");
				matchNumber = null;
				isRed = null;
				teamNumber = null;
				autoRun = false;
				autoSwitch = 0;
				autoWrongSwitch = 0;
				autoScale = 0;
				autoWrongScale = 0;
				autoOppSwitch = 0;
				autoOppWrongSwitch = 0;
				autoVault = 0;
				teleSwitch = 0;
				teleWrongSwitch = 0;
				teleScale = 0;
				teleWrongScale = 0;
				teleOppSwitch = 0;
				teleOppWrongSwitch = 0;
				teleVault = 0;
				parked = false;
				climb = false;
				start.setEnabled(true);
				climbType = "Default";
				notes = "Default";
				query = new StringBuilder( "INSERT INTO [dbo].[MATCH] ([TEAMNUM],[MATCHNUM],[AUTOLINE],[AUTOVAULT],[AUTOSWITCH],[AUTOSCALE],[VAULT],[SWITCH],[SCALE],[OPPSWITCH],[CLIMBTYPE],[NOTES]) VALUES  ");
			}
		}
		if (gameOn) {
			if (mode.getSelectedIndex() == 1) {
				// autonomous mode
				if (isRed) {
					// on red alliance
					if (event.getSource() == redLine) {
						autoRun = true;
						System.out.println("Auton Crossed");
					}
					if (event.getSource() == redTopSwitch) {
						autoSwitch++;
						System.out.println("Switch");
					}
					if (event.getSource() == redBottomSwitch) {
						autoWrongSwitch++;
						System.out.println("Wrong Switch");
					}
					if (event.getSource() == topScale) {
						autoScale++;
						System.out.println("Scale");
					}
					if (event.getSource() == bottomScale) {
						autoWrongScale++;
						System.out.println("Wrong Scale");
					}
					if (event.getSource() == blueTopSwitch) {
						autoOppSwitch++;
						System.out.println("Opponents' Switch");
					}
					if (event.getSource() == blueBottomSwitch) {
						autoOppWrongSwitch++;
						System.out.println("Opponents' Wrong Switch");
					}
					if (event.getSource() == redVault) {
						autoVault++;
						System.out.println("Vault");
					}
				} else {
					// on the blue alliance
					if (event.getSource() == blueLine) {
						autoRun = true;
						System.out.println("Auton Crossed");
					}
					if (event.getSource() == blueBottomSwitch) {
						autoSwitch++;
						System.out.println("Switch");
					}
					if (event.getSource() == blueTopSwitch) {
						autoWrongSwitch++;
						System.out.println("Wrong Switch");
					}
					if (event.getSource() == bottomScale) {
						autoScale++;
						System.out.println("Scale");
					}
					if (event.getSource() == topScale) {
						autoWrongScale++;
						System.out.println("Wrong Scale");
					}
					if (event.getSource() == redBottomSwitch) {
						autoOppSwitch++;
						System.out.println("Opponents' Switch");
					}
					if (event.getSource() == redTopSwitch) {
						autoOppWrongSwitch++;
						System.out.println("Opponents' Wrong Switch");
					}
					if (event.getSource() == blueVault) {
						autoVault++;
						System.out.println("Vault");
					}
				}
			} else if (mode.getSelectedIndex() == 2) {
				// teleop
				if (isRed) {
					// on red alliance
					if (event.getSource() == redTopSwitch) {
						teleSwitch++;
						System.out.println("Switch");
					}
					if (event.getSource() == redBottomSwitch) {
						teleWrongSwitch++;
						System.out.println("Wrong Switch");
					}
					if (event.getSource() == topScale) {
						teleScale++;
						System.out.println("Scale");
					}
					if (event.getSource() == bottomScale) {
						teleWrongScale++;
						System.out.println("Wrong Scale");
					}
					if (event.getSource() == blueTopSwitch) {
						teleOppSwitch++;
						System.out.println("Opponents' Switch");
					}
					if (event.getSource() == blueBottomSwitch) {
						teleOppWrongSwitch++;
						System.out.println("Opponents' Wrong Switch");
					}
					if (event.getSource() == redVault) {
						teleVault++;
						System.out.println("Vault");
					}
					if (event.getSource() == redPark) {
						parked = true;
						System.out.println("Parked");
					}
					if (event.getSource() == redRung) {
						climb = true;
						System.out.println("Climbed");
					}
				} else {
					// on the blue alliance
					if (event.getSource() == blueBottomSwitch) {
						teleSwitch++;
						System.out.println("Switch");
					}
					if (event.getSource() == blueTopSwitch) {
						teleWrongSwitch++;
						System.out.println("Wrong Switch");
					}
					if (event.getSource() == bottomScale) {
						teleScale++;
						System.out.println("Scale");
					}
					if (event.getSource() == topScale) {
						teleWrongScale++;
						System.out.println("Wrong Scale");
					}
					if (event.getSource() == redBottomSwitch) {
						teleOppSwitch++;
						System.out.println("Opponents' Switch");
					}
					if (event.getSource() == redTopSwitch) {
						teleOppWrongSwitch++;
						System.out.println("Opponents' Wrong Switch");
					}
					if (event.getSource() == blueVault) {
						teleVault++;
						System.out.println("Vault");
					}
					if (event.getSource() == bluePark) {
						parked = true;
						System.out.println("Parked");
					}
					if (event.getSource() == blueRung) {
						climb = true;
						System.out.println("Climbed");
					}
				}
			}
		}

	}

	/*
	 * writes data to the excel document
	 */
	/*private void writeData() throws IOException {
		String[] data = { matchNumber, teamNumber, autoRun.toString(), autoSwitch.toString(), autoWrongSwitch.toString(),
				autoScale.toString(), autoWrongScale.toString(), autoOppSwitch.toString(), autoOppWrongSwitch.toString(),
				autoVault.toString(), teleSwitch.toString(), teleWrongSwitch.toString(), teleScale.toString(), teleWrongScale.toString(), 
				teleOppSwitch.toString(), teleOppWrongSwitch.toString(), teleVault.toString(), parked.toString(), climb.toString() };
		System.out.println("Match Number: " + matchNumber + " Team Number: " + teamNumber + " Auton Crossing " + autoRun.toString() 
				+ " Auton Switch " + autoSwitch.toString() +  " Auton Wrong Switch " + autoWrongSwitch.toString() + " Auton Scale "
				+ autoScale.toString() + " Auton Wrong Scale " + autoWrongScale.toString() + " Auton Opponents' Switch "
				+ autoOppSwitch.toString() + " Auton Opponents' Wrong Switch " + autoOppWrongSwitch.toString()
				+ " Auton Vaults " + autoVault.toString() + " Teleop Switch " + teleSwitch.toString()
				+ " Teleop Wrong Switch " + teleWrongSwitch.toString() + " Teleop Scale " + teleScale.toString()
				+ " Teleop Wrong Scale " + teleWrongScale.toString() + " Teleop Opponents' Switch "
				+ teleOppSwitch.toString() + " Teleop Opponents' Wrong Switch " + teleOppWrongSwitch.toString()
				+ " Teleop Vaults " + teleVault.toString() + " Parked " + parked.toString() + " Climbed " + climb.toString());
		// Read Excel document first
		FileInputStream input = new FileInputStream(new File("res/data.xlsx"));
		// convert it into a POI object
		XSSFWorkbook workbook = new XSSFWorkbook(input);
		// Read excel sheet that needs to be updated
		XSSFSheet worksheet = workbook.getSheetAt(0);
		int rowNum = worksheet.getPhysicalNumberOfRows();
		// close InputStream
		input.close();
		Row row = worksheet.createRow(rowNum);
		int colNum = 0;
		for (String stat : data) {
			Cell cell = row.createCell(colNum++);
			cell.setCellValue(stat);
		}
		// Open FileOutputStream to write updates
		FileOutputStream output = new FileOutputStream(new File("res/data.xlsx"));
		// write changes
		workbook.write(output);
		workbook.close();
		// close the stream
		output.close();
	}

}*/
	private void writeData(){
		   try {
			   	Connection _connection;
			   	_connection = DriverManager.getConnection(connectionURL);
			   	  query.append('(');
			      query.append(teamNumber.toString());
			      query.append(',');
			      query.append(matchNumber.toString());
			      query.append(',');
			      if(autoRun) query.append(1);
			      else query.append(0);
			      query.append(',');
			      query.append(autoVault.toString());
			      query.append(',');
			      query.append(autoSwitch.toString());
			      query.append(',');
			      query.append(autoScale.toString());
			      query.append(',');
			      query.append(teleVault.toString());
			      query.append(',');
			      query.append(teleSwitch.toString());
			      query.append(',');
			      query.append(teleScale.toString());
			   	  query.append(',');
			   	  query.append(teleOppSwitch.toString());
			   	  query.append(',');
			   	  query.append(climbType.toString());
			   	  query.append(',');
			   	  query.append(notes.toString());
			   	  query.append(')');

			      System.out.println(query.toString());
			      PreparedStatement pstmt = _connection.prepareStatement(query.toString());
			      pstmt.execute();
			      //pstmt.executeQuery();
			      pstmt.close();
			   }
			   catch (Exception e) {
			      e.printStackTrace();
			   }
			}
	}
