package lcj.frequentwords;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javafx.scene.chart.XYChart.Data;
import lcj.fb.Conversation;
import lcj.fb.Message;

public class MostFrequentWordsFinder {

	public static final String[] DEFAULT_BLACKLIST = {
		"the","be","to","of","and","a","in","that","have","I","it","for","not","on","with","he","as","you","do","at","this","but",
		"his","by","from","they","we","say","her","she","or","an","will","my","one","all","would","there","their","what","so",
		"out","if","about","who","get","which","go","me","when","make","can","like","time","no","just","him","know",
		"take","people","into","your","good","some","could","them","see","other","than","then","now",
		"only","come","its","over","think","also","back","is","use","two","how","our","work","first","well",
		"way","even","new","want","because","any","these","give","day","most","us", "i", "I'll", "too", "should", "were",
		"was","are", "did", "that's", "got", "I'm", "up", "though"
	};
	
	public Trie trie = new Trie();
	private TopWordsList topWords;
	private final String userName;
	
	public MostFrequentWordsFinder(int numWords, HashMap<String, Conversation> convos, String userName){ //NO BLACKLIST
		this.userName = userName;
		topWords = new TopWordsList(numWords);
		buildTrie(numWords, convos);
	}
	
	public MostFrequentWordsFinder(int numWords, ArrayList<String> blacklist, HashMap<String, Conversation> convos, String userName){ //BLACKLIST
		this.userName = userName;
		topWords = new TopWordsList(numWords);
		
		ArrayList<String> newBL = new ArrayList<String>(blacklist.size()); 
		
		for (String word : blacklist){ //Get only lowercase letters
			newBL.add(word.replaceAll("[^a-zA-Z ]", "").replace(" ", "").toLowerCase());
		}
		
		buildTrie(numWords, newBL, convos);
	}
	
	private void buildTrie(int numWords, HashMap<String, Conversation> convos){ //NO BLACKLIST
		for (Map.Entry<String, Conversation> convo : convos.entrySet()) {
			ArrayList<Message> messages = convo.getValue().getAllMessages();
			
			for (int i = 0; i < messages.size(); i++){
				Message m = messages.get(i);
				
				if (m.getSender().equals(userName)){
					String text = m.getMessage();
					
					Scanner s = new Scanner(text);
					s.useDelimiter(" ");
					
					while (s.hasNext()){
						String word = s.next().replaceAll("[^a-zA-Z ]", "").toLowerCase();
						int frequency = trie.insert(word);
						topWords.add(word, frequency);
					}
					s.close();
				}
				
			}
        }
	}
	
	private void buildTrie(int numWords, ArrayList<String> blackList, HashMap<String, Conversation> convos){ //BLACKLIST
		for (Map.Entry<String, Conversation> convo : convos.entrySet()) {
			ArrayList<Message> messages = convo.getValue().getAllMessages();
			
			for (int i = 0; i < messages.size(); i++){
				Message m = messages.get(i);
				
				if (m.getSender().equals(userName)){
					String text = m.getMessage();
					
					Scanner s = new Scanner(text);
					s.useDelimiter(" ");
					
					while (s.hasNext()){
						String word = s.next().replaceAll("[^a-zA-Z ]", "").toLowerCase();
						
						if (!blackList.contains(word)){ //If word isn't blacklisted
							int frequency = trie.insert(word);
							topWords.add(word, frequency);
						}
					}
					s.close();
				}
				
			}
        }
	}
	
	public ArrayList<Data<String, Number>> getTopWords(){
		return topWords.getTopWordsData();
	}
	
}
