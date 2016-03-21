package org.calcwiki.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

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
        initializeToolbarAndDrawer(R.id.toolbar, R.id.drawer_main_in_main);
    }

    protected void initializeToolbarAndDrawer(@IdRes int toolbarId, @IdRes int drawerId) {
        toolbar = (Toolbar) findViewById(toolbarId);
        drawerLayout = (DrawerLayout) findViewById(drawerId);
        mainDrawer = MainDrawer.getInstance(drawerLayout);
        setSupportActionBar(toolbar);
        materialMenu = new MaterialMenuDrawable(this, Color.BLACK, MaterialMenuDrawable.Stroke.THIN);
        getSupportActionBar().setHomeAsUpIndicator(materialMenu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mainDrawer.setToggle(this, toolbar);
    }
}
