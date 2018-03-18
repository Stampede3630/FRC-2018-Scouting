import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Iterator;
import javax.swing.*;
import java.sql.*;
import java.util.Timer;

import acm.graphics.*;
import acm.program.GraphicsProgram;
import acm.program.GraphicsProgramInterface;

public class Scouting extends GraphicsProgram {

	/** True after clicking the start button **/
	private boolean gameOn;
	StringBuilder matchQuery;
	StringBuilder cycleQuery;
	// data to record
	private String connectionURL = "jdbc:sqlserver://192.168.36.30\\SQLEXPRESS;user=Stampede3630;password=whatsaspock;";
	private String matchNumber = null;
	private Boolean isRed = null;
	private String teamNumber = null;
	private Boolean autoRun = false;
	private Integer autoSwitch = 0;
	private Integer autoScale = 0;
	private Integer autoOppSwitch = 0;
	private Integer autoVault = 0;
	private Integer teleSwitch = 0;
	private Integer teleScale = 0;
	private Integer teleOppSwitch = 0;
	private Integer teleVault = 0;
	private String climbType = "Default";
	private String notes = "Default";
	private Long startTime = (long) 0;
	private Long currentTime = (long) 0;
	private Double cycleStart = (double) 0;
	private Double cycleEnd = (double) 0;
	private Double cycleTime = (double) 0;
	private String pathStart = "S";
	private String pathEnd = "S";
	
	// All the interactors that will be called more than once
	private GCanvas canvas = new GCanvas();
	private JTextField matchNum = new JTextField(5);
	private JTextField red1;
	private JTextField red2;
	private JTextField red3;
	private JTextField blue1;
	private JTextField blue2;
	private JTextField blue3;
	private JTextField notesEntry;
	private JComboBox<String> climbEnter;
	private JComboBox<String> mode;
	private JButton start;
	private JButton reset;
	private JButton submit;
	private JButton blueVault;
	private JButton redSwitch;
	private JButton line;
	private JButton redVault;
	private JButton scale;
	private JButton blueSwitch;
	private JButton blueTopPortal;
	private JButton blueBottomPortal;
	private JButton redPowerCubeZone;
	private JButton redPlatformCubes;
	private JButton bluePlatformCubes;
	private JButton bluePowerCubeZone;
	private JButton redTopPortal;
	private JButton redBottomPortal;
	private JButton redExchange;
	private JButton blueExchange;
	private JButton dropGround;
	private JButton pickupGround;
	private JButton resetCycle;

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
		matchQuery = new StringBuilder( "INSERT INTO [dbo].[MATCH] ([TEAMNUM],[MATCHNUM],[AUTOLINE],[AUTOVAULT],[AUTOSWITCH],[AUTOSCALE],[VAULT],[SWITCH],[SCALE],[OPPSWITCH],[CLIMBTYPE],[NOTES]) VALUES  ");
		cycleQuery = new StringBuilder("INSERT INTO [dbo].[CYCLETIME] ([TEAMNUM],[MATCHNUM],[PATH],[TIME]) VALUES ");
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
		JLabel climbEntry = new JLabel("CLIMB TYPE");
		JLabel notesEntry = new JLabel("NOTES");

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
		canvas.add(climbEntry, 410, 385);
		canvas.add(notesEntry, 200, 85);

		addInteractors();
		System.out.println("Initialized...");
	}

	/**
	 * Makes the game elements that can earn points.
	 **/
	private void addFieldComponents() {
		blueVault = new JButton("Dropoff");
		blueVault.setBorder(BorderFactory.createLineBorder(Color.BLUE));
		blueVault.setBackground(Color.BLUE);
		scale = new JButton("Scale");
		scale.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		redSwitch = new JButton("Switch");
		redSwitch.setBorder(BorderFactory.createLineBorder(Color.RED));
		line = new JButton("Auto Run");
		redVault = new JButton("Dropoff");
		redVault.setBorder(BorderFactory.createLineBorder(Color.RED));
		redVault.setBackground(Color.RED);
		blueSwitch = new JButton("Switch");
		blueSwitch.setBorder(BorderFactory.createLineBorder(Color.BLUE));
		blueTopPortal = new JButton("Portal");
		blueTopPortal.setBorder(BorderFactory.createLineBorder(Color.BLUE));
		blueTopPortal.setBackground(Color.BLUE);
		blueBottomPortal = new JButton("Portal");
		blueBottomPortal.setBorder(BorderFactory.createLineBorder(Color.BLUE));
		blueBottomPortal.setBackground(Color.BLUE);
		redPowerCubeZone = new JButton("PCZ");
		redPowerCubeZone.setBorder(BorderFactory.createLineBorder(Color.RED));
		redPowerCubeZone.setBackground(Color.RED);
		redPlatformCubes = new JButton("Platform Cubes");
		redPlatformCubes.setBorder(BorderFactory.createLineBorder(Color.RED));
		redPlatformCubes.setBackground(Color.RED);
		bluePlatformCubes = new JButton("Platform Cubes");
		bluePlatformCubes.setBorder(BorderFactory.createLineBorder(Color.BLUE));
		bluePlatformCubes.setBackground(Color.BLUE);
		bluePowerCubeZone = new JButton("PCZ");
		bluePowerCubeZone.setBorder(BorderFactory.createLineBorder(Color.BLUE));
		bluePowerCubeZone.setBackground(Color.BLUE);
		redTopPortal = new JButton("Portal");
		redTopPortal.setBorder(BorderFactory.createLineBorder(Color.RED));
		redTopPortal.setBackground(Color.RED);
		redBottomPortal = new JButton("Portal");
		redBottomPortal.setBorder(BorderFactory.createLineBorder(Color.RED));
		redBottomPortal.setBackground(Color.RED);
		redExchange = new JButton("Pickup");
		redExchange.setBorder(BorderFactory.createLineBorder(Color.RED));
		redExchange.setBackground(Color.RED);
		blueExchange = new JButton("Pickup");
		blueExchange.setBorder(BorderFactory.createLineBorder(Color.BLUE));
		blueExchange.setBackground(Color.BLUE);
		dropGround = new JButton("Drop Ground");
		pickupGround = new JButton("Pickup Ground");
		resetCycle = new JButton("Reset Cycle");
		
		blueVault.setSize(75, 28);
		canvas.add(blueVault, 815, 256);
		blueExchange.setSize(75, 28);
		canvas.add(blueExchange, 815, 284);
		redVault.setSize(75, 28);
		canvas.add(redVault, 113, 177);
		redExchange.setSize(75, 28);
		canvas.add(redExchange, 113, 205);
		redSwitch.setSize(64, 173);
		canvas.add(redSwitch, 299, 158);
		scale.setSize(58, 202);
		canvas.add(scale, 473, 143);
		blueSwitch.setSize(64, 173);
		canvas.add(blueSwitch, 640, 158);
		redTopPortal.setSize(173, 56);
		canvas.add(redTopPortal, 816, 45);
		redBottomPortal.setSize(173, 56);
		canvas.add(redBottomPortal, 816, 387);
		blueTopPortal.setSize(173, 56);
		canvas.add(blueTopPortal, 15, 45);
		blueBottomPortal.setSize(173, 56);
		canvas.add(blueBottomPortal, 15, 387);
		redPowerCubeZone.setSize(47, 52);
		canvas.add(redPowerCubeZone, 253, 218);
		bluePowerCubeZone.setSize(47, 52);
		canvas.add(bluePowerCubeZone, 704, 218);
		redPlatformCubes.setSize(110, 173);
		canvas.add(redPlatformCubes, 363, 158);
		bluePlatformCubes.setSize(110, 173);
		canvas.add(bluePlatformCubes, 531, 158);
		dropGround.setSize(100, 15);
		pickupGround.setSize(100, 15);
		canvas.add(dropGround, 160, 350);
		canvas.add(pickupGround, 295, 350);
		resetCycle.setSize(90, 15);
		canvas.add(resetCycle, 610, 350);
		line.setSize(90, 15);
		canvas.add(line, 745, 350);
		
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
		notesEntry = new JTextField(50);

		String[] modes = { "Pending", "Autonomous", "Teleop" };
		mode = new JComboBox<String>(modes);
		mode.setForeground(Color.RED);
		mode.setFont(new Font("Serif", Font.BOLD, 16));
		mode.setSelectedIndex(0);
		mode.setOpaque(true);
		mode.setBackground(Color.GRAY);

		String[] climbEntry = { "No Attempt", "Fail", "Success" };
		climbEnter = new JComboBox<String>(climbEntry);
		climbEnter.setForeground(Color.BLACK);
		climbEnter.setFont(new Font("Serif", Font.BOLD, 16));
		climbEnter.setSelectedIndex(0);
		climbEnter.setOpaque(true);
		climbEnter.setBackground(Color.WHITE);
		
		canvas.add(matchNum, 100, 10);
		canvas.add(mode, getWidth() / 2, 10);
		mode.setVisible(true);
		canvas.add(red1, 20, 145);
		canvas.add(red2, 20, 240);
		canvas.add(red3, 20, 335);
		canvas.add(blue3, 890, 145);
		canvas.add(blue2, 890, 240);
		canvas.add(blue1, 890, 335);
		canvas.add(climbEnter, 410, 400);
		canvas.add(notesEntry, 200, 100);
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
		currentTime = System.nanoTime();
		System.out.println((double) currentTime/1000000000-(double)startTime/1000000000);
		if (event.getSource() == start) {
			// When the match starts
			if (mode.getSelectedIndex() == 0) {
				startTime = System.nanoTime();
				cycleStart = (double) startTime;
				System.out.print("Start Time: ");
				System.out.println(startTime.toString());
			}
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
			System.out.println(currentTime-startTime);
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
			climbEnter.setSelectedIndex(0);
			notesEntry.setText("");
			matchNumber = null;
			isRed = null;
			teamNumber = null;
			autoRun = false;
			autoSwitch = 0;
			autoScale = 0;
			autoOppSwitch = 0;
			autoVault = 0;
			teleSwitch = 0;
			teleScale = 0;
			teleOppSwitch = 0;
			teleVault = 0;
			climbType = "Default";
			notes = "Default";
			startTime = System.nanoTime();
			currentTime = System.nanoTime();
			cycleStart = (double) 0;
			cycleEnd = (double) 0;
			cycleTime = (double) 0;
			pathStart = "S";
			pathEnd = "S";
			cycleQuery = new StringBuilder("INSERT INTO [dbo].[CYCLETIME] ([TEAMNUM],[MATCHNUM],[PATH],[TIME]) VALUES ");
			matchQuery = new StringBuilder( "INSERT INTO [dbo].[MATCH] ([TEAMNUM],[MATCHNUM],[AUTOLINE],[AUTOVAULT],[AUTOSWITCH],[AUTOSCALE],[VAULT],[SWITCH],[SCALE],[OPPSWITCH],[CLIMBTYPE],[NOTES]) VALUES  ");
		} else if (event.getSource() == submit) {
			// sends the data over
			if (gameOn && matchNumber != null && teamNumber != null && mode.getSelectedIndex() == 2) {
				gameOn = false;
				notes = notesEntry.getText();
				mode.setForeground(Color.RED);
				//try {
					writeMatchData();
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
				climbEnter.setSelectedIndex(0);
				notesEntry.setText("");
				matchNumber = null;
				isRed = null;
				teamNumber = null;
				autoRun = false;
				autoSwitch = 0;
				autoScale = 0;
				autoOppSwitch = 0;
				autoVault = 0;
				teleSwitch = 0;
				teleScale = 0;
				teleOppSwitch = 0;
				teleVault = 0;
				start.setEnabled(true);
				climbType = "Default";
				notes = "Default";
				cycleStart = (double) 0;
				cycleEnd = (double) 0;
				cycleTime = (double) 0;
				pathStart = "S";
				pathEnd = "S";
				startTime = System.nanoTime();
				currentTime = System.nanoTime();
				cycleQuery = new StringBuilder("INSERT INTO [dbo].[CYCLETIME] ([TEAMNUM],[MATCHNUM],[PATH],[TIME]) VALUES ");
				matchQuery = new StringBuilder( "INSERT INTO [dbo].[MATCH] ([TEAMNUM],[MATCHNUM],[AUTOLINE],[AUTOVAULT],[AUTOSWITCH],[AUTOSCALE],[VAULT],[SWITCH],[SCALE],[OPPSWITCH],[CLIMBTYPE],[NOTES]) VALUES  ");
			}
		}
		if (gameOn) {
			if (event.getSource() == resetCycle) {
				pathStart = "D";
			}
			if (event.getSource() == dropGround) {
				writeCycleData("G");
			}
			if (event.getSource() == pickupGround) {
				cycleStart = (double) currentTime;
				pathStart = "G";
			}
			climbType = climbEnter.getSelectedItem().toString();
			notes = notesEntry.getText();
			if (currentTime/1000000000-startTime/1000000000 >= 15) {
				mode.setSelectedIndex(2);
			}
			if (mode.getSelectedIndex() == 1) {
				// autonomous mode
				if (isRed) {
					// on red alliance
					if (event.getSource() == line) {
						autoRun = true;
						System.out.println("Auton Crossed");
					}
					if (event.getSource() == redSwitch) {
						autoSwitch++;
						writeCycleData("AS");
						System.out.println("Switch");
					}
					if (event.getSource() == scale) {
						autoScale++;
						writeCycleData("AM");
						System.out.println("Scale");
					}
					if (event.getSource() == blueSwitch) {
						autoOppSwitch++;
						writeCycleData("AO");
						System.out.println("Opponents' Switch");
					}
					if (event.getSource() == redVault) {
						autoVault++;
						writeCycleData("AV");
						System.out.println("Vault");
					}
					if (event.getSource() == redPowerCubeZone) {
						cycleStart = (double) currentTime;
						pathStart = "C";
					}
					if (event.getSource() == redPlatformCubes) {
						cycleStart = (double) currentTime;
						pathStart = "P";
					}
					if (event.getSource() == bluePlatformCubes) {
						cycleStart = (double) currentTime;
						pathStart = "M";
					}
					if (event.getSource() == redTopPortal || event.getSource() == redBottomPortal) {
						cycleStart = (double) currentTime;
						pathStart = "O";
					}
				} else {
					// on the blue alliance
					if (event.getSource() == line) {
						autoRun = true;
						System.out.println("Auton Crossed");
					}
					if (event.getSource() == blueSwitch) {
						autoSwitch++;
						writeCycleData("AS");
						System.out.println("Switch");
					}
					if (event.getSource() == scale) {
						autoScale++;
						writeCycleData("AM");
						System.out.println("Scale");
					}
					if (event.getSource() == redSwitch) {
						autoOppSwitch++;
						writeCycleData("AO");
						System.out.println("Opponents' Switch");
					}
					if (event.getSource() == blueVault) {
						autoVault++;
						writeCycleData("AV");
						System.out.println("Vault");
					}
					if (event.getSource() == bluePowerCubeZone) {
						cycleStart = (double) currentTime;
						pathStart = "C";
					}
					if (event.getSource() == bluePlatformCubes) {
						cycleStart = (double) currentTime;
						pathStart = "P";
					}
					if (event.getSource() == redPlatformCubes) {
						cycleStart = (double) currentTime;
						pathStart = "M";
					}
					if (event.getSource() == blueTopPortal || event.getSource() == blueBottomPortal) {
						cycleStart = (double) currentTime;
						pathStart = "O";
					}
				}
			} else if (mode.getSelectedIndex() == 2) {
				// teleop
				if (isRed) {
					// on red alliance
					if (event.getSource() == redSwitch) {
						teleSwitch++;
						writeCycleData("S");
						System.out.println("Switch");
					}
					if (event.getSource() == scale) {
						teleScale++;
						writeCycleData("M");
						System.out.println("Scale");
					}
					if (event.getSource() == blueSwitch) {
						teleOppSwitch++;
						writeCycleData("O");
						System.out.println("Opponents' Switch");
					}
					if (event.getSource() == redVault) {
						teleVault++;
						writeCycleData("V");
						System.out.println("Vault");
					}
					if (event.getSource() == redPowerCubeZone) {
						cycleStart = (double) currentTime;
						pathStart = "C";
					}
					if (event.getSource() == redPlatformCubes) {
						cycleStart = (double) currentTime;
						pathStart = "P";
					}
					if (event.getSource() == bluePlatformCubes) {
						cycleStart = (double) currentTime;
						pathStart = "M";
					}
					if (event.getSource() == redTopPortal || event.getSource() == redBottomPortal) {
						cycleStart = (double) currentTime;
						pathStart = "O";
					}
				} else {
					// on the blue alliance
					if (event.getSource() == blueSwitch) {
						teleSwitch++;
						writeCycleData("S");
						System.out.println("Switch");
					}
					if (event.getSource() == scale) {
						teleScale++;
						writeCycleData("M");
						System.out.println("Scale");
					}
					if (event.getSource() == redSwitch) {
						teleOppSwitch++;
						writeCycleData("O");
						System.out.println("Opponents' Switch");
					}
					if (event.getSource() == blueVault) {
						teleVault++;
						writeCycleData("V");
						System.out.println("Vault");
					}
					if (event.getSource() == bluePowerCubeZone) {
						cycleStart = (double) currentTime;
						pathStart = "C";
					}
					if (event.getSource() == bluePlatformCubes) {
						cycleStart = (double) currentTime;
						pathStart = "P";
					}
					if (event.getSource() == redPlatformCubes) {
						cycleStart = (double) currentTime;
						pathStart = "M";
					}
					if (event.getSource() == blueTopPortal || event.getSource() == blueBottomPortal) {
						cycleStart = (double) currentTime;
						pathStart = "O";
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
	private void writeMatchData(){
		   try {
			   	  matchQuery.append('(');
			      matchQuery.append(teamNumber.toString());
			      matchQuery.append(',');
			      matchQuery.append(matchNumber.toString());
			      matchQuery.append(',');
			      if(autoRun) matchQuery.append(1);
			      else matchQuery.append(0);
			      matchQuery.append(',');
			      matchQuery.append(autoVault.toString());
			      matchQuery.append(',');
			      matchQuery.append(autoSwitch.toString());
			      matchQuery.append(',');
			      matchQuery.append(autoScale.toString());
			      matchQuery.append(',');
			      matchQuery.append(teleVault.toString());
			      matchQuery.append(',');
			      matchQuery.append(teleSwitch.toString());
			      matchQuery.append(',');
			      matchQuery.append(teleScale.toString());
			   	  matchQuery.append(',');
			   	  matchQuery.append(teleOppSwitch.toString());
			   	  matchQuery.append(',');
			   	  matchQuery.append("'");
			   	  matchQuery.append(climbType.toString());
			   	  matchQuery.append("'");
			   	  matchQuery.append(',');
			   	  matchQuery.append("'");
			   	  matchQuery.append(notes.toString());
			   	  matchQuery.append("'");
			   	  matchQuery.append(')');

			      System.out.println(matchQuery.toString());
			      
			      DriverManager.setLoginTimeout(15);
			      Connection _connection;
				   	_connection = DriverManager.getConnection(connectionURL);
			      PreparedStatement pstmt = _connection.prepareStatement(matchQuery.toString());
			      pstmt.execute();
			      pstmt.close();
			   }
			   catch (Exception e) {
			      e.printStackTrace();
			      try {
			            FileWriter writer = new FileWriter("errorlog.txt", true);
			            writer.write(matchQuery.toString());
			            writer.write("\r\n");
			            writer.close();
			        } catch (IOException error) {
			            error.printStackTrace();
			        }
			   }
			}
		private void writeCycleData(String pathEnding){
			try {
				cycleEnd = (double) currentTime;
				cycleTime = cycleEnd - cycleStart;
				pathEnd = pathEnding;
				if (pathStart != "D") {
					cycleQuery.append('(');
				   	cycleQuery.append(teamNumber);
				   	cycleQuery.append(',');
				   	cycleQuery.append(matchNumber);
				   	cycleQuery.append(',');
				   	cycleQuery.append("'");
				   	cycleQuery.append(pathStart);
				   	cycleQuery.append(pathEnd);
				   	cycleQuery.append("'");
				   	cycleQuery.append(',');
				   	cycleQuery.append(cycleTime/1000000000);
				   	cycleQuery.append(')');
				   	
				   	System.out.println(cycleQuery.toString());
				   	
				   	pathStart = "D";
				   	
				   	DriverManager.setLoginTimeout(1);
					Connection _connection;
				   	_connection = DriverManager.getConnection(connectionURL);
				    PreparedStatement pstmt = _connection.prepareStatement(cycleQuery.toString());
				    pstmt.execute();
				    pstmt.close();
				    
				}
			   }
			   catch (Exception e) {
			      e.printStackTrace();
			      try {
			            FileWriter writer = new FileWriter("errorlog.txt", true);
			            writer.write(cycleQuery.toString());
			            writer.write("\r\n");
			            writer.close();
			        } catch (IOException error) {
			            error.printStackTrace();
			        }
			   }
			cycleQuery = new StringBuilder("INSERT INTO [dbo].[CYCLETIME] ([TEAMNUM],[MATCHNUM],[PATH],[TIME]) VALUES ");
			}
		}
