package com.e.mycomicreader.Common;

public interface AsyncTaskResponse {
    void processFinish(String output);

    void downloadFinish(String output);
}
