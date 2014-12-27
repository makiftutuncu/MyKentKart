/*
 * Copyright (C) 2015 Mehmet Akif Tütüncü
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mehmetakiftutuncu.mykentkart.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.mehmetakiftutuncu.mykentkart.R;
import com.mehmetakiftutuncu.mykentkart.adapters.KentKartAdapter;
import com.mehmetakiftutuncu.mykentkart.models.KentKart;
import com.mehmetakiftutuncu.mykentkart.tasks.LoadKentKartsTask;
import com.mehmetakiftutuncu.mykentkart.utilities.Constants;
import com.mehmetakiftutuncu.mykentkart.utilities.NFCUtils;
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

    private AlertDialog enableNFCDialog;
    private boolean isNFCDialogAnswered;

    private KentKartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (!preferences.getBoolean(Constants.PREFERENCE_IS_HELP_COMPLETED, false)) {
            Intent intent = new Intent(this, HelpActivity.class);
            startActivity(intent);
            finish();
        } else {
            setContentView(R.layout.activity_kentkart_list);

            Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
            if (mToolbar != null) {
                setSupportActionBar(mToolbar);
            }

            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView_kentKartList);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

            progressWidget = (ProgressWidget) findViewById(R.id.progressWidget_kentKartList);

            floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton_add);
            floatingActionButton.attachToRecyclerView(recyclerView);
            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), KentKartEditActivity.class);
                    startActivityForResult(intent, KentKartEditActivity.REQUEST_CODE);
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

            if (!isNFCDialogAnswered) {
                checkAndShowNFCDialog();
            }

            AppRate
                    .with(this)
                    .initialLaunchCount(Constants.RATE_LAUNCH_COUNT)
                    .text(R.string.rate_app)
                    .retryPolicy(RetryPolicy.INCREMENTAL)
                    .checkAndShow();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_more, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_more:
                Intent intent = new Intent(getApplicationContext(), MoreActivity.class);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == KentKartEditActivity.REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // Coming back after a change to KentKart list
            changeState(States.PROGRESS);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Dismiss enable NFC dialog if it is showing
        if (enableNFCDialog != null && enableNFCDialog.isShowing()) {
            enableNFCDialog.dismiss();
        }

        // Save current state
        outState.putString(Constants.STATE, state.toString());

        // Save NFC dialog answered state
        outState.putBoolean(Constants.IS_NFC_DIALOG_ANSWERED, isNFCDialogAnswered);

        // Save KentKart list
        if (adapter != null) {
            outState.putParcelableArrayList(Constants.KENT_KART_LIST, adapter.getKentKarts());
        }
    }

    private void restoreInstanceState(Bundle savedState) {
        if (savedState != null) {
            // Restore current state
            changeState(States.valueOf(savedState.getString(Constants.STATE)));

            // Restore NFC dialog answered state
            isNFCDialogAnswered = savedState.getBoolean(Constants.IS_NFC_DIALOG_ANSWERED);

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

    private void checkAndShowNFCDialog() {
        isNFCDialogAnswered = false;
        NFCUtils nfcUtils = NFCUtils.get(getApplicationContext());
        if (nfcUtils.hasNfc() && !nfcUtils.isNfcOn()) {
            enableNFCDialog = new AlertDialog.Builder(this)
                .setIcon(R.drawable.launcher_icon)
                .setTitle(getString(R.string.enableNFCDialog_title))
                .setMessage(getString(R.string.enableNFCDialog_message))
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isNFCDialogAnswered = true;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            Intent intent = new Intent(Settings.ACTION_NFC_SETTINGS);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                            startActivity(intent);
                        }
                        Toast.makeText(getApplicationContext(), getString(R.string.enableNFCDialog_hint), Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isNFCDialogAnswered = true;
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        isNFCDialogAnswered = true;
                    }
                })
                .create();

            enableNFCDialog.show();
        }
    }
}
