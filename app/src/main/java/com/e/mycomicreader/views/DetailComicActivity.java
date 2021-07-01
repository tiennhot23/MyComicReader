package com.e.mycomicreader.views;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.e.mycomicreader.Common.AsyncTaskResponse;
import com.e.mycomicreader.Common.Common;
import com.e.mycomicreader.Common.IRecylerClickListener;
import com.e.mycomicreader.R;
import com.e.mycomicreader.Retrofit.IComicAPI;
import com.e.mycomicreader.entity.FollowedComic;
import com.e.mycomicreader.entity.MarkedChapter;
import com.e.mycomicreader.fragments.MyBottomSheetFragement;
import com.e.mycomicreader.models.Chapter;
import com.e.mycomicreader.models.DetailComic;
import com.e.mycomicreader.models.MarkedChapterViewModel;
import com.squareup.picasso.Picasso;
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

public class DetailComicActivity extends AppCompatActivity implements AsyncTaskResponse, IRecylerClickListener {

    public static String endpoint;
    private TextView title_comic, author, status, rating, updated_on, genre, view, desc, read, read_continue, chapter_list, btn_follow;
    private ImageView theme, thumb ,btn_go_back;
    private CompositeDisposable compositeDisposable;
    private Toast toast;
    private DetailComic detailComic = new DetailComic();
    private String chapter_endpoint;
    private int taskCount = 0;
    private Chapter downloadChapter;

    IComicAPI iComicAPI;
    MyBottomSheetFragement bottomSheetDialog;
    MarkedChapterViewModel markedChapterViewModel;
    BackGroundTask backGroundTask;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_comic);

        endpoint = getIntent().getStringExtra("endpoint");

        compositeDisposable = new CompositeDisposable();
        iComicAPI = Common.getAPI();

        title_comic = findViewById(R.id.title_comic);
        author = findViewById(R.id.author);
        status = findViewById(R.id.status);
        rating = findViewById(R.id.rating);
        updated_on = findViewById(R.id.updated_on);
        genre = findViewById(R.id.genre);
        view = findViewById(R.id.view);
        desc = findViewById(R.id.desc);
        theme = findViewById(R.id.theme);
        thumb = findViewById(R.id.thumb);
        read = findViewById(R.id.read);
        read_continue = findViewById(R.id.read_continue);
        chapter_list = findViewById(R.id.chapter_list);
        btn_follow = findViewById(R.id.btn_follow);
        btn_go_back = findViewById(R.id.btn_go_back);
        progressDialog = new ProgressDialog(this);
        downloadChapter = new Chapter();

        markedChapterViewModel = new ViewModelProvider(this).get(MarkedChapterViewModel.class);

        btn_go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=detailComic.chapter_list.size()-1;
                Intent intent = new Intent(DetailComicActivity.this, ChapterActivity.class);
                intent.putExtra("position", position);
                intent.putExtra("chapter_list", (Serializable) detailComic.chapter_list);
                startActivity(intent);
            }
        });

        read_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=detailComic.chapter_list.size()-1;
                if(chapter_endpoint == null) return;
                for(int i=0 ;i<detailComic.chapter_list.size(); i++){
                    if(detailComic.chapter_list.get(i).chapter_endpoint.equals(chapter_endpoint)){
                        position = i;
                        break;
                    }
                }
                Intent intent = new Intent(DetailComicActivity.this, ChapterActivity.class);
                intent.putExtra("position", position);
                intent.putExtra("chapter_list", (Serializable) detailComic.chapter_list);
                startActivity(intent);
            }
        });

        fetchDetailComic();

        chapter_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.show(getSupportFragmentManager(), bottomSheetDialog.getTag());
            }
        });

        btn_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(toast != null) toast.cancel();
                toast = Toast.makeText(getBaseContext(),"Followed", Toast.LENGTH_SHORT);
                toast.show();
                MainActivity.followedComicViewModel.insert(new FollowedComic(endpoint));
                if(!MainActivity.isFollowed.containsKey(endpoint)){
                    MainActivity.isFollowed.put(endpoint, true);
                }
            }
        });

        backGroundTask = new BackGroundTask(this, markedChapterViewModel, endpoint);
        backGroundTask.res = this;
        backGroundTask.execute();
    }

    private void fetchDetailComic() {
        AlertDialog dialog = new SpotsDialog.Builder().setContext(this).setMessage("Loading...").build();
        dialog.show();
        compositeDisposable.add(iComicAPI.getDetailComic(endpoint)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<DetailComic>>() {
                    @Override
                    public void accept(List<DetailComic> detailComics) throws Exception {
                        detailComic = detailComics.get(0);
                        title_comic.setText(detailComics.get(0).title);
                        author.setText(detailComics.get(0).author);
                        status.setText(detailComics.get(0).status);
                        rating.setText(detailComics.get(0).rating);
                        updated_on.setText(detailComics.get(0).updated_on);
                        StringBuilder genrelist = new StringBuilder();
                        for(int i=0; i<detailComics.get(0).genre_list.size(); i++){
                            if(i==0)
                                genrelist.append(detailComics.get(0).genre_list.get(i).genre_name);
                            else
                                genrelist.append(",").append(detailComics.get(0).genre_list.get(i).genre_name);
                        }
                        genre.setText(genrelist.toString());
                        view.setText(detailComics.get(0).view);
                        desc.setText(detailComics.get(0).desc);
                        Picasso.get().load(detailComics.get(0).theme).into(theme);
                        Picasso.get().load(detailComics.get(0).thumb).into(thumb);
                        showBottomSheet();

                        dialog.dismiss();
                    }
                }));
    }

    private void showBottomSheet(){
        bottomSheetDialog = new MyBottomSheetFragement(getBaseContext(), detailComic.chapter_list, detailComic, this);
    }


    @Override
    protected void onDestroy() {
        if(backGroundTask != null){
            backGroundTask.cancel(true);
        }
        super.onDestroy();
    }

    @Override
    public void processFinish(String output) {
        chapter_endpoint = output;
    }

    @Override
    public void downloadFinish(String output) {
        taskCount += 1;
        progressDialog.setIndeterminate(false);
        progressDialog.setProgress(taskCount);
        if(progressDialog.getProgress() == progressDialog.getMax()){
            taskCount = 0;
            Toast.makeText(this, "Finish download", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
    }

    @Override
    public void onItemClick(int position) {
        List<String> chapter_image = new ArrayList<>();
        saveText(detailComic.desc, "", "desc");
//        fetchChapter(position);
//        progressDialog.setMessage("A message");
//        progressDialog.setIndeterminate(true);
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//        progressDialog.setCancelable(false);
//        progressDialog.show();
//        progressDialog.setMax(detailComic.chapter_list.get(position).chapter_image.size()+2);
//        DownloadTask downloadTask = new DownloadTask(this, detailComic.chapter_list.get(position),detailComic);
//        File file = new File(Environment.getExternalStoragePublicDirectory("/Download comic/"+detailComic.title), "/"+detailComic.chapter_list.get(position).chapter_title);
//        File parent = new File(Environment.getExternalStoragePublicDirectory("/Download comic"), "/"+detailComic.title);
//        if(!parent.exists()){
//            parent.mkdirs();
//            String path = "/Download comic/"+detailComic.title+"/info";
//            String fileName = "/theme.jpg";
//            downloadTask.execute(detailComic.theme, path, fileName);
//            path = "/Download comic/"+detailComic.title+"/info";
//            fileName = "/thumb.jpg";
//            downloadTask.execute(detailComic.thumb, path, fileName);
//        }
//        if(!file.exists()){
//            file.mkdirs();
//            String fileName;
//            String path = "/Download comic/"+detailComic.title+"/"+detailComic.chapter_list.get(position).chapter_title;
//            for(int i=0; i<detailComic.chapter_list.get(position).chapter_image.size(); i++){
//                fileName = "/"+i+".jpg";
//                downloadTask.execute(detailComic.theme, path, fileName);
//            }
//        }else{
//            Toast.makeText(this, "Chap này đã được tải", Toast.LENGTH_SHORT).show();
//        }
    }

    private void fetchChapter(int position) {
        progressDialog.setMessage("A message");
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.show();
        compositeDisposable.add(iComicAPI.getChapter(detailComic.chapter_list.get(position).chapter_endpoint)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Chapter>>() {
                    @Override
                    public void accept(List<Chapter> chapter) throws Exception {
                        progressDialog.setMax(chapter.get(0).chapter_image.size()+2);

                        File file = new File(Environment.getExternalStoragePublicDirectory("/Download comic/"+detailComic.title), "/"+detailComic.chapter_list.get(position).chapter_title);
                        File parent = new File(Environment.getExternalStoragePublicDirectory("/Download comic"), "/"+detailComic.title);
                        if(!parent.exists()){
                            parent.mkdirs();
                            DownloadTask downloadThemeTask = new DownloadTask();
                            String path = "/Download comic/"+detailComic.title+"/info";
                            String fileName = "/theme.jpg";
                            String res = downloadThemeTask.execute(detailComic.theme, path, fileName).get();
                            System.out.println(res);
                            DownloadTask downloadThumbTask = new DownloadTask();
                            path = "/Download comic/"+detailComic.title+"/info";
                            fileName = "/thumb.jpg";
                            res = downloadThumbTask.execute(detailComic.thumb, path, fileName).get();
                            System.out.println(res);


                        }
//                        if(!file.exists()){
//                            file.mkdirs();
//                            String fileName;
//                            String path = "/Download comic/"+detailComic.title+"/"+detailComic.chapter_list.get(position).chapter_title;
//                            for(int i=0; i<chapter.get(0).chapter_image.size(); i++){
//                                fileName = "/"+i+".jpg";
//                                downloadTask.execute(detailComic.theme, path, fileName);
//                            }
//                        }else{
//                            Toast.makeText(getBaseContext(), "Chap này đã được tải", Toast.LENGTH_SHORT).show();
//                        }
                    }
                }));
    }

    private static class BackGroundTask extends AsyncTask<Void, Void, String> {

        //Prevent leak
        private WeakReference<Activity> weakActivity;
        private MarkedChapterViewModel markedChapterViewModel;
        private String endpoint;
        private AlertDialog dialog;
        private AsyncTaskResponse res = null;

        public BackGroundTask(Activity activity, MarkedChapterViewModel markedChapterViewModel, String endpoint) {
            weakActivity = new WeakReference<>(activity);
            this.markedChapterViewModel = markedChapterViewModel;
            this.endpoint = endpoint;
        }

        @Override
        protected void onPreExecute() {
            dialog = new SpotsDialog.Builder().setContext(weakActivity.get()).setMessage("Loading...").build();
            dialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            MarkedChapter markedChapter = markedChapterViewModel.findMarkedChapter(endpoint);
            if(markedChapter == null) return null;
            String chapter_endpoint = markedChapter.chapter_endpoint;
            return chapter_endpoint;
        }

        @Override
        protected void onPostExecute(String chapter_endpoint) {
            Activity activity = weakActivity.get();
            if(activity == null) {
                return;
            }
            dialog.dismiss();
            res.processFinish(chapter_endpoint);
        }
    }


    private static class DownloadTask extends AsyncTask<String, Integer, String> {
        //Prevent leak

        private AsyncTaskResponse res = null;

        public DownloadTask() {
        }

        @Override
        protected String doInBackground(String... strings) {
            InputStream inputStream = null;
            OutputStream outputStream = null;
            HttpURLConnection httpURLConnection = null;
            try{
                URL url= new URL(strings[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();
                if (httpURLConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + httpURLConnection.getResponseCode()
                            + " " + httpURLConnection.getResponseMessage();
                }
                int fileLength = httpURLConnection.getContentLength();
                File path = Environment.getExternalStoragePublicDirectory(strings[1]);
                path.mkdir();
                File file = new File(path, strings[2]);
                // download the file
                inputStream = httpURLConnection.getInputStream();
                outputStream = new FileOutputStream(file);

                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = inputStream.read(data)) != -1) {
                    total += count;
                    if (fileLength > 0) // only if total length is known
                        publishProgress((int) (total * 100 / fileLength));
                    outputStream.write(data, 0, count);
                }
            }catch (Exception e) {
                return e.toString();
            } finally {
                try {
                    if (outputStream != null)
                        outputStream.close();
                    if (inputStream != null)
                        inputStream.close();
                } catch (IOException ignored) {
                }

                if (httpURLConnection != null)
                    httpURLConnection.disconnect();
            }

//            BaiHat baiHat = new BaiHat();
//            baiHat.setTenBaiHat(strings[1]);
//            baiHat.setLinkBaiHat(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Temp/"+strings[1]+".mp3");
////            baiHat.setCaSi(strings[3]);
//
//            MainActivity.arrOfflineSong.add(0, baiHat);

            String result = "finish";
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
//            res.downloadFinish(result);
            this.cancel(true);
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
                    .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,path + File.separator + filename);
            dm.enqueue(request);
        }catch (Exception e){
            Toast.makeText(this, "Image download failed.", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveText(String data, String path, String fileName){
        try {
            File dir = new File(Environment.getExternalStoragePublicDirectory("/Download comic") + path);
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