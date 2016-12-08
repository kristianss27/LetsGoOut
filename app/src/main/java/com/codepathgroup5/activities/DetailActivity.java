package com.codepathgroup5.activities;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepathgroup5.wanttoknow.R;
import com.squareup.picasso.Picasso;
import com.yelp.clientlib.entities.Business;

/**
 * Created by Swati on 11/15/2016.
 */

public class DetailActivity extends AppCompatActivity {

    private static final String LOG_TAG = "DetailActivity";
    Business mBusiness;

    ImageView picture, ratting;
    TextView name;
    TextView phone;
    TextView addr;
    TextView area;
    TextView city;
    TextView recentReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        name = (TextView) findViewById(R.id.detail_name);
        phone = (TextView) findViewById(R.id.detail_phone);
        addr = (TextView) findViewById(R.id.detail_addr);
        area = (TextView) findViewById(R.id.detail_area);
        city = (TextView) findViewById(R.id.detail_city);
        recentReview = (TextView) findViewById(R.id.reviews);

        picture = (ImageView) findViewById(R.id.detail_imageView);
        ratting = (ImageView) findViewById(R.id.ratting_imageView);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            mBusiness = (Business) intent.getSerializableExtra(Intent.EXTRA_TEXT);
            name.setText(mBusiness.name());
            phone.setText(mBusiness.phone());
            addr.setText(mBusiness.location().displayAddress().get(0));
            if (mBusiness.location().neighborhoods()!=null){
                area.setText(mBusiness.location().neighborhoods().get(0));
            }
            city.setText(mBusiness.location().city());
            recentReview.setText(mBusiness.snippetText());

            Picasso.with(this)
                    .load(mBusiness.imageUrl())
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .resize(200, 200)
                    .centerCrop()
                    .into(picture);
            Picasso.with(this)
                    .load(mBusiness.ratingImgUrlLarge())
                    .into(ratting);
            addr.setPaintFlags(addr.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            addr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri geoLocation = Uri.parse("geo:"
                            + mBusiness.location().coordinate().latitude() + ","
                            + mBusiness.location().coordinate().longitude());
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(geoLocation);

                    if (intent.resolveActivity(getApplicationContext().getPackageManager()) != null) {
                        startActivity(intent);
                    } else {
                        Log.d(LOG_TAG, "Couldn't call " + geoLocation.toString() + ", no receiving apps installed!");
                    }
                }
            });
            recentReview.setPaintFlags(recentReview.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            recentReview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String url = mBusiness.url();
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    if (intent.resolveActivity(getApplicationContext().getPackageManager()) != null) {
                        startActivity(intent);
                    }
                }
            });
        }
    }

    @Override
    public Intent getParentActivityIntent() {
        return super.getParentActivityIntent().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    }
}
