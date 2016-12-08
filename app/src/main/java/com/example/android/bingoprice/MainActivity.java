package com.example.android.bingoprice;

import android.content.Intent;
import android.graphics.Bitmap;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;


public class MainActivity extends AppCompatActivity {

    Button myButton, alertButton;
    CheckBox alertCheckBox;
    TextView txtResult, text;
    EditText text_price;       //the price I introduce in the editText
    float price;             //in price I have the price of the item I select
    float desiredPrice;     //is the desired price and if price goes under this price I should get a notification
    private String[] items = new String[]{"Select", "Man Utd Adidas Home Shirt", "Man Utd Adidas Beanie", "Man Utd Adidas Anthem Jacket", "Man Utd Adidas Hooded Zip",
                                            "Man Utd Nike Basic Core T-Shirt"};

    private String url = "http://www.uksoccershop.com//p-79012//2016-2017-Man-Utd-Adidas-Home-Football-Shirt.html";
    private String imageUrl = "http://cdn2.uksoccershop.com//images//MANCHESTER-UNITED-NIKE-2016-17-HOME-FOOTBALL-SHIRT-FRONT.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        alertButton = (Button) findViewById(R.id.alert_button);
        myButton=(Button) findViewById(R.id.myButton);
        txtResult=(TextView)findViewById(R.id.txtResult);
        text = (TextView) findViewById(R.id.text);
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
                                text.setText("The current price of the item is:");

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

    public void alert(View view){
        text_price = (EditText) findViewById(R.id.price_field);
        String s = text_price.getText().toString();
        alertCheckBox = (CheckBox) findViewById(R.id.checkboxAlert);
        boolean isAlert = alertCheckBox.isChecked();
        if(!s.matches("")){
            desiredPrice = Float.parseFloat(s);
        }
        if(isAlert && s.matches("")){
            Toast.makeText(this, "You did not enter a price", Toast.LENGTH_SHORT).show();
            return;
        }

        else if(isAlert && !s.matches("") && (desiredPrice >=  price)){

            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:")); // only email apps should handle this
            intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"koteles.krisztian@yahoo.ro"});
            intent.putExtra(Intent.EXTRA_SUBJECT, "BingoPrice");
            intent.putExtra(Intent.EXTRA_SUBJECT, "Pretul nou este: " + price);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }



    }


}
