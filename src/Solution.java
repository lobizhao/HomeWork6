import java.util.ArrayList;

public class Solution {
	public int getMask(String word, char c){
		int mask = 0;
		for(int i = 0; i< word.length(); i++){
			if(word.charAt(i) == c){
				mask |= 1<<i;
			}
		}
		return mask;
	}

}
