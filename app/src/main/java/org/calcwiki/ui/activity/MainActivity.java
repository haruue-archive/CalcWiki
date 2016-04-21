package org.calcwiki.ui.activity;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
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
import android.widget.TextView;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.jude.utils.JUtils;

import org.calcwiki.BuildConfig;
import org.calcwiki.R;
import org.calcwiki.data.network.helper.QueryApiHelper;
import org.calcwiki.data.storage.CurrentFragment;
import org.calcwiki.data.storage.CurrentUser;
import org.calcwiki.data.storage.changecaller.CurrentUserChangeCaller;
import org.calcwiki.ui.drawer.MainDrawer;
import org.calcwiki.ui.fragment.PageFragment;
import org.calcwiki.ui.fragment.SearchFragment;
import org.calcwiki.ui.item.MainDrawerHeader;
import org.calcwiki.ui.util.CurrentStateStorager;

public class MainActivity extends AppCompatActivity implements CurrentUserChangeCaller.CurrentUserChangeListener {

    Toolbar toolbar;
    MaterialMenuDrawable materialMenu;
    DrawerLayout drawerLayout;
    MainDrawer mainDrawer;
    EditText searchEditText;
    InputMethodManager inputMethodManager;
    public Listener listener = new Listener();
    int currentOptionsMenuStatus = -1;

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
        showPage("计算器百科:首页");
    }

    protected void initializeToolbarAndDrawer(@IdRes int toolbarId, @IdRes int drawerId) {
        toolbar = (Toolbar) findViewById(toolbarId);
        drawerLayout = (DrawerLayout) findViewById(drawerId);
        mainDrawer = MainDrawer.getInstance(drawerLayout);
        setSupportActionBar(toolbar);
        setTitle(getResources().getString(R.string.calcwiki));
        materialMenu = new MaterialMenuDrawable(this, Color.BLACK, MaterialMenuDrawable.Stroke.THIN);
        getSupportActionBar().setHomeAsUpIndicator(materialMenu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mainDrawer.setToggle(this, toolbar);
        mainDrawer.setCloseIMEOnDrawerStateChange(this);
        toolbar.setOnMenuItemClickListener(listener);
        // Initialize widget in toolbar
        searchEditText = (EditText) findViewById(R.id.edittext_search_in_toolbar);
        hideSearchBar();
        searchEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && searchEditText.getText().toString().equals("")) {
                    searchEditText.setVisibility(View.GONE);
                }
            }
        });
        searchEditText.setOnKeyListener(listener);
        CurrentUserChangeCaller.getInstance().addCurrentUserListener(this);
        CurrentUser.getInstance().refreshCurrentUser();
        QueryApiHelper.getBaseUserInfo(listener);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {  // Check Drawer
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (searchEditText.getVisibility() == View.VISIBLE) {  // Check Search Bar
            searchEditText.setText("");
            hideSearchBar();
        } else if (CurrentFragment.getInstance().hasPrev()) {  // Press back key for prev fragment
            CurrentFragment.FragmentInfo info = CurrentFragment.getInstance().getPrevFragmentInfo();
            if (getFragmentManager().findFragmentByTag(info.tag) != null) {
                setFragment(getFragmentManager().findFragmentByTag(info.tag), info.tag);
            } else {
                CurrentFragment.InitializibleFragment fragment = info.getReinitializeFragmentInstance();
                setFragment(fragment, info.tag);
            }
        } else {  // exit
            super.onBackPressed();
        }
    }

    @Override
    public void onCurrentUserChange() {
        invalidateOptionsMenu();
    }

    public class Listener implements Toolbar.OnMenuItemClickListener, View.OnKeyListener, QueryApiHelper.OnGetBaseUserInfoListener {

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_search:
                    if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                        drawerLayout.closeDrawer(GravityCompat.START);
                    }
                    if (searchEditText.getVisibility() == View.GONE) {
                        showSearchBar();
                        searchEditText.requestFocus();
                        inputMethodManager.showSoftInput(searchEditText, InputMethodManager.SHOW_IMPLICIT);
                    } else {
                        doSearch();
                    }
                    break;
            }
            return false;
        }

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            switch (v.getId()) {
                case R.id.edittext_search_in_toolbar:
                    if (keyCode == KeyEvent.KEYCODE_ENTER) {
                        doSearch();
                        return true;
                    }
                    break;
            }
            return false;
        }

        @Override
        public void onGetBaseUserInfo() {

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
    protected void onResume() {
        super.onResume();
        if (MainDrawerHeader.getInstance() != null) {
            MainDrawerHeader.getInstance().refresh();
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
            hideSearchBar();
            return;
        }
        String keyWord = searchEditText.getText().toString();
        SearchFragment fragment = new SearchFragment();
        fragment.initialize(keyWord);
        String tag = "search://" + keyWord;
        setFragment(fragment, tag);
        CurrentFragment.getInstance().push(fragment);
        searchEditText.setText("");
        hideSearchBar();
    }

    public void setFragment(Fragment fragment, String tag) {
        getFragmentManager().beginTransaction().replace(R.id.container, fragment, tag).commit();
    }

    public void setTitle(String text) {
        ((TextView) toolbar.findViewById(R.id.title_in_toolbar)).setText(text);
    }

    public void showSearchBar() {
        searchEditText.setVisibility(View.VISIBLE);
        // Make title gone
        toolbar.findViewById(R.id.title_in_toolbar).setVisibility(View.GONE);
    }

    public void hideSearchBar() {
        // show title
        toolbar.findViewById(R.id.title_in_toolbar).setVisibility(View.VISIBLE);
        searchEditText.setVisibility(View.GONE);
    }

    public void showPage(String pageName) {
        PageFragment fragment = new PageFragment();
        fragment.initialize(pageName);
        String tag = "page://" + pageName;
        setFragment(fragment, tag);
        CurrentFragment.getInstance().push(fragment);
    }

    public void showPageWithoutRedirect(String pageName) {
        pageName = pageName + "#NO_REDIRECT";
        showPage(pageName);
    }

    public void createPage(String pageName) {
        // TODO: complete it
        JUtils.Toast("创建页面" + pageName);
    }
}
