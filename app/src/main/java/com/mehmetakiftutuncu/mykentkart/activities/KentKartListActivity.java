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

import com.mehmetakiftutuncu.mykentkart.R;
import com.mehmetakiftutuncu.mykentkart.adapters.KentKartAdapter;
import com.mehmetakiftutuncu.mykentkart.models.KentKart;
import com.mehmetakiftutuncu.mykentkart.tasks.LoadKentKartsTask;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;

import ru.vang.progressswitcher.ProgressWidget;

public class KentKartListActivity extends ActionBarActivity implements LoadKentKartsTask.OnKentKartsLoadedListener {
    private enum States {PROGRESS, EMPTY, SUCCESS}

    private States state;

    private ProgressWidget progressWidget;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle drawerToggle;
    private FloatingActionButton floatingActionButton;

    private KentKartAdapter adapter;

    private static final String EXTRA_STATE     = "state";
    private static final String EXTRA_KENTKARTS = "kentKarts";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_kentkart_list);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView_kentKartList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        progressWidget = (ProgressWidget) findViewById(R.id.progressWidget_kentKartList);

        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton_add);
        floatingActionButton.attachToRecyclerView(recyclerView);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(KentKartListActivity.this, KentKartDetailsActivity.class);
                startActivity(intent);
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer);
        drawerToggle = new ActionBarDrawerToggle(this, drawer, mToolbar, R.string.app_name, R.string.app_name);
        drawer.setDrawerListener(drawerToggle);
        drawer.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        adapter = new KentKartAdapter();
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();

                showKentKartListResult();
            }
        });
        recyclerView.setAdapter(adapter);

        if (savedInstanceState != null) {
            restoreInstanceState(savedInstanceState);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        if (drawerToggle != null) {
            drawerToggle.syncState();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (state == null) {
            // State being null means this is the first time this activity is running, so load stuff
            changeState(States.PROGRESS);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return drawerToggle != null || super.onOptionsItemSelected(item);
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
        if (adapter != null) {
            adapter.setKentKarts(kentKarts);
        }

        showKentKartListResult();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save current state
        outState.putString(EXTRA_STATE, state.toString());

        // Save KentKart list
        if (adapter != null) {
            outState.putParcelableArrayList(EXTRA_KENTKARTS, adapter.getKentKarts());
        }
    }

    private void restoreInstanceState(Bundle savedState) {
        if (savedState != null) {
            // Restore current state
            changeState(States.valueOf(savedState.getString(EXTRA_STATE)));

            // Restore KentKart list
            ArrayList<KentKart> kentKarts = savedState.getParcelableArrayList(EXTRA_KENTKARTS);
            if (adapter != null) {
                adapter.setKentKarts(kentKarts);
            }
            showKentKartListResult();
        }
    }

    private void showKentKartListResult() {
        if (adapter != null) {
            boolean isEmpty = adapter.getItemCount() == 0;

            if (isEmpty) {
                changeState(States.EMPTY);
            } else {
                changeState(States.SUCCESS);
            }
        }
    }

    private void showProgressLayout() {
        if (progressWidget != null) {
            progressWidget.showProgress(true);
        }
    }

    private void showContentLayout() {
        if (progressWidget != null) {
            progressWidget.showContent(true);
        }
    }

    private void showEmptyLayout() {
        if (progressWidget != null) {
            progressWidget.showEmpty(true);
        }
    }

    private void showFloatingActionButton() {
        if (floatingActionButton != null) {
            floatingActionButton.setVisibility(View.VISIBLE);
        }
    }

    private void hideFloatingActionButton() {
        if (floatingActionButton != null) {
            floatingActionButton.setVisibility(View.GONE);
        }
    }

    private void changeState(States state) {
        /* Either
         *   state is null and new state is not, meaning that this is the first time activity is running and a state is being set
         * Or
         *   state is not null and a different state came, meaning that a state change is actually needed
         */
        boolean shouldChangeState = (this.state == null && state != null) || (this.state != null && !this.state.equals(state));

        if (shouldChangeState) {
            this.state = state;

            switch (state) {
                case PROGRESS:
                    showProgressLayout();
                    hideFloatingActionButton();
                    new LoadKentKartsTask(this).execute();
                    break;
                case EMPTY:
                    showEmptyLayout();
                    showFloatingActionButton();
                    break;
                case SUCCESS:
                    showContentLayout();
                    showFloatingActionButton();
                    break;
            }
        }
    }
}
