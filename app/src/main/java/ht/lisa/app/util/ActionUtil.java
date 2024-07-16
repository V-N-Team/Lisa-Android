package ht.lisa.app.util;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

public class ActionUtil {
    public static void openLisaTwitter(Context context) {
        Intent intent;
        try {
            context.getPackageManager().getPackageInfo("com.twitter.android", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=LisaLottoht"));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } catch (Exception e) {
            // browser
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/LisaLottoht"));
        }
        context.startActivity(intent);
    }

    public static void openLisaInstagram(Context context) {
        Intent instagramIntent;
        String nomPackageInfo = "com.instagram.android";
        try {
            context.getPackageManager().getPackageInfo(nomPackageInfo, 0);
            String scheme = "http://instagram.com/_u/lisalottoht";
            instagramIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(scheme));
        } catch (Exception e) {
            String path = "https://instagram.com/lisalottoht";
            instagramIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(path));
        }
        context.startActivity(instagramIntent);
    }
/*
    public static void openLisaFacebook(Context context) {
        Intent facebookIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(context.getString(R.string.httpMMe) + context.getString(R.string.lisalottoht)));
        context.startActivity(facebookIntent);
     *//*   try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("f/160025004086676"));
            context.startActivity(intent);
        } catch (Exception e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://web.facebook.com/160025004086676")));
        }*//*
    }*/

    public static Intent newFacebookIntent(Context context) {
        Uri uri = null;
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo("com.facebook.katana", 0);
            if (applicationInfo.enabled) {
                // http://stackoverflow.com/a/24547437/1048340
                uri = Uri.parse("fb://facewebmodal/f?href=" + "lisalottoht");
            }
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return new Intent(Intent.ACTION_VIEW, uri);
    }

    public static void openLisaFacebookMessenger(Context context) {
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://m.me/lisalottoht")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void openLisaYoutube(Context context) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:channel/UCK-Mj5OuiSLSfIv_uUSkK4Q"));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://www.youtube.com/channel/UCK-Mj5OuiSLSfIv_uUSkK4Q"));
        try {
            context.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            context.startActivity(webIntent);
        }
    }

    public static void openPhoneNumber(Context context, String tel) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tel));
        context.startActivity(intent);
    }
}
