package com.soco.ebusiness.soco;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.encoder.ByteMatrix;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.ParseGeoPoint;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Hashtable;

/**
 * Created by Rosen on 27.05.2015.
 */

public class App extends Application {

    private static boolean loginstate = false;
    private static String login_text;
    private static Context sContext;




    public static boolean get_loginstate() {
        return loginstate;
    }

    public static void setloginstate(boolean loginstate) {
        App.loginstate = loginstate;
    }



    @Override
    public void onCreate() {

        super.onCreate();
        sContext= this;
        //FacebookSdk.sdkInitialize(this.getApplicationContext());
        ParseObject.registerSubclass(Rezept.class);
        ParseObject.registerSubclass(User.class);
        ParseObject.registerSubclass(Event.class);

        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "r5kFDsKD0dBiQpdiSxSxpQkiIAy1ytBUV1olpd0U", "C8XCXf6LQp2GBYfedTlaDnLFjQWctB7yc0DzZ2L5");
        ParseFacebookUtils.initialize(this.getApplicationContext());
        ParsePush.subscribeInBackground("", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
                } else {
                    Log.e("com.parse.push", "failed to subscribe for push", e);
                }
            }
        });
        // Save the current Installation to Parse.
        ParseInstallation.getCurrentInstallation().saveInBackground();
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            // do stuff with the user
            loginstate = true;
        } else {
            loginstate= false;
            // show the signup or login screen
        }

    }
    public String islogin(){
        if(loginstate){
            login_text= getString(R.string.login);
            return login_text;
        } else {

            login_text= getString(R.string.logout);
            return login_text;
        }

    }


    public static Bitmap generateQrCode(String myCodeText) throws WriterException {
        Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<EncodeHintType, ErrorCorrectionLevel>();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H); // H = 30% damage

        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        int size = 256;
        myCodeText ="SOCO-"+ myCodeText;
        BitMatrix bitMatrix = qrCodeWriter.encode(myCodeText, BarcodeFormat.QR_CODE, size, size, hintMap);
        int width = bitMatrix.getWidth();
        Bitmap bmp = Bitmap.createBitmap(width, width, Bitmap.Config.RGB_565);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < width; y++) {
                bmp.setPixel(y, x, bitMatrix.get(x, y)==true ? Color.BLACK : Color.WHITE);
            }
        }
        return bmp;
    }

    public static boolean saveqrcode(Context context, Bitmap bmp) {
        String dirname = Environment.getExternalStorageDirectory() + "/DCIM/";
        File sddir = new File(dirname);
        if (!sddir.mkdirs()) {
            if (sddir.exists()) {
            } else {
                Toast.makeText(context, "Folder error", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        try {
            FileOutputStream fos = new FileOutputStream(dirname + "output.jpg");
            bmp.compress(Bitmap.CompressFormat.JPEG, 75, fos);

            fos.flush();
            fos.close();
            Toast.makeText(context,"QR Code saved", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("MyLog", e.toString());
        }
        return true;
    }
    public static Context getAppContext() {
        return sContext;
    }

}