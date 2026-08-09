package com.icesi.fit.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {

    // Current hash used in import.sql for seeded users
    public static final String SEEDED_HASH = "$2a$10$wb4oZY0NqKyoAI4zB2JYfO6DX7Rm7yDu4e3uijuf2ZYMtA8/P4cfm";

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        if (args.length == 0) {
            System.out.println("Usage: java PasswordGenerator <password> [verify]");
            System.out.println("Examples:");
            System.out.println("  java PasswordGenerator admin123       -> prints bcrypt hash for admin123");
            System.out.println("  java PasswordGenerator admin123 verify -> prints hash and verifies against seeded hash");
            return;
        }

        String password = args[0];
        String hash = encoder.encode(password);

        System.out.println("Password: " + password);
        System.out.println("Generated Hash: " + hash);

        if (args.length > 1 && "verify".equalsIgnoreCase(args[1])) {
            boolean matchesSeed = encoder.matches(password, SEEDED_HASH);
            System.out.println("Matches seeded import.sql hash: " + matchesSeed);
        }
    }
}