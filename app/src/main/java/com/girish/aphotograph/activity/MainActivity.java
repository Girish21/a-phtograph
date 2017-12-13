package com.girish.aphotograph.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

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
    Popular popularFragment;
    Bundle savedState;

    List<Fragment> fragments;

    int editorChecked = 0, popularChecked = 0;
    boolean editor = false, popular = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        savedState = savedInstanceState;

        editorsChoice = new EditorsChoice();
        popularFragment = new Popular();

        fragments = new ArrayList<>();
        fragments.add(editorsChoice);
        fragments.add(popularFragment);

        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        clearCache(this);

        tabLayout = findViewById(R.id.root_tab_layout);
        viewPager = findViewById(R.id.root_view_pager);

        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), fragments));
        viewPager.setOffscreenPageLimit(2);

        if (savedInstanceState != null) {
            editorChecked = savedInstanceState.getInt("com.girish.aphotograph.editorChecked");
            popularChecked = savedInstanceState.getInt("com.girish.aphotograph.popularChecked");

            Log.i("editor", "" + editorChecked);
            Log.i("popularFragment", "" + popularChecked);
        }

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
                editor = true;
                Log.i("Instance", "editor");
            } else if (fragments.get(viewPager.getCurrentItem()) instanceof Popular) {
                popular = true;
                Log.i("Instance", "popularFragment");
            }
            if (editor) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Sort by")
                        .setSingleChoiceItems(new String[]{"Create at", "Highest rating", "Most viewed"},
                                editorChecked, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        switch (i) {
                                            case 0:
                                                if (editorChecked != i)
                                                    doSort(0, EndPoints.BY_CREATED_AT);
//                                                    editorsChoice.sortData(EndPoints.BY_CREATED_AT);
                                                dialogInterface.dismiss();
//                                                editorsChoice = null;
                                                editor = false;
                                                editorChecked = 0;
                                                break;
                                            case 1:
                                                if (editorChecked != i)
                                                    doSort(0, EndPoints.BY_RATING);
//                                                    editorsChoice.sortData(EndPoints.BY_RATING);
                                                dialogInterface.dismiss();
//                                                editorsChoice = null;
                                                editor = false;
                                                editorChecked = 1;
                                                break;
                                            case 2:
                                                if (editorChecked != i)
                                                    doSort(0, EndPoints.BY_TIMES_VIEWED);
//                                                    editorsChoice.sortData(EndPoints.BY_TIMES_VIEWED);
                                                dialogInterface.dismiss();
//                                                editorsChoice = null;
                                                editor = false;
                                                editorChecked = 2;
                                                break;
                                        }
                                    }
                                })
                        .setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
//                                editorsChoice = null;
                                editor = false;
                            }
                        }).show();
            } else if (popular) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Sort by")
                        .setSingleChoiceItems(new String[]{"Create at", "Highest rating", "Most viewed"},
                                popularChecked, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        switch (i) {
                                            case 0:
                                                if (popularChecked != i)
                                                    doSort(1, EndPoints.BY_CREATED_AT);
//                                                    popularFragment.sortData(EndPoints.BY_CREATED_AT);
                                                dialogInterface.dismiss();
//                                                popularFragment = null;
                                                popular = false;
                                                popularChecked = 0;
                                                break;
                                            case 1:
                                                if (popularChecked != i)
                                                    doSort(1, EndPoints.BY_RATING);
//                                                    popularFragment.sortData(EndPoints.BY_RATING);
                                                dialogInterface.dismiss();
//                                                popularFragment = null;
                                                popular = false;
                                                popularChecked = 1;
                                                break;
                                            case 2:
                                                if (popularChecked != i)
                                                    doSort(1, EndPoints.BY_TIMES_VIEWED);
//                                                    popularFragment.sortData(EndPoints.BY_TIMES_VIEWED);
                                                dialogInterface.dismiss();
//                                                popularFragment = null;
                                                popular = false;
                                                popularChecked = 2;
                                                break;
                                        }
                                    }
                                })
                        .setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
//                                popularFragment = null;
                                popular = false;
                            }
                        }).show();
            }
        }

        return true;
    }

    public void doSort(int type, String sortType) {
        if (savedState == null) {
            switch (type) {
                case 0:
                    editorsChoice = (EditorsChoice) fragments.get(viewPager.getCurrentItem());
                    editorsChoice.sortData(sortType);
                    break;
                case 1:
                    popularFragment = (Popular) fragments.get(viewPager.getCurrentItem());
                    popularFragment.sortData(sortType);
                    break;
            }
        } else {
            switch (type) {
                case 0:
                    editorsChoice.sortData(sortType);
                    break;
                case 1:
                    popularFragment.sortData(sortType);
                    break;
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("com.girish.aphotograph.editorChecked", editorChecked);
        outState.putInt("com.girish.aphotograph.popularChecked", popularChecked);

        Log.i("savededitor", "" + editorChecked);
        Log.i("savedpopular", "" + popularChecked);
    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {

        List<Fragment> fragments;

        ViewPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @NonNull
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment createdFragment = (Fragment) super.instantiateItem(container, position);

            switch (position) {
                case 0:
                    editorsChoice = (EditorsChoice) createdFragment;
                    Log.i("ECheck", "" + editorsChoice.isAdded());
                    break;
                case 1:
                    popularFragment = (Popular) createdFragment;
                    Log.i("PCheck", "" + popularFragment.isAdded());
                    break;
            }

            return createdFragment;
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
