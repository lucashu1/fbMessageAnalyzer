package lcj.fb;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;


public class Message {

	private static final DateTimeFormatter format = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy 'at' h:mma z");
	//E.g. Wednesday, December 12, 2012 at 11:23pm PST
	
	private String convo;
	private String sender;
	private ZonedDateTime dateTime;
	private String message;
	
	public Message(String conversation, String content){
		parseMessage(conversation, content);
	}
	
	public void parseMessage(String conversation, String content){
		//Parse input passed from Conversation class
		//Remove final html tags
		//Store stuff in private fields
		
		convo = conversation;
		Scanner input = new Scanner(content);
		
		String delimiters = "<span class=\"user\">|</span><span class=\"meta\">|</span></div></div>|</p>";
		input.useDelimiter(delimiters);
		
		sender = input.next();
		parseDate(input.next().replace("pm", "PM").replace("am", "AM"));
		message = input.next();
		
		input.close();
		
		message = message.replaceFirst("<p>", ""); 
		//Scanner breaks for some reason when I add <p> to the delimiters, so this is a rough workaround
		
		//Parse out other html stuff
		message = message.replace("&#039;", "'").replace("&quot;", "\"").replace("&amp;", "&").replace("&#123;", "{").replace("&#125;", "}").replace("&#124;", "|").replace("&#126;", "~").replace("&#064;", "@");
	}
	
	private void parseDate(String dateStr){
		try {
			dateTime = ZonedDateTime.parse(dateStr, format);
			dateStr = dateTime.format(format);
		} catch (DateTimeParseException e) {
			System.out.println("Input could not be parsed as a Date!");
		}
	}
	
	public String getConvo(){
		return convo;
	}
	
	public String getSender(){
		return sender;
	}
	
	public String getDateString(){
		return dateTime.format(format);
	}
	
	public ZonedDateTime getDateTime(){
		return dateTime;
	}
	
	public String getMessage(){
		return message;
	}
	
	public void printMessage(){
		System.out.println("Conversation: " + convo);
		System.out.println("Sender: " + sender);
		System.out.println("Date/Time: " + getDateString());
		System.out.println("Content: " + message);
	}
	
	public String toString(){
		return getMessage();
	}
	
}
