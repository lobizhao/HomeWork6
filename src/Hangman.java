import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Hangman {
	private List<String> wordFamily;

	private final List<Character> guessHistory = new ArrayList<>();

	private final int wordLength;

	public Hangman() {
		this("engDictionary.txt");
	}

	public Hangman(String filename) {
		try
		{
			wordFamily = Files.readAllLines(Path.of(filename));
		} catch (IOException e) {
			System.out.printf(
					"Couldn't read from the file %s. Verify that you have it in the right place and try running again.",
					filename);
			System.exit(0); // stop the program--no point in trying if you don't have a dictionary
		}

		// choose a random word first, which determine the word length
		int index = (int)(Math.random() * wordFamily.size());
		wordLength = wordFamily.get(index).length();
		wordFamily = wordFamily.stream().filter(w -> w.length() == wordLength).toList();
	}

	public void start() {
		int revealedMask = 0;
		while (Integer.bitCount(revealedMask) != wordLength) {
			printStatus(revealedMask);
			char c = getInput();
			int mask = getMaskWithMaxChoices(c);

			Solution getSlution = new Solution();
			wordFamily = wordFamily.stream().filter(w -> getSlution.getMask(w, c) == mask).toList();
			revealedMask |= mask;
		}
		printVictory();
	}


	private int getMaskWithMaxChoices(char c) {
		int ret = 0;
		int max = 0;
		var counter = new HashMap<Integer, Integer>();

		for (var word : wordFamily) {
			Solution getSlution = new Solution();

			int mask =getSlution.getMask(word, c);
			if (counter.containsKey(mask)) {
				counter.put(mask, counter.get(mask) + 1);
			} else {
				counter.put(mask, 1);
			}
		}

		for (var set : counter.entrySet()) {
			if (set.getValue() > max) {
				max = set.getValue();
				ret = set.getKey();
			} else if (set.getValue() == max) { // choose to reveal less letters
				if (Integer.bitCount(set.getKey()) < Integer.bitCount(ret)) {
					ret = set.getKey();
				}
			}
		}

		return  ret;
	}

	private char getInput() {
		var scanner = new Scanner(System.in);
		while (true) {
			System.out.print("Guess a letter: ");
			var input = scanner.next();
			if (input.length() != 1) {
				System.out.println("Please enter a single character");
				continue;
			}

			char c = input.charAt(0);
			if (c < 'a' || c > 'z') {
				System.out.println("Please enter a character between a-z");
				continue;
			}

			if (guessHistory.contains(c)) {
				System.out.println("You've already guessed that.");
				continue;
			}
			guessHistory.add(c);

			System.out.println();
			return c;
		}
	}

	private void printStatus(int revealedMask) {
		var word = wordFamily.get(0);
		System.out.print("Current status: ");
		for (int i = 0; i < word.length(); ++i) {
			if (((1 << i) & revealedMask) != 0) {
				System.out.print(word.charAt(i));
			} else {
				System.out.print('_');
			}
		}
		System.out.println();

		System.out.print("Guess history: ");
		for (var c : guessHistory) {
			System.out.print(c);
		}
		System.out.println();
	}
	private void printVictory() {
		System.out.printf("Congrats! The word was %s%n", wordFamily.get(0));
	}
}
