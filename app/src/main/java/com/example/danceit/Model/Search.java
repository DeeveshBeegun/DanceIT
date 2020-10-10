/**
 * This class represents the model for the Search object
 *
 * @author Bohlale Motsieloa (MTSBOH002)
 * @date: 29/09/2020
 * @version: 1.0
 */
package com.example.danceit.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Search {

    private ArrayList<Video> allVideos;
    private String [] searchKeywords;
    int max;

    /*Constructor*/
    public Search(String [] searchKeywords, ArrayList<Video> allVideos) {
        this.searchKeywords = searchKeywords;
        this.allVideos = allVideos;
        this.max=1;
    }

    /*Returns a list of videos that have the most matches (determined by max) for the search keywords
      the user queried*/
    public ArrayList<Video> searchResults(){
        Map <Video, Integer> map = new HashMap<Video, Integer> (); // Stores videos and their match count, only
        //videos with a count equal to max are part of the search results

        ArrayList<Video> searchResults = new ArrayList<>();

        for (int a = 0; a < allVideos.size() ; a++) {
            Video video = allVideos.get(a);
            ArrayList<String> tagList = video.getTags();

            for(int i=0; i<searchKeywords.length; i++){
                if(tagList.contains(searchKeywords[i])){ //If video contains the specified tag
                    if(! map.isEmpty() && map.containsKey(video)){
                        int newValue = map.get(video)+1;
                        map.replace(video, newValue);
                        if (max<newValue){
                            max=newValue; //Update max
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
                    System.out.println("Yesssssssssssssssssssssssssssssss");
                    searchResults.add(item.getKey());
                    System.out.println("Noooooooooooooooooooooooo");
                }
            }
        }

        return searchResults;
    }
}
