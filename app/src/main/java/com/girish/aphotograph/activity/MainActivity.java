package com.girish.aphotograph.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.girish.aphotograph.R;
import com.girish.aphotograph.extra.EndPoints;
import com.girish.aphotograph.fragment.EditorsChoice;
import com.girish.aphotograph.fragment.Popular;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    EditorsChoice editorsChoice;
    Popular popular;

    List<Fragment> fragments;

    int editorChecked = 0, popularChecked = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragments = new ArrayList<>();
        fragments.add(new EditorsChoice());
        fragments.add(new Popular());

        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        clearCache(this);

        tabLayout = findViewById(R.id.root_tab_layout);
        viewPager = findViewById(R.id.root_view_pager);

        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), fragments));

    }

    private void clearCache(Context context) {
        clearCacheFolder(context.getCacheDir());
    }

    private void clearCacheFolder(File cacheDir) {
        if (cacheDir != null && cacheDir.isDirectory()) {
            long currentTime = new Date().getTime();
            try {
                for (File child : cacheDir.listFiles()) {
                    if (child.isDirectory())
                        clearCacheFolder(child);
                    if (child.lastModified() < currentTime - DateUtils.DAY_IN_MILLIS)
                        child.delete();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.popup_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.sort) {
            if (fragments.get(viewPager.getCurrentItem()) instanceof EditorsChoice) {
                editorsChoice = (EditorsChoice) fragments.get(viewPager.getCurrentItem());
                Log.i("Instance", "editor");
            } else if (fragments.get(viewPager.getCurrentItem()) instanceof Popular) {
                popular = (Popular) fragments.get(viewPager.getCurrentItem());
                Log.i("Instance", "popular");
            }
            if (editorsChoice != null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Sort by")
                        .setSingleChoiceItems(new String[]{"Create at", "Highest rating", "Most viewed"},
                                editorChecked, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        switch (i) {
                                            case 0:
                                                editorChecked = 0;
                                                editorsChoice.sortData(EndPoints.BY_CREAED_AT);
                                                dialogInterface.dismiss();
                                                editorsChoice = null;
                                                break;
                                            case 1:
                                                editorChecked = 1;
                                                editorsChoice.sortData(EndPoints.BY_RATING);
                                                dialogInterface.dismiss();
                                                editorsChoice = null;
                                                break;
                                            case 2:
                                                editorChecked = 2;
                                                editorsChoice.sortData(EndPoints.BY_TIMES_VIEWED);
                                                dialogInterface.dismiss();
                                                editorsChoice = null;
                                                break;
                                        }
                                    }
                                })
                        .setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                editorsChoice = null;
                            }
                        }).show();
            } else if (popular != null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Sort by")
                        .setSingleChoiceItems(new String[]{"Create at", "Highest rating", "Most viewed"},
                                popularChecked, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        switch (i) {
                                            case 0:
                                                popularChecked = 0;
                                                popular.sortData(EndPoints.BY_CREAED_AT);
                                                dialogInterface.dismiss();
                                                popular = null;
                                                break;
                                            case 1:
                                                popularChecked = 1;
                                                popular.sortData(EndPoints.BY_RATING);
                                                dialogInterface.dismiss();
                                                popular = null;
                                                break;
                                            case 2:
                                                popularChecked = 2;
                                                popular.sortData(EndPoints.BY_TIMES_VIEWED);
                                                dialogInterface.dismiss();
                                                popular = null;
                                                break;
                                        }
                                    }
                                })
                        .setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                popular = null;
                            }
                        }).show();
            }
        }

        return true;
    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {

        List<Fragment> fragments;

        ViewPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }
}
