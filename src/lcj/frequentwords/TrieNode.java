package lcj.frequentwords;

import java.util.HashMap;
import java.util.Map.Entry;

public class TrieNode {

	private int frequency = 0;
	public HashMap<Character, TrieNode> children = new HashMap<Character, TrieNode>();
	
	public int getFrequency() {return frequency;}
	public void incrementFrequency() {frequency++;}
	
	public TrieNode getChild(Character c) {return children.get(c);}
	public void setChild(Character c) {
		children.putIfAbsent(c, new TrieNode());
	}
	
	public void add(Character c){
		children.put(c, new TrieNode());
	}
	
	public boolean contains(Character c){
		return children.containsKey(c);
	}
	
	public int size(){
		if (children.isEmpty()){
			return 0;
		} else {
			int totalChildrenSize = 0;
			for (Entry<Character, TrieNode> entry : children.entrySet()){
				totalChildrenSize += entry.getValue().size();
			}
			return totalChildrenSize + 1;
		}
	}
	
}
