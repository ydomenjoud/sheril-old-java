package zIgzAg.utile;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

public class SlugUtils {

    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s_]+");
    private static final Pattern DUPLICATE_HYPHENS = Pattern.compile("-+");

    public static String toSlug(String input) {
        if (input == null || input.isBlank()) {
            return "";
        }

        // 1. Séparation des caractères et des accents (NFD)
        String nowhitespace = WHITESPACE.matcher(input.trim()).replaceAll("-");

        // 2. Normalisation Unicode + suppression des diacritiques (accents)
        String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD);
        String slug = normalized.replaceAll("\\p{M}", "");

        // 3. Passage en minuscules, suppression du non-ASCII et nettoyage des tirets
        return NONLATIN.matcher(slug)
                .replaceAll("")
                .toLowerCase(Locale.ENGLISH)
                .replaceAll("-+", "-")
                .replaceAll("^-|-$", ""); // Retire les tirets en début/fin
    }

}