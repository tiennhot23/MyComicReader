package com.e.mycomicreader.views;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.e.mycomicreader.Common.Common;
import com.e.mycomicreader.R;
import com.e.mycomicreader.Retrofit.IComicAPI;
import com.e.mycomicreader.models.Chapter;
import com.google.gson.Gson;
import dmax.dialog.SpotsDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import java.io.*;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DownloadActivity extends AppCompatActivity {

    List<Chapter> download_chapters;
    String title, thumb, theme, desc, genre;
    DownloadTask downloadTask;
    CompositeDisposable compositeDisposable;
    IComicAPI iComicAPI;
    AlertDialog dialog;
    int total=0;

    @Override
    protected void onDestroy() {
        compositeDisposable.dispose();
        if(downloadTask != null) downloadTask.cancel(true);
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        download_chapters = new ArrayList<>();
        download_chapters = (List<Chapter>) getIntent().getSerializableExtra("download_chapters");
        title = getIntent().getStringExtra("title");
        thumb = getIntent().getStringExtra("thumb");
        theme = getIntent().getStringExtra("theme");
        desc = getIntent().getStringExtra("desc");
        genre = getIntent().getStringExtra("genre");
        compositeDisposable = new CompositeDisposable();
        iComicAPI = Common.getAPI();

        dialog = new SpotsDialog.Builder().setContext(this).setMessage("Loading...").build();
        dialog.show();
        fetchListChapter(download_chapters);
    }

    private void fetchListChapter(List<Chapter> download_chapters) {
        List<String> chapter_endpoints = new ArrayList<>();
        for(int i=0 ;i<download_chapters.size(); i++){
            chapter_endpoints.add(download_chapters.get(i).chapter_endpoint);
        }
        String data = new Gson().toJson(chapter_endpoints);
        compositeDisposable.add(iComicAPI.getListChapter(data)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Chapter>>() {
                    @Override
                    public void accept(List<Chapter> chapters) throws Exception {
                        download_chapters.clear();
                        download_chapters.addAll(chapters);
                        dialog.dismiss();
                        download();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        System.out.println(throwable);
                    }
                }));
    }


    private void download(){
        File parent = new File(Environment.getExternalStoragePublicDirectory("/Download/Download comic/"), title);
        if(!parent.exists()){
            parent.mkdirs();
            File info = new File(Environment.getExternalStoragePublicDirectory("/Download/Download comic/"), title + "/info");
            info.mkdirs();
            downloadImage(theme, "/Download comic/" + title + "/info", "theme");
            downloadImage(thumb, "/Download comic/" + title + "/info", "thumb");
            saveText(title, "/Download comic/" + title + "/info", "title");
            saveText(desc, "/Download comic/" + title + "/info", "desc");
            saveText(genre, "/Download comic/" + title + "/info", "genre");
        }

        int fileLength = 0;
        for(int i=0; i<download_chapters.size(); i++){
            fileLength += download_chapters.get(i).chapter_image.size();
        }

        downloadTask = new DownloadTask(this, title, fileLength);
        Chapter[] chapters = new Chapter[download_chapters.size()];
        download_chapters.toArray(chapters);
        downloadTask.execute(chapters);
    }


    private static class DownloadTask extends AsyncTask<Chapter, Integer, String> {
        //Prevent leak
        private WeakReference<Activity> activity;
        private String title;
        private int fileLength;
        private int total=0;
        private ProgressDialog progressDialog;

        public DownloadTask(Activity activity, String title, int fileLength) {
            this.activity = new WeakReference<>(activity);
            this.title = title;
            this.fileLength = fileLength;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(activity.get());
            progressDialog.setMessage("Downloading...");
            progressDialog.setIndeterminate(true);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressDialog.setIndeterminate(false);
            progressDialog.setMax(fileLength);
            progressDialog.setProgress(values[0]);
        }

        @Override
        protected String doInBackground(Chapter... chapters) {
            System.out.println(chapters.length);
            InputStream inputStream = null;
            OutputStream outputStream = null;
            HttpURLConnection httpURLConnection = null;
            try{
                for(Chapter chapter : chapters){
                    File path = Environment.getExternalStoragePublicDirectory("/Download/Download comic/" + title +"/"+ chapter.chapter_title);
                    if(!path.exists()) path.mkdirs();
                    else{
                        total += 1;
                        publishProgress(total);
                        return null;
                    }
                    for(int i=0; i<chapter.chapter_image.size(); i++){
                        URL url = new URL(chapter.chapter_image.get(i));
                        httpURLConnection = (HttpURLConnection) url.openConnection();
                        httpURLConnection.connect();
                        if (httpURLConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                            return "Server returned HTTP " + httpURLConnection.getResponseCode()
                                    + " " + httpURLConnection.getResponseMessage();
                        }
                        File file = new File(path, "/" + i + ".jpg");
                        inputStream = httpURLConnection.getInputStream();
                        outputStream = new FileOutputStream(file);

                        byte data[] = new byte[4096];
                        int count;
                        while ((count = inputStream.read(data)) != -1) {
                            outputStream.write(data, 0, count);
                        }

                        try {
                            if (outputStream != null){
                                total += 1;
                                System.out.println(total);
                                publishProgress(total);
                                outputStream.flush();
                            }
                            if (outputStream != null){
                                outputStream.close();
                            }
                            if (inputStream != null)
                                inputStream.close();
                        } catch (IOException ignored) {
                        }
                        if (httpURLConnection != null)
                            httpURLConnection.disconnect();
                    }
                }
            }catch (Exception e) {
                return e.toString();
            }finally {
                try {
                    if (outputStream != null){
                        outputStream.flush();
                    }
                    if (outputStream != null){
                        outputStream.close();
                    }
                    if (inputStream != null)
                        inputStream.close();
                } catch (IOException ignored) {
                }

                if (httpURLConnection != null)
                    httpURLConnection.disconnect();
            }

            String result = "finish";
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if(progressDialog.isShowing()) progressDialog.dismiss();
            this.cancel(true);
            Toast.makeText(activity.get(), "Download finished", Toast.LENGTH_SHORT).show();
            activity.get().finish();
        }
    }


    private void downloadImage(String url, String path, String filename){
        try{
            DownloadManager dm = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            Uri downloadUri = Uri.parse(url);
            DownloadManager.Request request = new DownloadManager.Request(downloadUri);
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                    .setAllowedOverRoaming(false)
                    .setTitle(filename)
                    .setMimeType("image/jpeg") // Your file type. You can use this code to download other file types also.
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,path + File.separator + filename + ".jpg");
            dm.enqueue(request);
        }catch (Exception e){
            Toast.makeText(this, "Image download failed.", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveText(String data, String path, String fileName){
        try {
            File dir = new File(Environment.getExternalStoragePublicDirectory("/Download") + path);
            if(!dir.exists()) dir.mkdirs();
            File file = new File(dir, "/" + fileName + ".txt");
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(data);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Write data failed", Toast.LENGTH_SHORT).show();
        }

    }
}