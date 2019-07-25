package com.mukesh.countrypicker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.telephony.TelephonyManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import static com.mukesh.countrypicker.Countries.COUNTRIES;

public class CountryPicker
        implements CountryPickerDialog.CountryPickerDialogInteractionListener {

    // region Variables
    public static final int SORT_BY_NONE = 0;
    public static final int SORT_BY_NAME = 1;
    public static final int SORT_BY_ISO = 2;
    public static final int SORT_BY_DIAL_CODE = 3;
    private static final String COUNTRY_TAG = "COUNTRY_PICKER";

    private Context context;
    private int sortBy = SORT_BY_NONE;
    private OnCountryPickerListener onCountryPickerListener;
    private boolean canSearch = true;

    private List<Country> countries;
    // endregion

    // region Constructors
    private CountryPicker() {
    }

    CountryPicker(Builder builder) {
        sortBy = builder.sortBy;
        if (builder.onCountryPickerListener != null) {
            onCountryPickerListener = builder.onCountryPickerListener;
        }
        context = builder.context;
        canSearch = builder.canSearch;
        countries = new ArrayList<>(Arrays.asList(COUNTRIES));
        sortCountries(countries);
    }
    // endregion

    // region Listeners
    @Override
    public void sortCountries(@NonNull List<Country> countries) {
        switch (sortBy) {
            case SORT_BY_NAME:
                Collections.sort(countries, new Comparator<Country>() {
                    @Override
                    public int compare(Country country1, Country country2) {
                        return country1.getName().trim().compareToIgnoreCase(country2.getName().trim());
                    }
                });
            case SORT_BY_ISO:
                Collections.sort(countries, new Comparator<Country>() {
                    @Override
                    public int compare(Country country1, Country country2) {
                        return country1.getCode().trim().compareToIgnoreCase(country2.getCode().trim());
                    }
                });
            case SORT_BY_DIAL_CODE:
                Collections.sort(countries, new Comparator<Country>() {
                    @Override
                    public int compare(Country country1, Country country2) {
                        return country1.getDialCode().trim().compareToIgnoreCase(country2.getDialCode().trim());
                    }
                });
        }
    }

    @Override
    public List<Country> getAllCountries() {
        return countries;
    }

    @Override
    public boolean canSearch() {
        return canSearch;
    }

    // endregion

    // region Utility Methods
    public void showDialog(@NonNull FragmentManager supportFragmentManager) {
        if (countries == null || countries.isEmpty()) {
            throw new IllegalArgumentException(context.getString(R.string.error_no_countries_found));
        } else {
            CountryPickerDialog countryPickerDialog = CountryPickerDialog.newInstance();
            if (onCountryPickerListener != null) {
                countryPickerDialog.setCountryPickerListener(onCountryPickerListener);
            }
            countryPickerDialog.setDialogInteractionListener(this);
            countryPickerDialog.show(supportFragmentManager, COUNTRY_TAG);
        }
    }

    public void showDialog(String type, @NonNull FragmentManager supportFragmentManager) {
        if (countries == null || countries.isEmpty()) {
            throw new IllegalArgumentException(context.getString(R.string.error_no_countries_found));
        } else {
            CountryPickerDialog countryPickerDialog = CountryPickerDialog.newInstance(type);
            if (onCountryPickerListener != null) {
                countryPickerDialog.setCountryPickerListener(onCountryPickerListener);
            }
            countryPickerDialog.setDialogInteractionListener(this);
            countryPickerDialog.show(supportFragmentManager, COUNTRY_TAG);
        }
    }

    public void setCountries(@NonNull List<Country> countries) {
        this.countries.clear();
        this.countries.addAll(countries);
        sortCountries(this.countries);
    }

    public Country getCountryFromSIM() {
        TelephonyManager telephonyManager =
                (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null
                && telephonyManager.getSimState() != TelephonyManager.SIM_STATE_ABSENT) {
            return getCountryByISO(telephonyManager.getSimCountryIso());
        }
        return null;
    }

    public Country getCountryByLocale(@NonNull Locale locale) {
        String countryIsoCode = locale.getISO3Country().substring(0, 2).toLowerCase();
        return getCountryByISO(countryIsoCode);
    }

    public Country getCountryByName(@NonNull String countryName) {
        countryName = countryName.toUpperCase();
        Country country = new Country();
        country.setName(countryName);
        int i = Arrays.binarySearch(COUNTRIES, country, new NameComparator());
        if (i < 0) {
            return null;
        } else {
            return COUNTRIES[i];
        }
    }

    public Country getCountryByISO(@NonNull String countryIsoCode) {
        countryIsoCode = countryIsoCode.toUpperCase();
        Country country = new Country();
        country.setCode(countryIsoCode);
        int i = Arrays.binarySearch(COUNTRIES, country, new ISOCodeComparator());
        if (i < 0) {
            return null;
        } else {
            return COUNTRIES[i];
        }
    }
    // endregion

    // region Builder
    public static class Builder {
        private Context context;
        private int sortBy = SORT_BY_NONE;
        private boolean canSearch = true;
        private OnCountryPickerListener onCountryPickerListener;

        public Builder with(@NonNull Context context) {
            this.context = context;
            return this;
        }

        public Builder sortBy(@NonNull int sortBy) {
            this.sortBy = sortBy;
            return this;
        }

        public Builder listener(@NonNull OnCountryPickerListener onCountryPickerListener) {
            this.onCountryPickerListener = onCountryPickerListener;
            return this;
        }

        public Builder canSearch(@NonNull boolean canSearch) {
            this.canSearch = canSearch;
            return this;
        }

        public CountryPicker build() {
            return new CountryPicker(this);
        }
    }
    // endregion

    // region Comparators
    public static class ISOCodeComparator implements Comparator<Country> {
        @Override
        public int compare(Country country, Country nextCountry) {
            return country.getCode().compareTo(nextCountry.getCode());
        }
    }

    public static class NameComparator implements Comparator<Country> {
        @Override
        public int compare(Country country, Country nextCountry) {
            return country.getName().compareTo(nextCountry.getName());
        }
    }
    // endregion
}
