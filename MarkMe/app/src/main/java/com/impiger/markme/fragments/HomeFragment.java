package com.impiger.markme.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.impiger.markme.Constants;
import com.impiger.markme.FaceClient;
import com.impiger.markme.HomeActivity;
import com.impiger.markme.R;
import com.impiger.markme.adapter.DialogListAdapter;
import com.impiger.markme.adapter.PersonsAdapter;
import com.microsoft.projectoxford.face.contract.Person;
import com.microsoft.projectoxford.face.rest.ClientException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class HomeFragment extends Fragment {

    private static final String TAG = HomeFragment.class.getSimpleName();

    private Button loginButton;
    private Button logoutButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("MarkMe");
        loginButton = view.findViewById(R.id.log_in);
        logoutButton = view.findViewById(R.id.log_out);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPersons();
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPersons();
            }
        });
    }

    private void showPersonsDialog(final Person[] persons) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Select");
        builder.setAdapter(new DialogListAdapter(persons),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int item) {
                        Toast.makeText(getContext(), "You selected: " + persons[item].name,Toast.LENGTH_LONG).show();
                        getHomeActivity().setSelectedPersonId(persons[item].personId);
                        getHomeActivity().navigateFragment(Constants.PHOTO_FRAGMENT);
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
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
                    // show dialog
                    showPersonsDialog(personList);
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
