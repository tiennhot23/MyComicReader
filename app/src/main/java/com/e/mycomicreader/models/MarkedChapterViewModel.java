package com.e.mycomicreader.models;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.e.mycomicreader.entity.MarkedChapter;
import com.e.mycomicreader.repository.MarkedChapterRepository;

import java.util.List;

public class MarkedChapterViewModel extends AndroidViewModel {

   private MarkedChapterRepository markedChapterRepository;

   private final LiveData<List<MarkedChapter>> listMarkedChapter;

   public MarkedChapterViewModel (Application application) {
       super(application);
       markedChapterRepository = new MarkedChapterRepository(application);
       listMarkedChapter = markedChapterRepository.getAll();
   }

    public LiveData<List<MarkedChapter>> getAll(){
        return listMarkedChapter;
    }

    public MarkedChapter findMarkedChapter(String endpoint){
        return markedChapterRepository.findMarkedChapter(endpoint);
    }

    public void insertAll(MarkedChapter... markedChapters){
        markedChapterRepository.insertAll(markedChapters);
    }

    public void insert(MarkedChapter markedChapter){
        markedChapterRepository.insert(markedChapter);
    }

    public void delete(MarkedChapter markedChapter){

       markedChapterRepository.delete(markedChapter);
    }

    public void update(MarkedChapter markedChapter){
       markedChapterRepository.update(markedChapter);
    }
}