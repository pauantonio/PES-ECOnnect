package com.econnect.API.Translate;

import android.content.Context;

import com.econnect.API.JsonResult;
import com.econnect.API.Service;

import java.util.Locale;
import java.util.TreeMap;

public class TranslateService extends Service {

    public static class Translation {
        public final String translatedText;
        public final String detectedSourceLanguage;

        public Translation(String translatedText, String detectedSourceLanguage) {
            this.translatedText = translatedText;
            this.detectedSourceLanguage = detectedSourceLanguage;
        }
    }

    private static class Translations {
        public final Translation[] translations;

        public Translations(Translation[] translations) {
            this.translations = translations;
        }
    }

    public Translation translate(String source, String targetLanguageCode) {
        // targetLanguageCode must be a BCP-47 language code (en, es, ca)
        final String GOOGLE_TRANSLATE_URL = "https://translation.googleapis.com/language/translate/v2";

        TreeMap<String, String> params = new TreeMap<>();
        params.put("key", ApiKey.GOOGLE_TRANSLATE_KEY);
        params.put("target", targetLanguageCode);
        params.put("q", source);

        String response = getRaw(GOOGLE_TRANSLATE_URL, params);
        response = response.replace("&#39;", "'");
        JsonResult json = JsonResult.parse(response);
        Translations translationSet = json.getObject("data", Translations.class);
        if (translationSet.translations.length != 1) {
            throw new RuntimeException("Unexpected number of translations received");
        }
        return translationSet.translations[0];
    }

    public Translation translateToCurrentLang(Context context, String source) {
        String targetLang = context.getResources().getConfiguration().locale.getLanguage();
        return translate(source, targetLang);
    }
}
