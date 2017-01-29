package lcj.fb;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

class HelpGuide extends JDialog implements ActionListener{
	private static final long serialVersionUID = -8323436335939805077L;
	private JPanel step1, step2, step3, step4, step5, step6, step7;
	private GridBagConstraints cText, cPic, cCButton, c;
	private JPanel cards, background;
	private final Icon pressArrow = new ImageIcon(getClass().getResource("/res/pressArrow.png"));
	private final Icon chooseSettings = new ImageIcon(getClass().getResource("/res/chooseSettings.png"));
	private final Icon clickDownload = new ImageIcon(getClass().getResource("/res/clickDownload.png"));
	private final Icon clickStart = new ImageIcon(getClass().getResource("/res/clickStart.png"));
	private final Icon clickDownloadArchive = new ImageIcon(getClass().getResource("/res/clickDownloadArchive.png"));
	private final Icon unzip = new ImageIcon(getClass().getResource("/res/unzip.png"));
	private final Icon loacateMessages = new ImageIcon(getClass().getResource("/res/locateMessages.png"));
	private JLabel l1, l2, l3, l4, l5, l6, l7;
	private JButton continueButton;
	private JButton backButton;
	private int currentStep=0;
	private String[] steps = {"1", "2", "3", "4", "5", "6", "7"};

	public HelpGuide(){
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setBackground(Color.white);
		this.setResizable(false);
		initCards();
	}
	
	private void initCards(){
		init();
		
		c = new GridBagConstraints();
		background = new JPanel(new GridBagLayout());
		c.gridx=2;
		c.anchor = GridBagConstraints.LINE_START;
		background.add(continueButton,c);
		
		c.gridx = 0;
		c.anchor = GridBagConstraints.LINE_END;
		background.add(backButton, c);
		backButton.setEnabled(false);
		
		cards = new JPanel(new CardLayout());
		cards.setBackground(Color.white);
		cards.add(step1, "1");
		cards.add(step2, "2");
		cards.add(step3, "3");
		cards.add(step4, "4");
		cards.add(step5, "5");
		cards.add(step6, "6");
		cards.add(step7, "7");
		
		c.gridx=1;
		background.add(cards, c);
		background.setBackground(Color.white);
		this.add(background);
	}
	
	private void init(){
		this.setLayout(new GridBagLayout());
		this.setPreferredSize(new Dimension(800, 400));
		this.setBackground(Color.white);
		
		cText = new GridBagConstraints();
		cPic = new GridBagConstraints();
		cCButton = new GridBagConstraints();
		cText.gridy=0;cText.gridx=0;
		cText.insets = new Insets(0, 0, 30, 0);
		cPic.gridy=1; cPic.gridx=0;
		cCButton.gridy=1; cCButton.gridx=1;
		
		continueButton = new JButton("Continue");
		continueButton.addActionListener(this);
		continueButton.setActionCommand(""+currentStep++);
		
		backButton = new JButton("Back");
		backButton.addActionListener(this);
		backButton.setActionCommand(""+currentStep--);
		
		step1 = new JPanel(); step2 = new JPanel(); step3 = new JPanel(); step4 = new JPanel();
		step5 = new JPanel(); step6 = new JPanel(); step7 = new JPanel(); 
		step1.setLayout(new GridBagLayout()); step2.setLayout(new GridBagLayout());
		step3.setLayout(new GridBagLayout()); step4.setLayout(new GridBagLayout());
		step5.setLayout(new GridBagLayout()); step6.setLayout(new GridBagLayout());
		step7.setLayout(new GridBagLayout());
		step1.setBackground(Color.white); step2.setBackground(Color.white); 
		step3.setBackground(Color.white); step4.setBackground(Color.white); 
		step5.setBackground(Color.white); step6.setBackground(Color.white); 
		step7.setBackground(Color.white); 
		
		l1 = new JLabel("Go to your Facebook and press the arrow in the top menu.");
		l1.setFont(l1.getFont().deriveFont(20.0f));
		l1.setForeground(Display.fbBlue);
		step1.add(l1, cText);
		JLabel icon1 = new JLabel(pressArrow);
		step1.add(icon1, cPic);
		
		l2 = new JLabel("Click on \"Settings\".");
		l2.setFont(l1.getFont());
		l2.setForeground(Display.fbBlue);
		step2.add(l2, cText);
		JLabel icon2 = new JLabel(chooseSettings);
		step2.add(icon2, cPic);
		
		l3 = new JLabel("Click \"Download a copy\".");
		l3.setFont(l1.getFont());
		l3.setForeground(Display.fbBlue);
		step3.add(l3, cText);
		JLabel icon3 = new JLabel(clickDownload);
		step3.add(icon3, cPic);
		
		l4 = new JLabel("Click \"Start My Archive\" then wait about half an hour for an email.");
		l4.setFont(l1.getFont());
		l4.setForeground(Display.fbBlue);
		step4.add(l4, cText);
		JLabel icon4 = new JLabel(clickStart);
		step4.add(icon4, cPic);
		
		l5 = new JLabel("Follow the link from the email and click \"Download Archive\"");
		l5.setFont(l1.getFont());
		l5.setForeground(Display.fbBlue);
		step5.add(l5, cText);
		JLabel icon5 = new JLabel(clickDownloadArchive);
		step5.add(icon5, cPic);
		
		l6 = new JLabel("Unzip the file you downloaded.");
		l6.setFont(l1.getFont());
		l6.setForeground(Display.fbBlue);
		step6.add(l6, cText);
		JLabel icon6 = new JLabel(unzip);
		step6.add(icon6, cPic);
		
		l7 = new JLabel("Locate and use \"Messages.htm\"");
		l7.setFont(l1.getFont());
		l7.setForeground(Display.fbBlue);
		step7.add(l7, cText);
		JLabel icon7 = new JLabel(loacateMessages);
		step7.add(icon7, cPic);
		
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(continueButton)){
			currentStep++;
			CardLayout c = (CardLayout) cards.getLayout();
			c.show(cards, steps[currentStep]);
		} else {
			currentStep--;
			CardLayout c = (CardLayout) cards.getLayout();
			c.show(cards, steps[currentStep]);
		}
		
		if (currentStep < 1){
			backButton.setEnabled(false);
		} else {
			backButton.setEnabled(true);
		}
		
		if (currentStep > 5){
			continueButton.setEnabled(false);
		} else {
			continueButton.setEnabled(true);
		}
	}
}