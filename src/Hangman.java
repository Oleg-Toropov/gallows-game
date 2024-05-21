import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Hangman {
    static Scanner scanner = new Scanner(System.in);
    static List<Character> savedInputLetters;
    static List<Character> savedErrorLetters;

    public static void main(String[] args) {
        startGame();
    }

    public static void startGame() {
        while (beginOrEndGame()){
            String word = getRandomWord();
            assert word != null : "Ошибка при чтении файла: метод getRandomWord() вернул null";
            char[] hideWord = creatHideWord(word);
            savedInputLetters = new ArrayList<>();
            savedErrorLetters = new ArrayList<>();
            gameLoop(word, hideWord);
        }
    }

    public static void gameLoop(String word, char[] hideWord) {
        do {
            System.out.println();
            showHideWord(hideWord);
            getInputLetter();
            checkLetters(word, savedInputLetters, savedErrorLetters, hideWord);
            showGallows(savedErrorLetters);
            showErrorsLetter(savedErrorLetters);
            System.out.println("********************************************");
        } while (savedErrorLetters.size() < 6 && !checkWinnings(hideWord));

        showInfoForEndGameLoop(word, hideWord);
    }

    public static boolean beginOrEndGame() {
        System.out.println("Введите команду \"Старт\" для начала игры или \"Стоп\" для завершения!");

        do {
            String command = scanner.nextLine().toLowerCase().trim();
            if (command.equals("старт")) {
                return true;
            } else if (command.equals("стоп")) {
                return false;
            } else {
                System.out.println("Вы ввели несуществующую команду! \n" +
                        "Введите команду \"Cтарт\" или \"Cтоп\":");
            }
        } while (true);
    }

    public static String getRandomWord() {
        String filePath = "resources/words.txt";
        try {
            List<String> words = Files.readAllLines(Paths.get(filePath));
            Random random = new Random();
            return words.get(random.nextInt(words.size()));
        } catch (IOException e) {
            System.out.println("Ошибка при чтении файла: " + e.getMessage());
            return null; // Возвращаем null, если произошла ошибка
        }
    }

    public static char[] creatHideWord(String word) {
        char[] hideWord = new char[word.length()];
        Arrays.fill(hideWord, '_');
        return hideWord;
    }

    public static void getInputLetter() {
        System.out.println("Введите букву:");

        do {
            String input = scanner.nextLine().trim();
            if (input.matches("[А-Яа-я]")) {
                char letter = input.toUpperCase().charAt(0);
                if (!savedInputLetters.contains(letter)) {
                    savedInputLetters.add(letter);
                    return;
                } else {
                    System.out.println("Вы уже вводили данныю букву! Введите другую!");
                }
            } else {
                System.out.println("Вы допустили ошибку при вводе! Попробуйте еще раз! \n" +
                        "Введите одну букву русского алфавита:");
            }
        } while (true);
    }

    public static void checkLetters(String word, List<Character> savedInputLetters,
                                       List<Character> savedErrorLetters, char[] hideWord) {
        char currentLetter = savedInputLetters.get(savedInputLetters.size() - 1);
        boolean isError = true;
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) == currentLetter) {
                hideWord[i] = currentLetter;
                isError = false;
            }
        }

        if (isError) {
            savedErrorLetters.add(currentLetter);
        }
    }

    public static boolean checkWinnings(char[] hideWord) {
        for (char ch: hideWord) {
            if(ch == '_'){
              return false;
            }
        }
        return true;
    }

    public static void showHideWord(char[] hideWord) {
        StringBuilder hideWordStB = new StringBuilder();
        for (char ch: hideWord) {
            hideWordStB.append(ch).append(" ");
        }
        System.out.println("Слово: " + hideWordStB.toString());
    }

    public static void showErrorsLetter(List<Character> savedErrorLetters) {
        StringBuilder savedErrorLettersStB = new StringBuilder();
        for (char ch: savedErrorLetters) {
            savedErrorLettersStB.append(ch).append(" ");
        }
        System.out.println("Ошибки: (" + savedErrorLetters.size() + " из 6) " + savedErrorLettersStB.toString());
    }

    public static void showGallows(List<Character> savedErrorLetters) {
        int len = savedErrorLetters.size();
        String st1 = (len == 0) ? "  |" : "  |    O";
        String st2 = (len < 2) ? "  |" : (len == 2) ? "  |    |" : (len == 3) ? "  |    |~" : "  |   ~|~";
        String st3 = (len < 5) ? "  |" : (len == 5) ? "  |     \\" : "  |   / \\";

        System.out.println();
        System.out.println("  |----|");
        System.out.println(st1);
        System.out.println(st2);
        System.out.println(st3);
        System.out.println("__|______");
        System.out.println();
    }

    public static void showInfoForEndGameLoop(String word, char[] hideWord) {
        if(checkWinnings(hideWord)){
            System.out.println("Поздравляем вы победили! Ответ: " + word);
        } else {
            System.out.println("Вы проиграли! Ответ: " + word);
        }
    }
}