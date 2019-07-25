package com.odelan.chama.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

/**
 * Created by Administrator on 6/17/2016.
 */
public class Common {
    public static String TAG = "Common";
    public static ProgressDialog progressDialog;

    public static void showToast(Context context, String text){
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }

    public static void showSoftKeyboard(Context con, View view) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager)
                    con.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public static void hideSoftKeyboard(Context con, View view) {
        InputMethodManager imm = (InputMethodManager) con.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    public static String getInfoWithValueKey(Context context, String key) {

        SharedPreferences pref = context.getSharedPreferences("pref", context.MODE_PRIVATE);
        return pref.getString(key, "");
    }

    public static void saveInfoWithKeyValue(Context context, String key, String username) {

        SharedPreferences pref = context.getSharedPreferences("pref", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, username);
        editor.commit();
    }

    public static Bitmap resizeImage(Bitmap b, float rate) {
        float w = b.getWidth();
        float h = b.getHeight();
        return Bitmap.createScaledBitmap(b, ((int)(w/rate)), ((int)(h/rate)), false);
    }

    public static String encodeImgBase64(Bitmap bm) {
        Bitmap bmp = resizeImage(bm, 2);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public static Bitmap decodeImgBase64(String imageString) {
        byte[] imageBytes = Base64.decode(imageString, Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        return decodedImage;
    }

    public static String numberDotFormat(float fValue, int pos) {
        String format = "%." + String.valueOf(pos) + "f";
        return String.format(format, fValue).replace(",", ".");
    }

    public static String convertToCurrenyFormat(String amount) {
        return amount;
    }

    /*
    func goldFactor(isBuyCoin: Bool) -> Double {
        let date = Date()
        var calendar = Calendar.current
        calendar.timeZone = TimeZone(abbreviation: "UTC")!
                let components = calendar.dateComponents([.hour, .minute, .weekday], from: date)

        let hour = components.hour
        let minute = components.minute
        let weekday = components.weekday

        var factor: Double = 1.0
        if isBuyCoin {
            if weekday! > 5 {
                factor = G_FACTOR_BUY_COIN_MARKET_CLOSED
            } else {
                if hour! >= 15 {
                    factor = G_FACTOR_BUY_COIN_MARKET_CLOSED
                } else if hour! < 10 || (hour! == 10 && minute! < 30) {
                    factor = G_FACTOR_BUY_COIN_MARKET_CLOSED
                } else {
                    factor = G_FACTOR_BUY_COIN_MARKET_OPENED
                }
            }
        } else {
            // charge card

            if weekday! > 5 {
                factor = G_FACTOR_CHARGE_CARD_MARKET_CLOSED
            } else {
                if hour! >= 15 {
                    factor = G_FACTOR_CHARGE_CARD_MARKET_CLOSED
                } else if hour! < 10 || (hour! == 10 && minute! < 30) {
                    factor = G_FACTOR_CHARGE_CARD_MARKET_CLOSED
                } else {
                    factor = G_FACTOR_CHARGE_CARD_MARKET_OPENED
                }
            }
        }

        return factor
    }
    */

    /*
    public static String getUUID() {
        TelephonyManager tManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        String uuid = tManager.getDeviceId();
        return uuid;
    }
    */

    //Encryption
    private static String convertToHex(byte[] data) {
        StringBuilder buf = new StringBuilder();
        for (byte b : data) {
            int halfbyte = (b >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                buf.append((0 <= halfbyte) && (halfbyte <= 9) ? (char) ('0' + halfbyte) : (char) ('a' + (halfbyte - 10)));
                halfbyte = b & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }

    public static String SHA1(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(text.getBytes("iso-8859-1"), 0, text.length());
        byte[] sha1hash = md.digest();
        return convertToHex(sha1hash);
    }

    public static Bitmap getBitmapFromImageView(ImageView iv) {
        Drawable drawable = iv.getDrawable();
        BitmapDrawable bitmapDrawable = ((BitmapDrawable) drawable);
        Bitmap bitmap = bitmapDrawable .getBitmap();
        return bitmap;
    }

    public static Bitmap rotateBitmap(Bitmap original, float degrees) {
        int width = original.getWidth();
        int height = original.getHeight();

        Matrix matrix = new Matrix();
        matrix.preRotate(degrees);

        Bitmap rotatedBitmap = Bitmap.createBitmap(original, 0, 0, width, height, matrix, true);

        return rotatedBitmap;
    }

    public static void showProgressDialog(Context context, boolean flag) {

        if (flag) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Please wait...");
            progressDialog.show();

        } else {
            if (progressDialog != null)
                progressDialog.dismiss();
        }
    }

    public static String getJSONStringWithKey(JSONObject obj, String key){
        String result;
        try{
            result = obj.getString(key);

        } catch (JSONException e){
            Log.e(TAG, e.toString());
            return "";
        }

        return result;
    }

    public static JSONObject getJSONObjectWithKey(JSONObject obj, String key){
        JSONObject result;
        try{
            result = obj.getJSONObject(key);

        } catch (JSONException e){
            Log.e(TAG, e.toString());
            return null;
        }

        return result;
    }

    public static JSONArray getJSONArrayWithKey(JSONObject obj, String key){
        JSONArray result;
        try{
            result = obj.getJSONArray(key);

        } catch (JSONException e){
            Log.e(TAG, e.toString());
            return null;
        }

        return result;
    }

    public static int getDeviceWidth(Activity activity) {
        Point point;
        WindowManager wm;

        wm = activity.getWindowManager();
        point = new Point();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            wm.getDefaultDisplay().getSize(point);
            return point.x;
        } else {
            return wm.getDefaultDisplay().getWidth();
        }
    }

    public static int getDeviceHeight(Activity activity) {
        Point point;
        WindowManager wm;

        wm = activity.getWindowManager();
        point = new Point();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            wm.getDefaultDisplay().getSize(point);
            return point.y;
        } else {
            return wm.getDefaultDisplay().getHeight();
        }
    }

    public static float convertPixelsToDp(float px){
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return Math.round(dp);
    }

    public static float convertDpToPixel(float dp){
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }

    public static boolean setListViewHeightBasedOnItems(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                item.measure(0, 0);
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);

            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight;
            if(params.height>300){
                params.height = 300;
            }
            listView.setLayoutParams(params);
            listView.requestLayout();

            return true;

        } else {
            return false;
        }

    }

    public static String convertMillisecondToTimeString(int millis) {

        int hour = millis / (3600 * 1000);
        int min = (millis % (3600 * 1000)) / (60 * 1000);
        int second = (millis % (60 * 1000)) / 1000;
        int mil = millis % 1000 / 10;

        return String.format("%02d:%02d:%02d.%02d", hour, min, second, mil);
    }

    public static String convertMillisecondToSecondTimeString(int millis) {

        int hour = millis / (3600 * 1000);
        int min = (millis % (3600 * 1000)) / (60 * 1000);
        int second = (millis % (60 * 1000)) / 1000;

        return String.format("%02d:%02d:%02d", hour, min, second);
    }

    public static String generateTimeRand(String suffix) {
        if(suffix==null || suffix.equals("")) {
            return System.currentTimeMillis()+"";
        } else {
            return suffix + "_" + System.currentTimeMillis();
        }
    }

    public static String convertTimestampTo36() {
        String sampleChr = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        int num = sampleChr.length();

        long source = System.currentTimeMillis();
        String result = "";
        while (source > 0) {
            long rm = source % num;
            source = source / num;
            String subStr = sampleChr.substring((int)rm, (int)rm+1);
            result = result + subStr;
        }

        return result;
    }

    public static String convertTimestampTo62() {
        String sampleChr = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        int num = sampleChr.length();

        long source = System.currentTimeMillis();
        String result = "";
        while (source > 0) {
            long rm = source % num;
            source = source / num;
            String subStr = sampleChr.substring((int)rm, (int)rm+1);
            result = result + subStr;
        }

        return result;
    }

    public static String random36() {
        return convertTimestampTo36();
    }

    public static String random62() {
        return convertTimestampTo62();
    }

    public static String[] convertArrayListToArray (ArrayList<String> list) {
        String[] array = new String[list.size()];
        array = list.toArray(array);
        return array;
    }

    public static String creatAppInnerDir (Context con, String dirName) {
        String dirPath = "";
        PackageManager m = con.getPackageManager();
        String s = con.getPackageName();
        try {
            PackageInfo p = m.getPackageInfo(s, 0);
            s = p.applicationInfo.dataDir;
            //String dirName = Common.generateTimeRand("");
            File dir = new File(s + "/files/" + dirName);
            if (!(dir.exists() && dir.isDirectory())) {
                dir.mkdirs();
            }
            dirPath = s + "/files/" + dirName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.w("yourtag", "Error Package name not found ", e);
        }
        return dirPath;
    }

    public static boolean saveBmpToFile(Bitmap bmp, String path) {
        boolean result = true;
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(path);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static void savefile(Uri sourceuri){

        String sourceFilename= sourceuri.getPath();
        String destinationFilename = Environment.getExternalStorageDirectory().getPath()+ File.separatorChar+"audioTemp.mp3";

        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;

        try {
            bis = new BufferedInputStream(new FileInputStream(sourceFilename));
            bos = new BufferedOutputStream(new FileOutputStream(destinationFilename, false));
            byte[] buf = new byte[1024];
            bis.read(buf);
            do {
                bos.write(buf);
            } while(bis.read(buf) != -1);
        } catch (IOException e) {

        } finally {
            try {
                if (bis != null) bis.close();
                if (bos != null) bos.close();
            } catch (IOException e) {

            }
        }
    }

    public static File getSavefile(Uri sourceuri){

        String sourceFilename= sourceuri.getPath();
        String destinationFilename = Environment.getExternalStorageDirectory().getPath()+ File.separatorChar+"audioTemp.mp3";

        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;

        try {
            bis = new BufferedInputStream(new FileInputStream(sourceFilename));
            bos = new BufferedOutputStream(new FileOutputStream(destinationFilename, false));
            byte[] buf = new byte[1024];
            bis.read(buf);
            do {
                bos.write(buf);
            } while(bis.read(buf) != -1);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null) bis.close();
                if (bos != null) bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new File(destinationFilename);
    }

    public static boolean copyFile(String from, String to) {
        try {
            File sd = Environment.getExternalStorageDirectory();
            if (sd.canWrite()) {
                int end = from.toString().lastIndexOf("/");
                String str1 = from.toString().substring(0, end);
                String str2 = from.toString().substring(end+1, from.length());
                File source = new File(str1, str2);
                File destination= new File(to, str2);
                if (source.exists()) {
                    FileChannel src = new FileInputStream(source).getChannel();
                    FileChannel dst = new FileOutputStream(destination).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
