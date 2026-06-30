# 🔒 Basic Encryption & Decryption (Caesar Cipher)

A command-line tool built in Java that demonstrates basic encryption and decryption using the Caesar Cipher technique — encrypting user-provided text with a custom shift key, decrypting it back, and verifying the round-trip is correct.

## Goal

Implement a simple, classic encryption and decryption technique to understand the fundamentals of how text-based ciphers work.

## Features

- **Encryption** — Encrypts any text input using a Caesar Cipher with a user-defined shift key (1–25)
- **Decryption** — Decrypts the encrypted text back to its original form using the inverse shift
- **Dual output display** — Shows the original text, encrypted text, and decrypted text side by side for easy comparison
- **Automatic verification** — Confirms that decrypting the encrypted text returns the exact original input (✔ / ✘ check)
- **Input validation** — Rejects empty input and invalid shift keys, prompting the user to re-enter
- **Interactive CLI loop** — Test multiple texts and shift keys in a single session
- **Color-coded terminal output** for readability

## Demo

```
╔════════════════════════════════════════════╗
║   BASIC ENCRYPTION & DECRYPTION (CAESAR)    ║
╚════════════════════════════════════════════╝

Enter text to encrypt (or 'quit' to exit): Hello World
Enter shift key (1-25): 3

══════════════ RESULT ══════════════
  Shift Key       : 3
  Original Text   : Hello World
  Encrypted Text  : Khoor Zruog
  Decrypted Text  : Hello World
  ✔ Decryption matches the original text.
═════════════════════════════════════
```

## How It Works

The **Caesar Cipher** is one of the oldest and simplest encryption techniques. Each letter in the input text is shifted forward in the alphabet by a fixed number of positions (the "shift key"), wrapping around from Z back to A if needed.

- **Encryption:** `encryptedChar = (originalChar - 'A' + shift) % 26 + 'A'`
- **Decryption:** Applies the inverse shift (`26 - shift`) to reverse the transformation and recover the original text
- Non-alphabetic characters (numbers, spaces, punctuation) are left unchanged, so sentence structure and formatting are preserved

## Tech Stack

- **Language:** Java
- **Libraries:** `java.util.Scanner`
- **Interface:** Command-line (ANSI escape codes for colored output)

## Getting Started

### Prerequisites
- Java JDK 8 or higher installed

### Run it

```bash
# Compile
javac EncryptionDecryption.java

# Run
java EncryptionDecryption
```

Enter any text, then a shift key between 1–25, to see the encrypted and decrypted output. Type `quit` to exit.

> **Note:** Colored output relies on ANSI escape codes. These work natively on most Linux/macOS terminals and modern Windows terminals (Windows Terminal, VS Code integrated terminal).

## Key Skills Demonstrated

- Encryption/decryption concepts and cipher logic
- Modular arithmetic for character shifting and alphabet wraparound
- Data protection fundamentals
- Input validation and defensive programming

## Possible Future Improvements

- Support additional cipher techniques (Vigenère cipher, ROT13, XOR cipher)
- Add brute-force decryption mode (try all 25 shifts to crack unknown ciphertext)
- Build a GUI or web version for broader accessibility
- Add file encryption/decryption support, not just plain text input

## License

This project is open source and available under the [MIT License](LICENSE).

---

⭐ If you found this useful, consider giving the repo a star!
