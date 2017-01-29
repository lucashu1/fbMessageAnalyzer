package lcj.fb;

public class ParseRunnable implements Runnable{
	public void run(){
		try{
			Display.initParse();
		} catch (InterruptedException e){
			System.out.println("Parsing interrupted!");
		}
	}
	public static void main(String args[]){
		(new Thread(new ParseRunnable())).start();
	}
}