package com.odelan.chama.utils;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;

import com.odelan.chama.ui.activity.intro.SplashActivity;

import java.util.Locale;

public class MultiLanguageHelper {

    public static final String LANG_KEY = "language_key";

    public static final String LANG_VAL_ENGLISH = "en";
    public static final String LANG_VAL_GERMAN = "de";
    public static final String LANG_VAL_FRENCH = "fr";
    public static final String LANG_VAL_ITALY = "it";
    public static final String LANG_VAL_SPANISH = "es";
    public static final String LANG_VAL_PORTUGAL = "pt";
    public static final String LANG_VAL_ROMANIA = "ro";
    public static final String LANG_VAL_JAPANESE = "ja";
    public static final String LANG_VAL_CHINA = "zh";

    public Activity context;

    public MultiLanguageHelper() {}

    public MultiLanguageHelper(Activity con) {
        context = con;
    }

    /**
     *
     * @param activityClass activity.class
     * @param languageCode English: en, Chinese: zh etc
     */
    public void setLanguage(Class activityClass, String languageCode) {

        //setting new configuration
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getApplicationContext().getResources().updateConfiguration(config, null);

        //store current language in prefrence
        Common.saveInfoWithKeyValue(context, LANG_KEY, languageCode);


        //With new configuration start activity again
        /*Intent intent = new Intent(context.getApplicationContext(), activityClass);
        context.startActivity(intent);
        context.finish();
        context.overridePendingTransition(0, 0);*/

        Intent intent = new Intent(context.getApplicationContext(), SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        context.finish();
        context.overridePendingTransition(0, 0);
    }

    public void setLanguageRefresh(Class activityClass, String languageCode) {

        //setting new configuration
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getApplicationContext().getResources().updateConfiguration(config, null);

        //store current language in prefrence
        Common.saveInfoWithKeyValue(context, LANG_KEY, languageCode);


        //With new configuration start activity again
        Intent intent = new Intent(context.getApplicationContext(), activityClass);
        context.startActivity(intent);
        context.finish();
        context.overridePendingTransition(0, 0);
    }

    /**
     *
     * @return languageCode: English: en, Chinese: zh etc
     */
    public String getCurrentLanguage() {
        return Common.getInfoWithValueKey(context, LANG_KEY);
    }
}
