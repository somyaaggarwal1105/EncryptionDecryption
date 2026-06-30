import java.util.Scanner;

/**
 * Project 2: Basic Encryption & Decryption
 *
 * Goal:
 *   Implement a simple encryption and decryption technique.
 *
 * Key Requirements:
 *   - Encrypt user text using a basic logic (Caesar Cipher)
 *   - Decrypt the encrypted text
 *   - Display both encrypted and decrypted output
 *
 * Key Skills demonstrated:
 *   Encryption concepts, logic building, data protection basics
 */
public class EncryptionDecryption {

    // ANSI color codes for terminal output
    static final String RED    = "\u001B[31m";
    static final String GREEN  = "\u001B[32m";
    static final String CYAN   = "\u001B[36m";
    static final String YELLOW = "\u001B[33m";
    static final String BOLD   = "\u001B[1m";
    static final String RESET  = "\u001B[0m";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println(BOLD + CYAN);
        System.out.println("╔════════════════════════════════════════════╗");
        System.out.println("║   BASIC ENCRYPTION & DECRYPTION (CAESAR)    ║");
        System.out.println("╚════════════════════════════════════════════╝" + RESET);

        while (true) {
            System.out.print("\nEnter text to encrypt (or 'quit' to exit): ");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("quit")) {
                System.out.println(CYAN + "\nGoodbye! 👋" + RESET);
                break;
            }

            if (input.isEmpty()) {
                System.out.println(RED + "⚠ Input cannot be empty. Please try again." + RESET);
                continue;
            }

            int shift = readShiftKey(scanner);

            // ── Encrypt ──────────────────────────────────────────
            String encrypted = caesarEncrypt(input, shift);

            // ── Decrypt (to prove correctness / round-trip) ───────
            String decrypted = caesarDecrypt(encrypted, shift);

            // ── Display Results ────────────────────────────────────
            System.out.println("\n" + BOLD + "══════════════ RESULT ══════════════" + RESET);
            System.out.println("  Shift Key       : " + YELLOW + shift + RESET);
            System.out.println("  Original Text   : " + input);
            System.out.println("  Encrypted Text  : " + GREEN + encrypted + RESET);
            System.out.println("  Decrypted Text  : " + CYAN + decrypted + RESET);

            if (decrypted.equals(input)) {
                System.out.println(GREEN + "  ✔ Decryption matches the original text." + RESET);
            } else {
                System.out.println(RED + "  ✘ Decryption mismatch — please check the logic." + RESET);
            }
            System.out.println(BOLD + "═════════════════════════════════════" + RESET);
        }

        scanner.close();
    }

    /**
     * Reads and validates the shift key (encryption key) from the user.
     */
    static int readShiftKey(Scanner scanner) {
        while (true) {
            System.out.print("Enter shift key (1-25): ");
            String line = scanner.nextLine().trim();
            try {
                int shift = Integer.parseInt(line);
                if (shift >= 1 && shift <= 25) {
                    return shift;
                }
                System.out.println(RED + "⚠ Please enter a number between 1 and 25." + RESET);
            } catch (NumberFormatException e) {
                System.out.println(RED + "⚠ Invalid number. Please try again." + RESET);
            }
        }
    }

    /**
     * Encrypts text using the Caesar Cipher technique.
     * Each alphabetic character is shifted forward by 'shift' positions
     * in the alphabet (wrapping around from Z to A). Non-alphabetic
     * characters (numbers, spaces, punctuation) are left unchanged.
     */
    static String caesarEncrypt(String text, int shift) {
        StringBuilder result = new StringBuilder();

        for (char ch : text.toCharArray()) {
            if (Character.isUpperCase(ch)) {
                char encryptedChar = (char) ((ch - 'A' + shift) % 26 + 'A');
                result.append(encryptedChar);
            } else if (Character.isLowerCase(ch)) {
                char encryptedChar = (char) ((ch - 'a' + shift) % 26 + 'a');
                result.append(encryptedChar);
            } else {
                // Keep digits, spaces, and symbols unchanged
                result.append(ch);
            }
        }

        return result.toString();
    }

    /**
     * Decrypts text that was encrypted using the Caesar Cipher technique.
     * Shifts each alphabetic character backward by 'shift' positions
     * (the inverse operation of caesarEncrypt).
     */
    static String caesarDecrypt(String text, int shift) {
        // Decryption is simply encryption with the inverse shift
        int inverseShift = 26 - (shift % 26);
        return caesarEncrypt(text, inverseShift);
    }
}