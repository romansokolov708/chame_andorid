package com.odelan.chama.ui.base;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.odelan.chama.R;
import com.odelan.chama.utils.Common;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 7/20/2016.
 */
public abstract class BaseFragment extends Fragment {
    public BaseActivity mContext;
    public View mView;
    public String TAG = "BaseFragment";

    protected OnFragmentInteractionListener mOnFragmentInteractionListener;

    protected abstract int getLayoutResID();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = (BaseActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(getLayoutResID(), container, false);
        ButterKnife.bind(this, mView);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //mView = view;
    }

    public void saveKeyValue (String key, String value) {
        Common.saveInfoWithKeyValue(mContext, key, value);
    }

    public String getValueFromKey (String key) {
        return Common.getInfoWithValueKey(mContext, key);
    }

    public void startActivityWithAnim(Intent intent) {
        mContext.startActivityWithAnim(intent);
    }

    private Toast mToast;
    public void showToast(String message) {
        if (mToast != null) {
            mToast.cancel();
            mToast = null;
        }
        mToast = Toast.makeText(mContext, message, Toast.LENGTH_SHORT);
        mToast.show();
    }

    private KProgressHUD kProgressHUD = null;
    public void showLoading() {
        if (kProgressHUD == null) {
            kProgressHUD = KProgressHUD.create(mContext)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel(getString(R.string.please_wait))
                    .setWindowColor(Color.parseColor("#DDDDDDDD"))
                    .setCancellable(true)
                    .setAnimationSpeed(1)
                    .setDimAmount(0.3f);
        }

        kProgressHUD.show();
    }

    public void showLoading(String message) {
        if (kProgressHUD == null) {
            kProgressHUD = KProgressHUD.create(mContext)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel(message)
                    .setWindowColor(Color.parseColor("#DDDDDDDD"))
                    .setCancellable(true)
                    .setAnimationSpeed(1)
                    .setDimAmount(0.3f);
        } else {
            kProgressHUD.setLabel(message);
        }

        kProgressHUD.show();
    }

    public void dismissLoading() {
        if(kProgressHUD != null) {
            kProgressHUD.dismiss();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mOnFragmentInteractionListener = (OnFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mOnFragmentInteractionListener = null;
    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(BaseFragment fragment);
    }
}
