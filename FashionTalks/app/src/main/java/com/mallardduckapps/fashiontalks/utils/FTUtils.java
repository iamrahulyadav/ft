package com.mallardduckapps.fashiontalks.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mallardduckapps.fashiontalks.R;
import com.mallardduckapps.fashiontalks.loaders.Exclude;
import com.mallardduckapps.fashiontalks.objects.BasicNameValuePair;
import com.mallardduckapps.fashiontalks.objects.Post;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by oguzemreozcan on 12/01/15.
 */
public class FTUtils {

    public static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    public static String getBasicJson(BasicNameValuePair... params) throws JSONException {
        JSONObject object = new JSONObject();
        if (params == null) {
            return null;
        }
        for (BasicNameValuePair param : params) {
            if(param != null){
                object.put(param.getName(), param.getValue());
            }
        }
        String jsonText = "";
        try{
            jsonText = object.toString();
        }catch(OutOfMemoryError e){
            e.printStackTrace();
            object = object.put("file", "");
            jsonText = object.toString();
        }

        //Log.d("JSON", "JSON: " + object.toString());
        return jsonText;
    }

    public static boolean instagramAppInstalledOrNot(Activity activity) {

        boolean app_installed = false;
        try {
            ApplicationInfo info = activity.getPackageManager().getApplicationInfo("com.instagram.android", 0);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    public static float dpFromPx(int px, Context context) {
        return px / context.getResources().getDisplayMetrics().density;
    }

    public static float pxFromDp(int dp, Context context) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public static int[] getScreenSize(Activity activity) {
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);
        int width = size.x;
        int height = size.y;
        int[] sizes = {width, height};
        Log.d("FT_UTILS_SCREEN_SIZE", "WIDTH: " + width);
        Log.d("FT_UTILS_SCREEN_SIZE", "HEIGHT: " + height);
        Log.d("FT_UTILS_SCREEN_SIZE", "Density: " + activity.getResources().getDisplayMetrics().density);
        return sizes;
    }

    public static String convertPostToString(Post post){
        Exclude ex = new Exclude();
        Gson gson = new GsonBuilder().addDeserializationExclusionStrategy(ex).addSerializationExclusionStrategy(ex).create();
        Type type = new TypeToken<Post>() {
        }.getType();
        String json = gson.toJson(post, type);
        Log.d("FT_UTILS", "POST string: " + json);
        return json;
    }

    public static Post convertStringToPost(String json){
        //JsonArray dataObjects = new JsonParser().parse(json).getAsJsonObject().getAsJsonArray("data");
        Exclude ex = new Exclude();
        //ArrayList<Post> popularPostItems = new ArrayList<Post>();
        Gson gson = new GsonBuilder().addDeserializationExclusionStrategy(ex).addSerializationExclusionStrategy(ex).create();
        Post post = gson.fromJson(json, Post.class);
        return post;
    }

    public static String convertListToString(ArrayList<Post> postArrayList){
        Exclude ex = new Exclude();
        Gson gson = new GsonBuilder().addDeserializationExclusionStrategy(ex).addSerializationExclusionStrategy(ex).create();
        Type type = new TypeToken<ArrayList<Post>>() {
        }.getType();
        String json = gson.toJson(postArrayList, type);
        Log.d("FT_UTILS", "POST LIST string: " + json);
        return json;
    }

    public static ArrayList<Post> convertStringToPostArray(String json){
        //JsonArray dataObjects = new JsonParser().parse(json).getAsJsonObject().getAsJsonArray();
        Exclude ex = new Exclude();
        ArrayList<Post> popularPostItems = new ArrayList<Post>();
        Gson gson = new GsonBuilder().addDeserializationExclusionStrategy(ex).addSerializationExclusionStrategy(ex).create();
        Type type = new TypeToken<ArrayList<Post>>() {
        }.getType();
        //for (JsonElement item : dataObjects) {
        popularPostItems  = gson.fromJson(json, type);
            //popularPostItems.add(post);
        //}
        return popularPostItems;
    }

    public static Bitmap screenShot(View view) {
        Log.d("FTUTILS", "TAKE SCREEN SHOT !!");
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(),
                view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    public static Uri getImageUri(Context inContext, Bitmap inImage, String imageTitle) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, imageTitle, null);
        return Uri.parse(path);
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    /**
     * Checks if the device is a tablet or a phone
     *
     * @param context The Activity Context.
     * @return Returns true if the device is a Tablet
     */
    //TODO CHECK IF STILL WORKS
    public static boolean isTablet(Context context) {
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }

    public static Drawable getRoundedCornerImage(Drawable bitmapDrawable,
                                                 float roundCorner) {
        Bitmap bitmap = ((BitmapDrawable) bitmapDrawable).getBitmap();
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = roundCorner;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        Drawable image = new BitmapDrawable(output);
        return image;

    }

    //TODO ANY OFFLINE USAGE?
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        // if no network is available networkInfo will be null
        // otherwise check if we are connected
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    public static void sendMail(String email, String recipient, String subject, String imagePath, Activity activity) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:")); // .concat(recipient)

        emailIntent.setType("message/rfc822");
        //emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { recipient });
        emailIntent.putExtra(Intent.EXTRA_TEXT, email);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        if(imagePath != null){
            Uri pngUri = Uri.parse("file://" + imagePath);//Uri.parse(imagePath);
            emailIntent.putExtra(android.content.Intent.EXTRA_STREAM, pngUri);
            //emailIntent.setType("application/image");
        }
        //i.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(pic));

       // i.setType("image/png");
        try {
            activity.startActivity(Intent.createChooser(emailIntent, activity.getString(R.string.send_mail)));
            // finish();
            Log.i("Email", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(activity, activity.getString(R.string.no_mail_app),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * A helper loading a custom font.
     *
     * @param assetManager
     * App's asset manager.
     * @param filePath
     * The path of the file.
     * @return Return {@link android.graphics.Typeface} or null if the path is
     * invalid.
     */
    //TODO BEST PRACTISE TO LOAD CUSTOM FONT
    private static final HashMap<String, Typeface> sCachedFonts = new HashMap<String, Typeface>();

    public static Typeface loadFont(final AssetManager assetManager,
                                    final String filePath) {
        synchronized (sCachedFonts) {
            try {
                if (!sCachedFonts.containsKey(filePath)) {
                    final Typeface typeface = Typeface.createFromAsset(
                            assetManager, filePath);
                    sCachedFonts.put(filePath, typeface);
                    return typeface;
                }
            } catch (Exception e) {
                Log.w("Calligraphy",
                        "Can't create asset from "
                                + filePath
                                + ". Make sure you have passed in the correct path and file name.",
                        e);
                sCachedFonts.put(filePath, null);
                return null;
            }
            return sCachedFonts.get(filePath);
        }
    }

    /*
     * Sets the font on all TextViews in the ViewGroup. Searches
     * recursively for all inner ViewGroups as well. Just add a
     * check for any other views you want to set as well (EditText,
     * etc.)
     */
    public static void setFont(ViewGroup group, Typeface font) {
        int count = group.getChildCount();
        View v;
        for (int i = 0; i < count; i++) {
            v = group.getChildAt(i);
            if (v instanceof TextView || v instanceof Button || v instanceof EditText/*etc.*/)
                ((TextView) v).setTypeface(font);
            else if (v instanceof ViewGroup)
                setFont((ViewGroup) v, font);
        }
    }

    public static void setLayoutFont(Typeface tf, TextView... params) {
        for (TextView tv : params) {
            tv.setTypeface(tf);
        }
    }
}
