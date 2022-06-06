package com.myapp.dictionary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;
import com.myapp.GlobalVariables;
import com.myapp.R;
import com.myapp.adapter.SliderAdapter;
import com.myapp.adapter.ViewPagerAdapterProdDetail;
import com.myapp.dtbassethelper.DatabaseAccess;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

public class Product_DictionaryActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapterProdDetail viewPagerAdapterProdDetail;
SliderView sliderView;
int [] sliderImages = {R.drawable.dictionary_black, R.drawable.dictionary_blue, R.drawable.dictionary_red};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_dictionary);

        setControl();
        setEvent();
    }
    public void setControl(){
        sliderView = findViewById(R.id.image_slider);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPagerAdapterProdDetail = new ViewPagerAdapterProdDetail(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapterProdDetail);
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }
    public void setEvent(){
        SliderAdapter sliderAdapter = new SliderAdapter(sliderImages);
        sliderView.setSliderAdapter(sliderAdapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
        sliderView.startAutoCycle();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater findMenuItems = getMenuInflater();
        findMenuItems.inflate(R.menu.product_dictionary, menu);
//        optionsMenu = menu;
//        menuSave_unSaveWord = (MenuItem) optionsMenu.findItem(R.id.menu_save_unsave);
//        if (GlobalVariables.listSavedWordId.contains(savedWord.getId())) {
//            unsave = true;
//            menuSave_unSaveWord.setIcon(R.drawable.icons8_filled_bookmark_ribbon_32px_1);
//        } else {
//            unsave = false;
//            menuSave_unSaveWord.setIcon(R.drawable.icons8_bookmark_outline_32px);
//        }
//        if (GlobalVariables.userId.equalsIgnoreCase("") || GlobalVariables.userId == null) {
//            menuSave_unSaveWord.setVisible(false);
//        } else {
//            menuSave_unSaveWord.setVisible(true);
//        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_chat:

                break;

        }
        return super.onOptionsItemSelected(item);
    }
}