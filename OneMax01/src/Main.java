import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
    public static boolean checkPalindrome(String input) {
        int[] letters = new int[26];
        for (int i = 0; i < 26; i++) letters[i] = 0;
        int countOnes = 0;
        for (char c: input.toCharArray()) {
            if (!Character.isAlphabetic(c)) continue; // с пробелом будут ошибки
            int letter = Character.toLowerCase(c) - 'a';
            letters[letter]++;
            if (letters[letter] % 2 == 1) countOnes++;
            else countOnes--;
        }
        return countOnes <= 1;
    }


    public static void main(String[] args) {
        System.out.println(checkPalindrome("Tact Coa"));
    }

}
