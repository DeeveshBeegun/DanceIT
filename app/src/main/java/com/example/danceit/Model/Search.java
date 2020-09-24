package com.example.danceit.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Search {

    private List<Video> allVideos;
    private Tag [] searchKeywords;
    int max;

    /*Constructor*/
    public Search(Tag [] searchKeywords, List<Video> allVideos) {
        this.searchKeywords = searchKeywords;
        this.allVideos = allVideos;
        this.max=1;
    }

    public ArrayList<Video> searchResults(){
        Map <Video, Integer> map = new HashMap<Video, Integer> ();
        ArrayList<Video> searchResults = new ArrayList<>();

        for (int a = 0; a < allVideos.size() ; a++) {
            Video video = allVideos.get(a);
            ArrayList<Tag> tagList = video.getTag_list();

            for(int i=0; i<searchKeywords.length; i++){
                if(tagList.contains(searchKeywords[i])){ //If video has the specified tag do the following below
                    if(! map.isEmpty() && map.containsKey(video)){
                        int newValue = map.get(video)+1;
                        map.replace(video, newValue);
                        if (max<newValue){
                            max=newValue;
                        }
                    }
                    else{
                        map.put(video, 1);
                    }
                }
            }
        }

        if(!map.isEmpty()){
            for(Map.Entry <Video, Integer> item: map.entrySet()) {
                if(item.getValue()==max){
                    searchResults.add(item.getKey());
                }
            }
        }

        return searchResults;
    }
}
