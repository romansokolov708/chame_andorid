package com.odelan.chama;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Environment;
import android.support.multidex.MultiDexApplication;

import android.app.Notification;
import android.app.NotificationManager;

import com.google.gson.JsonObject;
import com.odelan.chama.R;
import com.androidnetworking.AndroidNetworking;
import com.elvishew.xlog.LogConfiguration;
import com.elvishew.xlog.XLog;
import com.elvishew.xlog.flattener.ClassicFlattener;
import com.elvishew.xlog.printer.AndroidPrinter;
import com.elvishew.xlog.printer.ConsolePrinter;
import com.elvishew.xlog.printer.Printer;
import com.elvishew.xlog.printer.file.FilePrinter;
import com.elvishew.xlog.printer.file.backup.NeverBackupStrategy;
import com.elvishew.xlog.printer.file.naming.DateFileNameGenerator;
import com.odelan.chama.data.model.ChamaGroupMD;
import com.odelan.chama.data.model.User;
import com.odelan.chama.info.Info;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.github.nkzawa.emitter.Emitter;
import com.odelan.chama.ui.activity.main.HomeActivity;

import java.net.URISyntaxException;

import java.io.File;
import java.util.List;
import org.json.JSONObject;
import org.json.JSONArray;
import android.widget.Toast;

/**
 * Created by Administrator on 6/28/2016.
 */
public class MyApplication extends MultiDexApplication {

    public final String LOG_TAG = "Chama_" + BuildConfig.VERSION_NAME + "_" + BuildConfig.VERSION_CODE;
    public final String LOG_FILE_DIR = "Chama_XLog";

    public static boolean isLoggedin = false;
    public static boolean isProduction = BuildConfig.DEBUG;

    /** Constant */

    public static final String[] OPTIONS = {User.STATUS_ACTIVE, User.STATUS_SUSPEND, User.STATUS_REMOVE};
    public static final String[] OPTIONS_SUSPEND = {User.STATUS_SUSPEND, User.STATUS_REMOVE};
    public static String[] OPTIONS_UNSUSPEND = {User.STATUS_ACTIVE, User.STATUS_REMOVE};

    public static final String[] ROLES = {User.TYPE_SECRETARY, User.TYPE_TREASURE, User.TYPE_MEMBER};

    public static Socket mSocket;
    public static User sUser;
    public static Integer interest_rate;
    public static Integer transaction_fee;
    public static Integer contribution_amount;
    public static Integer borrow_amount;
    public static Integer loan_amount;


    @Override
    public void onCreate() {
        super.onCreate();

        try {
            mSocket = IO.socket(Info.BASE_SOCKET_URL);
            mSocket.connect();

            mSocket.on("c2bsuccess", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    JSONObject data = (JSONObject)args[0];
                    NotificationManager notif=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                    Notification notify=new Notification.Builder
                            (getApplicationContext()).setContentTitle("C2BSuccess").setContentText("You paid to Chama Plus").
                            setContentTitle("Payment Success to Chama Plus").setSmallIcon(R.drawable.ic_money).build();

                    notify.flags |= Notification.FLAG_AUTO_CANCEL;
                    notif.notify(0, notify);
                }
            });

            mSocket.on("b2csuccess", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    JSONObject data = (JSONObject)args[0];
                    NotificationManager notif=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                    Notification notify=new Notification.Builder
                            (getApplicationContext()).setContentTitle("B2CSuccess").setContentText("You received from Chama Plus").
                            setContentTitle("Payment Success from Chama Plus").setSmallIcon(R.drawable.ic_money).build();

                    notify.flags |= Notification.FLAG_AUTO_CANCEL;
                    notif.notify(0, notify);
                }
            });
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        isProduction = !BuildConfig.DEBUG;

        //File logger init
        String logFilePath = Environment.getExternalStorageDirectory().getPath() + "/" + LOG_FILE_DIR + "/";
        File logFileDir = new File(logFilePath);
        if (!logFileDir.isDirectory()) {
            logFileDir.mkdirs();
        }

        Printer androidPrinter = new AndroidPrinter();             // Printer that print the log using android.util.Log
        Printer consolePrinter = new ConsolePrinter();             // Printer that print the log to console using System.out
        Printer filePrinter = new FilePrinter                      // Printer that print the log to the file system
                .Builder(logFilePath)                              // Specify the path to save log file
                .fileNameGenerator(new DateFileNameGenerator())        // Default: ChangelessFileNameGenerator("log")
                .backupStrategy(new NeverBackupStrategy())             // Default: FileSizeBackupStrategy(1024 * 1024)
                .logFlattener(new ClassicFlattener())                       // Default: DefaultFlattener
                .build();

        LogConfiguration config = new LogConfiguration.Builder()
                .tag(LOG_TAG)
                .st(3)                                                 // Enable stack trace info with depth 2, disabled by default
                .b()
                .build();
        XLog.init(                                                 // Initialize XLog
                config,                                                // Specify the log configuration, if not specified, will use new LogConfiguration.Builder().build()
                androidPrinter,                                        // Specify printers, if no printer is specified, AndroidPrinter(for Android)/ConsolePrinter(for java) will be used.
                consolePrinter,
                filePrinter);

        AndroidNetworking.initialize(getApplicationContext());
    }

    public static Typeface getFontAwesome(Context context, String font){
        return Typeface.createFromAsset(context.getAssets(), font);
    }

    public static boolean isAppForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> services = activityManager
                .getRunningTasks(Integer.MAX_VALUE);
        boolean isActivityFound = false;

        if (services.get(0).topActivity.getPackageName().toString().equalsIgnoreCase(context.getPackageName().toString())) {
            isActivityFound = true;
        }
        return isActivityFound;
    }

    /** Round Drawables **/

    public static Drawable getRectDrawable(Activity activity, int solidColor, int strokeColor, int strokeWidth) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setColor(solidColor);
        drawable.setStroke(getScaledLength(activity, strokeWidth), strokeColor);
        return drawable;
    }

    public static Drawable getRoundDrawable(Activity activity, int cornerRadius, int solidColor, int strokeColor, int strokeWidth) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setCornerRadius(getScaledLength(activity, cornerRadius));
        drawable.setColor(solidColor);
        drawable.setStroke(getScaledLength(activity, strokeWidth), strokeColor);
        return drawable;
    }

    public static Drawable getRoundDrawable(Activity activity, int cornerRadius, int solidColor) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setCornerRadius(getScaledLength(activity, cornerRadius));
        drawable.setColor(solidColor);
        return drawable;
    }

    public static Drawable getOvalDrawable(Activity activity, int solidColor, int strokeColor, int strokeWidth) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.OVAL);
        drawable.setColor(solidColor);
        drawable.setStroke(getScaledLength(activity, strokeWidth), strokeColor);
        return drawable;
    }

    public static Drawable getOvalDrawable(Activity activity, int solidColor) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.OVAL);
        drawable.setColor(solidColor);
        return drawable;
    }

    public static int getScaledLength(Activity activity, int length) {
        int scaledLength = (int)(length * activity.getResources().getDisplayMetrics().density);
        return scaledLength;
    }

    /*
    public static void getAccountBalance(final Context con, String accountNumber, final OnGetBalanceListener listener) {
        AndroidNetworking.post(SERVER_URL_WRAP_NEXORONE + "new/getAccountBalance")
                .addHeaders("AuthenticationToken", WRAP_NEXORONE_TOKEN)
                .addBodyParameter("AccountNumber", accountNumber)
                .setTag("getAccountBalance")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String responseCode = response.getString("ResponseCode");
                            if (responseCode.equals("0")) {
                                String currency = response.getString("Currency");
                                String availableBalance = response.getString("AvailableBalance");
                                float availableBal = Float.valueOf(Common.numberDotFormat(Float.valueOf(availableBalance), 2));
                                listener.onSuccess(currency, availableBal);
                            } else {
                                String responseMessage = response.getString("ResponseMessage");
                                if (responseMessage != null && !responseMessage.isEmpty()) {
                                    listener.onError(1, responseMessage);
                                } else {
                                    listener.onError(2, "responseCode != 0");
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            try {
                                String errorResponse = response.getString("errorResponse");
                                if (errorResponse != null && !errorResponse.isEmpty()) {
                                    listener.onError(3, errorResponse);
                                } else {
                                    listener.onError(1, con.getString(R.string.parse_error));
                                }
                            } catch (Exception e1) {
                                e1.printStackTrace();
                                listener.onError(2, con.getString(R.string.network_error));
                            }
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        listener.onError(2, con.getString(R.string.network_error));
                    }
                });
    }

    public interface OnGetBalanceListener {
        void onSuccess(String currency, float balance);
        void onError(int errorCode, String errorMsg);
    }
    */
}


/** Android Networking useful */

/* Get
AndroidNetworking.get("https://fierce-cove-29863.herokuapp.com/getAllUsers/{pageNumber}")
        .addPathParameter("pageNumber", "0")
        .addQueryParameter("limit", "3")
        .addHeaders("token", "1234")
        .setTag("test")
        .setPriority(Priority.LOW)
        .build()
        .getAsJSONArray(new JSONArrayRequestListener() {
@Override
public void onResponse(JSONArray response) {
        // do anything with response
        }
@Override
public void onError(ANError error) {
        // handle error
        }
        });
*/
/* Post
AndroidNetworking.post("https://fierce-cove-29863.herokuapp.com/createAnUser")
        .addBodyParameter("firstname", "Amit")
        .addBodyParameter("lastname", "Shekhar")
        .setTag("test")
        .setPriority(Priority.MEDIUM)
        .build()
        .getAsJSONObject(new JSONObjectRequestListener() {
@Override
public void onResponse(JSONObject response) {
        // do anything with response
        }
@Override
public void onError(ANError error) {
        // handle error
        }
        });
*/