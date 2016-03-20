package org.calcwiki.ui.activity;

import android.graphics.Color;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.balysv.materialmenu.MaterialMenuDrawable;

import org.calcwiki.R;
import org.calcwiki.ui.drawer.MainDrawer;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    MaterialMenuDrawable materialMenu;
    DrawerLayout drawerLayout;
    MainDrawer mainDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_main_in_main);
        mainDrawer = MainDrawer.getInstance(drawerLayout);
        toolbar = ((Toolbar) findViewById(R.id.toolbar));
        setSupportActionBar(toolbar);
        materialMenu = new MaterialMenuDrawable(this, Color.BLACK, MaterialMenuDrawable.Stroke.THIN);
        toolbar.setNavigationOnClickListener(new DrawerOpenListener());
        getSupportActionBar().setHomeAsUpIndicator(materialMenu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mainDrawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                materialMenu.animateIconState(MaterialMenuDrawable.IconState.ARROW);
                toolbar.setNavigationOnClickListener(new DrawerCloseListener());
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                materialMenu.animateIconState(MaterialMenuDrawable.IconState.BURGER);
                toolbar.setNavigationOnClickListener(new DrawerOpenListener());
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    class DrawerOpenListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            materialMenu.animateIconState(MaterialMenuDrawable.IconState.ARROW);
            mainDrawer.openDrawer();
            toolbar.setNavigationOnClickListener(new DrawerCloseListener());
        }
    }

    class DrawerCloseListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            materialMenu.animateIconState(MaterialMenuDrawable.IconState.BURGER);
            mainDrawer.closeDrawer();
            toolbar.setNavigationOnClickListener(new DrawerOpenListener());
        }
    }
}
