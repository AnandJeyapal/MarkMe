package com.impiger.markme.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.impiger.markme.Constants;
import com.impiger.markme.FaceClient;
import com.impiger.markme.HomeActivity;
import com.impiger.markme.R;
import com.impiger.markme.adapter.PersonsAdapter;
import com.microsoft.projectoxford.face.contract.Person;
import com.microsoft.projectoxford.face.rest.ClientException;

import java.io.IOException;

public class PersonGroupFragment extends Fragment {

    private static final String TAG = PersonGroupFragment.class.getSimpleName();
    private RecyclerView personGroupList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_person_group, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Settings");
        personGroupList = view.findViewById(R.id.person_group);
        personGroupList.setLayoutManager(new LinearLayoutManager(getActivity()));
        getPersons();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void getPersons() {
        AsyncTask<Void, Void, Person[]> fetchPersonsTask = new AsyncTask<Void, Void, Person[]>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                getHomeActivity().showProgress();
            }

            @Override
            protected Person[] doInBackground(Void... voids) {
                try {
                    return FaceClient.getClient().listPersons(Constants.PERSON_GROUP_ID);
                } catch (ClientException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Person[] personList) {
                super.onPostExecute(personList);
                getHomeActivity().hideProgress();
                if(personList != null) {
                    // set adapter
                    PersonsAdapter adapter = new PersonsAdapter(personList);
                    personGroupList.setAdapter(adapter);
                } else {
                    Log.e(TAG, "Error in getting persons");
                }
            }
        };
        fetchPersonsTask.execute();
    }

    private HomeActivity getHomeActivity() {
        return (HomeActivity)getActivity();
    }
}
