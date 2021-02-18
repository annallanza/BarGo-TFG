package com.example.bargo;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;

public class RetosInfo {
    private Context context;
    private ArrayList<String> challengeName;
    private ArrayList<Boolean> challengeCompleted;
    private ArrayList<Integer> totalProgress;
    private ArrayList<Integer> progress;
    private ArrayList<Integer> points;
    private ArrayList<String> challengeType;
    public static RetosInfo instance = null;

    public RetosInfo(){
        setChallengeNames();
        setChallengeCompleted();
        setTotalProgress();
        setProgress();
        setPoints();
        setChallengeType();
    }

    public static RetosInfo getInstance(){
        if(instance == null)
            instance = new RetosInfo();
        return instance;
    }

    private void setChallengeNames(){
        this.challengeName = new ArrayList<String>();
        this.challengeName.add("Visita 10 bares");
        this.challengeName.add("Consume 10 productos");
        this.challengeName.add("Obten un producto");
    }
    public ArrayList<String> getChallengeName() {
        return challengeName;
    }
    private void setChallengeCompleted(){
        this.challengeCompleted = new ArrayList<Boolean>();
        this.challengeCompleted.add(false);
        this.challengeCompleted.add(true);
        this.challengeCompleted.add(false);
    }
    public ArrayList<Boolean> getChallengeCompleted() {
        return challengeCompleted;
    }
    private void setTotalProgress(){
        this.totalProgress = new ArrayList<Integer>();
        this.totalProgress.add(10);
        this.totalProgress.add(10);
        this.totalProgress.add(1);
    }
    public ArrayList<Integer> getTotalProgress() {
        return totalProgress;
    }
    private void setProgress(){
        this.progress = new ArrayList<Integer>();
        this.progress.add(5);
        this.progress.add(10);
        this.progress.add(0);
    }
    public ArrayList<Integer> getProgress() {
        return progress;
    }
    private void setPoints(){
        this.points = new ArrayList<Integer>();
        this.points.add(2400);
        this.points.add(3000);
        this.points.add(200);
    }
    public ArrayList<Integer> getPoints() {
        return points;
    }
    private void setChallengeType(){
        this.challengeType = new ArrayList<String>();
        this.challengeType.add("BAR");
        this.challengeType.add("BEER");
        this.challengeType.add("PROD");
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void changeChallenges(String type){
        for(int i = 0; i < challengeType.size(); i++){
            System.out.println(challengeType.get(i));
            if((challengeType.get(i)).equals(type)){
                if(!challengeCompleted.get(i)) {
                    int val = progress.get(i);
                    val++;
                    progress.set(i, val);
                    if (val == totalProgress.get(i)) {
                        challengeCompleted.set(i, true);
                        Toast.makeText(context, "Reto completado!", Toast.LENGTH_SHORT).show();
                        User.getInstance().addPoints(points.get(i));
                    }
                }
            }
        }
    }
}
