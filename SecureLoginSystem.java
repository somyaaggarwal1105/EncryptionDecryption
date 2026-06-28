import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class SecureLoginSystem {

    // ── ANSI Colors ──────────────────────────────────────────────
    static final String RED    = "\u001B[31m";
    static final String GREEN  = "\u001B[32m";
    static final String YELLOW = "\u001B[33m";
    static final String CYAN   = "\u001B[36m";
    static final String BOLD   = "\u001B[1m";
    static final String RESET  = "\u001B[0m";

    // ── Constants ─────────────────────────────────────────────────
    static final int MAX_LOGIN_ATTEMPTS = 3;
    static final int LOCKOUT_DURATION_MS = 30000; // 30 seconds

    // ── User Data Storage ─────────────────────────────────────────
    // Key: username, Value: UserRecord
    static Map<String, UserRecord> userDatabase = new HashMap<>();

    // ── UserRecord Inner Class ────────────────────────────────────
    static class UserRecord {
        String username;
        String hashedPassword;
        String salt;
        int failedAttempts;
        long lockoutTime;

        UserRecord(String username, String hashedPassword, String salt) {
            this.username       = username;
            this.hashedPassword = hashedPassword;
            this.salt           = salt;
            this.failedAttempts = 0;
            this.lockoutTime    = 0;
        }

        boolean isLocked() {
            if (failedAttempts >= MAX_LOGIN_ATTEMPTS) {
                long elapsed = System.currentTimeMillis() - lockoutTime;
                if (elapsed < LOCKOUT_DURATION_MS) return true;
                // Lockout expired — reset
                failedAttempts = 0;
                lockoutTime    = 0;
            }
            return false;
        }

        long remainingLockoutSeconds() {
            long elapsed = System.currentTimeMillis() - lockoutTime;
            return (LOCKOUT_DURATION_MS - elapsed) / 1000;
        }
    }

    // ── Main ──────────────────────────────────────────────────────
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        printBanner();

        while (true) {
            printMenu();
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> registerUser(scanner);
                case "2" -> loginUser(scanner);
                case "3" -> {
                    System.out.println(CYAN + "\nStay secure! Goodbye. 👋" + RESET);
                    scanner.close();
                    return;
                }
                default  -> System.out.println(RED + "⚠ Invalid option. Please choose 1, 2, or 3." + RESET);
            }
        }
    }

    // ── Banner ────────────────────────────────────────────────────
    static void printBanner() {
        System.out.println(BOLD + CYAN);
        System.out.println("╔══════════════════════════════════════════════╗");
        System.out.println("║     SECURE LOGIN SYSTEM WITH ATTACK          ║");
        System.out.println("║            PREVENTION  🔐                    ║");
        System.out.println("╚══════════════════════════════════════════════╝" + RESET);
    }

    // ── Menu ──────────────────────────────────────────────────────
    static void printMenu() {
        System.out.println("\n" + BOLD + "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" + RESET);
        System.out.println("  1️⃣  Register");
        System.out.println("  2️⃣  Login");
        System.out.println("  3️⃣  Exit");
        System.out.println(BOLD + "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" + RESET);
    }

    // ── REGISTER ──────────────────────────────────────────────────
    static void registerUser(Scanner scanner) {
        System.out.println("\n" + BOLD + "── REGISTER ──────────────────────────" + RESET);

        System.out.print("Enter username: ");
        String username = scanner.nextLine().trim();

        if (username.isEmpty()) {
            System.out.println(RED + "⚠ Username cannot be empty." + RESET);
            return;
        }

        if (userDatabase.containsKey(username)) {
            System.out.println(RED + "⚠ Username already exists. Please choose another." + RESET);
            return;
        }

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        // Validate password strength before registering
        if (!isPasswordStrong(password)) {
            System.out.println(RED + "\n⚠ Password is too weak! Requirements:" + RESET);
            System.out.println("  ▸ At least 8 characters");
            System.out.println("  ▸ At least one uppercase letter");
            System.out.println("  ▸ At least one lowercase letter");
            System.out.println("  ▸ At least one number");
            System.out.println("  ▸ At least one special character (!@#$%^&*)");
            return;
        }

        // Generate salt + hash password
        String salt           = generateSalt();
        String hashedPassword = hashPassword(password, salt);

        userDatabase.put(username, new UserRecord(username, hashedPassword, salt));

        System.out.println(GREEN + "\n✅ Registration successful! Welcome, " + username + "!" + RESET);
        System.out.println(YELLOW + "🔒 Your password is stored securely using SHA-256 + Salt hashing." + RESET);
    }

    // ── LOGIN ─────────────────────────────────────────────────────
    static void loginUser(Scanner scanner) {
        System.out.println("\n" + BOLD + "── LOGIN ──────────────────────────────" + RESET);

        System.out.print("Enter username: ");
        String username = scanner.nextLine().trim();

        // Check if user exists
        if (!userDatabase.containsKey(username)) {
            // Generic message — don't reveal if user exists (security best practice)
            System.out.println(RED + "⚠ Invalid username or password." + RESET);
            return;
        }

        UserRecord user = userDatabase.get(username);

        // ── Brute Force / Lockout Check ───────────────────────────
        if (user.isLocked()) {
            long remaining = user.remainingLockoutSeconds();
            System.out.println(RED + "\n🔒 ACCOUNT LOCKED!" + RESET);
            System.out.println(YELLOW + "   Too many failed attempts. Try again in "
                    + remaining + " seconds." + RESET);
            System.out.println(YELLOW + "   (Brute force protection active 🛡)" + RESET);
            return;
        }

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        // Hash the entered password with stored salt and compare
        String hashedInput = hashPassword(password, user.salt);

        if (hashedInput.equals(user.hashedPassword)) {
            // ✅ Login successful
            user.failedAttempts = 0;
            user.lockoutTime    = 0;
            System.out.println(GREEN + "\n✅ LOGIN SUCCESSFUL! Welcome back, " + username + "! 🎉" + RESET);
            System.out.println(CYAN + "   Session started securely." + RESET);
        } else {
            // ❌ Wrong password
            user.failedAttempts++;
            int remaining = MAX_LOGIN_ATTEMPTS - user.failedAttempts;

            System.out.println(RED + "\n❌ Invalid username or password." + RESET);

            if (user.failedAttempts >= MAX_LOGIN_ATTEMPTS) {
                user.lockoutTime = System.currentTimeMillis();
                System.out.println(RED + "🔒 ACCOUNT LOCKED for " + (LOCKOUT_DURATION_MS / 1000)
                        + " seconds due to too many failed attempts!" + RESET);
                System.out.println(YELLOW + "   (Brute force attack prevention triggered 🛡)" + RESET);
            } else {
                System.out.println(YELLOW + "⚠ " + remaining + " attempt(s) remaining before lockout." + RESET);
            }
        }
    }

    // ── PASSWORD STRENGTH CHECK ───────────────────────────────────
    static boolean isPasswordStrong(String password) {
        if (password.length() < 8) return false;
        boolean hasUpper   = password.chars().anyMatch(Character::isUpperCase);
        boolean hasLower   = password.chars().anyMatch(Character::isLowerCase);
        boolean hasDigit   = password.chars().anyMatch(Character::isDigit);
        boolean hasSpecial = password.chars().anyMatch(c ->
                "!@#$%^&*()_+-=[]{}|;':\",./<>?`~\\".indexOf(c) >= 0);
        return hasUpper && hasLower && hasDigit && hasSpecial;
    }

    // ── SALT GENERATION ───────────────────────────────────────────
    static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] saltBytes = new byte[16];
        random.nextBytes(saltBytes);
        return Base64.getEncoder().encodeToString(saltBytes);
    }

    // ── PASSWORD HASHING (SHA-256 + Salt) ────────────────────────
    static String hashPassword(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            String saltedPassword = salt + password; // prepend salt
            byte[] hashBytes = md.digest(saltedPassword.getBytes());
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Hashing algorithm not found!", e);
        }
    }
}








