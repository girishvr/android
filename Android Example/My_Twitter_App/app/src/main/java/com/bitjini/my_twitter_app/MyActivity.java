package com.bitjini.my_twitter_app;

/**
 * Created by bitjini on 28/3/16.
 */

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MyActivity extends Activity {

    Activity mActivity;
    ListView listView;
    public RelativeLayout mlayout;
    EditText textSearch;
    ProgressBar progressBar;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.twitter_tweets_list);
        listView = (ListView) findViewById(R.id.list);
        Button searchBtn = (Button) findViewById(R.id.searchBtn);
        textSearch = (EditText) findViewById(R.id.enterText);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        mlayout = (RelativeLayout) findViewById(R.id.relativeLayout1);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // hide the keyboard
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (getCurrentFocus() != null) {
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }



                AndroidNetworkUtility androidNetworkUtility = new AndroidNetworkUtility();
                if (androidNetworkUtility.isConnected(getApplicationContext())) {
                    mActivity = MyActivity.this;

                    mActivity.runOnUiThread(new Runnable() {
                        public void run() {
                            String query = textSearch.getText().toString();
                            new TwitterAsyncTask().execute(query, this);
                        }
                    });

                } else {
                    Toast.makeText(MyActivity.this, "Network not Available!", Toast.LENGTH_LONG).show();
                }


            }
        });
        // timer to update ui every 5 sec
        if (textSearch.getText().toString().length()!=0) {
            new Timer().scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {

                    Start();
                }
            }, 0, 10000);//put here time 1000 milliseconds=1 second
        }

    }

    public void Start() {
        Log.e("On start called..", "");

        AndroidNetworkUtility androidNetworkUtility = new AndroidNetworkUtility();
        if (androidNetworkUtility.isConnected(getApplicationContext())) {
            mActivity = MyActivity.this;

            mActivity.runOnUiThread(new Runnable() {
                public void run() {
                    String query = textSearch.getText().toString();
                    new TwitterAsyncTask().execute(query, this);
                }
            });

        } else {
            Toast.makeText(MyActivity.this, "Network not Available!", Toast.LENGTH_LONG).show();
        }
    }

    public class TwitterAsyncTask extends AsyncTask<Object, Void, ArrayList<Search>> {



        final static String TWITTER_API_KEY = "H0vLNqlGfVXEQ7QeXML63lSMb";
        final static String TWITTER_API_SECRET = "t1pbXaepA3LsYUxM35061Mc5cMFefpUBXyU7QAwCvotxtF71yp";

        protected void onPreExecute() {

            ObjectAnimator animation = ObjectAnimator.ofInt (progressBar, "progress", 0, 500); // see this max value coming back here, we animale towards that value
            animation.setDuration (5000); //in milliseconds
            animation.setInterpolator (new DecelerateInterpolator());
            animation.start ();
        }
        @Override
        protected ArrayList<Search> doInBackground(Object... params) {
            ArrayList<Search> twitterTweets = null;
            if (params.length > 0) {
                try {
                    TwitterAPI twitterAPI = new TwitterAPI(TWITTER_API_KEY, TWITTER_API_SECRET);
                    twitterTweets = twitterAPI.getTwitterTweets(params[0].toString());
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
            }
            return twitterTweets;
        }

        @Override
        protected void onPostExecute(ArrayList<Search> twitterTweets) {

            // create an object of adapter class
            MyClassAdapter adapter =
                    new MyClassAdapter(MyActivity.this, R.layout.twitter_tweets_list, twitterTweets);
            listView.setAdapter(adapter);
            progressBar.clearAnimation();

        }
    }

    public class MyClassAdapter extends ArrayAdapter<Search> {


        private TextView itemView;
        private ImageView imageView;
        private RelativeLayout layout;


        public MyClassAdapter(Context context, int textViewResourceId, ArrayList<Search> items) {
            super(context, textViewResourceId, items);
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            // Use Layout inflator to call xml file
            // inflate layout for each row
            if (convertView == null) {
                convertView = LayoutInflater.from(this.getContext())
                        .inflate(R.layout.activity_main, parent, false);


                itemView = (TextView) convertView.findViewById(R.id.listTextView);
                imageView = (ImageView) convertView.findViewById(R.id.image);
                layout = (RelativeLayout) findViewById(R.id.relativeLayout2);


            }

            // retrieve data model object
            Search item = getItem(position);
            if (item != null) {

                itemView.setText(String.format("%s :- %s :- %s", item.getText(), item.getName(), item.getDateCreated()));
                imageView.setTag(String.valueOf(item.getProfile_image_url()));
                new DownloadImagesTask().execute(imageView);

                ColorList c = new ColorList();


                String example = getItem(0).getText().toLowerCase();
                Log.e(" text ", "" + example);
                if (example.contains(c.Red)) {
                    Log.e("example", "" + c.Red);
                    mlayout.setBackgroundColor(Color.RED);
                    layout.setBackgroundColor(Color.RED);
                }
                if (example.contains(c.Green)) {
                    Log.e("example", "" + c.Green);
                    mlayout.setBackgroundColor(Color.GREEN);
                    layout.setBackgroundColor(Color.GREEN);
                }
                if (example.contains(c.Yellow)) {
                    Log.e("example", "" + c.Yellow);
                    mlayout.setBackgroundColor(Color.YELLOW);
                    layout.setBackgroundColor(Color.YELLOW);
                }

                if (example.contains(c.Blue)) {
                    Log.e("example", "" + c.Blue);
                    mlayout.setBackgroundColor(Color.BLUE);
                    layout.setBackgroundColor(Color.BLUE);
                }
                if (example.contains(c.Lavender)) {
                    Log.e("example", "" + c.Lavender);
                    mlayout.setBackgroundColor(Color.parseColor("#F1A7FE"));
//                        viewHolder.layout.setBackgroundColor(Color.RED);
                }
                if (example.contains(c.QueenBlue)) {
                    Log.e("example", "" + c.QueenBlue);
                    mlayout.setBackgroundColor(Color.parseColor("#436B95"));
//                        viewHolder.layout.setBackgroundColor(Color.RED);
                }
                if (example.contains(c.Purple)) {
                    Log.e("example", "" + c.Purple);
                    mlayout.setBackgroundColor(Color.parseColor("#800080"));
//                        viewHolder.layout.setBackgroundColor(Color.RED);
                }
                if (example.contains(c.Peru)) {
                    Log.e("example", "" + c.Peru);
                    mlayout.setBackgroundColor(Color.parseColor("#CD853F"));
//                        viewHolder.layout.setBackgroundColor(Color.RED);
                }

                if (example.contains(c.WildStrawberry)) {
                    Log.e("example", "" + c.WildStrawberry);
                    mlayout.setBackgroundColor(Color.parseColor("#FF43A4"));
//                        viewHolder.layout.setBackgroundColor(Color.RED);
                }
                if (example.contains(c.Zaffre)) {
                    Log.e("example", "" + c.Zaffre);
                    mlayout.setBackgroundColor(Color.parseColor("#0014A8"));
//                        viewHolder.layout.setBackgroundColor(Color.RED);
                }
                if (example.contains(c.Zomp)) {
                    Log.e("example", "" + c.Zomp);
                    mlayout.setBackgroundColor(Color.parseColor("#39A78E"));
//                        viewHolder.layout.setBackgroundColor(Color.RED);
                }
                if (example.contains(c.Peach)) {
                    Log.e("example", "" + c.Peach);
                    mlayout.setBackgroundColor(Color.parseColor("#FFCBA4"));
//                        viewHolder.layout.setBackgroundColor(Color.RED);
                }
                if (example.contains(c.Pearl)) {
                    Log.e("example", "" + c.Pearl);
                    mlayout.setBackgroundColor(Color.parseColor("#EAE0C8"));
//                        viewHolder.layout.setBackgroundColor(Color.RED);
                }

                if (example.contains(c.Folly)) {
                    Log.e("example", "" + c.Folly);
                    mlayout.setBackgroundColor(Color.parseColor("#FF004F"));
//                        viewHolder.layout.setBackgroundColor(Color.RED);
                }
                if (example.contains(c.FuzzyWuzzy)) {
                    Log.e("example", "" + c.FuzzyWuzzy);
                    mlayout.setBackgroundColor(Color.parseColor("#CC6666"));
//                        viewHolder.layout.setBackgroundColor(Color.RED);
                }
                if (example.contains(c.Olivine)) {
                    Log.e("example", "" + c.Olivine);
                    mlayout.setBackgroundColor(Color.parseColor("#9AB973"));
//                        viewHolder.layout.setBackgroundColor(Color.RED);
                }
                if (example.contains(c.Rose)) {
                    Log.e("example", "" + c.Rose);
                    mlayout.setBackgroundColor(Color.parseColor("#FF007F"));
//                        viewHolder.layout.setBackgroundColor(Color.RED);
                }
                if (example.contains(c.TealGreen)) {
                    Log.e("example", "" + c.TealGreen);
                    mlayout.setBackgroundColor(Color.parseColor("#00827F"));
//                        viewHolder.layout.setBackgroundColor(Color.RED);
                }
                if (example.contains(c.TwilightLavender)) {
                    Log.e("example", "" + c.TwilightLavender);
                    mlayout.setBackgroundColor(Color.parseColor("#8A496B"));
//                        viewHolder.layout.setBackgroundColor(Color.RED);
                }
                if (example.contains(c.Violet)) {
                    Log.e("example", "" + c.Violet);
                    mlayout.setBackgroundColor(Color.parseColor("#8F00FF"));
//                        viewHolder.layout.setBackgroundColor(Color.RED);
                }
                if (example.contains(c.BlackBean)) {
                    Log.e("example", "" + c.BlackBean);
                    mlayout.setBackgroundColor(Color.parseColor("#3D0C02"));
//                        viewHolder.layout.setBackgroundColor(Color.RED);
                }
                if (example.contains(c.Charcoal)) {
                    Log.e("example", "" + c.Charcoal);
                    mlayout.setBackgroundColor(Color.parseColor("#36454F"));
//                        viewHolder.layout.setBackgroundColor(Color.RED);
                }
                if (example.contains(c.BrightPink)) {
                    Log.e("example", "" + c.BrightPink);
                    mlayout.setBackgroundColor(Color.parseColor("#FF007F"));
//                        viewHolder.layout.setBackgroundColor(Color.RED);
                }
                if (example.contains(c.BubbleGum)) {
                    Log.e("example", "" + c.BubbleGum);
                    mlayout.setBackgroundColor(Color.parseColor("#FFC1CC"));
//                        viewHolder.layout.setBackgroundColor(Color.RED);
                }
                if (example.contains(c.Cherry)) {
                    Log.e("example", "" + c.Cherry);
                    mlayout.setBackgroundColor(Color.parseColor("#DE3163"));
//                        viewHolder.layout.setBackgroundColor(Color.RED);
                }
                if (example.contains(c.CGBlue)) {
                    Log.e("example", "" + c.CGBlue);
                    mlayout.setBackgroundColor(Color.parseColor("#007AA5"));
//                        viewHolder.layout.setBackgroundColor(Color.RED);
                }
                if (example.contains(c.Cream)) {
                    Log.e("example", "" + c.Cream);
                    mlayout.setBackgroundColor(Color.parseColor("#FFFDD0"));
//                        viewHolder.layout.setBackgroundColor(Color.RED);
                }
                if (example.contains(c.White)) {
                    Log.e("example", "" + c.White);
                    mlayout.setBackgroundColor(Color.WHITE);
//                        viewHolder.layout.setBackgroundColor(Color.RED);
                }
                if (example.contains(c.Gray)) {
                    Log.e("example", "" + c.Gray);
                    mlayout.setBackgroundColor(Color.GRAY);
//                        viewHolder.layout.setBackgroundColor(Color.RED);
                }
                if (example.contains(c.Black)) {
                    Log.e("example", "" + c.Black);
                    mlayout.setBackgroundColor(Color.BLACK);
//                        viewHolder.layout.setBackgroundColor(Color.RED);
                }
                if (example.contains(c.Aqua)) {
                    Log.e("example", "" + c.Aqua);
                    mlayout.setBackgroundColor(Color.parseColor("#00FFFF"));
//                        viewHolder.layout.setBackgroundColor(Color.RED);
                }
                if (example.contains(c.Azure)) {
                    Log.e("example", "" + c.Azure);
                    mlayout.setBackgroundColor(Color.parseColor("#007FFF"));
//                        viewHolder.layout.setBackgroundColor(Color.RED);
                }
                if (example.contains(c.BabyPink)) {
                    Log.e("example", "" + c.BabyPink);
                    mlayout.setBackgroundColor(Color.parseColor("#F4C2C2"));
//                        viewHolder.layout.setBackgroundColor(Color.RED);
                }
                if (example.contains(c.BarbiePink)) {
                    Log.e("example", "" + c.BarbiePink);
                    mlayout.setBackgroundColor(Color.parseColor("#E0218A"));
//                        viewHolder.layout.setBackgroundColor(Color.RED);
                }
                if (example.contains(c.BarnRed)) {
                    Log.e("example", "" + c.BarnRed);
                    mlayout.setBackgroundColor(Color.parseColor("#7C0A02"));
//                        viewHolder.layout.setBackgroundColor(Color.RED);
                }
                if (example.contains(c.Bisque)) {
                    Log.e("example", "" + c.Bisque);
                    mlayout.setBackgroundColor(Color.parseColor("#FFE4C4"));
//                        viewHolder.layout.setBackgroundColor(Color.RED);
                }

                if (example.contains(c.Orange)) {
                    Log.e("example", "" + c.Orange);
                    mlayout.setBackgroundColor(Color.parseColor("#FF5800"));
//                        viewHolder.layout.setBackgroundColor(Color.RED);
                }
                if (example.contains(c.Cyan)) {
                    Log.e("example", "" + c.Cyan);
                    mlayout.setBackgroundColor(Color.CYAN);
//                        viewHolder.layout.setBackgroundColor(Color.RED);
                }
            }


            return convertView;
        }
    }
}

