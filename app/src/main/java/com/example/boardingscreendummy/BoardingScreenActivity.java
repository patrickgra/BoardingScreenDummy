package com.example.boardingscreendummy;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class BoardingScreenActivity extends AppCompatActivity {

    private Preferences preferences;
    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private LinearLayout dotsLayout;
    private TextView[]dots;
    private int[] layouts;
    private Button btnSkip, btnNext;

    /*
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = new Preferences(this);
        if(!preferences.isFirstTimeLaunch()){
            launchHomeScreen();
            finish(); //unnecessary?
        }

        setContentView(R.layout.activity_boarding_screen);
        changeStatusBarColor();
        initiateVariables();
    }

    private void initiateVariables(){
        viewPager = findViewById(R.id.view_pager);
        dotsLayout = findViewById(R.id.bs_layoutDots);
        btnSkip = findViewById(R.id.bs_btn_skip);
        btnNext = findViewById(R.id.bs_btn_next);

        layouts = new int[]{
                R.layout.bs_screen1,
                R.layout.bs_screen2,
                R.layout.bs_screen3
        };

        initiateViewPager();

        btnSkip.setOnClickListener(view -> launchHomeScreen());
        btnNext.setOnClickListener(view -> {
            int current = getItem(1);
            if (current < layouts.length){
                viewPager.setCurrentItem(current);
            }else {
                launchHomeScreen();
            }
        });
    }

    private void initiateViewPager(){
        ViewPager.OnPageChangeListener viewPagerPageOnChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                addBottomDots(position);

                if (position == layouts.length-1){
                    btnNext.setText(getString(R.string.start));
                    btnSkip.setVisibility(View.GONE);
                }else {
                    btnNext.setText(getString(R.string.next));
                    btnSkip.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        };

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageOnChangeListener);

    }*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Checking for first time launch
        preferences = new Preferences(this);
        if (!preferences.isFirstTimeLaunch()) {
            launchHomeScreen();
            finish();
        }

        setContentView(R.layout.activity_boarding_screen);

        initiateVariables();
        addBottomDots(0);
        changeStatusBarColor();

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        btnSkip.setOnClickListener(v -> launchHomeScreen());

        btnNext.setOnClickListener(v -> {
            // if last page home screen will be launched
            int current = getItem(+1);
            if (current < layouts.length) {
                // move to next screen
                viewPager.setCurrentItem(current);
            } else {
                launchHomeScreen();
            }
        });
    }

    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);

            // adjusting buttons depending on slide
            if (position == layouts.length - 1) {
                btnNext.setText(getString(R.string.start));
                btnSkip.setVisibility(View.GONE);
            } else {
                btnNext.setText(getString(R.string.next));
                btnSkip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    };

    private void initiateVariables(){
        viewPager = findViewById(R.id.view_pager);
        dotsLayout = findViewById(R.id.bs_layoutDots);
        btnSkip = findViewById(R.id.bs_btn_skip);
        btnNext = findViewById(R.id.bs_btn_next);


        // layouts of all welcome sliders
        layouts = new int[]{
                R.layout.bs_screen1,
                R.layout.bs_screen2,
                R.layout.bs_screen3};
    }

    private int getItem(int i){
        return viewPager.getCurrentItem() + i;
    }

    private void launchHomeScreen(){
        preferences.setFirstTimeLaunch(false);
        startActivity(new Intent(BoardingScreenActivity.this, MainActivity.class));
        finish();
    }

    // making notification bar transparent
    private void changeStatusBarColor(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private void addBottomDots(int currentPage){
        dots = new TextView[layouts.length];

        int[] colorActive = getResources().getIntArray(R.array.bs_array_dot_active);
        int[] colorInactive = getResources().getIntArray(R.array.bs_array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++){
            dots[i] = new TextView(this);
            //android >= 24
            dots[i].setText(Html.fromHtml("&#8226", Html.FROM_HTML_MODE_COMPACT));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }
        if(dots.length > 0){
            dots[currentPage].setTextColor(colorActive[currentPage]);
        }
    }

    public class MyViewPagerAdapter extends PagerAdapter{

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            LayoutInflater layoutInflater = getLayoutInflater();
            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}
