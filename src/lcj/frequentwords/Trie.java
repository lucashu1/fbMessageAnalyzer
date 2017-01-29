package lcj.frequentwords;


public class Trie {

	TrieNode rootNode = new TrieNode();
	
	public int insert(String word){
		if (word.length() == 0) return 0;
		TrieNode node = rootNode;
		
		//Add characters
		for (int i = 0; i < word.length(); i++){
			
			if (!node.contains(word.charAt(i))){
				node.add(word.charAt(i));
			}
			node = node.children.get(word.charAt(i));
		}
		
		//Increment last character (location at complete word)
		node.incrementFrequency();
		return node.getFrequency();
		
	}
	
	public int freqOfWord(String word){
		if (word.length() == 0) return 0;
		TrieNode node = rootNode;
		
		for (int i = 0; i < word.length(); i++){
			if (!node.contains(word.charAt(i))){
				return 0;
			}
			node = node.children.get(word.charAt(i));
		}
		return node.getFrequency();
	}
	
	public int size(){
		return rootNode.size();
	}

	
}
