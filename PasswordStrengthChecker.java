import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PasswordStrengthChecker {

    // ANSI color codes for terminal output
    static final String RED    = "\u001B[31m";
    static final String YELLOW = "\u001B[33m";
    static final String GREEN  = "\u001B[32m";
    static final String RESET  = "\u001B[0m";
    static final String BOLD   = "\u001B[1m";
    static final String CYAN   = "\u001B[36m";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println(BOLD + CYAN);
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║      PASSWORD STRENGTH CHECKER       ║");
        System.out.println("╚══════════════════════════════════════╝" + RESET);

        while (true) {
            System.out.print("\nEnter a password to check (or 'quit' to exit): ");
            String password = scanner.nextLine();

            if (password.equalsIgnoreCase("quit")) {
                System.out.println(CYAN + "\nStay secure! Goodbye. 👋" + RESET);
                break;
            }

            if (password.isEmpty()) {
                System.out.println(RED + "⚠ Password cannot be empty. Please try again." + RESET);
                continue;
            }

            analyzePassword(password);
        }

        scanner.close();
    }

    static void analyzePassword(String password) {
        // ── Checks ──────────────────────────────────────────────
        int length         = password.length();
        boolean hasUpper   = password.chars().anyMatch(Character::isUpperCase);
        boolean hasLower   = password.chars().anyMatch(Character::isLowerCase);
        boolean hasDigit   = password.chars().anyMatch(Character::isDigit);
        boolean hasSpecial = password.chars().anyMatch(c ->
                "!@#$%^&*()_+-=[]{}|;':\",./<>?`~\\".indexOf(c) >= 0);

        // ── Score ────────────────────────────────────────────────
        int score = 0;
        if (length >= 8)  score++;
        if (length >= 12) score++;
        if (length >= 16) score++;
        if (hasUpper)     score++;
        if (hasLower)     score++;
        if (hasDigit)     score++;
        if (hasSpecial)   score++;

        // ── Suggestions ──────────────────────────────────────────
        List<String> suggestions = new ArrayList<>();
        if (length < 8)
            suggestions.add("❌ Use at least 8 characters (currently " + length + ")");
        else if (length < 12)
            suggestions.add("⚠ Consider using 12+ characters for better security");
        if (!hasUpper)
            suggestions.add("❌ Add at least one UPPERCASE letter (A-Z)");
        if (!hasLower)
            suggestions.add("❌ Add at least one lowercase letter (a-z)");
        if (!hasDigit)
            suggestions.add("❌ Add at least one number (0-9)");
        if (!hasSpecial)
            suggestions.add("❌ Add at least one special character (!@#$%^&* etc.)");

        // ── Strength Classification ───────────────────────────────
        String strength;
        String color;
        String emoji;
        String bar;

        if (score <= 3) {
            strength = "WEAK";
            color    = RED;
            emoji    = "🔴";
            bar      = RED + "██░░░░░░░░" + RESET;
        } else if (score <= 5) {
            strength = "MEDIUM";
            color    = YELLOW;
            emoji    = "🟡";
            bar      = YELLOW + "█████░░░░░" + RESET;
        } else {
            strength = "STRONG";
            color    = GREEN;
            emoji    = "🟢";
            bar      = GREEN + "██████████" + RESET;
        }

        // ── Output ───────────────────────────────────────────────
        System.out.println("\n" + BOLD + "══════════════ ANALYSIS RESULT ══════════════" + RESET);
        System.out.println("  Password  : " + "*".repeat(length) + " (" + length + " chars)");
        System.out.println("  Strength  : " + color + BOLD + emoji + " " + strength + RESET);
        System.out.println("  Score     : " + color + score + "/7" + RESET);
        System.out.println("  Meter     : " + bar);

        System.out.println("\n" + BOLD + "──────── Criteria Check ────────" + RESET);
        System.out.println("  Length ≥ 8   : " + tick(length >= 8));
        System.out.println("  Length ≥ 12  : " + tick(length >= 12));
        System.out.println("  Uppercase    : " + tick(hasUpper));
        System.out.println("  Lowercase    : " + tick(hasLower));
        System.out.println("  Numbers      : " + tick(hasDigit));
        System.out.println("  Special Char : " + tick(hasSpecial));

        if (!suggestions.isEmpty()) {
            System.out.println("\n" + BOLD + "──────── Suggestions ────────" + RESET);
            for (String s : suggestions) {
                System.out.println("  " + s);
            }
        } else {
            System.out.println(GREEN + "\n  ✅ Excellent! Your password meets all criteria." + RESET);
        }

        System.out.println(BOLD + "═════════════════════════════════════════════" + RESET);
    }

    // Helper to show tick or cross
    static String tick(boolean condition) {
        return condition ? GREEN + "✔ Yes" + RESET : RED + "✘ No" + RESET;
    }
}









