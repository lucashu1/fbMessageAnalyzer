package lcj.fb;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart.Data;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileFilter;

import lcj.frequentwords.MostFrequentWordsFinder;

public class Display extends JPanel implements ActionListener, KeyListener, MouseListener{
	private static final long serialVersionUID = -2289129201469729871L; //just to get rid of the serial warning
	public static JFrame mainFrame;
	public static final Dimension prefSize = new Dimension(1100, 650);
	public static final Color fbBlue = new Color(59, 87, 157);
	public static final String FILE_SELECTION_CARD = "fileSelectionCard", 
							   MAIN_MENU_CARD = "mainMenuCard",
							   STATS_CARD = "statsCard",
							   PERSON_SELECTION_CARD = "personSelectionCard",
							   STATS_PERSON_CARD = "statsPersonCard",
							   USAGE_BY_HOUR_CARD = "usageByHourCard",
							   USAGE_BY_DAY_CARD = "usageByDayCard",
							   USAGE_BY_TIME_CARD = "usageOverTimeCard",
							   WORD_SELECTION_CARD = "wordSelectionCard",
							   WORD_USAGE_CARD = "specificWordUsageCard",
							   MOST_USED_WORDS_CARD = "mostUsedWordsCard",
							   BLACKLIST_CARD = "blacklistCard";
	private GridBagConstraints cFile, cMain, cStats, cPStats, cHour,
							   cDay, cTime, cWord, cFix, cCWord, cMFWord,
							   cBlack;
    private JButton fileSelectButton, messageBackwardButton, messageForwardButton;
    private JLabel takeAWhile, loading, personWelcome, pTotalMessages, helpFindText, helpIconLabel;
    private static JFileChooser fc;
    private JFormattedTextField messageTextField, personTextField, wordTextField, blacklistTextField;
    private JTextField numberWordsTextField;
    private JPanel overallCards, fileSelectionCard, personSelectionCard, statsPersonCard,
    	   mainMenuCard, statsCard, usageByHourCard, usageByDayCard, usageByTimeCard, creditsPane,
    	   wordUsageCard, wordSelectionCard, mostUsedWordsCard, blacklistCard, fileSelectionBG;
    private final Icon fbIcon = new ImageIcon(getClass().getResource("/res/fbicon.png"));
    public static Image progImage = new ImageIcon(Display.class.getResource("/res/fbicon.png")).getImage();
    private final Icon helpIcon = new ImageIcon(getClass().getResource("/res/questionMark.png"));
    private ImageIcon loadingImageIcon;
    public static Inbox userInbox;
    private static Conversation personSelectionConvo;
    private int messageIndex = 0;
    private ArrayList<String> currentBlacklist = new ArrayList<String>();
    private JTextArea blacklistDisplay, currentMessage;
	JTextArea credits;
    private String selectedPerson = "Enter full name here";
    private String selectedPersonDefault = "Enter full name here";
    private String selectedWord = "Enter word here";
    private String selectedWordDefault = "Enter word here";
    private int numberWords = 10;
    String css = Main.class.getResource("styling.css").toExternalForm();
    
    //JavaFX declarations
    private JFXPanel usageByHourJFX, usageByDayJFX, usageByTimeJFX, wordUsageJFX, mostUsedWordsJFX;
    private Scene usageByHourScene, usageByDayScene, usageByTimeScene, wordUsageScene, usageByMUWordsScene;
    private BarChart<String, Number> byHour, byDay, byTime, byWord;
    private Scanner scan;
    
    public void addComponentsToPane(Container pane){
    	pane.remove(0); //take away file selection stuffs
    	addMainMenuComponents(); //populate mainMenuCard
    	addGenStatsComponents(); //populate statsCard
    	addPersonSelectionComponents(); //populate personSelectionCard and statsPersonCard
        addUsageByHourComponents(); //Add JavaFX scene to panel
        addUsageByDayComponents(); //Add JavaFX scene to panel
        addUsageByTimeComponents();
        addWordUsageByTimeComponents();
        addMostUsedWordsComponents();
        addGraphOptionsComponents();

        overallCards = new JPanel(new CardLayout());
        overallCards.add(mainMenuCard, MAIN_MENU_CARD);
        overallCards.add(statsCard, STATS_CARD);
        overallCards.add(personSelectionCard, PERSON_SELECTION_CARD);
        overallCards.add(statsPersonCard, STATS_PERSON_CARD);
        overallCards.add(usageByHourCard, USAGE_BY_HOUR_CARD);
        overallCards.add(usageByDayCard, USAGE_BY_DAY_CARD);
        overallCards.add(usageByTimeCard, USAGE_BY_TIME_CARD);
        overallCards.add(wordSelectionCard, WORD_SELECTION_CARD);
        overallCards.add(wordUsageCard, WORD_USAGE_CARD);
        overallCards.add(mostUsedWordsCard, MOST_USED_WORDS_CARD);
        overallCards.add(blacklistCard, BLACKLIST_CARD);
        
        pane.add(overallCards);
    }
    
    public void addFileSelectComponents(){
    	fileSelectionBG = new JPanel();
    	fileSelectionBG.setLayout(new BoxLayout(fileSelectionBG, BoxLayout.LINE_AXIS));
    	fileSelectionBG.setBackground(Color.white);
    	
    	addFileSelectionPaneComponents(); //file selection card now has stuff in
    	addCreditsPaneComponents(); //credits pane now has stuff in
    	
    	fileSelectionBG.add(Box.createRigidArea(new Dimension(150, 100)));
    	fileSelectionBG.add(fileSelectionCard);
    	fileSelectionBG.add(Box.createVerticalGlue());
    	fileSelectionBG.add(creditsPane, BorderLayout.PAGE_END);
    }
    
    public void addCreditsPaneComponents(){
    	creditsPane = new JPanel();
    	creditsPane.setBackground(Color.white);
    	
    	credits = new JTextArea();
    	credits.setPreferredSize(new Dimension(150, 400));
    	credits.setWrapStyleWord(true);
    	credits.setLineWrap(true);
    	credits.setEditable(false);
    	
    	Font font = new Font("Tahoma", Font.PLAIN, 16);
    	credits.setFont(font);
    	credits.setForeground(fbBlue);
    	credits.setText("Made by Chris Daw and Lucas Hu of Gunn High School");
    	
    	creditsPane.add(credits);
    }
    
    public void addFileSelectionPaneComponents(){
    	cFile = new GridBagConstraints();
    	
        fileSelectionCard = new JPanel();
        fileSelectionCard.setBackground(Color.WHITE);
        fileSelectionCard.setLayout(new GridBagLayout());

        JLabel fbIconLabel = new JLabel(fbIcon);
        cFile.gridx=0; cFile.gridy=0; cFile.weightx=0.0; cFile.weighty=0.0; cFile.anchor = GridBagConstraints.CENTER;
        cFile.insets = new Insets(15, 0, 0, 0);
        fileSelectionCard.add(fbIconLabel, cFile);
        
    	fc = new JFileChooser();
    	fc.setAcceptAllFileFilterUsed(false); //don't accept all files
        fc.addChoosableFileFilter(new HTMLFilter());  //only accept .htm or .html
        fileSelectButton = new JButton("Choose HTML file...");
        fileSelectButton.addActionListener(this);
        cFile.gridx = 0; cFile.gridy = 1; cFile.weightx = 0; cFile.weighty = 0;
        cFile.anchor = GridBagConstraints.CENTER;
        fileSelectionCard.add(fileSelectButton, cFile);
        
        helpIconLabel = new JLabel(helpIcon);
        cFile.anchor = GridBagConstraints.CENTER;
        cFile.gridy=2;
        helpIconLabel.addMouseListener(new MouseAdapter(){
        	public void mouseClicked(MouseEvent e){
        		HelpGuide hg = new HelpGuide();
        		hg.pack();
        		hg.setVisible(true);
        		
        	}
        });
        fileSelectionCard.add(helpIconLabel, cFile);
        
        cFile.gridx=0; cFile.gridy=3; cFile.weightx = 0; cFile.weighty = 0; cFile.anchor = GridBagConstraints.CENTER;
        takeAWhile = new JLabel("Parsing file... This process can take up to a minute.");
        takeAWhile.setFont(takeAWhile.getFont().deriveFont(15.0f));
        takeAWhile.setForeground(Color.white);
        cFile.insets = new Insets(5, 0, 0, 0);
        fileSelectionCard.add(takeAWhile, cFile);
        
        loadingImageIcon = new ImageIcon(getClass().getResource("/res/loading.gif"));
        loading = new JLabel(loadingImageIcon);
        cFile.gridy =4;
        cFile.insets = new Insets(5, 0, 0, 0);
        fileSelectionCard.add(loading, cFile);
        
        loading.setVisible(false);
    }

    public void addMainMenuComponents(){
    	cMain = new GridBagConstraints();
    	mainMenuCard = new JPanel();
        mainMenuCard.setBackground(Color.WHITE);
        mainMenuCard.setLayout(new GridBagLayout());        
        
        JLabel welcomeText = new JLabel();
        welcomeText.setText("Welcome, " + userInbox.getUsername() + "!");
        welcomeText.setForeground(fbBlue);
        welcomeText.setFont(welcomeText.getFont().deriveFont(32.0f));
        cMain.gridx=0;cMain.gridy=3; cMain.weightx=0.33; cMain.weighty=0.10;
        mainMenuCard.add(welcomeText, cMain);
        
        JLabel welcomeDescriptionText1 = new JLabel("to the Facebook Message Analyzer.");
        JLabel welcomeDescriptionText2 = new JLabel("Click on one of the options or hover for more info.");
        welcomeDescriptionText1.setFont(welcomeDescriptionText1.getFont().deriveFont(20.0f));
        welcomeDescriptionText2.setFont(welcomeDescriptionText2.getFont().deriveFont(20.0f));
        cMain.gridx=0;cMain.gridy=4;
        mainMenuCard.add(welcomeDescriptionText1, cMain);
        cMain.gridy=5;
        mainMenuCard.add(welcomeDescriptionText2, cMain);
        
        JButton statsButton = new JButton("Your General Stats");
        statsButton.setToolTipText("View stats of every message you have ever sent.");
        statsButton.addActionListener(this);
        statsButton.setActionCommand(STATS_CARD);
        cMain.gridx=1; cMain.gridy=1; cMain.anchor=GridBagConstraints.LINE_START;
        cMain.weightx=0.50; cMain.weighty=0.15; 
        cMain.fill = GridBagConstraints.BOTH;
        cMain.insets = new Insets(5, 5, 5, 10);
        mainMenuCard.add(statsButton, cMain); 
        
        JButton statsPersonButton = new JButton("Message Stats by Person");
        statsPersonButton.setToolTipText("View messages from your conversation with a given friend.");
        statsPersonButton.addActionListener(this);
        statsPersonButton.setActionCommand(PERSON_SELECTION_CARD);
        cMain.gridx=1; cMain.gridy=2; 
        mainMenuCard.add(statsPersonButton, cMain);
        
        JButton usageByHourButton = new JButton("Messenger Usage by Hour of Day");
        usageByHourButton.setToolTipText("View a graph of when you are most active during a day.");
        usageByHourButton.addActionListener(this);
        usageByHourButton.setActionCommand(USAGE_BY_HOUR_CARD);
        cMain.gridx=1; cMain.gridy=3;
        mainMenuCard.add(usageByHourButton, cMain);
        
        JButton usageByDayButton = new JButton("Messenger Usage by Day of Week");
        usageByDayButton.setToolTipText("View a graph of the days of the week in which you are the most active.");
        usageByDayButton.addActionListener(this);
        usageByDayButton.setActionCommand(USAGE_BY_DAY_CARD);
        cMain.gridx=1; cMain.gridy=4;
        mainMenuCard.add(usageByDayButton, cMain);
        
        JButton usageOverTimeButton = new JButton("Messenger Usage Over Time");
        usageOverTimeButton.setToolTipText("View a graph of your activity over time, by month/year.");
        usageOverTimeButton.addActionListener(this);
        usageOverTimeButton.setActionCommand(USAGE_BY_TIME_CARD);
        cMain.gridx=1; cMain.gridy=5;
        mainMenuCard.add(usageOverTimeButton, cMain);
        
        JButton specificWordFreqButton = new JButton("Usage of a Specific Word");
        specificWordFreqButton.setToolTipText("Specify a word and view a graph of your usage of that word over time.");
        specificWordFreqButton.addActionListener(this);
        specificWordFreqButton.setActionCommand(WORD_SELECTION_CARD);
        cMain.gridx=1; cMain.gridy=6;
        mainMenuCard.add(specificWordFreqButton, cMain);
        
        JButton multipleWordFreqButton = new JButton("Most Frequently Used Words");
        multipleWordFreqButton.setToolTipText("View a graph of most frequently used words.\nYou may set a blacklist of words you do not want to see.");
        multipleWordFreqButton.addActionListener(this);
        multipleWordFreqButton.setActionCommand(BLACKLIST_CARD);
        cMain.gridx=1; cMain.gridy=7;
        mainMenuCard.add(multipleWordFreqButton, cMain);
    }
    
    public void addGenStatsComponents(){
    	cStats = new GridBagConstraints();
    	
    	statsCard = new JPanel();
    	statsCard.setBackground(Color.WHITE);
    	statsCard.setLayout(new GridBagLayout());
    	
    	JButton mainMenuButton = new JButton("Main Menu");
    	mainMenuButton.addActionListener(this);
    	mainMenuButton.setActionCommand(MAIN_MENU_CARD);
    	cStats.gridx=0; cStats.gridy=2; cStats.ipadx = 20; cStats.ipady = 30; cStats.insets = new Insets(10, 0, 30, 0);
    	statsCard.add(mainMenuButton, cStats);
    	
    	JLabel generalStats = new JLabel("General Stats for " + userInbox.getPartOfName(0) + ": ");
    	generalStats.setFont(generalStats.getFont().deriveFont(30.0f));
    	generalStats.setForeground(fbBlue);
    	cStats.gridx=0; cStats.gridy=1; cStats.weightx=0.50; cStats.weighty=0.15;
    	cStats.anchor = GridBagConstraints.PAGE_START; cStats.insets = new Insets(0, 0, 0, 0);
    	statsCard.add(generalStats, cStats);
    	
    	JLabel totalConvos = new JLabel("Total number of conversations: " + userInbox.numConvos());
    	totalConvos.setFont(totalConvos.getFont().deriveFont(15.0f));
    	cStats.gridx=2; cStats.gridy=1; cStats.anchor=GridBagConstraints.LINE_START; 
    	cStats.insets = new Insets(10, 0, 10, 0);
    	statsCard.add(totalConvos, cStats);
    	
    	JLabel totalMessages = new JLabel("Total number of messages: " + userInbox.numMessages());
    	totalMessages.setFont(totalMessages.getFont().deriveFont(15.0f));
    	cStats.gridx=2; cStats.gridy=2;
    	statsCard.add(totalMessages, cStats);
    	
    	JLabel averageMessages = new JLabel("Average messages per conversation: " + userInbox.getAverageMessagesInConvo() + " messages");
    	averageMessages.setFont(averageMessages.getFont().deriveFont(15.0f));
    	cStats.gridx=2; cStats.gridy=3;
    	statsCard.add(averageMessages, cStats);
    	
    	JLabel first = new JLabel("<html>"+ "Largest Conversation: " + userInbox.getLargestConvos(5).get(0).getPersonChattedWith() + 
							  " with " + userInbox.getLargestConvos(5).get(0).getSize() + " messages" + "</html>");
    	JLabel second = new JLabel("<html>"+ "Second Largest Conversation: " + userInbox.getLargestConvos(5).get(1).getPersonChattedWith() + 
				  			  " with " + userInbox.getLargestConvos(5).get(1).getSize() + " messages" + "</html>");
    	JLabel third = new JLabel("<html>"+ "Third Largest Conversation: " + userInbox.getLargestConvos(5).get(2).getPersonChattedWith() + 
				  			  " with " + userInbox.getLargestConvos(5).get(2).getSize() + " messages" + "</html>");
    	JLabel fourth = new JLabel("<html>"+ "Fourth Largest Conversation: " + userInbox.getLargestConvos(5).get(3).getPersonChattedWith() + 
				  			  " with " + userInbox.getLargestConvos(5).get(3).getSize() + " messages" + "</html>");
    	JLabel fifth = new JLabel("<html>"+ "Fifth Largest Conversation: " + userInbox.getLargestConvos(5).get(4).getPersonChattedWith() + 
				  			  " with " + userInbox.getLargestConvos(5).get(4).getSize() + " messages" + "</html>");
    	first.setFont(first.getFont().deriveFont(15.0f));
    	second.setFont(second.getFont().deriveFont(15.0f));
    	third.setFont(third.getFont().deriveFont(15.0f));
    	fourth.setFont(fourth.getFont().deriveFont(15.0f));
    	fifth.setFont(fifth.getFont().deriveFont(15.0f));
    	cStats.gridy=5;
    	cStats.fill = GridBagConstraints.BOTH;
    	cStats.gridwidth = GridBagConstraints.REMAINDER;
    	statsCard.add(first, cStats);
    	cStats.gridy=6;
    	statsCard.add(second, cStats);
    	cStats.gridy=7;
    	statsCard.add(third, cStats);
    	cStats.gridy=8;
    	statsCard.add(fourth, cStats);
    	cStats.gridy=9;
    	statsCard.add(fifth, cStats);
    	
    	
    }
    
    public void addPersonSelectionComponents(){
    	cPStats = new GridBagConstraints();
    	
    	personSelectionCard = new JPanel();
    	personSelectionCard.setBackground(Color.WHITE);
    	personSelectionCard.setLayout(new GridBagLayout());
    	
    	JLabel selectTitle = new JLabel("Pick a friend to view your conversation stats with them.");
    	selectTitle.setFont(selectTitle.getFont().deriveFont(25.0f));
    	selectTitle.setForeground(fbBlue);
    	cPStats.gridx=1; cPStats.gridy=0; cPStats.insets = new Insets(20, 0, 20, 0); 
    	cPStats.gridwidth = GridBagConstraints.REMAINDER;
    	personSelectionCard.add(selectTitle, cPStats);
    	
    	JLabel helpText = new JLabel("Enter the full name of a friend below then press Enter.");
    	helpText.setFont(helpText.getFont().deriveFont(20.0f));
    	cPStats.gridx=1; cPStats.gridy=1; cPStats.insets = new Insets(20, 0, 20, 0);
    	personSelectionCard.add(helpText, cPStats);
    	
    	personTextField = new JFormattedTextField(selectedPerson);    	
    	personTextField.setFont(personTextField.getFont().deriveFont(15.0f));
    	personTextField.setValue(new String(selectedPerson));
    	personTextField.setColumns(25);
    	personTextField.setHorizontalAlignment(SwingConstants.CENTER);
    	personTextField.addKeyListener(this);
    	personTextField.addFocusListener(new FocusListener(){ //to have the hint text be hint text.
    		public void focusGained(FocusEvent fe){
    			if(personTextField.getText().equals(selectedPersonDefault)){
    				personTextField.setText("");
    			}else if(personTextField.getText().length() > 0){
    				personTextField.setText(personTextField.getText());
    			}else{
    				personTextField.setText("");
    			}
    		}
    		public void focusLost(FocusEvent fe){
    			if(personTextField.getText().length()==0){
    				personTextField.setValue(selectedPerson);
    			}
    		}
    	});
    	cPStats.gridx=1; cPStats.gridy=2; 
    	cPStats.insets = new Insets(20, 0, 20, 0);
    	personSelectionCard.add(personTextField, cPStats);
    	
    	JButton sMainMenuButton = new JButton("Main Menu");
    	sMainMenuButton.addActionListener(this);
    	sMainMenuButton.setActionCommand(MAIN_MENU_CARD);
    	cPStats.gridx=1; cPStats.gridy=3; cPStats.ipadx=20; cPStats.ipady=20;
    	cPStats.insets = new Insets(10, 0, 30, 0);
    	personSelectionCard.add(sMainMenuButton, cPStats);
    	
    	
    	cFix = new GridBagConstraints();
    	cFix.weightx=0.25;
    	
    	statsPersonCard = new JPanel();
    	statsPersonCard.setLayout(new GridBagLayout());
    	statsPersonCard.setBackground(Color.WHITE);
    	
    	cFix.weightx=.75;
    	JButton pReselectButton = new JButton("Back");
    	pReselectButton.addActionListener(this);
    	pReselectButton.setActionCommand(PERSON_SELECTION_CARD);
    	cFix.gridx=0; cFix.gridy=0; cFix.ipadx=20; cFix.ipady=20; cFix.insets = new Insets(20, 20, 0, 0);
    	cFix.anchor = GridBagConstraints.FIRST_LINE_START;
    	statsPersonCard.add(pReselectButton, cFix);
    	
    	JButton pMainMenuButton = new JButton("Main Menu");
    	pMainMenuButton.addActionListener(this);
    	pMainMenuButton.setActionCommand(MAIN_MENU_CARD);
    	cFix.gridx=1; cFix.gridy=0; cFix.ipadx=20; cFix.ipady=20; cFix.insets = new Insets(20, 0, 30, 20);
    	cFix.anchor = GridBagConstraints.FIRST_LINE_END;
    	statsPersonCard.add(pMainMenuButton, cFix);
    	
    	cFix.gridwidth = GridBagConstraints.REMAINDER;
    	cFix.weighty=0.15;
    	
    	personWelcome = new JLabel();
    	personWelcome.setFont(personWelcome.getFont().deriveFont(30.0f));
    	personWelcome.setForeground(fbBlue);
    	cFix.gridx=0; cFix.gridy=1; cFix.anchor=GridBagConstraints.PAGE_START;  cFix.ipadx=0; cFix.ipady=0; 
    	statsPersonCard.add(personWelcome, cFix);
    	
    	pTotalMessages = new JLabel();
    	pTotalMessages.setFont(pTotalMessages.getFont().deriveFont(20.0f));
    	pTotalMessages.setForeground(fbBlue);
    	cFix.gridx=0; cFix.gridy = 2; cFix.anchor=GridBagConstraints.PAGE_START;
    	cFix.insets = new Insets(0, 0, 0, 0);
    	statsPersonCard.add(pTotalMessages, cFix);
    	
    	JLabel findMessageText = new JLabel("find a message in your conversation");
    	findMessageText.setFont(findMessageText.getFont().deriveFont(20.0f));
    	findMessageText.setForeground(fbBlue);
    	cFix.gridx=0; cFix.gridy=4; cFix.anchor = GridBagConstraints.PAGE_END;
    	statsPersonCard.add(findMessageText, cFix);
    	
    	NumberFormat amountFormat = NumberFormat.getNumberInstance();
    	messageTextField =  new JFormattedTextField(amountFormat);
    	messageTextField.setColumns(20);
    	messageTextField.setHorizontalAlignment(SwingConstants.CENTER);
    	messageTextField.setMinimumSize(new Dimension(200, 30));
    	messageTextField.setFont(messageTextField.getFont().deriveFont(15.0f));
    	messageTextField.setValue(new Integer(messageIndex));
    	messageTextField.addFocusListener(new FocusListener(){ //to have the hint text be hint text.
    		public void focusGained(FocusEvent fe){
    			messageTextField.setText("");
    		}
			public void focusLost(FocusEvent e) {
				messageTextField.setText(""+messageIndex);
			}
    	});
    	messageTextField.addKeyListener(this);
    	cFix.gridx=0; cFix.gridy=5; cFix.anchor = GridBagConstraints.CENTER;
    	statsPersonCard.add(messageTextField, cFix);
    	
    	messageBackwardButton = new JButton("-1 message");
    	messageBackwardButton.setToolTipText("Go back one message.");
    	messageBackwardButton.addActionListener(this);
    	messageBackwardButton.setEnabled(false);
    	cFix.gridx=0; cFix.gridy=5; cFix.anchor = GridBagConstraints.LINE_START;
    	cFix.insets = new Insets(0, 120, 0, 0); cFix.ipadx=10; cFix.ipady=10;
    	statsPersonCard.add(messageBackwardButton, cFix);
    	
    	messageForwardButton = new JButton("+1 message");
    	messageForwardButton.setToolTipText("Go forward one message.");
    	messageForwardButton.addActionListener(this);
    	cFix.gridx=0; cFix.gridy=5; cFix.anchor = GridBagConstraints.LINE_END;
    	cFix.insets = new Insets(0, 0, 0, 120); 
    	statsPersonCard.add(messageForwardButton, cFix);
    	
    	helpFindText = new JLabel();
    	helpFindText.setFont(helpFindText.getFont().deriveFont(15.0f));
    	helpFindText.setForeground(fbBlue);
    	cFix.gridx=0; cFix.gridy=6; cFix.anchor = GridBagConstraints.PAGE_START;
    	cFix.insets = new Insets(0, 0, 0, 0);  cFix.ipadx=0; cFix.ipady=0; 
    	statsPersonCard.add(helpFindText, cFix);
    	
    	currentMessage = new JTextArea();
    	currentMessage.setEditable(false);
    	currentMessage.setText("Message will display here.");
    	currentMessage.setLineWrap(true);
    	currentMessage.setWrapStyleWord(true);
    	currentMessage.setFont(currentMessage.getFont().deriveFont(20.0f)); 
    	cFix.ipadx = 20;
    	cFix.gridx=0; cFix.gridy=3; cFix.anchor= GridBagConstraints.CENTER;
    	cFix.fill = GridBagConstraints.BOTH; 
    	cFix.gridwidth = GridBagConstraints.REMAINDER;
    	JScrollPane scrolly = new JScrollPane(currentMessage);
    	scrolly.setPreferredSize(new Dimension(300, 300));
    	statsPersonCard.add(scrolly, cFix);

    }
   
    public Scene createUsageByHourScene(){
    	ArrayList<Data<String, Number>> arrayList = userInbox.getHourlyActivity();
    	byHour = userInbox.createChartFromAL(arrayList, "Messages Sent", "Hour of Day (e.g. 15 = 3PM)", "Messages Sent By Hour of Day");
    	
    	Scene scene = new Scene(byHour, 1050, 500);
    	scene.getStylesheets().add(css);
    	
    	return (scene);
    }
    
    public void addUsageByHourComponents(){
    	cHour = new GridBagConstraints();
    	usageByHourCard = new JPanel();
    	usageByHourCard.setLayout(new GridBagLayout());
    	
    	usageByHourJFX = new JFXPanel();
    	usageByHourJFX.setBackground(Color.WHITE);
    	usageByHourScene = createUsageByHourScene();
    	usageByHourJFX.setScene(usageByHourScene);
    	
    	usageByHourCard.setBackground(Color.white);
    	cHour.gridx=0; cHour.gridy=0; cHour.weightx = 1; cHour.weighty = 1;
    	usageByHourCard.add(usageByHourJFX, cHour);
    	
    	JButton hMainMenuButton = new JButton("Main Menu");
    	hMainMenuButton.addActionListener(this);
    	hMainMenuButton.setActionCommand(MAIN_MENU_CARD);
    	cHour.gridx=0; cHour.gridy=1; cHour.ipadx = 20; cHour.ipady = 30; 
    	cHour.insets = new Insets(10, 0, 30, 0);
    	cHour.anchor = GridBagConstraints.PAGE_END;
    	usageByHourCard.add(hMainMenuButton, cHour);
    }
    
    public Scene createUsageByDayScene(){
    	ArrayList<Data<String, Number>> arrayList = userInbox.getDailyActivity();
    	byDay = userInbox.createChartFromAL(arrayList, "Messages Sent", "Day of Week", "Messages Sent by Day of Week");
    	
    	Scene scene = new Scene(byDay, 1050, 500);
    	scene.getStylesheets().add(css);
    	
    	return (scene);
    }
    
    public void addUsageByDayComponents(){
    	cDay = new GridBagConstraints();
    	usageByDayCard = new JPanel();
    	usageByDayCard.setLayout(new GridBagLayout());
    	
    	usageByDayJFX = new JFXPanel();
    	usageByDayJFX.setBackground(Color.WHITE);
    	usageByDayScene = createUsageByDayScene();
    	usageByDayJFX.setScene(usageByDayScene);
    	
    	usageByDayCard.setBackground(Color.white);
    	cDay.gridx=0; cDay.gridy=0; cDay.weightx = 1; cDay.weighty = 1;
    	usageByDayCard.add(usageByDayJFX, cDay);
    	
    	JButton dMainMenuButton = new JButton("Main Menu");
    	dMainMenuButton.addActionListener(this);
    	dMainMenuButton.setActionCommand(MAIN_MENU_CARD);
    	cDay.gridx=0; cDay.gridy=1; cDay.ipadx = 20; cDay.ipady = 30; 
    	cDay.insets = new Insets(10, 0, 30, 0);
    	cDay.anchor = GridBagConstraints.PAGE_END;
    	usageByDayCard.add(dMainMenuButton, cDay);
    }
    
    public Scene createUsageByTimeScene(){
    	ArrayList<Data<String, Number>> arrayList = userInbox.getActivityOverTime();
    	byTime = userInbox.createChartFromAL(arrayList, "Messages Sent", "Month and Year", "Messages Sent Over Time");
    	byTime.minWidth(10);
    	
    	Scene scene = new Scene(byTime, 1050, 500);
    	scene.getStylesheets().add(css);
    	
    	return (scene);
    }
    
    public void addUsageByTimeComponents(){
    	cTime = new GridBagConstraints();
    	usageByTimeCard = new JPanel();
    	usageByTimeCard.setLayout(new GridBagLayout());
    	
    	usageByTimeJFX = new JFXPanel();
    	usageByTimeJFX.setBackground(Color.WHITE);
    	usageByTimeScene = createUsageByTimeScene();
    	usageByTimeJFX.setScene(usageByTimeScene);
    	
    	usageByTimeCard.setBackground(Color.white);
    	cTime.gridx=0; cTime.gridy=0; cTime.weightx = 1; cTime.weighty = 1;
    	usageByTimeCard.add(usageByTimeJFX, cTime);
    	
    	JButton tMainMenuButton = new JButton("Main Menu");
    	tMainMenuButton.addActionListener(this);
    	tMainMenuButton.setActionCommand(MAIN_MENU_CARD);
    	cTime.gridx=0; cTime.gridy=1; cTime.ipadx = 20; cTime.ipady = 30; 
    	cTime.insets = new Insets(10, 0, 30, 0);
    	cTime.anchor = GridBagConstraints.PAGE_END;
    	usageByTimeCard.add(tMainMenuButton, cTime);
    }
    
    public Scene createWordUsageByTimeScene(){
    	ArrayList<Data<String, Number>> arrayList = userInbox.getWordFrequencyOverTime(selectedWord);
    	byWord = userInbox.createChartFromAL(arrayList, "Times Sent Given Word", "Month and Time", "Usage of \"" + selectedWord + "\" Over Time");
    	
    	Scene scene = new Scene(byWord, 1050, 500);
    	scene.getStylesheets().add(css);
    	
    	return scene;
    }

    public void addWordUsageByTimeComponents(){
    	cCWord = new GridBagConstraints();
    	
    	wordSelectionCard = new JPanel();
    	wordSelectionCard.setBackground(Color.WHITE);
    	wordSelectionCard.setLayout(new GridBagLayout());
    	
    	JLabel selectTitle = new JLabel("Pick a word and view a graph of its usage");
    	selectTitle.setFont(selectTitle.getFont().deriveFont(25.0f));
    	selectTitle.setForeground(fbBlue);
    	cCWord.gridx=1; cCWord.gridy=0; cCWord.insets = new Insets(20, 0, 20, 0); 
    	cCWord.gridwidth = GridBagConstraints.REMAINDER;
    	wordSelectionCard.add(selectTitle, cCWord);
    	
    	JLabel helpText = new JLabel("Enter a word below then press Enter.");
    	helpText.setFont(helpText.getFont().deriveFont(20.0f));
    	cCWord.gridx=1; cCWord.gridy=1; cCWord.insets = new Insets(20, 0, 20, 0);
    	wordSelectionCard.add(helpText, cCWord);
    	
    	wordTextField = new JFormattedTextField(selectedWord);    	
    	wordTextField.setFont(wordTextField.getFont().deriveFont(15.0f));
    	wordTextField.setHorizontalAlignment(SwingConstants.CENTER);
    	wordTextField.setColumns(20);
    	wordTextField.setValue(new String(selectedWord));
    	wordTextField.addKeyListener(this);
    	wordTextField.addFocusListener(new FocusListener(){ //to have the hint text be hint text.
    		public void focusGained(FocusEvent fe){
    			if(wordTextField.getText().equals(selectedWordDefault)){
    				wordTextField.setText("");
    			}else if(wordTextField.getText().length() > 0){
    				wordTextField.setText(wordTextField.getText());
    			}else{
    				wordTextField.setText("");
    			}
    		}
    		public void focusLost(FocusEvent fe){
    			if(wordTextField.getText().length()==0){
    				wordTextField.setValue(selectedWord);
    			}
    		}
    	});
    	cCWord.gridx=1; cCWord.gridy=2; cCWord.anchor = GridBagConstraints.CENTER;
    	cCWord.insets = new Insets(20, 0, 20, 0);
    	wordSelectionCard.add(wordTextField, cCWord);
    	
    	JButton sMainMenuButton = new JButton("Main Menu");
    	sMainMenuButton.addActionListener(this);
    	sMainMenuButton.setActionCommand(MAIN_MENU_CARD);
    	cCWord.gridx=1; cCWord.gridy=3; cCWord.ipadx=20; cCWord.ipady=20;
    	cCWord.insets = new Insets(10, 0, 30, 0);
    	wordSelectionCard.add(sMainMenuButton, cCWord);
    	
    	
    	cWord = new GridBagConstraints();
    	
    	wordUsageCard = new JPanel();
    	wordUsageCard.setLayout(new GridBagLayout());
    	wordUsageCard.setBackground(Color.white);
    	
    	wordUsageJFX = new JFXPanel();
    	wordUsageJFX.setBackground(Color.white);
    	wordUsageScene = createWordUsageByTimeScene();
    	wordUsageJFX.setScene(wordUsageScene);
    	cWord.gridy=0; cWord.weighty=1;
    	wordUsageCard.add(wordUsageJFX, cWord);
    	
    	JButton backButton = new JButton("New Word");
    	backButton.setActionCommand(WORD_SELECTION_CARD);
    	backButton.addActionListener(this);
    	cWord.gridy=1; cWord.weighty=0; //cWord.insets = new Insets(0,0, 10, 0); 
    	cWord.ipadx=20; cWord.ipady=20;
    	wordUsageCard.add(backButton, cWord);
    	
    	JButton mainMenuButton = new JButton ("Main Menu");
    	mainMenuButton.setActionCommand(MAIN_MENU_CARD);
    	mainMenuButton.addActionListener(this);
    	cWord.gridy=2; cWord.insets = new Insets (0, 0, 10, 0);
    	wordUsageCard.add(mainMenuButton, cWord);
    }
    
    public void addGraphOptionsComponents(){
    	for(String word : MostFrequentWordsFinder.DEFAULT_BLACKLIST){
    		currentBlacklist.add(word);
    	}
    	
    	cBlack = new GridBagConstraints();
    	
    	blacklistCard = new JPanel();
    	blacklistCard.setLayout(new GridBagLayout());
    	blacklistCard.setBackground(Color.white);
    	
    	JLabel numWords = new JLabel();
    	numWords.setForeground(fbBlue);
    	numWords.setText("Number of words to display");
    	numWords.setFont(numWords.getFont().deriveFont(32.0f));
    	cBlack.gridx = 0; cBlack.gridy = 0;
    	cBlack.anchor = GridBagConstraints.PAGE_START;
    	blacklistCard.add(numWords, cBlack);
    	
    	JLabel numHelp = new JLabel();
    	numHelp.setText("anywhere between 10 - 50 is a good range.");
    	numHelp.setFont(numHelp.getFont().deriveFont(15.0f));
    	cBlack.anchor = GridBagConstraints.CENTER;
    	cBlack.insets = new Insets(30,0,0,0);
    	blacklistCard.add(numHelp, cBlack);
    	
    	JLabel space = new JLabel("\n\n");
    	cBlack.gridy=1; cBlack.insets = new Insets(0,0,0,0);
    	blacklistCard.add(space, cBlack);
    	
    	numberWordsTextField = new JTextField(numberWords);
    	numberWordsTextField.setFont(numberWordsTextField.getFont().deriveFont(15.0f));
    	numberWordsTextField.setText(new String(""+numberWords));
    	numberWordsTextField.setHorizontalAlignment(SwingConstants.CENTER);
    	numberWordsTextField.addFocusListener(new FocusListener(){ //to have the hint text be hint text.
    		public void focusGained(FocusEvent fe){
    			if(numberWordsTextField.getText().equals(""+numberWords)){
    				numberWordsTextField.setText("");
    			}else if(numberWordsTextField.getText().length() > 0){
    				numberWordsTextField.setText(numberWordsTextField.getText());
    			}else{
    				numberWordsTextField.setText("");
    			}
    		}
    		public void focusLost(FocusEvent fe){
    			if(numberWordsTextField.getText().length()==0){
    				numberWordsTextField.setText(""+numberWords);
    			}
    		}
    	});
    	cBlack.gridx=1; cBlack.gridy=0; 
    	cBlack.insets = new Insets(20, 0, 20, 0);
    	blacklistCard.add(numberWordsTextField, cBlack);
    	
    	JLabel blHelp = new JLabel();
    	blHelp.setForeground(fbBlue);
    	blHelp.setText("Words to Exclude");
    	blHelp.setFont(blHelp.getFont().deriveFont(32.0f));
    	cBlack.gridx=0; cBlack.gridy=2;
    	cBlack.insets = new Insets(0,0,65,0);
    	cBlack.anchor = GridBagConstraints.CENTER;
    	blacklistCard.add(blHelp, cBlack);
    	
    	JLabel helpSub = new JLabel();
    	helpSub.setFont(helpSub.getFont().deriveFont(15.0f));
    	helpSub.setText("separate words by commas");
    	cBlack.gridx=0; cBlack.gridy=2;
    	cBlack.insets = new Insets(0,0,0,0);
    	cBlack.anchor = GridBagConstraints.CENTER;
    	blacklistCard.add(helpSub, cBlack);
    	
    	GridBagConstraints cTemp = new GridBagConstraints();
    	
    	blacklistDisplay = new JTextArea();
    	blacklistDisplay.setText(populateBlacklist(currentBlacklist));
    	blacklistDisplay.setLineWrap(true);
    	blacklistDisplay.setWrapStyleWord(true);
    	cTemp.gridx=1; cTemp.gridy = 2;
    	cTemp.anchor = GridBagConstraints.LINE_END;
    	JScrollPane scrolly = new JScrollPane(blacklistDisplay);
    	scrolly.setPreferredSize(new Dimension(300, 300));
    	blacklistCard.add(scrolly, cTemp);
    	
    	JButton saveChangesButton = new JButton("Save Changes");
    	saveChangesButton.addActionListener(this);
    	saveChangesButton.setActionCommand(MOST_USED_WORDS_CARD);
    	cBlack.gridx = 1; cBlack.gridy=3; cBlack.anchor = GridBagConstraints.LINE_END;
    	cBlack.ipadx = 20; cBlack.ipady = 20; 
    	cBlack.insets = new Insets(30, 0, 5, 0);
    	blacklistCard.add(saveChangesButton, cBlack);
    	
    	JButton mainMenuButton = new JButton("Main Menu");
    	mainMenuButton.addActionListener(this);
    	mainMenuButton.setActionCommand(MAIN_MENU_CARD);
    	cBlack.gridx=1; cBlack.gridy=4; 
    	cBlack.anchor = GridBagConstraints.FIRST_LINE_END;
    	blacklistCard.add(mainMenuButton, cBlack);
    }
    
    public Scene createMostUsedWordsScene(){
    	ArrayList<Data<String, Number>> arrayList = userInbox.getMostUsedWords(numberWords, currentBlacklist);

    	byHour = userInbox.createChartFromAL(arrayList, "Word Frequency", "Word", "Most Used Words");
    	
    	Scene scene = new Scene(byHour, 1050, 500);
    	scene.getStylesheets().add(css);
    	
    	return (scene);
    }
    
    public void addMostUsedWordsComponents(){
    	cMFWord = new GridBagConstraints();
    	mostUsedWordsCard = new JPanel();
    	mostUsedWordsCard.setLayout(new GridBagLayout());
    	
    	mostUsedWordsJFX= new JFXPanel();
    	mostUsedWordsJFX.setBackground(Color.WHITE);
    	usageByMUWordsScene = createMostUsedWordsScene();
    	mostUsedWordsJFX.setScene(usageByMUWordsScene);
    	
    	mostUsedWordsCard.setBackground(Color.white);
    	cMFWord.gridx=0; cMFWord.gridy=0; cMFWord.weightx = 1; cMFWord.weighty = 1;
    	mostUsedWordsCard.add(mostUsedWordsJFX, cMFWord);
    	
    	JButton blacklistButton = new JButton("Edit graph contents");
    	blacklistButton.addActionListener(this);
    	blacklistButton.setActionCommand(BLACKLIST_CARD);
    	cMFWord.weightx=0; cMFWord.weighty = 0;
    	cMFWord.gridx=0; cMFWord.gridy=2; cMFWord.ipadx = 5; cMFWord.ipady = 20; 
    	cMFWord.insets = new Insets(5, 0, 0, 0);
    	cMFWord.anchor = GridBagConstraints.PAGE_END;
    	mostUsedWordsCard.add(blacklistButton, cMFWord);
    	
    	JButton MainMenuButton = new JButton("Main Menu");
    	MainMenuButton.addActionListener(this);
    	MainMenuButton.setActionCommand(MAIN_MENU_CARD);
    	cMFWord.gridx=0; cMFWord.gridy=3; cMFWord.ipadx = 20; cMFWord.ipady = 20; 
    	cMFWord.insets = new Insets(5, 0, 5, 0);
    	cMFWord.anchor = GridBagConstraints.PAGE_END;
    	mostUsedWordsCard.add(MainMenuButton, cMFWord);
    }
    
    private String populateBlacklist(ArrayList<String> al){
    	StringBuilder sb = new StringBuilder();
    	for(String word : al){
    		sb.append(word + ", ");
    	}
    	return sb.toString();
    }
    
    public static void initParse() throws InterruptedException{
    	userInbox = new Inbox(fc.getSelectedFile().toString());
        personSelectionConvo = userInbox.getLargestConvo(); //this is just to init the variable so it doesn't throw errors
        createAndShowGUI();
    }
    
    public void actionPerformed(ActionEvent e) {
        //Handle open button action.
        if (e.getSource() == fileSelectButton) {
            int returnVal = fc.showOpenDialog(Display.this);
            
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                ParseRunnable runParse = new ParseRunnable();
                Thread t = new Thread(runParse);
                takeAWhile.setForeground(fbBlue);
                helpIconLabel.setVisible(false);
                loading.setVisible(true);
                fc.setEnabled(false);
                fileSelectButton.setEnabled(false);
                t.start();
            }else{
            	System.out.println("file selection cancelled");
            }
        }else if(e.getSource() == messageBackwardButton){
        	if(messageIndex > 1){
        		messageIndex-=1;
        		messageTextField.setText(""+ messageIndex);
        		Message message = personSelectionConvo.getMessage(messageIndex-1);
        		currentMessage.setText("\"" + message.toString() + "\"  - " + message.getSender() + " on " + message.getDateString());
    			if (messageIndex > 1) messageBackwardButton.setEnabled(true);
    			else messageBackwardButton.setEnabled(false);
    			if (messageIndex < personSelectionConvo.getSize()) messageForwardButton.setEnabled(true);
    			else messageForwardButton.setEnabled(false);
        	}else{
        	}
        }else if(e.getSource() == messageForwardButton){
        	if(messageIndex < personSelectionConvo.getSize()){
        		messageIndex+=1;
        		messageTextField.setText(""+ messageIndex);
        		Message message = personSelectionConvo.getMessage(messageIndex-1);
        		currentMessage.setText("\"" + message.toString() + "\"  - " + message.getSender() + " on " + message.getDateString());
        		if (messageIndex < personSelectionConvo.getSize()) messageForwardButton.setEnabled(true);
    			else messageForwardButton.setEnabled(false);
        		if (messageIndex > 1) messageBackwardButton.setEnabled(true);
    			else messageBackwardButton.setEnabled(false);
        	}else{
        	}
        }else if(e.getActionCommand() == MAIN_MENU_CARD){
        	messageIndex = 0;
        	messageBackwardButton.setEnabled(false);
        	messageForwardButton.setEnabled(true);
        	currentMessage.setText("message will display here");
        	messageTextField.setText(""+1);
        	wordTextField.setText(selectedWordDefault);
        	CardLayout cards = (CardLayout) (overallCards.getLayout());
        	cards.show(overallCards, MAIN_MENU_CARD);
        }else if(e.getActionCommand() == MOST_USED_WORDS_CARD){
        	String blacklist = blacklistDisplay.getText();
        	currentBlacklist = makeBlacklist(blacklist);
        	numberWords = Integer.parseInt(numberWordsTextField.getText());
        	
        	mostUsedWordsJFX.setScene(createMostUsedWordsScene());
        	
        	CardLayout cards = (CardLayout) overallCards.getLayout();
        	cards.show(overallCards, MOST_USED_WORDS_CARD);
        }	
        
        else{
        	messageIndex = 0;
        	messageBackwardButton.setEnabled(false);
        	messageForwardButton.setEnabled(true);
        	currentMessage.setText("message will display here");
        	messageTextField.setText(""+1);
        	wordTextField.setText(selectedWordDefault);
        	CardLayout cards = (CardLayout)(overallCards.getLayout());
        	cards.show(overallCards, e.getActionCommand());
        	
        }
    }

	public void keyPressed(KeyEvent e) {
		Object source = e.getSource();
		if(e.getKeyCode() == KeyEvent.VK_ENTER){
	    	if(source == messageTextField){
	    		try {
		    		messageIndex = Integer.parseInt(messageTextField.getText());
	    			if (messageIndex < personSelectionConvo.getSize()) messageForwardButton.setEnabled(true);
	    			else messageForwardButton.setEnabled(false);
	    			if (messageIndex > 1) messageBackwardButton.setEnabled(true);
	    			else messageBackwardButton.setEnabled(false);
		    		Message message = personSelectionConvo.getMessage(messageIndex - 1);
		    		currentMessage.setText("\"" + message.toString() + "\"  - " + message.getSender() + " on " 
		    								+ message.getDateString());
		    		
	    		} catch (NullPointerException error) {
	    			System.out.println("Specified index out of bounds!");
	    		}
	    	}else if(source == blacklistTextField){
	    		
	    	
			}else if(source == personTextField){
	    		if(!personTextField.getText().equals(selectedPersonDefault)){
		    		try{ 
		    			personSelectionConvo = userInbox.getConvo(personTextField.getText());
		        		CardLayout cards = (CardLayout)(overallCards.getLayout());
		            	if(userInbox.isConvo(personSelectionConvo.getName())){
		            		//we have to set all the stats to the person whose name is in the personTextField here
		            		//because otherwise they will all be either null or outdated
		            		currentMessage.setText("Message will display here.");
		            		personWelcome.setText("Your messages with " + personTextField.getText() + ":");
		            		pTotalMessages.setText(personSelectionConvo.getSize() + " total messages with " + 
		            							   getFirstName(personTextField.getText()));
		            		personTextField.setText(selectedPersonDefault);
		            		helpFindText.setText("Enter a number between 1 and " + personSelectionConvo.getSize() + " then press Enter.");
		
		            		cards.show(overallCards, STATS_PERSON_CARD);
		            		personTextField.setText(selectedPersonDefault);
		            	}
		        	} catch (NullPointerException error) {
		        		System.out.println("Conversation not found!");
		        	}
	    		}
	    	}else if(source == wordTextField){
	    		if(!wordTextField.getText().equals(selectedWordDefault)){
	    			CardLayout cards = (CardLayout) (overallCards.getLayout());
	        		selectedWord = wordTextField.getText();
	        		
	        		wordUsageJFX.setScene(createWordUsageByTimeScene());
	        		
	        		cards.show(overallCards, WORD_USAGE_CARD);
	        		wordTextField.setText(selectedWordDefault);
	    		}
	    	}
		}
		
	}
    
    public ArrayList<String> makeBlacklist(String words){
    	ArrayList<String> newBlacklist = new ArrayList<String>();
    	scan = new Scanner(words);
    	scan.useDelimiter(", ");
    	scan.useDelimiter(",");
    	while(scan.hasNext()){
    		newBlacklist.add(scan.next());
    	}
    	
    	return newBlacklist;
    }
    
    public void addFileSelectComponentsToPane(Container pane){
    	addFileSelectComponents();
    	pane.add(fileSelectionBG);
    }
    
    //THIS IS DIFFERENT THAN REGULAR CREATEANDSHOWGUI
    //must be done before everything else so that nothing is null!
    public static void createAndShowFileSelection(){
    	//add the file selection to the frame
    	mainFrame = new JFrame("Facebook Message Analyzer");
        hack(mainFrame);
        
        Display display = new Display();
        display.addFileSelectComponentsToPane(mainFrame.getContentPane());
        
        
        mainFrame.setIconImage(progImage);
        mainFrame.setResizable(false);
        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }
    
    private static void hack(JFrame main){
    	main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	main.setPreferredSize(prefSize);
    	main.setBackground(Color.white);
    }
    
    public static void createAndShowGUI() {
        //Add the rest of the content to the window.
        Display display = new Display();
        display.addComponentsToPane(mainFrame.getContentPane());
        
        mainFrame.setResizable(false);
        mainFrame.pack();
        mainFrame.setVisible(true);
    }
    
    public String getFirstName(String username){
		Scanner userScanner = new Scanner(username);
		userScanner.useDelimiter(" ");
		String firstName = userScanner.next();
		userScanner.close();
		System.out.println();
		return firstName;
	}

	public void keyTyped(KeyEvent e) {
	}

	public void keyReleased(KeyEvent e) {
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}
}


class HTMLFilter extends FileFilter{
	
	public boolean accept(File f){
		return f.isDirectory() || f.getAbsolutePath().endsWith(".htm") || 
				f.getAbsolutePath().endsWith(".html");
	}

	public String getDescription() {
		return "HTML file (*.htm, *.html)";
	}
}