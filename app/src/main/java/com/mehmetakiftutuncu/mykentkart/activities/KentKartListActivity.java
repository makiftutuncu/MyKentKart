package com.mehmetakiftutuncu.mykentkart.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.mehmetakiftutuncu.mykentkart.R;
import com.mehmetakiftutuncu.mykentkart.adapters.KentKartAdapter;
import com.mehmetakiftutuncu.mykentkart.models.KentKart;
import com.mehmetakiftutuncu.mykentkart.tasks.KentKartLoaderTask;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;

public class KentKartListActivity extends ActionBarActivity implements KentKartLoaderTask.OnKentKartsLoadedListener {
    private enum States {LOADING, EMPTY, NONEMPTY}

    private DrawerLayout drawer;
    private ActionBarDrawerToggle drawerToggle;
    private LinearLayout loadingLayout;
    private LinearLayout emptyLayout;
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;

    private KentKartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }

        drawer = (DrawerLayout) findViewById(R.id.drawer);
        drawerToggle = new ActionBarDrawerToggle(this, drawer, mToolbar, R.string.app_name, R.string.app_name);
        drawer.setDrawerListener(drawerToggle);
        drawer.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        loadingLayout = (LinearLayout) findViewById(R.id.linearLayout_loading);

        emptyLayout = (LinearLayout) findViewById(R.id.linearLayout_kentKartList_empty);

        adapter = new KentKartAdapter();
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();

                showKentKartListOrEmptyLayout();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_kentKartList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton_add);
        floatingActionButton.attachToRecyclerView(recyclerView);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(KentKartListActivity.this, KentKartDetailsActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadKentKarts();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return drawerToggle != null || super.onOptionsItemSelected(item);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        if (drawerToggle != null) {
            drawerToggle.syncState();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (drawerToggle != null) {
            drawerToggle.onConfigurationChanged(newConfig);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer != null && drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onKentKartsLoaded(ArrayList<KentKart> kentKarts) {
        if (recyclerView != null && adapter != null) {
            adapter.setKentKarts(kentKarts);
        }

        showKentKartListOrEmptyLayout();
    }

    private void showKentKartListOrEmptyLayout() {
        if (adapter != null) {
            boolean isEmpty = adapter.getItemCount() == 0;

            if (isEmpty) {
                changeState(States.EMPTY);
            } else {
                changeState(States.NONEMPTY);
            }
        }
    }

    private void loadKentKarts() {
        changeState(States.LOADING);
        new KentKartLoaderTask(this).execute();
    }

    private void setLoadingLayoutVisibitility(int visibility) {
        if (loadingLayout != null) {
            loadingLayout.setVisibility(visibility);
        }
    }

    private void setRecyclerViewVisibitility(int visibility) {
        if (recyclerView != null) {
            recyclerView.setVisibility(visibility);
        }
    }

    private void setEmptyLayoutVisibitility(int visibility) {
        if (emptyLayout != null) {
            emptyLayout.setVisibility(visibility);
        }
    }

    private void setFloatingActionButtonVisibitility(int visibility) {
        if (floatingActionButton != null) {
            floatingActionButton.setVisibility(visibility);
        }
    }

    private void changeState(States state) {
        switch (state) {
            case LOADING:
                setLoadingLayoutVisibitility(View.VISIBLE);
                setEmptyLayoutVisibitility(View.GONE);
                setRecyclerViewVisibitility(View.GONE);
                setFloatingActionButtonVisibitility(View.GONE);
                break;
            case EMPTY:
                setLoadingLayoutVisibitility(View.GONE);
                setEmptyLayoutVisibitility(View.VISIBLE);
                setRecyclerViewVisibitility(View.GONE);
                setFloatingActionButtonVisibitility(View.VISIBLE);
                break;
            case NONEMPTY:
                setLoadingLayoutVisibitility(View.GONE);
                setEmptyLayoutVisibitility(View.GONE);
                setRecyclerViewVisibitility(View.VISIBLE);
                setFloatingActionButtonVisibitility(View.VISIBLE);
                break;
        }
    }
}
