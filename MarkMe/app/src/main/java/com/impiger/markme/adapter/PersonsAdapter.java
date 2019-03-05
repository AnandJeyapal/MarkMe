package com.impiger.markme.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.impiger.markme.R;
import com.microsoft.projectoxford.face.contract.Person;

public class PersonsAdapter extends RecyclerView.Adapter<PersonsAdapter.PersonHolder> {
    private Person[] persons;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class PersonHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView title;
        public TextView subTitle;
        public PersonHolder(View v) {
            super(v);
            title = v.findViewById(R.id.person_name);
            subTitle = v.findViewById(R.id.person_desc);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public PersonsAdapter(Person[] myDataset) {
        persons = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public PersonsAdapter.PersonHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_person_item, parent, false);
        PersonHolder personHolder = new PersonHolder(v);
        return personHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(PersonHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Person person = persons[position];
        holder.title.setText(person.name);
        holder.subTitle.setText(person.userData);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return persons.length;
    }
}