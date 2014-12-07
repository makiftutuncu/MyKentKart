package com.mehmetakiftutuncu.mykentkart.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.mehmetakiftutuncu.mykentkart.R;
import com.mehmetakiftutuncu.mykentkart.adapters.KentKartAdapter;
import com.mehmetakiftutuncu.mykentkart.models.KentKart;
import com.mehmetakiftutuncu.mykentkart.tasks.LoadKentKartsTask;
import com.mehmetakiftutuncu.mykentkart.utilities.Constants;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;

import fr.nicolaspomepuy.discreetapprate.AppRate;
import fr.nicolaspomepuy.discreetapprate.RetryPolicy;
import ru.vang.progressswitcher.ProgressWidget;

public class KentKartListActivity extends ActionBarActivity implements LoadKentKartsTask.OnKentKartsLoadedListener {
    private enum States {PROGRESS, EMPTY, SUCCESS}

    private States state;

    private ProgressWidget progressWidget;
    private FloatingActionButton floatingActionButton;

    private KentKartAdapter adapter;

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
                startActivityForResult(intent, KentKartDetailsActivity.REQUEST_CODE);
            }
        });

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

        AppRate
            .with(this)
            .initialLaunchCount(3)
            .text(R.string.rate_app)
            .retryPolicy(RetryPolicy.EXPONENTIAL)
            .checkAndShow();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_more, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_more:
                Intent intent = new Intent(this, MoreActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
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
        outState.putString(Constants.STATE, state.toString());

        // Save KentKart list
        if (adapter != null) {
            outState.putParcelableArrayList(Constants.KENT_KART_LIST, adapter.getKentKarts());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == KentKartDetailsActivity.REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // Coming back after a change to KentKart list
            changeState(States.PROGRESS);
        }
    }

    private void restoreInstanceState(Bundle savedState) {
        if (savedState != null) {
            // Restore current state
            changeState(States.valueOf(savedState.getString(Constants.STATE)));

            // Restore KentKart list
            ArrayList<KentKart> kentKarts = savedState.getParcelableArrayList(Constants.KENT_KART_LIST);
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
