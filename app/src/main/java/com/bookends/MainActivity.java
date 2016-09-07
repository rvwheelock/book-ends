package com.bookends;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.common.StringUtils;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener {
    private Button scanBtn;
    private Button searchBtn;
    private Button exploreBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        TextView tx = (TextView)findViewById(R.id.title);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Alice-Regular.ttf");
        tx.setTypeface(custom_font);

        TextView tx1 = (TextView)findViewById(R.id.scan);
        Typeface custom_font1 = Typeface.createFromAsset(getAssets(), "fonts/new_athena_unicode.ttf");
        tx1.setTypeface(custom_font1);

        TextView tx2 = (TextView)findViewById(R.id.search);
        Typeface custom_font2 = Typeface.createFromAsset(getAssets(), "fonts/new_athena_unicode.ttf");
        tx2.setTypeface(custom_font2);

        TextView tx3 = (TextView)findViewById(R.id.explore);
        Typeface custom_font3 = Typeface.createFromAsset(getAssets(), "fonts/new_athena_unicode.ttf");
        tx3.setTypeface(custom_font3);

//        TextView tx1 = (TextView)findViewById(R.id.would);
//        Typeface custom_font1 = Typeface.createFromAsset(getAssets(), "fonts/Alice-Regular.ttf");
//        tx1.setTypeface(custom_font1);

        scanBtn = (Button)findViewById(R.id.scan);
        scanBtn.setOnClickListener(this);
        scanBtn.setBackgroundColor(Color.parseColor("#F29C89"));

        searchBtn = (Button)findViewById(R.id.search);
        searchBtn.setOnClickListener(this);
        searchBtn.setBackgroundColor(Color.parseColor("#F29C89"));

        exploreBtn = (Button)findViewById(R.id.explore);
        exploreBtn.setOnClickListener(this);
        exploreBtn.setBackgroundColor(Color.parseColor("#F29C89"));


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClick(View v){
        //respond to clicks
        switch (v.getId()) {

            case R.id.scan:
                IntentIntegrator scanIntegrator = new IntentIntegrator(this);
                scanIntegrator.initiateScan();
                break;

            case R.id.search:
                Intent i = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(i);
                break;

            case R.id.explore:
                Intent in = new Intent(getApplicationContext(), ExploreActivity.class);
                startActivity(in);
                break;

            default:
                break;
        }

//
//        if(v.getId()==R.id.scan){
//            //scan
//            IntentIntegrator scanIntegrator = new IntentIntegrator(this);
//            scanIntegrator.initiateScan();
//        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //retrieve scan result
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            //we have a result
            String scanContent = scanningResult.getContents();
            if (scanContent != null && !scanContent.isEmpty()) {
                Intent i = new Intent(getApplicationContext(), BookProfileActivity.class);
                i.putExtra("key",scanContent);

                /* send basic http request */
                /* google books api query will be here */
                // Instantiate the RequestQueue.
                RequestQueue queue = Volley.newRequestQueue(this);
                String url ="http://www.google.com";

                // Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Display the first 500 characters of the response string.
                                System.out.print("Response is: "+ response.substring(0,500));
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.print("That didn't work!");
                    }
                });
                // Add the request to the RequestQueue.
                queue.add(stringRequest);

                startActivity(i);
            }
            else {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "No scan data received!", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
