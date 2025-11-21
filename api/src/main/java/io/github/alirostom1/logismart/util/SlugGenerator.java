package io.github.alirostom1.logismart.util;

public class SlugGenerator {
    public static String toSlug(String input) {
        if (input == null) return "";

        return input.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "") // Remove special chars
                .replaceAll("\\s+", "-")         // Spaces to hyphens
                .replaceAll("-+", "-")           // Remove duplicate hyphens
                .replaceAll("^-|-$", "");        // Remove leading/trailing hyphens
    }
}
