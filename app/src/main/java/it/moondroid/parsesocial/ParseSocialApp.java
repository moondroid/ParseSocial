package it.moondroid.parsesocial;

import android.app.Application;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.SystemClock;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseAnalytics;
import com.parse.ParseObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import it.moondroid.sociallib.entities.Comment;
import it.moondroid.sociallib.entities.Like;
import it.moondroid.sociallib.entities.Post;

/**
 * Created by Marco on 22/11/2014.
 */
public class ParseSocialApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        /*
		 * Fill in this section with your Parse credentials
		 */
        Parse.initialize(this, getResources().getString(R.string.application_key), getResources().getString(R.string.client_key));


        /*
		 * For more information on app security and Parse ACL:
		 * https://www.parse.com/docs/android_guide#security-recommendations
		 */
        ParseACL defaultACL = new ParseACL();

		/*
		 * If you would like all objects to be private by default, remove this
		 * line
		 */
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(false);

		/*
		 * Default ACL is public read access, and user read/write access
		 */
        ParseACL.setDefaultACL(defaultACL, true);


        /*
         * Register custom objects
         */
        ParseObject.registerSubclass(Post.class);
        ParseObject.registerSubclass(Comment.class);
        ParseObject.registerSubclass(Like.class);

        //requires ACCESS_COARSE_LOCATION or ACCESS_FINE_LOCATION permission
//        final LocationManager mlocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
//        final Location currentGeoLocation = mlocManager
//                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//        Log.d("ParseSocialApp", "currentGeoLocation: "+currentGeoLocation!=null? currentGeoLocation.toString(): "NULL");

//        Map<String, String> dimensions = new HashMap<String, String>();
//        // What type of news is this?
//        //dimensions.put("location", "politics");
//        // Is it a weekday or the weekend?
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
//        dimensions.put("date", dateFormat.format(new Date()));
//        dimensions.put("time", timeFormat.format(new Date()));
//        // Send the dimensions to Parse along with the 'read' event
//        ParseAnalytics.trackEvent("app_start", dimensions);
    }
}
