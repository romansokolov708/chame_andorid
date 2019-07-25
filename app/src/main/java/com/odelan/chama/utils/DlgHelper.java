package com.odelan.chama.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.odelan.chama.R;


public class DlgHelper {

    Context mContext;
    static Dialog dialog = null;
    static float WIDTH_PERCENT = 0.7f;

    public DlgHelper() {}

    public DlgHelper(Context context) {
        mContext = context;
    }

    public void showDialog(String title,
                           String message,
                           String mainBtnTitle,
                           String otherBtnTitle1,
                           String otherBtnTitle2,
                           OnDialogBtnClickListener mainBtnOnClickListener,
                           OnDialogBtnClickListener otherBtnOnClickListener1,
                           OnDialogBtnClickListener otherBtnOnClickListener2) {

        dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dlg_custom_white); // dlg_custom: black
        dialog.setCanceledOnTouchOutside(false);

        int width = (int)(mContext.getResources().getDisplayMetrics().widthPixels * WIDTH_PERCENT);
        int height = WindowManager.LayoutParams.WRAP_CONTENT;

        dialog.getWindow().setLayout(width, height);

        TextView titleTV = dialog.findViewById(R.id.titleTV);
        titleTV.setText(title);
        TextView messageTV = dialog.findViewById(R.id.messageTV);
        messageTV.setText(message);

        LinearLayout btnLL2 = dialog.findViewById(R.id.btnLL2);
        LinearLayout btnLL3 = dialog.findViewById(R.id.btnLL3);
        Button btn1 = dialog.findViewById(R.id.btn1);
        Button btn2 = dialog.findViewById(R.id.btn2);
        Button btn3 = dialog.findViewById(R.id.btn3);
        Button btn4 = dialog.findViewById(R.id.btn4);

        if (title.equals("")) {
            titleTV.setVisibility(View.GONE);
        }

        dialog.show();

        OnDialogBtnClickListener defaultOnClickListener = new OnDialogBtnClickListener() {
            @Override
            public void onClickProc(View view) {

            }
        };

        if (otherBtnTitle2 != null) {
            btnLL2.setVisibility(View.VISIBLE);
            btnLL3.setVisibility(View.VISIBLE);

            btn3.setText(otherBtnTitle1);
            btn4.setText(otherBtnTitle2);

            if (otherBtnOnClickListener1 == null) {
                btn3.setOnClickListener(defaultOnClickListener);
            } else {
                btn3.setOnClickListener(otherBtnOnClickListener1);
            }

            if (otherBtnOnClickListener2 == null) {
                btn4.setOnClickListener(defaultOnClickListener);
            } else {
                btn4.setOnClickListener(otherBtnOnClickListener2);
            }
        } else {
            if (otherBtnTitle1 != null) {
                btn2.setVisibility(View.VISIBLE);
                btn2.setText(otherBtnTitle1);

                if (otherBtnOnClickListener1 == null) {
                    btn2.setOnClickListener(defaultOnClickListener);
                } else {
                    btn2.setOnClickListener(otherBtnOnClickListener1);
                }
            } else {
                if (mainBtnTitle != null) {
                    btn1.setText(mainBtnTitle);

                    if (mainBtnOnClickListener == null) {
                        btn1.setOnClickListener(defaultOnClickListener);
                    } else {
                        btn1.setOnClickListener(mainBtnOnClickListener);
                    }
                } else {
                    btn1.setText(mContext.getString(R.string.dialog_ok));
                    btn1.setOnClickListener(defaultOnClickListener);
                }
            }
        }

        if (mainBtnTitle == null) {
            btn1.setText(mContext.getString(R.string.dialog_ok));
            btn1.setOnClickListener(defaultOnClickListener);
        } else {
            btn1.setText(mainBtnTitle);
            if (mainBtnOnClickListener == null) {

            } else {
                btn1.setOnClickListener(mainBtnOnClickListener);

                if(otherBtnTitle1 != null) {
                    btn2.setText(otherBtnTitle1);
                }
            }
        }
    }

    public void showDialogAutoDismiss(String title, String message, int milliseconds) {
        showDialog(title, message, null, null, null, null, null, null);

        final Handler handler  = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        };

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                handler.removeCallbacks(runnable);
            }
        });

        handler.postDelayed(runnable, milliseconds);
    }

    public void showDialog(String title, String message) {
        showDialog(title, message, null, null, null, null, null, null);
    }

    public void showDialog(String title, String message, String mainBtnTitle) {
        showDialog(title, message, mainBtnTitle, null, null, null, null, null);
    }

    public void showDialog(String title, String message, String mainBtnTitle, OnDialogBtnClickListener mainBtnOnClickListener) {
        showDialog(title, message, mainBtnTitle, null, null, mainBtnOnClickListener, null, null);
    }

    public void showDialog(String title, String message, String mainBtnTitle, String otherBtnTitle1, OnDialogBtnClickListener mainBtnOnClickListener) {
        showDialog(title, message, mainBtnTitle, otherBtnTitle1, null, mainBtnOnClickListener, null, null);
    }

    public void showDialog(String title, String message, String mainBtnTitle, String otherBtnTitle1,
                           OnDialogBtnClickListener mainBtnOnClickListener, OnDialogBtnClickListener otherBtnOnClickListener1) {
        showDialog(title, message, mainBtnTitle, otherBtnTitle1, null, mainBtnOnClickListener, otherBtnOnClickListener1, null);
    }

    public void showDialog(String title, String message, String mainBtnTitle, String otherBtnTitle1, String otherBtnTitle2,
                           OnDialogBtnClickListener mainBtnOnClickListener, OnDialogBtnClickListener otherBtnOnClickListener1) {
        showDialog(title, message, mainBtnTitle, otherBtnTitle1, otherBtnTitle2, mainBtnOnClickListener, otherBtnOnClickListener1, null);
    }

    public void showDialog(Context con,
                           String title,
                           String message,
                           String mainBtnTitle,
                           String otherBtnTitle1,
                           String otherBtnTitle2,
                           OnDialogBtnClickListener mainBtnOnClickListener,
                           OnDialogBtnClickListener otherBtnOnClickListener1,
                           OnDialogBtnClickListener otherBtnOnClickListener2) {

        dialog = new Dialog(con);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dlg_custom_white); // dlg_custom: black
        dialog.setCanceledOnTouchOutside(false);

        int width = (int)(con.getResources().getDisplayMetrics().widthPixels * WIDTH_PERCENT);
        int height = WindowManager.LayoutParams.WRAP_CONTENT;

        dialog.getWindow().setLayout(width, height);

        TextView titleTV = dialog.findViewById(R.id.titleTV);
        titleTV.setText(title);
        TextView messageTV = dialog.findViewById(R.id.messageTV);
        messageTV.setText(message);

        LinearLayout btnLL2 = dialog.findViewById(R.id.btnLL2);
        LinearLayout btnLL3 = dialog.findViewById(R.id.btnLL3);
        Button btn1 = dialog.findViewById(R.id.btn1);
        Button btn2 = dialog.findViewById(R.id.btn2);
        Button btn3 = dialog.findViewById(R.id.btn3);
        Button btn4 = dialog.findViewById(R.id.btn4);

        if (title.equals("")) {
            titleTV.setVisibility(View.GONE);
        }

        dialog.show();

        OnDialogBtnClickListener defaultOnClickListener = new OnDialogBtnClickListener() {
            @Override
            public void onClickProc(View view) {

            }
        };

        if (otherBtnTitle2 != null) {
            btnLL2.setVisibility(View.VISIBLE);
            btnLL3.setVisibility(View.VISIBLE);

            btn3.setText(otherBtnTitle1);
            btn4.setText(otherBtnTitle2);

            if (otherBtnOnClickListener1 == null) {
                btn3.setOnClickListener(defaultOnClickListener);
            } else {
                btn3.setOnClickListener(otherBtnOnClickListener1);
            }

            if (otherBtnOnClickListener2 == null) {
                btn4.setOnClickListener(defaultOnClickListener);
            } else {
                btn4.setOnClickListener(otherBtnOnClickListener2);
            }
        } else {
            if (otherBtnTitle1 != null) {
                btn2.setVisibility(View.VISIBLE);
                btn2.setText(otherBtnTitle1);

                if (otherBtnOnClickListener1 == null) {
                    btn2.setOnClickListener(defaultOnClickListener);
                } else {
                    btn2.setOnClickListener(otherBtnOnClickListener1);
                }
            } else {
                if (mainBtnTitle != null) {
                    btn1.setText(mainBtnTitle);

                    if (mainBtnOnClickListener == null) {
                        btn1.setOnClickListener(defaultOnClickListener);
                    } else {
                        btn1.setOnClickListener(mainBtnOnClickListener);
                    }
                } else {
                    btn1.setText(con.getString(R.string.dialog_ok));
                    btn1.setOnClickListener(defaultOnClickListener);
                }
            }
        }

        if (mainBtnTitle == null) {
            btn1.setText(con.getString(R.string.dialog_ok));
            btn1.setOnClickListener(defaultOnClickListener);
        } else {
            btn1.setText(mainBtnTitle);
            if (mainBtnOnClickListener == null) {

            } else {
                btn1.setOnClickListener(mainBtnOnClickListener);

                if(otherBtnTitle1 != null) {
                    btn2.setText(otherBtnTitle1);
                }
            }
        }
    }

    public void showDialogAutoDismiss(Context con, String title, String message, int milliseconds) {
        showDialog(con, title, message, null, null, null, null, null, null);

        final Handler handler  = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        };

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                handler.removeCallbacks(runnable);
            }
        });

        handler.postDelayed(runnable, milliseconds);
    }

    public void showDialog(Context con, String title, String message) {
        showDialog(con, title, message, null, null, null, null, null, null);
    }

    public void showDialog(Context con, String title, String message, String mainBtnTitle) {
        showDialog(con, title, message, mainBtnTitle, null, null, null, null, null);
    }

    public void showDialog(Context con, String title, String message, String mainBtnTitle, OnDialogBtnClickListener mainBtnOnClickListener) {
        showDialog(con, title, message, mainBtnTitle, null, null, mainBtnOnClickListener, null, null);
    }

    public void showDialog(Context con, String title, String message, String mainBtnTitle, String otherBtnTitle1, OnDialogBtnClickListener mainBtnOnClickListener) {
        showDialog(con, title, message, mainBtnTitle, otherBtnTitle1, null, mainBtnOnClickListener, null, null);
    }

    public void showDialog(Context con, String title, String message, String mainBtnTitle, String otherBtnTitle1,
                           OnDialogBtnClickListener mainBtnOnClickListener, OnDialogBtnClickListener otherBtnOnClickListener1) {
        showDialog(con, title, message, mainBtnTitle, otherBtnTitle1, null, mainBtnOnClickListener, otherBtnOnClickListener1, null);
    }

    public void showDialog(Context con, String title, String message, String mainBtnTitle, String otherBtnTitle1, String otherBtnTitle2,
                           OnDialogBtnClickListener mainBtnOnClickListener, OnDialogBtnClickListener otherBtnOnClickListener1) {
        showDialog(con, title, message, mainBtnTitle, otherBtnTitle1, otherBtnTitle2, mainBtnOnClickListener, otherBtnOnClickListener1, null);
    }



    public static abstract class OnDialogBtnClickListener implements View.OnClickListener {
        public abstract void onClickProc(View view);

        @Override
        public void onClick(View view) {
            if (dialog != null) {
                onClickProc(view);
                dialog.dismiss();
                dialog = null;
            }
        }
    }
}
