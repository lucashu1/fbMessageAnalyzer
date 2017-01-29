package lcj.fb;
//AUTHOR: Lucas Hu

//KEY METHODS (WORKING):
//getLargestConvo(), returns Conversation
//getLargestConvos(), returns ArrayList<Conversation>
//getDailyActivity(), returns HashMap<String, Integer>
//getHourlyActivity(), returns HashMap<Integer, Integer>
//getActivityOverTime(), returns HashMap<String, Integer>
//getWordFrequencyOverTime(String word), returns HashMap<String, Integer>

//PLANNED METHODS:
//getMostUsedWords(), returns HashMap<String, Integer>

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.DayOfWeek;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

import javafx.scene.chart.Axis;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import lcj.frequentwords.MostFrequentWordsFinder;


public class Inbox {
	
	
	
	private Scanner input, userScanner;
	private String userName;
	
	private HashMap<String, Conversation> convos = new HashMap<String, Conversation>();

	public Inbox(String filePath){
		try {
			input = new Scanner(new FileInputStream(filePath));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		parse();
//		System.out.println(getMostUsedWords(5).get(0).getXValue());
	}

	private void parse(){
		
		//HOW TO PARSE:
		//Go through code using conversation headers as delimiters
		//Be sure to do so in a way that you can retrieve the conversation name and pass it into the Converstaion constructor
		//As you go through, store each new convo by adding a Conversation object to AllConvos.allConvos ArrayList
		//Use convo text (html and all) as argument to constructor --> further parsing happens in Conversation constructor
		
		
		//Skip file header
		input.useDelimiter("<div class=\"contents\"><h1>");
		input.next();
		
		//Get user name
		input.useDelimiter("<div class=\"contents\"><h1>|</h1>");
		userName = input.next();
		
		//Parse out footer
		input.useDelimiter("</div></div></div><div class=\"footer\">");
		String html = input.next();
		input.close();
		
		//Parse for conversations
		Scanner convoScanner = new Scanner(html);
		
		convoScanner.useDelimiter("<div class=\"thread\">|</p></div><div class=\"thread\">");
		while(convoScanner.hasNext()){
			String content = convoScanner.next();
			Conversation c = new Conversation(userName, content);
			if (convos.containsKey(c.getName())){
				//If this conversation is a continuation of an existing convo, append it to the existing one
				convos.get(c.getName()).appendConvo(c);
			} else { 
				//If this conversation is a new one, add a new entry to the hashmap
				convos.putIfAbsent(c.getName(), c);
			}
		}
		
		convoScanner.close();
		
	}
	
	
	/** START DATA ANALYSIS **/
	
	public String getUsername(){
		return userName;
	}
	
	//0 = first name; 1 = last name
	public String getPartOfName(int firstOrLast){
		String fullName = getUsername();
		userScanner = new Scanner(fullName);
		userScanner.useDelimiter(" ");
		ArrayList<String> names = new ArrayList<String>(0);
		while (userScanner.hasNext()){
			names.add(userScanner.next());
		}
		userScanner.close();
		System.out.println();
		if(firstOrLast == 0){
			return names.get(0);
		}else{
			return names.get(names.size() - 1);
		}
	}
	
	public String getPartOfName(String name, int firstOrLast){
		String fullName = name;
		Scanner nameScanner = new Scanner(fullName);
		nameScanner.useDelimiter(" ");
		ArrayList<String> names = new ArrayList<String>(0);
		while (nameScanner.hasNext()){
			names.add(nameScanner.next());
		}
		nameScanner.close();
		System.out.println();
		if(firstOrLast == 0){
			return names.get(0);
		}else{
			return names.get(names.size() - 1);
		}
	}
	
	public int getAverageMessagesInConvo(){
		return numMessages()/numConvos();
	}
	
	public Conversation getLargestConvo(){
		
		Conversation mostChatted = null;
		int largestSize = 0;
		
		for (Map.Entry<String, Conversation> convo : convos.entrySet()) {
			
	        int convoSize = convo.getValue().getSize();
	        
	        if (convoSize > largestSize) {
	        	mostChatted = convo.getValue();
	        	largestSize = convoSize;
	        }
        }
	    
		return mostChatted;
	}
	
	private Conversation getLargestConvoInArrayList(ArrayList<Conversation> c){
		Conversation mostChatted = null;
		int largestSize = 0;
		
		for (int i = 0; i < c.size(); i++) {
			
	        int convoSize = c.get(i).getSize();
	        
	        if (convoSize > largestSize) {
	        	mostChatted = c.get(i);
	        	largestSize = convoSize;
	        }
        }
	    
		return mostChatted;
	}
	
	//Returns an ArrayList of your N largest conversations in descending order
	public ArrayList<Conversation> getLargestConvos(int numConvos){
		
		ArrayList<Conversation> topConvos = new ArrayList<Conversation>(numConvos);
		
		if (numConvos >= convos.size()){
			for (Map.Entry<String, Conversation> convo : convos.entrySet()){
				topConvos.add(convo.getValue());
			}
			return topConvos;
		} else if (numConvos <= 0){
			System.out.println("Input to getLargestConvos must be greater than 0!");
			return null;
		} else {
			
			ArrayList<Conversation> remainingConvos = new ArrayList<Conversation>();
			for (Map.Entry<String, Conversation> convo : convos.entrySet()){
				remainingConvos.add(convo.getValue());
			}
			
			//Similar to selection sort, but only iterates N times
			for (int i = 0; i < numConvos; i++){
				Conversation largestConvo = getLargestConvoInArrayList(remainingConvos);
				topConvos.add(largestConvo);
				remainingConvos.remove(largestConvo);
			}
			
			return topConvos;
				
		}
	}
	
	public int numMessages(){
		int size = 0;
		
		for (Map.Entry<String, Conversation> convo : convos.entrySet()) {
			size += convo.getValue().getSize();
        }
		
		return size;
	}
	
	public int numConvos(){
		return convos.size();
	}
	
	public boolean isConvo(String convoName){
		if(getConvo(convoName) == null){
			return false;
		}else{
			return true;
		}
	}
	
	public Conversation getConvo(String convoName){
		try {
			return convos.get(convoName);
		} catch (NullPointerException e) {
			System.out.println("Conversation not found!");
			return null;
		}
	}
	
	//Key: Day of week string (e.g. "Thursday")
	//Value: # of messages that the USER sent on that day
	public ArrayList<Data<String, Number>> getDailyActivity(){
		ArrayList<Data<String, Number>> dayData = new ArrayList<Data<String, Number>>(7);
		
		//Populate the ArrayList Monday (index 0) --> Sunday (index 6)
		for (int i = 1; i <= 7; i++){
			dayData.add(new Data<String, Number>(DayOfWeek.of(i).getDisplayName(TextStyle.FULL, Locale.US), 0));
		}
		
		for (Map.Entry<String, Conversation> convo : convos.entrySet()) {
			ArrayList<Message> messages = convo.getValue().getAllMessages();
			
			for (int i = 0; i < messages.size(); i++){
				
				Message m = messages.get(i);
				if (m.getSender().equals(userName)){
					Integer dayIndex = m.getDateTime().getDayOfWeek().getValue() - 1;
					Integer count = dayData.get(dayIndex).getYValue().intValue();
					count++;
					dayData.set(dayIndex, new Data<String, Number>(DayOfWeek.of(dayIndex + 1).getDisplayName(TextStyle.FULL, Locale.US), count));
				}
			}
        }
		
		//Move Sunday to beginning
		dayData.add(0, dayData.remove(6));
		
		return dayData;
	}
	
	//Key: Hour (0-23)
	//Value: # of messages that the USER sent in that hour
	public ArrayList<Data<String, Number>> getHourlyActivity(){
		ArrayList<Data<String, Number>> hourData = new ArrayList<Data<String, Number>>(24);
		
		//Populate the arraylist, pre-sorted
		for (Integer i = 0; i < 24; i++){
			hourData.add(new Data<String, Number>(i.toString(), 0));
		}
		
		for (Map.Entry<String, Conversation> convo : convos.entrySet()) {
			ArrayList<Message> messages = convo.getValue().getAllMessages();
			
			for (int i = 0; i < messages.size(); i++){
				
				Message m = messages.get(i);
				if (m.getSender().equals(userName)){
					Integer hr = m.getDateTime().getHour();
					Integer count = hourData.get(hr).getYValue().intValue();
					count++;
					hourData.set(hr, new Data<String, Number>(hr.toString(), count));
				}
			}
        }
		return hourData;
	}
	
	//String: month (in short form) + year (e.g. "Jan 2014")
	//Number: # messages the USER sent during that time period
	public ArrayList<Data<String, Number>> getActivityOverTime(){
		final DateTimeFormatter format = DateTimeFormatter.ofPattern("MMM yyyy");
		
		ArrayList<Data<String, Number>> monthData = new ArrayList<Data<String, Number>>();
		
		//Fill the series with the data
		for (Map.Entry<String, Conversation> convo : convos.entrySet()) {
			ArrayList<Message> messages = convo.getValue().getAllMessages();
			
			for (int i = 0; i < messages.size(); i++){
				Message m = messages.get(i);
				
				if (m.getSender().equals(userName)){
					YearMonth month = YearMonth.of(m.getDateTime().getYear(), m.getDateTime().getMonth());
					String monthStr = month.format(format);
					
					boolean containsMonth = false;
					for (Data<String, Number> datum : monthData){
						if (datum.getXValue().equals(monthStr)){
							datum.setYValue(datum.getYValue().intValue() + 1);
							containsMonth = true;
						}
					}
					if (!containsMonth){
						monthData.add(new Data<String, Number>(monthStr, 1));
					}
				}
			}
        }
		
		//Sort the series
		monthData.sort(yearMonthComp);
		
		//Fill the series's empty spots
		int i = 0;
		while (i < monthData.size() - 1){
			YearMonth currentYM = YearMonth.parse(monthData.get(i).getXValue(), format);
			YearMonth nextYM = YearMonth.parse(monthData.get(i + 1).getXValue(), format);
			if(!nextYM.equals(currentYM.plusMonths(1))){
				YearMonth insertYM = currentYM.plusMonths(1);
				String insertYMString = insertYM.format(format);
				Data<String, Number> insertYMData = new Data<String, Number>(insertYMString, 0);
				monthData.add(i + 1, insertYMData);
			}
			i++;
		}
		
		return monthData;
	}
	
	
	//Key: String with month (in short form) and year (e.g. "Jul 2014")
	//Value: Number of times the word parameter is found within that period
	public ArrayList<Data<String, Number>> getWordFrequencyOverTime(String word){
		
		word = word.toLowerCase().replace("'", "");
		
		final DateTimeFormatter format = DateTimeFormatter.ofPattern("MMM yyyy");
		
		ArrayList<Data<String, Number>> monthData = new ArrayList<Data<String, Number>>();
		
		//Fill the series with the data
		for (Map.Entry<String, Conversation> convo : convos.entrySet()) {
			ArrayList<Message> messages = convo.getValue().getAllMessages();
			
			for (int i = 0; i < messages.size(); i++){
				Message m = messages.get(i);
				//Not case sensitive, ignores apostrophes
				String text = m.getMessage().toLowerCase().replace("'", "");
				
				if (m.getSender().equals(userName) && text.contains(word)){
					
					YearMonth month = YearMonth.of(m.getDateTime().getYear(), m.getDateTime().getMonth());
					String monthStr = month.format(format);
					
					boolean containsMonth = false;
					for (Data<String, Number> datum : monthData){
						if (datum.getXValue().equals(monthStr)){
							int withWordLength = text.length();
							int withoutWordLength = text.replace(word, "").length();
							int occurrences = (withWordLength - withoutWordLength) / word.length();
							datum.setYValue(datum.getYValue().intValue() + occurrences);
							containsMonth = true;
							break;
						}
					}
					if (!containsMonth){
						int withWordLength = text.length();
						int withoutWordLength = text.replace(word, "").length();
						int occurrences = (withWordLength - withoutWordLength) / word.length();
						monthData.add(new Data<String, Number>(monthStr, occurrences));
					}
				}
			}
        }
		
		//Sort the series
		monthData.sort(yearMonthComp);
		
		//Fill the series's empty spots
		int i = 0;
		while (i < monthData.size() - 1){
			YearMonth currentYM = YearMonth.parse(monthData.get(i).getXValue(), format);
			YearMonth nextYM = YearMonth.parse(monthData.get(i + 1).getXValue(), format);
			if(!nextYM.equals(currentYM.plusMonths(1))){
				YearMonth insertYM = currentYM.plusMonths(1);
				String insertYMString = insertYM.format(format);
				Data<String, Number> insertYMData = new Data<String, Number>(insertYMString, 0);
				monthData.add(i + 1, insertYMData);
			}
			i++;
		}
		
		return monthData;
	}
	
	//PARAMETERS:
		//n: how many words to find
	//String: word
	//Number: frequency
	public ArrayList<Data<String, Number>> getMostUsedWords(int n){ //NO BLACKLIST --> ALL WORDS ARE FAIR GAME
		MostFrequentWordsFinder fw = new MostFrequentWordsFinder(n, convos, userName);
		return fw.getTopWords();
	}
	
	//PARAMETERS:
		//n: how many words to find
		//blacklist: words to ignore
	//String: word
	//Number: frequency
		//E.g. to get top 10 words excluding "the" and "a", do this:
		//ArrayList<String> blacklist = new ArrayList<String>();
		//blacklist.add("the");
		//blacklist.add("a");
		//ArrayList<Data<String, Number>> topWords = getMostUsedWords(10, blacklist);
	public ArrayList<Data<String, Number>> getMostUsedWords(int n, ArrayList<String> blacklist){ //WITH BLACKLIST
		MostFrequentWordsFinder fw = new MostFrequentWordsFinder(n, blacklist, convos, userName);
		return fw.getTopWords();
	}
	
	public BarChart<String, Number> createChartFromAL(ArrayList<Data<String, Number>> data, String yAxisLabel, String xAxisLabel, String chartTitle){
		CategoryAxis xAxis = new CategoryAxis();
		xAxis.setLabel(xAxisLabel);
		Axis<Number> yAxis = new NumberAxis();
		yAxis.setLabel(yAxisLabel);
		BarChart<String, Number> barChart = new BarChart<String, Number>(xAxis, yAxis);
		XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();
		barChart.setTitle(chartTitle);
		
		for(Data<String, Number> bar : data){
			XYChart.Data<String, Number> dataPoint = new XYChart.Data<>();
			dataPoint = createDataPoint(bar);
			series.getData().add(dataPoint);
		}
		barChart.getData().add(series);
		
		barChart.setLegendVisible(false);
		return barChart;
	}
	
	
	public XYChart.Data<String, Number> createDataPoint(Data<String, Number> data){
		XYChart.Data<String, Number> dataPoint = new Data<String, Number>();
		dataPoint.setXValue(data.getXValue());
		dataPoint.setYValue(data.getYValue());
		return dataPoint;
	}
	
	public static Comparator<Data<String, Number>> yearMonthComp = new Comparator<Data<String, Number>>(){
		final DateTimeFormatter format = DateTimeFormatter.ofPattern("MMM yyyy");
		
		public int compare(Data<String, Number> data1, Data<String, Number> data2){
			YearMonth ym1 = YearMonth.parse(data1.getXValue(), format);
			YearMonth ym2 = YearMonth.parse(data2.getXValue(), format);
			return ym1.compareTo(ym2);
		}
		
	};
}

