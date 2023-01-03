package com.uom.cps3230.Alerts;

public class AlertOperator {

    private int alertCnt = 0;
    private boolean badDelete = false, tooMany = false;
    boolean addAlert(){
        if(alertCnt < 5){
            alertCnt++;
            badDelete = false;
            tooMany = false;
        }else {
            alertCnt = 5;
            badDelete = false;
            tooMany = true;
        }
        return true;
    }

    boolean deleteAlert(){
        if (alertCnt >= 1){
            alertCnt--;
            badDelete = false;
            tooMany = false;
        }else {
            alertCnt = 0;
            badDelete = true;
            tooMany = false;
        }
        return true;
    }

    int allAlert(){
        return alertCnt;
    }

    public int getAlertCnt(){
        return alertCnt;
    }
    public boolean getTooMany(){return tooMany;}
    public boolean getBadDelete(){return badDelete;}
}
