package com.example.danceit.Model;

import android.app.Application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.opencensus.tags.Tags;

public class TagManagement {
    private Map <String, Integer> allTags;
    private List<Video> allLibraryVideos;
    private Application application;

    /*Constructor*/
    public TagManagement(List<Video> allLibraryVideos, Application application) {
        this.allLibraryVideos=allLibraryVideos;
        this.allTags = new HashMap<String,Integer>();
        this.application = application;
        extractTags();
    }

    public List<Video> getAllLibraryVideos() {
        return allLibraryVideos;
    }

    public void setAllLibraryVideos(List<Video> allLibraryVideos) {
        this.allLibraryVideos = allLibraryVideos;
    }

    private void extractTags (){
        for(Video video : this.allLibraryVideos){
            for(String videoTag : video.getTags()){
                if(! allTags.isEmpty() && allTags.containsKey(videoTag)){
                    allTags.replace(videoTag, allTags.get(videoTag)+1);
                }
                else{
                    allTags.put(videoTag,1);
                }
            }
        }
    }

    public void replaceTag(String tag, String newTag){
        for (Video video : allLibraryVideos) {
            ArrayList<String> list = video.getTags();
            if (list.contains(tag)){
                list.set(list.indexOf(tag), newTag);
                video.setTags(list);
                //videoViewModel.update(video);
            }
        }
    }

    public void deleteTag(String tag){
        for (Video video : allLibraryVideos) {
            ArrayList<String> list = video.getTags();
            if (list.contains(tag)){
                list.remove(tag);
                video.setTags(list);
                //videoViewModel.update(video);
            }
        }
    }

    public void addTag(String tag, String newTag){
        for (Video video : allLibraryVideos) {
            ArrayList<String> list = video.getTags();
            if (list.contains(tag)){
                list.set(list.indexOf(tag), newTag);
                video.setTags(list);
                //videoViewModel.update(video);
            }
        }
    }


    public int occurrenceOfTag(String tag){
        return allTags.get(tag);
    }

}



