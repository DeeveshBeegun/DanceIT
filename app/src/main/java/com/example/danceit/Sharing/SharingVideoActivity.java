package com.example.danceit.Sharing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SearchView;

import com.example.danceit.Model.Video;
import com.example.danceit.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.api.Page;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.grpc.internal.SharedResourceHolder;

public class SharingVideoActivity extends AppCompatActivity {

     FirebaseAuth mAuth;
    FirebaseFirestore database = FirebaseFirestore.getInstance();
    List<String> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharing_video);
        final RecyclerView recyclerView=(RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        database.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
              users = new ArrayList<>();
                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                        users.add(document.getId());
                    }
                }
                final MyAdapter myAdapter =new MyAdapter(users);
                recyclerView.setAdapter(myAdapter);

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
                CheckBox checkBoxSel=(CheckBox)  findViewById(R.id.checkbox_selected);
                checkBoxSel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){
                            myAdapter.showSelectedUsers();

                        }else{
                            myAdapter.showAllSelectedUsers();

                        }

                    }
                });

            }
        });




                FloatingActionButton floatingActionButton=(FloatingActionButton) findViewById(R.id.sendFloatingActionButton);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = "receiver";

                Bundle bundle = getIntent().getExtras();
                assert bundle != null;
                final Video video = bundle.getParcelable("video_obj");

                assert video != null;
                System.out.println("tag size: "  + video.getTags().size());

            CollectionReference reference = database.collection("video_sent").document(username)
                    .collection("video_received");
                assert video != null;
                reference.add(video);

            }
        });





    }
}