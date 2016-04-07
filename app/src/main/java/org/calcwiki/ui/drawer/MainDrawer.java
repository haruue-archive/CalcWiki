package org.calcwiki.ui.drawer;

import android.app.Activity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.utils.JUtils;

import org.calcwiki.R;
import org.calcwiki.ui.activity.MainActivity;
import org.calcwiki.ui.adapter.MainDrawerMenuAdapter;
import org.calcwiki.ui.item.MainDrawerHeader;
import org.calcwiki.ui.item.MainDrawerMenuItem;
import org.calcwiki.util.Utils;

public class MainDrawer {

    static MainDrawer mainDrawer;
    DrawerLayout drawerLayout;
    EasyRecyclerView menuList;
    MainDrawerMenuAdapter menuAdapter;
    ActionBarDrawerToggle toggle;

    private MainDrawer() {

    }

    public static MainDrawer getInstance() throws NullPointerException {
        if (mainDrawer == null) {
            throw new NullPointerException("MainDrawer may not be initialize.");
        }
        return mainDrawer;
    }

    public static MainDrawer getInstance(DrawerLayout drawerLayout) {
        if (mainDrawer == null) {
            mainDrawer = new MainDrawer();
        }
        if (!drawerLayout.equals(mainDrawer.drawerLayout)) {
            mainDrawer.drawerLayout = drawerLayout;
            mainDrawer.init();
        }
        return mainDrawer;
    }

    private void init() {
        // Initialize Menu List
        this.menuList = (EasyRecyclerView) drawerLayout.findViewById(R.id.menu_list_in_drawer_main);
        menuList.setLayoutManager(new LinearLayoutManager(drawerLayout.getContext()));
        menuAdapter = new MainDrawerMenuAdapter(drawerLayout.getContext());
        menuAdapter.setOnItemClickListener(new OnMenuItemClickListener());
        menuList.setAdapter(menuAdapter);
        // Add Menu Items
        menuAdapter.add(new MainDrawerMenuItem(R.drawable.ic_home_gray_800_24dp, R.string.main_page));
        menuAdapter.add(new MainDrawerMenuItem(R.drawable.ic_schedule_gray_800_24dp, R.string.recent_changes));
        menuAdapter.add(new MainDrawerMenuItem(R.drawable.ic_photo_filter_gray_800_24dp, R.string.random_article));
        menuAdapter.add(new MainDrawerMenuItem(R.drawable.ic_help_outline_gray_800_24dp, R.string.help));
        // Add Header
        MainDrawerHeader.getInstance().initialize(drawerLayout);
        menuAdapter.addHeader(MainDrawerHeader.getInstance());
        menuAdapter.notifyDataSetChanged();
    }

    public void setToggle(Activity activity, Toolbar toolbar) {
        this.toggle = new ActionBarDrawerToggle(activity, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    public class OnMenuItemClickListener implements RecyclerArrayAdapter.OnItemClickListener {

        @Override
        public void onItemClick(int position) {
            String itemTitle = menuAdapter.getItem(position).getTitle();
            if (itemTitle.equals(Utils.getApplication().getResources().getString(R.string.main_page))) {
                ((MainActivity) drawerLayout.getContext()).showPage("计算器百科:首页");
            }
            drawerLayout.closeDrawer(Gravity.LEFT);
        }
    }

    /**
     * Drawer 活动时关闭输入法以防止 layout 显示异常
     * @param activity 调用的 Activity
     */
    public void setCloseIMEOnDrawerStateChange(final Activity activity) {
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {
                if (newState == DrawerLayout.STATE_SETTLING) {
                    JUtils.closeInputMethod(activity);
                }
            }
        });
    }

}
