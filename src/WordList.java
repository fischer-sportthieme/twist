import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class WordList {
	private List<String> stringList;
	
	public WordList(List<String> stringList) {
		this.stringList = stringList;
	}
	
	/**
	 * 
	 * @param twistedWord A word that has been twisted
	 * @return Words that could be the original word before it was twisted
	 */
	public List<String> getFittingWords(String twistedWord) {
		char[] sortedCharacters = new char[twistedWord.length() - 2];
		twistedWord.getChars(1, twistedWord.length() - 1, sortedCharacters, 0);
		Arrays.sort(sortedCharacters); // now they are actually sorted
		String sortedRemainingCharacters = new String(sortedCharacters);
		
		List<String> fittingWords = new ArrayList<String>();
		boolean foundBeginning = false;
		
		for (String word : this.stringList) {
			if ( // first letter matches
				word.charAt(0) == twistedWord.charAt(0)
			) {
				if (!foundBeginning) {
					foundBeginning = true;
				}
				if ( // same length and last letter matches
					word.length() == twistedWord.length() &&
					word.charAt(word.length() - 1) == twistedWord.charAt(twistedWord.length() - 1)
				) {
					if (word.length() < 3) {
						// if the twistedWord is less than 3 characters long
						// then there is only one word that fits
						fittingWords.add(word);
						return fittingWords;
					}
					// sort the remaining characters since fitting words
					// will have the same sortedCharacters as the twistedWord
					char[] _sortedCharacters = new char[word.length() - 2];
					word.getChars(1, word.length() - 1, _sortedCharacters, 0);
					Arrays.sort(_sortedCharacters); // now they are actually sorted
					String _sortedRemainingCharacters = new String(_sortedCharacters);
					if ( // same letters between the first and last letter
						Objects.equals(_sortedRemainingCharacters, sortedRemainingCharacters)
					) {
						fittingWords.add(word);
					}
				}
			} else if (foundBeginning) {
				// no need to continue if we're past words that start with the correct letter
				break;
			}
		}
		
		return fittingWords;
	}
}
