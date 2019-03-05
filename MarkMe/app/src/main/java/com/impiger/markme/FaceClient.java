package com.impiger.markme;

import com.microsoft.projectoxford.face.FaceServiceClient;
import com.microsoft.projectoxford.face.FaceServiceRestClient;

public class FaceClient {
    private static FaceServiceClient ourInstance = null;

    public static FaceServiceClient getClient() {
        if (ourInstance == null) {
            ourInstance = new FaceServiceRestClient(Constants.API_ENDPOINT, Constants.SUBSCRIPTION_KEY);
        }
        return ourInstance;
    }

    private FaceClient() {
    }
}
