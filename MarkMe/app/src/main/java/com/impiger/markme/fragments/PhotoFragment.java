package com.impiger.markme.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.impiger.markme.Constants;
import com.impiger.markme.FaceClient;
import com.impiger.markme.HomeActivity;
import com.impiger.markme.R;
import com.microsoft.projectoxford.face.contract.Face;
import com.microsoft.projectoxford.face.contract.VerifyResult;
import com.microsoft.projectoxford.face.rest.ClientException;

import java.io.IOException;
import java.util.UUID;

public class PhotoFragment extends Fragment {

    private static final String TAG = PhotoFragment.class.getSimpleName();
    public static final int UPLOAD_TAKE_PICTURE = 56;

    private Button retryButton;
    private Button processButton;
    private ImageView previewImage;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_photo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Capture Photo");
        previewImage = view.findViewById(R.id.preview_image);
        retryButton = view.findViewById(R.id.retry_button);
        processButton = view.findViewById(R.id.process_button);
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // capture the image again
                getHomeActivity().getCameraPermission();
            }
        });

        processButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // call Face verify API and close this fragment
                //FaceClient.getClient().detect(null,false, false, null);
                detectFace();
            }
        });
        // launch the camera once view created.
        getHomeActivity().getCameraPermission();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private HomeActivity getHomeActivity() {
        return (HomeActivity)getActivity();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case UPLOAD_TAKE_PICTURE:
                    Uri selectedImage = getHomeActivity().getImageURI();
                    previewImage.setImageURI(selectedImage);
                    break;
            }
        }
    }

    private void detectFace() {
        AsyncTask<Void, Void, Face[]> detectFaceTask = new AsyncTask<Void, Void, Face[]>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                getHomeActivity().setProgressMessage("Detecting Face..");
                getHomeActivity().showProgress();
            }

            @Override
            protected Face[] doInBackground(Void... voids) {
                try {
                   return FaceClient.getClient().detect(getHomeActivity().getInputStream(), true, false, null);
                } catch (ClientException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Face[] personList) {
                super.onPostExecute(personList);
                getHomeActivity().hideProgress();
                if(personList != null && personList.length > 1) {
                    // too many faces throw error
                } else if(personList != null && personList.length == 1) {
                    // set adapter
                    UUID faceId = personList[0].faceId;
                    verifyFace(faceId);
                } else {
                    Log.e(TAG, "Error in detecting persons");
                    Toast.makeText(getContext(), "Error in detecting persons",Toast.LENGTH_LONG).show();
                }
            }
        };
        detectFaceTask.execute();
    }

    private void verifyFace(final UUID faceId) {
        AsyncTask<UUID, Void, VerifyResult> detectFaceTask = new AsyncTask<UUID, Void, VerifyResult>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                getHomeActivity().setProgressMessage("Verifying Face..");
                getHomeActivity().showProgress();
            }

            @Override
            protected VerifyResult doInBackground(UUID... params) {
                try {
                    Log.d(TAG, getHomeActivity().getSelectedPersonId().toString());
                    Log.d(TAG, faceId.toString());
                    return FaceClient.getClient().verifyInPersonGroup(faceId, Constants.PERSON_GROUP_ID, getHomeActivity().getSelectedPersonId());
                } catch (ClientException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(VerifyResult result) {
                super.onPostExecute(result);
                getHomeActivity().hideProgress();
                if(result != null) {
                    Log.d(TAG, "IsIdentical: "+result.isIdentical);
                    Log.d(TAG, "confidence: "+result.confidence);
                    Toast.makeText(getContext(), "IsIdentical: "+result.isIdentical + " Confidence: "+result.confidence,Toast.LENGTH_LONG).show();
                    getHomeActivity().getSupportFragmentManager().popBackStack();
                } else {
                    Log.e(TAG, "Error in verifying person");
                    Toast.makeText(getContext(), "Error in verifying person",Toast.LENGTH_LONG).show();
                }
            }
        };
        detectFaceTask.execute();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
    }
}
