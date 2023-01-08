import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {
	final static String OS = System.getProperty("os.name");
	private static Scanner scan = new Scanner(System.in);
	
	private static String input() {
		return scan.nextLine();
	}
	
	private static String input(String prompt) {
		System.out.print(prompt);
		return scan.nextLine();
	}
	
	private static void clearScreen() {
		// this only clears the screen on
		// terminals where ANSI sequences
		// are allowed to be destructive
		if (isWindows() && !isEclipseConsole()) {
			try {
				Runtime.getRuntime().exec("cls");
			} catch (IOException e) {}
		} else {
			// sadly this doesn't work in Eclipse due to a bug
			if (isEclipseConsole()) return;
			System.out.printf("\033[H\033[2J");
			System.out.flush();
		}
	}
	
	private static boolean isWindows() {
		return OS.contains("Windows");
	}
	
	private static boolean isEclipseConsole() {
		Optional<ProcessHandle> parentProcess = ProcessHandle.current().parent();
		if (!parentProcess.isPresent()) return false;
		ProcessHandle actualParentProcess = parentProcess.get();
		Optional<String> command = actualParentProcess.info().command();
		if (!command.isPresent()) return false;
		String actualCommand = command.get();
		return actualCommand.contains("eclipse");
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
					if (_word.length() < 2) {
						System.out.println("That doesn't look like a word to me...");
						return;
					}
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
					if (_twistedWord.length() < 2) {
						System.out.println("That doesn't look like a word to me...");
						return;
					}
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
		// parsing args
		String mode = "";
		String word = "";
		
		if (args.length >= 1) {
			mode = args[0];
			if (args.length >= 2) {
				word = args[1];
			}
		}
		
		if (Objects.equals(mode, "")) {
			String temp = "";
			char _mode = 0;
			
			while (true) {
				System.out.printf("Choose a mode by pressing a key:%n%n");
				System.out.println("Press T for Twisting, press U for Untwisting.");
				System.out.printf("Confirm with Enter.%n%n");

				temp = input();

				if (Objects.equals(temp, "")) continue;
				_mode = temp.charAt(0);
				
				switch (_mode) {
					case 't':
						mode = "twist";
						break;
					case 'T':
						mode = "twist";
						break;
					case 'u':
						mode = "untwist";
						break;
					case 'U':
						mode = "untwist";
						break;
					default:
						continue;
				}
				break;
			}
			System.out.println();
			clearScreen();
		}
		
		if (Objects.equals(mode, "twist")) {
			if (!Objects.equals(word, "")) {
				twist(Optional.of(word));
			} else {
				twist(Optional.empty());
			}
		} else if (Objects.equals(mode, "untwist")) {
			if (!Objects.equals(word, "")) {
				untwist(Optional.of(word));
			} else {
				untwist(Optional.empty());
			}
		}
	}
}
