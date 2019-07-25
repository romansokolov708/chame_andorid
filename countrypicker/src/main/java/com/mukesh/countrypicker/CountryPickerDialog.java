package com.mukesh.countrypicker;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CountryPickerDialog extends DialogFragment implements OnItemClickListener {

    // region Variables
    private CountryPickerDialogInteractionListener dialogInteractionListener;
    private EditText searchEditText;
    private RecyclerView countriesRecyclerView;
    private CountriesAdapter adapter;
    private List<Country> searchResults;
    private OnCountryPickerListener listener;
    private String type = "country"; // country , currency
    // endregion

    // region Constructors
    public static CountryPickerDialog newInstance() {
        return new CountryPickerDialog();
    }
    public static CountryPickerDialog newInstance(String type) {
        CountryPickerDialog dlg = new CountryPickerDialog();
        dlg.type = type;
        return dlg;
    }
    // endregion

    // region Lifecycle
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.country_picker, null);
        //getDialog().setTitle(R.string.country_picker_header);

        // Initialize a new foreground color span instance
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(getResources().getColor(R.color.golden_text_color));

        // Initialize a new spannable string builder instance
        SpannableStringBuilder ssBuilder = new SpannableStringBuilder(getString(R.string.country_picker_header));

        // Apply the text color span
        ssBuilder.setSpan(
                foregroundColorSpan,
                0,
                getString(R.string.country_picker_header).length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        // Set the alert dialog title using spannable string builder
        getDialog().setTitle(ssBuilder);

        int dividerId = getDialog().getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
        View divider = getDialog().findViewById(dividerId);
        if (divider != null) {
            divider.setBackgroundColor(getResources().getColor(R.color.golden_text_color));
        }

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#181818")));
        searchEditText = view.findViewById(R.id.country_code_picker_search);
        countriesRecyclerView = view.findViewById(R.id.countries_recycler_view);
        setupRecyclerView(type);
        if (!dialogInteractionListener.canSearch()) {
            searchEditText.setVisibility(View.GONE);
        }
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable searchQuery) {
                search(searchQuery.toString());
            }
        });
        return view;
    }

    @Override
    public void onItemClicked(Country country) {
        if (listener != null) {
            listener.onSelectCountry(country);
        }
        dismiss();
    }
    // endregion

    // region Setter Methods
    public void setCountryPickerListener(OnCountryPickerListener listener) {
        this.listener = listener;
    }

    public void setDialogInteractionListener(
            CountryPickerDialogInteractionListener dialogInteractionListener) {
        this.dialogInteractionListener = dialogInteractionListener;
    }
    // endregion

    // region Utility Methods
    private void search(String searchQuery) {
        searchResults.clear();
        for (Country country : dialogInteractionListener.getAllCountries()) {
            if (country.getName().toLowerCase(Locale.ENGLISH).contains(searchQuery.toLowerCase())) {
                searchResults.add(country);
            }
        }
        dialogInteractionListener.sortCountries(searchResults);
        adapter.notifyDataSetChanged();
    }

    private void setupRecyclerView(String type) {
        searchResults = new ArrayList<>();
        searchResults.addAll(dialogInteractionListener.getAllCountries());
        //adapter = new CountriesAdapter(getActivity(), searchResults, this);
        adapter = new CountriesAdapter(getActivity(), searchResults, type,this);
        countriesRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        countriesRecyclerView.setLayoutManager(layoutManager);
        countriesRecyclerView.setAdapter(adapter);
    }

    // endregion

    //region Interface
    public interface CountryPickerDialogInteractionListener {
        List<Country> getAllCountries();

        void sortCountries(List<Country> searchResults);

        boolean canSearch();
    }
    // endregion
}
