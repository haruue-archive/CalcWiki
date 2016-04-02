package org.calcwiki.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.jude.utils.JUtils;

import org.calcwiki.BuildConfig;
import org.calcwiki.R;
import org.calcwiki.data.storage.CurrentUser;
import org.calcwiki.data.storage.changecaller.CurrentUserChangeCaller;
import org.calcwiki.ui.drawer.MainDrawer;
import org.calcwiki.ui.fragment.SearchFragment;
import org.calcwiki.ui.util.CurrentStateStorager;

public class MainActivity extends AppCompatActivity implements CurrentUserChangeCaller.CurrentUserChangeListener {

    Toolbar toolbar;
    MaterialMenuDrawable materialMenu;
    DrawerLayout drawerLayout;
    MainDrawer mainDrawer;
    EditText searchEditText;
    InputMethodManager inputMethodManager;
    Handler handler;
    int currentOptionsMenuStatus = -1;
    String currentFragmentTag;

    public class OptionsMenuButtons {
        public final static int ACTION_SEARCH = 1;
        public final static int ACTION_EDIT = 2;
        public final static int ACTION_VIEW_SOURCE = 4;
        public final static int ACTION_CREATE = 8;
        public final static int ACTION_HISTORY = 16;
        public final static int ACTION_MOVE = 32;
        public final static int ACTION_DELETE = 64;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Initialize JUtils
        JUtils.setDebug(BuildConfig.DEBUG, this.getClass().getName());
        initializeToolbarAndDrawer(R.id.toolbar, R.id.drawer_main_in_main);
        inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        // Initialize Handler
        handler = new Handler(getMainLooper());
    }

    protected void initializeToolbarAndDrawer(@IdRes int toolbarId, @IdRes int drawerId) {
        toolbar = (Toolbar) findViewById(toolbarId);
        drawerLayout = (DrawerLayout) findViewById(drawerId);
        mainDrawer = MainDrawer.getInstance(drawerLayout);
        toolbar.setTitle(R.string.calcwiki);
        setSupportActionBar(toolbar);
        materialMenu = new MaterialMenuDrawable(this, Color.BLACK, MaterialMenuDrawable.Stroke.THIN);
        getSupportActionBar().setHomeAsUpIndicator(materialMenu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mainDrawer.setToggle(this, toolbar);
        mainDrawer.setCloseIMEOnDrawerStateChange(this);
        toolbar.setOnMenuItemClickListener(new Listener());
        // Initialize widget in toolbar
        searchEditText = (EditText) findViewById(R.id.edittext_search_in_toolbar);
        searchEditText.setVisibility(View.GONE);
        searchEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && searchEditText.getText().toString().equals("")) {
                    searchEditText.setVisibility(View.GONE);
                }
            }
        });
        CurrentUserChangeCaller.getInstance().addCurrentUserListener(this);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (searchEditText.getVisibility() == View.VISIBLE) {
            searchEditText.setText("");
            searchEditText.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onCurrentUserChange() {
        invalidateOptionsMenu();
    }

    public class Listener implements Toolbar.OnMenuItemClickListener {

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_search:
                    if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                        drawerLayout.closeDrawer(GravityCompat.START);
                    }
                    if (searchEditText.getVisibility() == View.GONE) {
                        searchEditText.setVisibility(View.VISIBLE);
                        searchEditText.requestFocus();
                        inputMethodManager.showSoftInput(searchEditText, InputMethodManager.SHOW_IMPLICIT);
                    } else {
                        doSearch();
                    }
                    break;
            }
            return false;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_in_toolbar, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (currentOptionsMenuStatus == -1) {
            if (CurrentUser.getInstance().isLogin) {
                currentOptionsMenuStatus = OptionsMenuButtons.ACTION_SEARCH | OptionsMenuButtons.ACTION_VIEW_SOURCE | OptionsMenuButtons.ACTION_HISTORY;
            } else {
                currentOptionsMenuStatus = OptionsMenuButtons.ACTION_SEARCH | OptionsMenuButtons.ACTION_EDIT | OptionsMenuButtons.ACTION_HISTORY | OptionsMenuButtons.ACTION_MOVE;
            }
        }
        menu.findItem(R.id.action_search).setVisible((currentOptionsMenuStatus & OptionsMenuButtons.ACTION_SEARCH) != 0);
        menu.findItem(R.id.action_edit).setVisible((currentOptionsMenuStatus & OptionsMenuButtons.ACTION_EDIT) != 0);
        menu.findItem(R.id.action_view_source).setVisible((currentOptionsMenuStatus & OptionsMenuButtons.ACTION_VIEW_SOURCE) != 0);
        menu.findItem(R.id.action_create).setVisible((currentOptionsMenuStatus & OptionsMenuButtons.ACTION_CREATE) != 0);
        menu.findItem(R.id.action_history).setVisible((currentOptionsMenuStatus & OptionsMenuButtons.ACTION_HISTORY) != 0);
        menu.findItem(R.id.action_move).setVisible((currentOptionsMenuStatus & OptionsMenuButtons.ACTION_MOVE) != 0);
        menu.findItem(R.id.action_delete).setVisible((currentOptionsMenuStatus & OptionsMenuButtons.ACTION_DELETE) != 0);
        return true;
    }

    public void setOptionsMenuStatus(int status) {
        currentOptionsMenuStatus = status;
        invalidateOptionsMenu();
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_ENTER:
                if (searchEditText.hasFocus()) {
                    doSearch();
                }
                return true;
            default:
                return super.onKeyUp(keyCode, event);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (MainDrawer.getInstance() != null) {
            MainDrawer.getInstance().checkLogin();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        CurrentStateStorager.save(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        CurrentStateStorager.restore(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CurrentUserChangeCaller.getInstance().removeCurrentUserListener(this);
    }

    public void doSearch() {
        JUtils.closeInputMethod(this);
        // Check empty and satisfy someone who enjoy the appearing and disappearing of the search view
        if (searchEditText.getText().toString().equals("")) {
            searchEditText.setVisibility(View.GONE);
            return;
        }
        String keyWord = searchEditText.getText().toString();
        SearchFragment fragment = new SearchFragment();
        fragment.initialize(keyWord);
        currentFragmentTag = "search://" + keyWord;
        getFragmentManager().beginTransaction().replace(R.id.container, fragment, currentFragmentTag).commit();
    }
}
