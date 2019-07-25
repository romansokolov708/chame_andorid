package com.odelan.chama.ui.activity.main.loans.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.odelan.chama.data.model.LoanMD;
import com.odelan.chama.data.model.TransactionMD;
import com.odelan.chama.ui.base.BaseFragment;
import com.odelan.chama.utils.DlgHelper;
import com.odelan.chama.utils.TabAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.odelan.chama.info.Info.BASE_API_URL;

public class LoansFragment extends BaseFragment {

    @BindView(R.id.chart)
    PieChart chart;

    @BindView(R.id.borrowedAmountTV)
    TextView borrowedAmountTV;

    @BindView(R.id.repayAmountTV)
    TextView repayAmountTV;

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_loans;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();
    }

    private void init() {
        setupChart();
        setChartData();
    }

    public void setupChart() {
        chart.setUsePercentValues(true);
        chart.getDescription().setEnabled(false);
        chart.setExtraOffsets(0, 0, 0, 10);

        chart.setDrawHoleEnabled(false);
        chart.setDrawCenterText(true);

        chart.setRotationAngle(-90);
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
        entries.add(new PieEntry(MyApplication.borrow_amount == 0 ? 100 : MyApplication.loan_amount * 100 / MyApplication.borrow_amount, "Paid"));
        entries.add(new PieEntry((MyApplication.borrow_amount == 0 ? 0 : (MyApplication.borrow_amount - MyApplication.loan_amount) * 100 / MyApplication.borrow_amount), "Out Standing"));

        borrowedAmountTV.setText(Integer.toString(MyApplication.borrow_amount) + " KES");
        repayAmountTV.setText(Integer.toString(MyApplication.loan_amount) + " KES");

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

        PieData pdata = new PieData(dataSet);
        pdata.setValueFormatter(new PercentFormatter(new DecimalFormat("##.0")));
        pdata.setValueTextSize(11f);
        pdata.setValueTextColor(Color.WHITE);
        chart.setData(pdata);

        // undo all highlights
        chart.highlightValues(null);

        chart.invalidate();
    }

    @OnClick(R.id.sendBtn) public void onSend() {
        if (MyApplication.borrow_amount <= MyApplication.loan_amount) {
            showToast("You didn't borrow money.");
            return;
        }
        showLoading();
        AndroidNetworking.post(BASE_API_URL + "transactions/newPayback")
                .addBodyParameter("chama_code", MyApplication.sUser.chama_code)
                .addBodyParameter("phone", MyApplication.sUser.phone)
                .addBodyParameter("amount", Integer.toString((MyApplication.borrow_amount - MyApplication.loan_amount)))
                .addBodyParameter("type", TransactionMD.TYPE_LOANS)
                .setTag("payback")
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
                            MyApplication.loan_amount = MyApplication.borrow_amount;
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
