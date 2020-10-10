package com.example.danceit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

//import com.example.danceit.Database.VideoViewModel;
import com.example.danceit.Model.Video;
import com.example.danceit.TagManagement.TagManagementActivity;
import com.example.danceit.ui.login.LoginActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;

import android.os.Parcelable;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private ArrayList<Video> allVideos; // List that stores all the videos in a user's library
    int Fragment = 1; // Determines which fragment the Main Activity is on
    MaterialSearchView searchView;
    Toolbar toolbar;
    //VideoViewModel videoViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        //videoViewModel = new VideoViewModel(getApplication());

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(),AddVideoActivity.class);
                startActivity(intent);
            }
        });

        //searchViewCode();

        TabLayout tabLayout=(TabLayout) findViewById(R.id.tabLayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            int state=0;
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //Select the tab selected
                if(tab.getPosition()==0){

                    //from second fragment to the first fragment
                    if(state==1){
                      NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                      NavController navController = navHostFragment.getNavController();
                      navController.navigate(R.id.action_SecondFragment_to_FirstFragment);
                        Fragment=1;
                    }else{
                        // switch the second fragment
                        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                        NavController navController = navHostFragment.getNavController();
                        navController.navigate(R.id.action_ThirdFragment_to_FirstFragment);
                        Fragment=1;

                    } // switch the second fragment
                    state=0;

                }else if(tab.getPosition()==1){
                    // switch the second fragment

                    if (state == 0) {

                        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                        NavController navController = navHostFragment.getNavController();
                        navController.navigate(R.id.action_FirstFragment_to_SecondFragment);
                        Fragment=2;
                    }else {
                        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                        NavController navController = navHostFragment.getNavController();
                        navController.navigate(R.id.action_ThirdFragment_to_SecondFragment);
                        Fragment=2;
                    }
                    state=1;

                }else {

                    if(state==0){
                    // switch the second fragment
                        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                        NavController navController = navHostFragment.getNavController();
                        navController.navigate(R.id.action_FirstFragment_to_ThirdFragment);
                        Fragment=3;
                    }else{

                        // switch the second fragment
                        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                        NavController navController = navHostFragment.getNavController();
                        navController.navigate(R.id.action_SecondFragment_to_ThirdFragment);
                        Fragment=3;

                    }
                    state=2;

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }


    @Override
    public void onBackPressed() {
        if(searchView.isSearchOpen()){
            searchView.closeSearch();
        }
        else{
            super.onBackPressed();
        }
        moveTaskToBack(true);

    }


    /*This method initiates the LibrarySearchActivity and handles the receiving of search queries
          from the user. The search string and the list of all a user's videos are sent with an intent.*/
    private void searchViewCode(){
        String [] yes = {"dog", "johnwick", "german", "germany", "nolan", "batman", "robert"};
        searchView=(MaterialSearchView) findViewById(R.id.search_view);
        searchView.setSuggestions(yes); // Pre-saved autocompletion words and phrases for searching
        searchView.setEllipsize(true);

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Bundle bundle = new Bundle();
                String[] searchKeywords = s.split(" ");
                bundle.putStringArray("Search Keywords", searchKeywords);

                Bundle bundle1 = new Bundle();
                bundle1.putParcelableArrayList("Videos", (ArrayList<? extends Parcelable>) allVideos);

                if (Fragment==1){
                    Intent intent = new Intent(MainActivity.this, LibrarySearchActivity.class);
                    intent.putExtras(bundle);
                    intent.putExtras(bundle1);
                    startActivity(intent);
                    System.out.println("Fragment 1");
                }
                else if(Fragment==2){
                    Intent intent = new Intent(MainActivity.this, PublicSearchActivity.class);
                    intent.putExtras(bundle);
                    intent.putExtras(bundle1);
                    startActivity(intent);
                    System.out.println("Fragment 2");
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem search = menu.findItem(R.id.search);
        searchView.setMenuItem(search);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if (id == R.id.search){
            return true;
        }else if(id==R.id.logout){
            mAuth.signOut();
            Intent intent=new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            return true;
        }else if(id==R.id.tag_management){
            Intent intent=new Intent(MainActivity.this, TagManagementActivity.class);
            startActivity(intent);
            return true;
        }
        /*else if (id == R.id.tag_management){
            Intent intent = new Intent(MainActivity.this, TagManagementActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("Videos", (ArrayList<? extends Parcelable>) allVideos);
            intent.putExtras(bundle);
            startActivity(intent);
        }*/


        return super.onOptionsItemSelected(item);
    }

    /*This method allows related fragments and activities to set the copy of the videos
      list/database that the Main Activity has of a user's library */
    public void setAllVideos(ArrayList<Video> allVideos) {
        this.allVideos = allVideos;
    }
}