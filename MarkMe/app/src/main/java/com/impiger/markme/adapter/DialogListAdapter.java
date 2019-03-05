package com.impiger.markme.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.impiger.markme.R;
import com.microsoft.projectoxford.face.contract.Person;

public class DialogListAdapter extends BaseAdapter {

    private Person[] persons;

    public DialogListAdapter( Person[] persons) {
        this.persons = persons;
    }

    @Override
    public int getCount() {
        return persons.length;
    }

    @Override
    public Object getItem(int position) {
        return persons[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dlg_person_item, null);
            holder = new ViewHolder();
            holder.nameView = convertView.findViewById(R.id.person_name);
            holder.descView = convertView.findViewById(R.id.person_desc);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.nameView.setText(persons[position].name);
        holder.descView.setText(persons[position].userData);

        return convertView;
    }

    static class ViewHolder {
        TextView nameView;
        TextView descView;
    }

}
