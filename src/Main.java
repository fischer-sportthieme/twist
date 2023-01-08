import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {
	private static Scanner scan = new Scanner(System.in);
	
	private static String input(String prompt) {
		System.out.print(prompt);
		return scan.nextLine();
	}
	
	public static void twist(Optional<String> word) {
		if (word.isPresent()) {
			String twistedWord = twistWord(word.get());
			System.out.println(twistedWord);
			return;
		} else {
			System.out.printf("Word Twister (Exit with Ctrl + C)%n%n");
			try {
				while (true) {
					String _word = input("Enter a word: ");
					String twistedWord = twistWord(_word);
					System.out.println("Twisted: " + twistedWord);
				}
			} finally {
				scan.close();
			}
		}
	}
	
	public static String twistWord(String word) {
		IntStream chars = word.substring(1, word.length() - 1).chars();
		List<Character> charList = chars.mapToObj(e->(char)e).collect(Collectors.toList());
		Collections.shuffle(charList); // pseudo-randomly shuffle the characters
		
		StringBuilder sb = new StringBuilder();
		for (char ch : charList) sb.append(ch);
		return word.charAt(0) // first letter
				+ sb.toString() // letters in the middle
				+ word.charAt(word.length() - 1); // last letter
	}
	
	public static void untwist(Optional<String> twistedWord) {
		WordList wordList = new WordList(WordSource.getWordList());
		if (twistedWord.isPresent()) {
			List<String> untwistedWords = wordList.getFittingWords(twistedWord.get());
			printFittingWords(untwistedWords);
		} else {
			System.out.printf("Word Untwister (Exit with Ctrl + C)%n%n");
			try {
				while (true) {
					String _twistedWord = input("Enter a twisted word: ");
					System.out.println("I will try to untwist your word...");
					List<String> untwistedWords = wordList.getFittingWords(_twistedWord);
					printFittingWords(untwistedWords);
				}
			} finally {
				scan.close();
			}
		}
	}

	public static void printFittingWords(List<String> fittingWords) {
		if (fittingWords.size() == 0) {
			System.out.println("I could not untwist your word.");
		} else if (fittingWords.size() == 1) {
			System.out.println("Your untwisted word is: " + fittingWords.get(0));
		} else {
			System.out.println("I think your untwisted word is one of these:");
			System.out.println(String.join(System.lineSeparator(), fittingWords));
		}
	}
	
	public static void main(String[] args) {
		if (args.length == 0) {
			// twist by default
			twist(Optional.empty());
		} else if (Objects.equals(args[0], "twist")) {
			if (args.length > 1) {
				twist(Optional.of(args[1]));
			} else {
				twist(Optional.empty());
			}
		} else if (Objects.equals(args[0], "untwist")) {
			if (args.length > 1) {
				untwist(Optional.of(args[1]));
			} else {
				untwist(Optional.empty());
			}
		}
	}
}
