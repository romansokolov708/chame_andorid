package com.odelan.chama.ui.activity.main.contribute.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.MPPointF;
import com.mukesh.countrypicker.Country;
import com.mukesh.countrypicker.CountryPicker;
import com.mukesh.countrypicker.OnCountryPickerListener;
import com.odelan.chama.MyApplication;
import com.odelan.chama.R;
import com.odelan.chama.data.model.TransactionMD;
import com.odelan.chama.ui.activity.main.borrow.BorrowSavingsActivity;
import com.odelan.chama.ui.base.BaseFragment;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.odelan.chama.info.Info.BASE_API_URL;

public class ContributeFragment extends BaseFragment {

    @BindView(R.id.amountTV)
    TextView amountTV;

    @BindView(R.id.cycleTV)
    TextView cycleTV;

    @BindView(R.id.spinner)
    Spinner spinner;

    @BindView(R.id.chart)
    PieChart chart;

    /*@BindView(R.id.rateTV)
    TextView rateTV;

    @BindView(R.id.feeTV)
    TextView feeTV;*/

    List<String> mPayments = new ArrayList<>();

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_contribute;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();
    }

    private void init() {
        mPayments = new ArrayList<>();
        mPayments.add("MPESA");

        ArrayAdapter<String> paymentsAdapter = new ArrayAdapter<String>(mContext,
                android.R.layout.simple_spinner_item, mPayments);
        paymentsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(paymentsAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                showToast("Not selected");
            }
        });

        amountTV.setText(MyApplication.sUser.contribution.toString() + " KES");
        cycleTV.setText(MyApplication.sUser.contribution_cycle);

        setupChart();
        setChartData();

        /*rateTV.setText("Interest rate : 2% monthly");
        feeTV.setText("Processing fee : 2% of amount");*/
    }

    public void setupChart() {
        chart.setUsePercentValues(true);
        chart.getDescription().setEnabled(false);
        chart.setExtraOffsets(0, 0, 0, 10);

        chart.setDrawHoleEnabled(false);
        chart.setDrawCenterText(true);

        chart.setRotationAngle(0);
        // enable rotation of the chart by touch
        chart.setRotationEnabled(false);
        chart.setHighlightPerTapEnabled(true);

        // add a selection listener
        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

            }

            @Override
            public void onNothingSelected() {

            }
        });

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(true);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // entry label styling
        chart.setEntryLabelColor(Color.WHITE);
        chart.setEntryLabelTextSize(12f);
    }

    private void setChartData() {
        ArrayList<PieEntry> entries = new ArrayList<>();

        entries.add(new PieEntry(MyApplication.sUser.emergency_fund, "Emergency fund"));
        entries.add(new PieEntry(MyApplication.sUser.my_saving, "Savings"));
        entries.add(new PieEntry(MyApplication.sUser.merry_go_round, "Merry-Go-Round"));

        PieDataSet dataSet = new PieDataSet(entries, "");

        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#ee8133"));
        colors.add(Color.parseColor("#a7cd3a"));
        colors.add(Color.parseColor("#f9cb33"));

        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter(new DecimalFormat("##.0")));
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        chart.setData(data);

        // undo all highlights
        chart.highlightValues(null);

        chart.invalidate();
    }

    @OnClick(R.id.sendBtn) public void onSend() {
        if (MyApplication.sUser.contribution_status == true) {
            showToast("You have already contributed.");
            return;
        }
        showLoading();
        AndroidNetworking.post(BASE_API_URL + "transactions/newContribution")
                .addBodyParameter("chama_code", MyApplication.sUser.chama_code)
                .addBodyParameter("phone", MyApplication.sUser.phone)
                .addBodyParameter("amount", MyApplication.sUser.contribution.toString())
                .addBodyParameter("type", TransactionMD.TYPE_CONTRIBUTE)
                .setTag("contribute")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        dismissLoading();
                        try {
                            String message = response.getString("message");
                            showToast(message);
                            MyApplication.sUser.contribution_status = true;
                        } catch (Exception e) {
                            e.printStackTrace();
                            showToast("Response parse error");
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        dismissLoading();
                        showToast("Network Error");
                    }
                });
    }
}
