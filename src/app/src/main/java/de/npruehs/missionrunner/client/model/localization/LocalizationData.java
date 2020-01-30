package de.npruehs.missionrunner.client.model.localization;

import java.util.Hashtable;
import java.util.Locale;

public class LocalizationData {
    private String hash;

    private LocalizedString[] strings;

    private Hashtable<String, Hashtable<String, String>> languages;

    public LocalizationData() {
    }

    public LocalizationData(String hash, LocalizedString[] strings) {
        this.hash = hash;

        setStrings(strings);
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public LocalizedString[] getStrings() {
        return strings;
    }

    public void setStrings(LocalizedString[] strings) {
        this.strings = strings;

        // Build dictionary.
        languages = new Hashtable<>();
        languages.put("en", new Hashtable<String, String>());
        languages.put("de", new Hashtable<String, String>());

        for (LocalizedString s : strings) {
            languages.get("en").put(s.getId(), s.getEn());
            languages.get("de").put(s.getId(), s.getDe());
        }
    }

    public String get(String id) {
        if (languages == null) {
            return id;
        }

        Hashtable<String, String> language = languages.get(Locale.getDefault().getLanguage());

        if (language == null) {
            return id;
        }

        String text = language.get(id);

        return text != null ? text : id;
    }
}
