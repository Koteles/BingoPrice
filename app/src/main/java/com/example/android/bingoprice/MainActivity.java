package com.example.android.bingoprice;

import android.graphics.Bitmap;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;

import android.widget.Spinner;
import android.widget.TextView;


import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    Button myButton;
    TextView txtResult;
    float price;        //in price I have the price of the item I select
    float desiredPrice;     //is the desired price and if price goes under this price I should get a notification
    private String[] items = new String[]{"Select", "Man Utd Adidas Home Shirt", "Man Utd Adidas Beanie", "Man Utd Adidas Anthem Jacket", "Man Utd Adidas Hooded Zip",
                                            "Man Utd Nike Basic Core T-Shirt"};

    private String[] prices = new String[]{"10","20","30","40","50","60","70"};
    private String url = "http://www.uksoccershop.com//p-79012//2016-2017-Man-Utd-Adidas-Home-Football-Shirt.html";
    private String imageUrl = "http://cdn2.uksoccershop.com//images//MANCHESTER-UNITED-NIKE-2016-17-HOME-FOOTBALL-SHIRT-FRONT.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myButton=(Button) findViewById(R.id.myButton);
        txtResult=(TextView)findViewById(R.id.txtResult);

        Spinner dropdown = (Spinner)findViewById(R.id.spinner1);
        Spinner dropdown2 = (Spinner)findViewById(R.id.spinner2);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        dropdown.setAdapter(adapter);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, prices);
        dropdown2.setAdapter(adapter2);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown.setAdapter(adapter);

        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown2.setAdapter(adapter2);



        dropdown2.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                switch (arg2) {

                    case 1:
                        desiredPrice = 10;
                        break;
                    case 2:
                        desiredPrice = 20;
                        break;
                    case 3:
                        desiredPrice = 30;
                        break;
                    case 4:
                        desiredPrice = 40;
                        break;
                    case 5:
                        desiredPrice = 50;
                        break;
                    case 6:
                        desiredPrice = 60;
                        break;
                    case 7:
                        desiredPrice = 70;
                        break;
                }
            }
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });


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
                        url = "http://www.uksoccershop.com//p-87340//2016-2017-Man-Utd-Adidas-3S-Beanie-(Mineral-Blue).html";
                        imageUrl = "http://cdn2.uksoccershop.com//images//man-utd-2016-2017-adidas-beanie-hat-mineral-blue.jpg";
                        break;
                    case 3:
                        url = "http://www.uksoccershop.com//p-79039//2016-2017-Man-Utd-Adidas-Anthem-Jacket-(Red).html";
                        imageUrl = "http://cdn2.uksoccershop.com//images//MANCHESTER-UNITED-NIKE-2016-17-ANTHEM-JACKET-RED.jpg";
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
                                if(result.contains("itemprop='price'"));
                                String key = "itemprop='price' content='";
                                int beginIndex = result.lastIndexOf(key) + key.length();
                                int endIndex = beginIndex + 5;
                                String content = result.substring(beginIndex, endIndex);
                                price = Float.parseFloat(content);
                                txtResult.setText(content + "€");


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

}
