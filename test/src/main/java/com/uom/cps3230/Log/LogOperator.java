package com.uom.cps3230.Log;


import java.util.Random;

public class LogOperator{
    private boolean logged = false;
    int count = 0;

    public boolean login(boolean userId) throws InterruptedException {

        if (userId == true){
            logged = true;
            count = 0;

        }else {
            if(count < 2){
                logged = false;
                count++;
            }
            else{
                Thread.sleep(2000);
                logged = false;
                count = 0;
            }
        }
        return true;
    }

    boolean logout(){

        logged = false;
        count = 0;
        return true;
    }

    public boolean isLogged(){
        return logged;
    }

    public int getCount(){
        return count;
    }

}
