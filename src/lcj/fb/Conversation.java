package lcj.fb;

import java.util.ArrayList;
import java.util.Scanner;


public class Conversation {
	private static int consoleLoadingBarCounter=0;
	private String name;
	private String username;
	
	private ArrayList<Message> messages = new ArrayList<Message>();
	
	public Conversation(String username, String content){
		this.username = username;
		convoParse(content);
		//this is just for making sure that the parsing is doing something and
		//that I didn't break anything with the GUI
		consoleLoadingBarCounter +=1;
		if((consoleLoadingBarCounter%5) == 0){
			System.out.print("|");
		}
	}
	
	private void convoParse(String content) {
		
		Scanner input = new Scanner(content);
		input.useDelimiter("<div class=\"message\"><div class=\"message_header\">|<div class=\"message\"><div class=\"message_header\">");
		
		name = parseName(input.next());
		
		while (input.hasNext()){
			String messageContent = input.next();
			Message m = new Message(name, messageContent);
			messages.add(0, m);
		}
		
//		messages.sort(messageTimeSort);
		
		input.close();
	}
	
	//Convos in HTM file are split into chunks of 10,000 messages
		//E.g. there are 5 conversations between Jer and Nina, each with 10K messages
	//If the scanner comes across a continuation of a convo, use this method to append the continuation to the existing ArrayList
	public void appendConvo(Conversation convo){
		messages.addAll(convo.messages);
	}
	
	private String parseName(String names){
		return names.replace(username + ", ", "").replace(", " + username, "");
	}
	
	public String getName() { 
		return name; 
	}
	
	//supposed to return everybody in the conversation minus the user's name
	//because they will be in everything.
	public String getPersonChattedWith(){
		return getName().replace(username + ", ", "").replace(", " + username, "");
	}
	
	public int getSize(){
		return messages.size();
	}
	
	public ArrayList<Message> getAllMessages(){
		return messages;
	}
	
	public Message getMessage(int i){
		try {
			return messages.get(i);
		} catch (IndexOutOfBoundsException e){
			System.out.println("Specified index out of array bounds!");
			return null;
		}
	}
	
	
}
