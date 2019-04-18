package asus.WeatherApp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Time;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.MobileAds;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity {

    String sehirid;
    String sehiradı;
    String ilceadı;
    String ilcekodu;
    String sehirWebserviceURL = "https://ezanvakti.herokuapp.com/sehirler?ulke=2";


    private String[] namaz = new String[7];
    private String[] json = new String[7];
    private String[] sehiradıarr = new String[1];
    private String[] ilceadıarr = new String[1];
    private String[] il = new String[81];
    private String[] il2 = new String[81];
    ArrayList<String> ilce = new ArrayList<String>();
    private String[] ilce2 = new String[20];



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // link the XML layout to this JAVA class
        setContentView(R.layout.activity_main);


        MobileAds.initialize(this, "ca-app-pub-5022701894978172~4956426400");

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                Toast.makeText(getApplicationContext(), "Ad Closed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                Toast.makeText(getApplicationContext(), "Ad Failed To Load", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
                Toast.makeText(getApplicationContext(), "Ad Opened", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Toast.makeText(getApplicationContext(), "Ad Loaded", Toast.LENGTH_SHORT).show();
            }
        });


        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        sehiradı=sharedPref.getString("sehiradı","0");
        sehiradıarr[0]=sehiradı;
        ilceadı=sharedPref.getString("ilceadı","0");
        ilceadıarr[0]=ilceadı;
        sehirid = sharedPref.getString("sehirid", "0");
        ilcekodu = sharedPref.getString("ilcekodu", "0");


        if (ilcekodu.equals("0")) {
            jso();
        }
        else{
            jso2();
        }
        Button bttn = (Button)findViewById(R.id.button);
        bttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                ilcekodu = "0";
                editor.putString("ilcekodu", ilcekodu);
                TextView il=findViewById(R.id.textView4);
                TextView ilce=findViewById(R.id.textView5);
                il.setVisibility(View.GONE);
                ilce.setVisibility(View.GONE);
                editor.commit(); //Kayıt
                Spinner spn = (Spinner) findViewById(R.id.spinner);
                spn.setVisibility(View.VISIBLE);


                Spinner spn2 = (Spinner) findViewById(R.id.spinner2);
                spn2.setVisibility(View.VISIBLE);

                jso();

            }
        });
    }

        public void jso(){
            final JsonArrayRequest jsonArrReq = new JsonArrayRequest(Request.Method.GET,
                    sehirWebserviceURL, null, new Response.Listener<JSONArray>() {

                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onResponse(JSONArray response) {
                    // Parsing json object response
                    // response will be a json object
                    final Spinner sehir = (Spinner) findViewById(R.id.spinner);

                    try {

                        for (int i = 0; i < 81; i++) {
                            final JSONObject object = response.getJSONObject(i);
                            il[i] = object.getString("SehirAdi");
                            il2[i] = object.getString("SehirID");
                        }
                        sehir.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                SharedPreferences sharedPref = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                sehiradı = il[parent.getSelectedItemPosition()];
                                sehirid = il2[parent.getSelectedItemPosition()];
                                editor.putString("sehirid", sehirid);
                                editor.putString("sehiradı",sehiradı);
                                editor.commit();
                                final JsonArrayRequest jsonArReq = new JsonArrayRequest(Request.Method.GET,
                                        " https://ezanvakti.herokuapp.com/ilceler?sehir=" + sehirid, null, new Response.Listener<JSONArray>() {

                                    @RequiresApi(api = Build.VERSION_CODES.N)
                                    @Override
                                    public void onResponse(JSONArray response) {
                                        // Parsing json object response
                                        // response will be a json object
                                        final Spinner ice = (Spinner) findViewById(R.id.spinner2);

                                        try {
                                            for (int i = 0; i < response.length(); i++) {

                                                JSONObject object = response.getJSONObject(i);

                                                ilce.add(object.getString("IlceAdi"));
                                                ilce2[i] = object.getString("IlceID");
                                            }
                                            ice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                @Override
                                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                                    SharedPreferences sharedPref = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
                                                    SharedPreferences.Editor editor = sharedPref.edit();
                                                    ilceadı=parent.getSelectedItem().toString();
                                                    ilcekodu = ilce2[parent.getSelectedItemPosition()];
                                                    editor.putString("ilcekodu", ilcekodu);
                                                    editor.putString("ilceadı",ilceadı);
                                                    editor.commit(); //Kayıt
                                                    final JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET,
                                                            "https://ezanvakti.herokuapp.com/vakitler?ilce=" + ilcekodu, null, new Response.Listener<JSONArray>() {

                                                        @RequiresApi(api = Build.VERSION_CODES.N)
                                                        @Override
                                                        public void onResponse(JSONArray response) {
                                                            // Parsing json object response
                                                            // response will be a json object
                                                            TextView txt=findViewById(R.id.textView);
                                                            final ListView list = (ListView) findViewById(R.id.list);

                                                            try {

                                                                JSONObject object = response.getJSONObject(0);

                                                                json[0]=object.getString("MiladiTarihUzun");
                                                                json[1]=object.getString("Imsak");
                                                                json[2]=object.getString("Gunes");
                                                                json[3]=object.getString("Ogle");
                                                                json[4]=object.getString("Ikindi");
                                                                json[5]=object.getString("Aksam");
                                                                json[6]=object.getString("Yatsi");
                                                                namaz[0] = json[0];
                                                                namaz[1] = "         İmsak:  " + json[1];
                                                                namaz[2] = "         Güneş:  " + json[2];
                                                                namaz[3] = "         Öğle:     " + json[3];
                                                                namaz[4] = "         İkindi:    " + json[4];
                                                                namaz[5] = "         Akşam:  " + json[5];
                                                                namaz[6] = "         Yatsı:     " + json[6];

                                                                saat();
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }

                                                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, namaz);

                                                            list.setAdapter(adapter);


                                                        }


                                                    }, new Response.ErrorListener() {

                                                        @Override
                                                        public void onErrorResponse(VolleyError error) {
                                                            VolleyLog.d("tag", "Error: " + error.getMessage());
                                                            Toast.makeText(getApplicationContext(), "Namaz Error while loading ... ", Toast.LENGTH_SHORT).show();
                                                            // hide the progress dialog

                                                        }
                                                    });
                                                    AppController.getInstance(MainActivity.this).addToRequestQueue(jsonObjReq);
                                                }

                                                @Override
                                                public void onNothingSelected(AdapterView<?> parent) {

                                                }
                                            });
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, ilce);
                                        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                        ice.setAdapter(adapter2);


                                    }


                                }, new Response.ErrorListener() {

                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        VolleyLog.d("tag", "Error: " + error.getMessage());
                                        Toast.makeText(getApplicationContext(), "Ilce Error while loading ... ", Toast.LENGTH_SHORT).show();
                                        // hide the progress dialog

                                    }
                                });
                                AppController.getInstance(MainActivity.this).addToRequestQueue(jsonArReq);
                                ilce.clear();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, il);
                    adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    sehir.setAdapter(adapter1);


                }


            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d("tag", "Error: " + error.getMessage());
                    Toast.makeText(getApplicationContext(), "İl Error while loading ... ", Toast.LENGTH_SHORT).show();
                    // hide the progress dialog

                }
            });


            // Adding request to request queue

            AppController.getInstance(this).addToRequestQueue(jsonArrReq);
        }
        public void jso2(){
            Spinner spn = (Spinner) findViewById(R.id.spinner);
            spn.setVisibility(View.GONE);
            TextView il=findViewById(R.id.textView4);
            il.setText(sehiradıarr[0]);

            Spinner spn2 = (Spinner) findViewById(R.id.spinner2);
            spn2.setVisibility(View.GONE);
            TextView ilce=findViewById(R.id.textView5);
            ilce.setText(ilceadıarr[0]);


            final JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET,
                    "https://ezanvakti.herokuapp.com/vakitler?ilce=" + ilcekodu, null, new Response.Listener<JSONArray>() {

                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onResponse(JSONArray response) {
                    // Parsing json object response
                    // response will be a json object
                    final ListView list = (ListView) findViewById(R.id.list);

                    try {

                        JSONObject object = response.getJSONObject(0);
                        json[0]=object.getString("MiladiTarihUzun");
                        json[1]=object.getString("Imsak");
                        json[2]=object.getString("Gunes");
                        json[3]=object.getString("Ogle");
                        json[4]=object.getString("Ikindi");
                        json[5]=object.getString("Aksam");
                        json[6]=object.getString("Yatsi");
                        namaz[0] = json[0];
                        namaz[1] = "         İmsak:  " + json[1];
                        namaz[2] = "         Güneş:  " + json[2];
                        namaz[3] = "         Öğle:     " + json[3];
                        namaz[4] = "         İkindi:    " + json[4];
                        namaz[5] = "         Akşam:  " + json[5];
                        namaz[6] = "         Yatsı:     " + json[6];
saat();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, namaz);

                    list.setAdapter(adapter);


                }


            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d("tag", "Error: " + error.getMessage());
                    Toast.makeText(getApplicationContext(), "Namaz Error while loading ... ", Toast.LENGTH_SHORT).show();
                    // hide the progress dialog

                }
            });
            AppController.getInstance(MainActivity.this).addToRequestQueue(jsonObjReq);

        }




    public void saat() {
        final TextView txt=findViewById(R.id.textView);
        int interTime;
        final int interMin;
        int sysMin;
        int nmzMin;
        RelativeLayout rl=findViewById(R.id.rltv);
        Time today = new Time(Time.getCurrentTimezone());
        today.setToNow();
        String a=String.valueOf(today.format("%k"));
        String b=String.valueOf(today.format("%M"));
        String c=String.valueOf(today.format("%S"));

double d=Double.parseDouble(a);
double e=Double.parseDouble(b);
final double f=Double.parseDouble(c);

        int g=Integer.parseInt(json[1].substring(1,2));
        int h=Integer.parseInt(json[2].substring(1,2));
        int j=Integer.parseInt(json[3].substring(0,2));
        int k=Integer.parseInt(json[4].substring(0,2));
        int l=Integer.parseInt(json[5].substring(0,2));
        int m=Integer.parseInt(json[6].substring(0,2));

        if(g<d && d<=h){
            nmzMin=(h*60)+Integer.parseInt(json[2].substring(3,5));
            sysMin= (int) ((d*60)+e);
            interTime=nmzMin-sysMin;
            rl.setBackgroundColor(0x00F2FF);

        }
        else if(h<d && d<=j){
            nmzMin=(j*60)+Integer.parseInt(json[3].substring(3,5));
            sysMin= (int) ((d*60)+e);
            interTime=nmzMin-sysMin;
            rl.setBackgroundColor(0xFFFE00);
        }
        else if(j<d && d<=k){
            nmzMin=(k*60)+Integer.parseInt(json[4].substring(3,5));
            sysMin= (int) ((d*60)+e);
            interTime=nmzMin-sysMin;
        }
        else if(k<d && d<=l){
            nmzMin=(l*60)+Integer.parseInt(json[5].substring(3,5));
            sysMin= (int) ((d*60)+e);
            interTime=nmzMin-sysMin;
        }
        else if(l<d && d<=m){
            nmzMin=(m*60)+Integer.parseInt(json[6].substring(3,5));
            sysMin= (int) ((d*60)+e);
            interTime=nmzMin-sysMin;
        }
        else{
            nmzMin=(g*60)+Integer.parseInt(json[1].substring(3,5));
            sysMin= (int) ((d*60)+e);
            interTime=nmzMin-sysMin;
        }

       int time=interTime*60000;
       final int ns= ((int) f)*1000;
       final int ns2=60-ns;
        new CountDownTimer(time, 1000) {
            public void onTick(long millisUntilFinished) {
                txt.setText(""+String.format("%d Saat, %d Dakika, %d Saniye",
                        TimeUnit.MILLISECONDS.toHours( millisUntilFinished),
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)-TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }
            public void onFinish() {
            System.exit(0);
        }
    }.start();

    }




}


