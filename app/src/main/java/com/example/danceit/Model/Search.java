package com.example.danceit.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This class represents the Search Object and which implements the
 * search algorithm for finding Video Objects.
 */

public class Search {

    private ArrayList<Video> allVideos;
    private String [] searchKeywords;
    int max;

    /**
     * Constructor
     *@param searchKeywords search queries by user.
     *@param allVideos all videos which need to be searched.
     */
    public Search(String [] searchKeywords, ArrayList<Video> allVideos) {
        this.searchKeywords = searchKeywords;
        this.allVideos = allVideos;
        this.max=1;
    }

    /**
     * The implementation of the search algorithm
     */
    public ArrayList<String> searchResults(){
        Map <Video, Integer> map = new HashMap<Video, Integer> (); // Stores videos and their match count, only
        //videos with a count equal to max are part of the search results

        ArrayList<String> searchResults = new ArrayList<String>();

        for (int a = 0; a < allVideos.size() ; a++) {
            Video video = allVideos.get(a);
            ArrayList<String> tagList = video.getTags();

            /*Converts all tags to lower case for same comparison
              below.*/
            for(int i = 0; i<tagList.size(); i++){
                tagList.set(i, tagList.get(i).toLowerCase());
            }

            for(int i=0; i<searchKeywords.length; i++){
                if(tagList.contains(searchKeywords[i].toLowerCase())){ //searchKeyword to lower case
                    if(! map.isEmpty() && map.containsKey(video)){
                        int newValue = map.get(video)+1;
                        map.replace(video, newValue);
                        if (max<newValue){
                            max=newValue; //Update newly found max
                        }
                    }
                    else{
                        map.put(video, 1);
                    }
                }
            }
        }

        /*Insert Videos with the highest number of matching
          tags in ArrayList */
        if(!map.isEmpty()){
            for(Map.Entry <Video, Integer> item: map.entrySet()) {
                if(item.getValue()==max){
                    Video video = item.getKey();
                    searchResults.add(video.getVideoId());
                }
            }
        }

        return searchResults;
    }
}
