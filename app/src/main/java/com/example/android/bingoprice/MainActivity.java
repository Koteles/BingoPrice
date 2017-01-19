package com.example.android.bingoprice;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import android.os.Handler;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import android.widget.Spinner;
import android.widget.TextView;


import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

import static android.R.attr.tag;
import static android.R.id.button1;



public class MainActivity extends AppCompatActivity {

    Button myButton, alertButton;
    CheckBox alertCheckBox;
    TextView txtResult;
    EditText text_price;       //the price I introduce in the editText
    float price;             //in price I have the price of the item I select
    int contor = 0;
    float desiredPrice;     //is the desired price and if price goes under this price I should get a notification
    private String[] items = new String[]{"Select", "2016-17 Man Utd Adidas Shirt", "2014-15 Man Utd Nike Shirt", "Man Utd Adidas Beanie", "Man Utd Adidas Hooded Zip",
                                            "Man Utd Nike Basic Core T-Shirt"};

    NotificationCompat.Builder notification;
    private String url = "http://www.uksoccershop.com//p-79012//2016-2017-Man-Utd-Adidas-Home-Football-Shirt.html";
    private String imageUrl = "http://cdn2.uksoccershop.com//images//MANCHESTER-UNITED-NIKE-2016-17-HOME-FOOTBALL-SHIRT-FRONT.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notification = new NotificationCompat.Builder(this);

        alertButton = (Button) findViewById(R.id.alert_button);
        myButton=(Button) findViewById(R.id.myButton);
        txtResult=(TextView)findViewById(R.id.txtResult);
        text_price = (EditText) findViewById(R.id.price_field);
        Spinner dropdown = (Spinner)findViewById(R.id.spinner1);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        dropdown.setAdapter(adapter);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown.setAdapter(adapter);


        dropdown.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {

                switch (arg2) {

                    case 1:
                        url = "http://www.uksoccershop.com//p-79012//2016-2017-Man-Utd-Adidas-Home-Football-Shirt.html";
                        imageUrl = "http://cdn2.uksoccershop.com//images//MANCHESTER-UNITED-NIKE-2016-17-HOME-FOOTBALL-SHIRT-FRONT.jpg";
                        break;
                    case 2:
                        url = "http://www.uksoccershop.com/p-45427/2014-15-Man-Utd-Home-Nike-Football-Shirt.html";
                        imageUrl = "http://cdn2.uksoccershop.com/images/manchester-united-2014-2015-nike-home-football-kit.jpg";
                        break;
                    case 3:
                        url = "http://www.uksoccershop.com//p-87340//2016-2017-Man-Utd-Adidas-3S-Beanie-(Mineral-Blue).html";
                        imageUrl = "http://cdn2.uksoccershop.com//images//man-utd-2016-2017-adidas-beanie-hat-mineral-blue.jpg";
                        break;
                    case 4:
                        url = "http://www.uksoccershop.com//p-88359//2016-2017-Man-Utd-Adidas-3S-Hooded-Zip-(Red).html";
                        imageUrl = "http://cdn2.uksoccershop.com//images//man-utd-2016-2017-adidas-3s-hooded-zip-red.jpg";
                        break;
                    case 5:
                        url = "http://www.uksoccershop.com/p-26400/2012-13-Man-Utd-Nike-Basic-Core-T-Shirt-(Black).html";
                        imageUrl = "http://cdn2.uksoccershop.com/images/man-utd-basic-core-t-shirt-black-2012-13.jpg";
                        break;

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }

        });


        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ion.with(getApplicationContext())
                        .load(url)
                        .asString()
                        .setCallback(new FutureCallback<String>() {
                            @Override
                            public void onCompleted(Exception e, String result) {

                                if(result.contains("itemprop='price'")) {
                                    String key = "itemprop='price' content='";
                                    int beginIndex = result.lastIndexOf(key) + key.length();    //lastIndexOf imi returneaza indexul unde apare ultima data ce caut eu
                                                                                                //eu caut stringul key in stringul result
                                    int endIndex = beginIndex + 5;

                                    int endIndexSpecialPrice = endIndex + 150;                   //caut mai departe
                                    String specialPrice = result.substring(endIndex, endIndexSpecialPrice); //stringul de dupa pretul normal
                                    String content = " ";
                                    if(specialPrice.contains("productSalePrice")){              //daca acest string contine productSalePrice => exista un pret special
                                        String key2 = "&nbsp;&euro;";
                                        int beginIndexSpecial = specialPrice.lastIndexOf(key2) + key2.length();
                                        int endIndexSpecial = beginIndexSpecial + 5;
                                        content = specialPrice.substring(beginIndexSpecial,endIndexSpecial);
                                    }
                                    else if(specialPrice.contains("productSpecialPrice")){
                                        String key2 = "&euro;";
                                        int beginIndexSpecial = specialPrice.lastIndexOf(key2) + key2.length();
                                        int endIndexSpecial = beginIndexSpecial + 5;
                                        content = specialPrice.substring(beginIndexSpecial,endIndexSpecial);
                                    }
                                    else {
                                        content = result.substring(beginIndex, endIndex);
                                    }
                                        price = Float.parseFloat(content);
                                        //contor++;         // pentru a verifica daca butonul este apasat automat de mai multe ori
                                        txtResult.setText(content + "€");
                                        getPrice();         //metoda care imi creeaza un nou proces si imi apasa pe buton automat
                                        //acest lucru se face pt a avea cel mai recent pret al produsului


                                }

                            }
                        });

                Ion.with(getApplicationContext()).load(imageUrl).withBitmap().asBitmap()
                        .setCallback(new FutureCallback<Bitmap>() {
                            @Override
                            public void onCompleted(Exception e, Bitmap result) {

                                ImageView imageView = (ImageView) findViewById(R.id.myImage);
                                imageView.setImageBitmap(result);
                            }
                        });
            }
        });

    }
    public void getPrice(){
       Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                myButton.performClick();
            }
        }, 20000);
    }
    public void notifyMe(){             //metoda care sa imi apese butonul alert automat la interval de 60 secunde
        Handler handler = new Handler();    //este necesar pt ca daca scade pretul sub pretul dorit at trebuie sa fiu notificat
        handler.postDelayed(new Runnable() {
            public void run(){
                alertButton.performClick();
            }
        }, 22000);
    }
    public void alert(View view){
        text_price = (EditText) findViewById(R.id.price_field);
        String s = text_price.getText().toString();
        alertCheckBox = (CheckBox) findViewById(R.id.checkboxAlert);
        boolean isAlert = alertCheckBox.isChecked();
        if(!s.matches("")){
            desiredPrice = Float.parseFloat(s);
        }

        if(isAlert && s.matches("")){
            Toast.makeText(this, getString(R.string.price_error), Toast.LENGTH_SHORT).show();
            return;
        }

        else if(isAlert && !s.matches("") && (desiredPrice >=  price)){

            notification.setAutoCancel(true);                               //the notification to dissapear after click on it
            notification.setDefaults(Notification.FLAG_SHOW_LIGHTS);        //to light when I have a notification
            notification.setLights(0xff00ff00, 300, 100);
            notification.setSmallIcon(R.drawable.utd2);
            notification.setWhen(System.currentTimeMillis());
            notification.setContentTitle("Bingo Price");
            notification.setContentText("The price dropped under the desired price");
            //what is going to happen when the user clicks on the notification:
            Intent intent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT); //gives the device access to the intents in my app
            notification.setContentIntent(pendingIntent);
            //builds notification and issues it
            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            nm.notify(0, notification.build());
            notifyMe();
        }



    }


}
