package com.introvesia.nihongonesia.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.introvesia.nihongonesia.R;
import com.introvesia.nihongonesia.data.WordTag;
import com.introvesia.nihongonesia.fragments.KanjiDetailFragment;
import com.introvesia.nihongonesia.fragments.KanjiFragment;
import com.introvesia.nihongonesia.fragments.KanjiReadingFilterFragment;
import com.introvesia.nihongonesia.fragments.WordDetailFragment;
import com.introvesia.nihongonesia.fragments.WordFragment;
import com.introvesia.nihongonesia.fragments.KanjiPracticeFragment;
import com.introvesia.nihongonesia.lib.SQLiteDb;
import com.introvesia.nihongonesia.lib.NavigationManager;
import com.introvesia.nihongonesia.models.WordTagModel;

import java.util.ArrayList;
import java.util.Hashtable;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        KanjiReadingFilterFragment.KanjiYomiFilterFragmentListener,
        WordDetailFragment.KotobaDetailFragmentListener,
        KanjiDetailFragment.KanjiDetailFragmentListener,
        KanjiFragment.KanjiListFragmentListener,
        WordFragment.KotobaFragmentListener,
        TextWatcher {
    private static Fragment fragment;
    private int current_menu;
    private EditText keywordText;
    private Toolbar toolbar;
    private RelativeLayout searchForm;
    private int currentSearchOption;
    private Hashtable kotobaTag = new Hashtable();
    private String kanji_level = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        toolbar = findViewById(R.id.toolbar);
        searchForm = findViewById(R.id.search_form);
        setSupportActionBar(toolbar);

        SQLiteDb db = new SQLiteDb(this);
        db.connect(true);

        // Init fragment
        NavigationManager.setFragmentManager(getSupportFragmentManager());
        keywordText = findViewById(R.id.inputSearch);
        keywordText.addTextChangedListener(this);

        Button search_opt_btn = findViewById(R.id.search_opt_button);
        search_opt_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (current_menu == R.id.nav_kanji)
                    showKanjiMenu(view);
                else if (current_menu == R.id.nav_kotoba)
                    showKotobaMenu(view);
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Initial page
        initKanjiPage();

    }

    private void showKotobaMenu(View view) {
        PopupMenu menu = new PopupMenu (this, view);
        menu.setOnMenuItemClickListener (new PopupMenu.OnMenuItemClickListener () {
            @Override
            public boolean onMenuItemClick (MenuItem item) {
                int id = item.getItemId();
                String tag = kotobaTag.get(id) + "";
                ((WordFragment)fragment).find("+" + tag);
                return true;
            }
        });

        menu.inflate (R.menu.word_search_menu);
        menu.show();

        WordTagModel model = new WordTagModel();
        ArrayList<WordTag> data = model.getAllTag();

        menu.getMenu().clear();
        for (WordTag item : data) {
            int id = item.getId();
            kotobaTag.put(id, item.getTag());
            menu.getMenu().add(0, id, id, item.getName());
        }
    }

    public void showKanjiMenu (View view) {
        PopupMenu menu = new PopupMenu (this, view);
        final MainActivity that = this;
        menu.setOnMenuItemClickListener (new PopupMenu.OnMenuItemClickListener () {
            @Override
            public boolean onMenuItemClick (MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.opt_all:
                        currentSearchOption = id;
                        keywordText.setHint("Semua..");
                        break;
                    case R.id.opt_kunyomi:
                        currentSearchOption = id;
                        keywordText.setHint("Berdasarkan kun'yomi..");
                        break;
                    case R.id.opt_onkun:
                        currentSearchOption = id;
                        keywordText.setHint("Berdasarkan on'yomi & kun'yomi..");
                        break;
                    case R.id.opt_onyomi:
                        currentSearchOption = id;
                        keywordText.setHint("Berdasarkan on'yomi..");
                        break;
                    case R.id.opt_meaning:
                        currentSearchOption = id;
                        keywordText.setHint("Berdasarkan arti..");
                        break;
                    case R.id.opt_level5:
                        kanji_level = "N5";
                        break;
                    case R.id.opt_level4:
                        kanji_level = "N4";
                        break;
                    case R.id.opt_level3:
                        kanji_level = "N3";
                        break;
                    case R.id.opt_level2:
                        kanji_level = "N2";
                        break;
                    case R.id.opt_level1:
                        kanji_level = "N1";
                        break;
                    case R.id.opt_all_level:
                        kanji_level = "";
                        break;
                }
                ((KanjiFragment)fragment).find(currentSearchOption, kanji_level, keywordText.getText() + "");
                return true;
            }
        });
        menu.inflate (R.menu.kanji_search_menu);
        menu.show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (!NavigationManager.isLast())
                NavigationManager.pop();
            else
                super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        current_menu = item.getItemId();

        if (current_menu == R.id.nav_kanji) {
            searchForm.setVisibility(View.VISIBLE);
            initKanjiPage();
        } else if (current_menu == R.id.nav_kotoba) {
            searchForm.setVisibility(View.VISIBLE);
            initKotobaPage();
        } else if (current_menu == R.id.practice_kanji) {
            searchForm.setVisibility(View.GONE);
            practiceKanji();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void practiceKanji() {
        toolbar.setTitle("Latihan Kanji");
        // Init fragment
        fragment = new KanjiPracticeFragment();
        NavigationManager.reset();
        NavigationManager.push(fragment);
    }

    private void initKotobaPage() {
        toolbar.setTitle("Kosa Kata");
        // Init fragment
        fragment = new WordFragment();
        ((WordFragment)fragment).registerForListener(this);
        NavigationManager.reset();
        NavigationManager.push(fragment);

        // Init listener
        keywordText.setHint("Pencarian kosa kata..");
        keywordText.setText("");
    }

    private void initKanjiPage() {
        toolbar.setTitle("Kanji");
        // Init fragment
        fragment = new KanjiFragment();
        ((KanjiFragment)fragment).registerForListener(this);
        NavigationManager.reset();
        NavigationManager.push(fragment);

        // Init listener
        keywordText.setHint("Pencarian Kanji..");
        keywordText.setText("");

        // Search options
        currentSearchOption = R.id.opt_all;
        current_menu = R.id.nav_kanji;
    }

    @Override
    public void onItemKanjiClickedListener(String valueClicked) {
        KanjiDetailFragment frg = new KanjiDetailFragment();
        frg.registerForListener(this);
        frg.setKanji(valueClicked);
        NavigationManager.push(frg);
    }

    @Override
    public void onItemTagClickedListener(String valueClicked) {
        NavigationManager.pop();
        ((WordFragment)fragment).find("+" + valueClicked);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        NavigationManager.resetToHome();
        if (current_menu == R.id.nav_kanji) {
            ((KanjiFragment)fragment).find(currentSearchOption, kanji_level, editable + "");
        } else if (current_menu == R.id.nav_kotoba) {
            ((WordFragment)fragment).find(editable + "");
        }
    }

    @Override
    public void onItemKanjiYomiClickedListener(String valueClicked) {
        KanjiReadingFilterFragment frg = new KanjiReadingFilterFragment();
        frg.registerForListener(this);
        frg.setYomi(valueClicked);
        NavigationManager.push(frg);
    }

    @Override
    public void onItemYomiKanjiClickedListener(String valueClicked) {
        KanjiDetailFragment frg = new KanjiDetailFragment();
        frg.registerForListener(this);
        frg.setKanji(valueClicked);
        if (!NavigationManager.isLast()) {
            NavigationManager.pop();
            NavigationManager.pop();
        }
        NavigationManager.push(frg);
    }

    @Override
    public void onItemKotobaClickedListener(String valueClicked) {
        WordDetailFragment frg = new WordDetailFragment();
        frg.registerForListener(this);
        frg.setId(valueClicked);
        if (!NavigationManager.isLast()) {
            NavigationManager.pop();
            NavigationManager.pop();
        }
        NavigationManager.push(frg);
    }
}