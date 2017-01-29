package lcj.frequentwords;

import java.util.ArrayList;

import javafx.scene.chart.XYChart.Data;

public class TopWordsList { //SORTED IN DESCENDING ORDER

	private ArrayList<String> topWords;
	private ArrayList<Integer> topFreq;
	private int numWords;
	
	public TopWordsList(int numWords){
		this.numWords = numWords;
		topWords = new ArrayList<String>(numWords);
		topFreq = new ArrayList<Integer>(numWords);
	}
	
	public void add(String word, int frequency){
		if (topWords.contains(word)){
			int ind = topWords.indexOf(word);
			int prevFreq = topFreq.get(ind);
			topFreq.set(ind, prevFreq + 1); //Increment frequency
			
			if (ind > 0){ //See if a swap is necessary to keep words sorted by frequency
				if (topFreq.get(ind) > topFreq.get(ind - 1)){
					String tempWord = topWords.get(ind - 1);
					Integer tempFreq = topFreq.get(ind - 1);
					
					topWords.set(ind - 1, topWords.get(ind));
					topFreq.set(ind - 1, topFreq.get(ind));
					
					topWords.set(ind, tempWord);
					topFreq.set(ind, tempFreq);
				}
			}
		} else {
			if (topWords.size() == 0){ //AL is empty
				topWords.add(word);
				topFreq.add(frequency);
			} else if (frequency > topFreq.get(topFreq.size() - 1)){ //If word belongs in AL, frequency above threshold
				if (topWords.size() > 0 && topFreq.size() < numWords){ //ALs aren't full
					for (int i = 0; i < topFreq.size(); i++){
						if (frequency > topFreq.get(i)){
							topWords.add(i, word);
							topFreq.add(i, frequency);
							break;
						}
					}
				} else { //ALs are full
					for (int i = 0; i < topFreq.size(); i++){
						if (frequency > topFreq.get(i)){
							topWords.add(i, word);
							topFreq.add(i, frequency);
							topWords.remove(numWords);
							topFreq.remove(numWords);
							break;
						}
					}
				}
			}
		}
	}
	
	public ArrayList<Data<String, Number>> getTopWordsData(){
		ArrayList<Data<String, Number>> data = new ArrayList<Data<String, Number>>(numWords);
		for (int i = 0; i < topWords.size(); i++){
			Data<String, Number> d = new Data<String, Number>(topWords.get(i), topFreq.get(i));
			data.add(d);
		}
		return data;
	}
	
}
