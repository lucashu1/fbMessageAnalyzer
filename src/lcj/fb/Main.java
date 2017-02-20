package lcj.fb;

import java.io.File;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

/**
 * MADE BY CHRIS DAW AND LUCAS HU OF GUNN HIGH SCHOOL
 * COPYRIGHT 2032
 * ALL RIGHTS RESTRICTED
 * WE ACCEPT DONATIONS OF CASH AND/OR TRIDENT LAYERS GUM
 * AND/OR A PARTY BUS
 * */

public class Main {

    public static File messagesFile;
    
    public static void main(String args[]) {
    	 javax.swing.SwingUtilities.invokeLater(new Runnable() {
             public void run() {
                 Display.createAndShowFileSelection();
             }
         });
    	 
    	 try {
    	     for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
    	         if ("Windows".equals(info.getName())) {
    	             UIManager.setLookAndFeel(info.getClassName());
    	             break;
    	         }else if("Macintosh".equals(info.getName())){
    	        	 UIManager.setLookAndFeel(info.getClassName());
    	        	 break;
    	         }
    	     }
    	 } catch (Exception e) {
    		 System.out.println("THERE WAS A PROBLEM: " + e);
    	 }
		
    }
}
