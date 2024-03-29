package com.example.danceit.Sharing;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.danceit.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> implements Filterable {
    private List<String> mDataset;
    private List<String> mOriginalDataset;
    public static List<String> users;


    public static List<String> getUsers() {
        //method to get all the selected users
        return users;
    }




    public void showSelectedUsers(){
        mDataset=users;
        notifyDataSetChanged();
    }


    public void showAllSelectedUsers(){
        mDataset=mOriginalDataset;
        notifyDataSetChanged();
    }




    //stores the chosen  users

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                List<String> list;
                if(charSequence.length()==0){
                    list=mOriginalDataset;
                }else {
                    list=getFilteredResults(charSequence.toString().toLowerCase());
                }
                FilterResults results = new FilterResults();
                results.values = list;

                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mDataset=(List) filterResults.values;
                notifyDataSetChanged();

            }
        } ;

    }

    protected List<String> getFilteredResults(String constraint) {
        List<String> results = new ArrayList<>();

        for (String item : mOriginalDataset) {
            if (item.toLowerCase().contains(constraint)) {
                results.add(item);
            }
        }
        return results;
    }


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView textView;
        public CheckBox checkBox;
        public MyViewHolder(View v) {
            super(v);

            textView = v.findViewById(R.id.checkbox_textView);


            checkBox=(CheckBox) v.findViewById(R.id.checkbox);

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        if(!users.contains(textView.getText().toString())){
                            users.add(textView.getText().toString());
                        }
                    }else{
                        users.remove(textView.getText().toString());
                    }
                }
            });




        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(List myDataset) {
        mOriginalDataset=myDataset;
        mDataset = myDataset;
        users=new ArrayList<>();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_text_view, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.textView.setText(mDataset.get(position));
        if(users.contains(mDataset.get(position))){

            holder.checkBox.setChecked(true);
        }else {
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if(mDataset==null ){
            return 0;
        }
        return mDataset.size();
    }
}
