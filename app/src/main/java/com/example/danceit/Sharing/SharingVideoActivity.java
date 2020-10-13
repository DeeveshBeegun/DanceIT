package com.example.danceit.Sharing;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.danceit.Model.FirebaseManager;
import com.example.danceit.Model.Video;
import com.example.danceit.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This class is responsible for sending a video object to another user.
 */

public class SharingVideoActivity extends AppCompatActivity {

    FirebaseFirestore database = FirebaseFirestore.getInstance();
    List<String> users;
    MyAdapter myAdapter = null;
    FirebaseManager firebaseManager = new FirebaseManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharing_video);
        final RecyclerView recyclerView=(RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        populateRecyclerViewWithUser(recyclerView);
        initialiseSearchView();
        createCheckBox();
        sendVideo();


    }

    /**
     * This method sends video to the selected user when the floating button is pressed.
     */
    private void sendVideo() {
        FloatingActionButton floatingActionButton=(FloatingActionButton) findViewById(R.id.sendFloatingActionButton);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = getIntent().getExtras();
                List<String> selected_users = MyAdapter.getUsers(); // store the name of selected users

                assert bundle != null;
                if (Objects.equals(bundle.getString("single_user"), "single_user")) {
                    final Video video = bundle.getParcelable("video_obj"); // video to send
                    assert video != null;
                    video.setPrivacy("received");
                    for (int i = 0; i< selected_users.size(); i ++)
                        firebaseManager.sendVideoToUsers(selected_users, i, video);

                }
                else {
                    List<Video> videoList = bundle.getParcelableArrayList("video_list");

                    for(int i = 0; i < selected_users.size(); i++) {
                        assert videoList != null;
                        for(int j = 0; j < videoList.size(); j++) {
                            Video video = videoList.get(j);
                            video.setPrivacy("received");
                            firebaseManager.sendVideoToUsers(selected_users, i, video);
                        }
                    }

                }





            }
        });
    }

    /**
     * This method creates the selected item check box and listen to events i.e. selected or unselected events
     */
    private void createCheckBox() {
        final CheckBox checkBoxSel=(CheckBox)  findViewById(R.id.checkbox_selected);
        checkBoxSel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    myAdapter.showSelectedUsers(); // when checkbox is checked it shows only the selected users

                }else{
                    myAdapter.showAllSelectedUsers(); // show all users

                }

            }
        });
    }

    /**
     * This method initialise the search view and listens for events such as search queries
     */
    private void initialiseSearchView() {
        final SearchView searchView=(SearchView) findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {


                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                myAdapter.getFilter().filter(s);
                return true;
            }
        });

    }

    /**
     * This method populates the recyclerview with users found in the firestore database.
     * @param recyclerView to display users.
     */
    public void populateRecyclerViewWithUser(final RecyclerView recyclerView) {
        firebaseManager.getUser_reference().get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
        @Override
        public void onComplete(@NonNull Task<QuerySnapshot> task) {
            users = new ArrayList<>(); // store name of users
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                        if(Objects.equals(Objects.requireNonNull(mAuth.getCurrentUser()).getDisplayName(),
                                document.getId()))
                            continue;
                        users.add(document.getId());

                    }


                    myAdapter = new MyAdapter(users);
                    recyclerView.setAdapter(myAdapter);
                }

        }

        });

    }


}