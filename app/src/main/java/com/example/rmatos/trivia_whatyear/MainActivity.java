package com.example.rmatos.trivia_whatyear;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import java.io.Serializable;

/** RESOURCES
 * https://tvnews.vanderbilt.edu/explore_category?category=war
 * https://www.infoplease.com/yearbyyear
 * http://news.bbc.co.uk/onthisday/hi/years/default.stm
 */
public class MainActivity extends AppCompatActivity {

    private static final int SETTINGS_INFO = 1;
    private static String difficulty;
    private static boolean isMusicOn;
    private static boolean isSoundsOn;
    private static boolean isVibrationOn;
    private static String name;
    private static String email;
    private static Categories categories;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Action-bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");

        //Sets state from previous data or default
        getPreviouslySavedData(savedInstanceState);


    }

    private void getPreviouslySavedData(Bundle savedInstanceState) {

        //Uses savedInstanceState, if thats null uses sharedPreferences
        if (savedInstanceState == null) {
            difficulty = getPreferences(Context.MODE_PRIVATE).getString("difficulty","NORMAL");
            isMusicOn = getPreferences(Context.MODE_PRIVATE).getBoolean("music",true);
            isSoundsOn = getPreferences(Context.MODE_PRIVATE).getBoolean("sound",true);
            isVibrationOn = getPreferences(Context.MODE_PRIVATE).getBoolean("vibration",true);
            name = getPreferences(Context.MODE_PRIVATE).getString("name","");
            email = getPreferences(Context.MODE_PRIVATE).getString("email","");

            String str = getPreferences(Context.MODE_PRIVATE).getString("categoriesState","");

            System.out.println("str: " +str);
            System.out.println("Email: " +email);

            if (!str.equals("")) {
                categories = Categories.Deserialize(str);
            }

        } else {
            difficulty = savedInstanceState.getString("difficulty");
            isMusicOn = savedInstanceState.getBoolean("music");
            isSoundsOn = savedInstanceState.getBoolean("sound");
            isVibrationOn = savedInstanceState.getBoolean("vibration");
            name = savedInstanceState.getString("name");
            email = savedInstanceState.getString("email");
            categories = Categories.Deserialize(savedInstanceState.getString("categoriesState"));
        }

        if (categories == null) {
            categories = new Categories(true);
        }

    }

    //Inflates menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //Menu event handler
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //Opens settings activity
        if (id == R.id.menu_settings)
        {
            Intent getSettingsScreenIntent = new Intent(this, SettingsActivity.class);
            final int result = 1;

            System.out.println("test1: "+ difficulty);
            getSettingsScreenIntent.putExtra("difficulty", difficulty);
            getSettingsScreenIntent.putExtra("isMusicOn", isMusicOn);
            getSettingsScreenIntent.putExtra("isSoundsOn", isSoundsOn);
            getSettingsScreenIntent.putExtra("isVibrationOn", isVibrationOn);
            getSettingsScreenIntent.putExtra("name", name);
            getSettingsScreenIntent.putExtra("email", email);

            startActivityForResult(getSettingsScreenIntent, result);
        }


        return super.onOptionsItemSelected(item);
    }


    //Play button clicked
    public void onPlay(View view) {

        Intent getCategoriesScreenIntent = new Intent(this, PlayActivity.class);
        final int result = 3;

        getCategoriesScreenIntent.putExtra("difficulty", difficulty);
        getCategoriesScreenIntent.putExtra("cbStates", categories);

        startActivityForResult(getCategoriesScreenIntent, result);
    }


    //Category button clicked
    public void onCategory(View view) {

        Intent getCategoriesScreenIntent = new Intent(this, CategoriesActivity.class);
        final int result = 2;

        getCategoriesScreenIntent.putExtra("cbStates", categories);

        startActivityForResult(getCategoriesScreenIntent, result);
    }

    //Obtains data from previous activities
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Settings activity
        if (requestCode == 1) {
            difficulty = data.getStringExtra("difficulty");
            isMusicOn = data.getBooleanExtra("music", false);
            isSoundsOn = data.getBooleanExtra("sound", false);
            isVibrationOn = data.getBooleanExtra("vibration", false);
            name = data.getStringExtra("name");
            email = data.getStringExtra("email");
        }

        //Categories activity
        if (requestCode == 2) {
            categories = (Categories) data.getSerializableExtra("cbStates");
        }
    }

    //If OS crashes or orientation change saves state
    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putString("difficulty", difficulty);
        outState.putBoolean("music", isMusicOn);
        outState.putBoolean("sound", isSoundsOn);
        outState.putBoolean("vibration", isVibrationOn);
        outState.putString("name", email);
        outState.putString("email", name);
        outState.putString("categoriesState", Categories.Serialize(categories));

        super.onSaveInstanceState(outState);
    }

    //If user closes app saves state
    @Override
    protected void onStop() {
        SharedPreferences.Editor sPEditor = getPreferences(Context.MODE_PRIVATE).edit();

        sPEditor.putString("difficulty", difficulty);
        sPEditor.putBoolean("music", isMusicOn);
        sPEditor.putBoolean("sound", isSoundsOn);
        sPEditor.putBoolean("vibration", isVibrationOn);
        sPEditor.putString("name", email);
        sPEditor.putString("email", name);
        sPEditor.putString("categoriesState", Categories.Serialize(categories));

        sPEditor.commit();
        super.onStop();
    }




}
