package com.uom.cps3230.Alerts;

import com.uom.cps3230.Alerts.Enum.AlertState;
import com.uom.cps3230.Log.Enum.LogState;
import com.uom.cps3230.Log.LogModelTest;
import nz.ac.waikato.modeljunit.Action;
import nz.ac.waikato.modeljunit.FsmModel;
import nz.ac.waikato.modeljunit.GreedyTester;
import nz.ac.waikato.modeljunit.StopOnFailureListener;
import nz.ac.waikato.modeljunit.coverage.ActionCoverage;
import nz.ac.waikato.modeljunit.coverage.StateCoverage;
import nz.ac.waikato.modeljunit.coverage.TransitionPairCoverage;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;

public class AlertModelTest implements FsmModel {
    AlertOperator sut = new AlertOperator();
    AlertState stateEnum = AlertState.START;
    int alertCnt = 0;


    @Override
    public AlertState getState() {
        return stateEnum;
    }

    @Override
    public void reset(boolean b) {

        if(b) {
            sut = new AlertOperator();
        }

        alertCnt = 0;
        stateEnum = AlertState.START;
    }

    public boolean addAlertGuard (){
        return getState().equals(AlertState.START) || getState().equals(AlertState.OK);
    }
    public @Action void addAlert() throws InterruptedException {
        sut.addAlert();
        if (alertCnt < 5){
            stateEnum = AlertState.OK;
            alertCnt++;
            assertEquals(alertCnt, sut.getAlertCnt());
            assertEquals(false, sut.getTooMany());
            assertEquals(false, sut.getBadDelete());
        }
        else {
            stateEnum = AlertState.TOOMANY;
            alertCnt = 5;
            assertEquals(alertCnt, sut.getAlertCnt());
            assertEquals(true, sut.getTooMany());
            assertEquals(false, sut.getBadDelete());
        }


    }

    public boolean deleteAlertGuard (){
        return getState().equals(AlertState.START) || getState().equals(AlertState.OK);
    }
    public @Action void deleteAlert() throws InterruptedException {
        sut.deleteAlert();

        if (alertCnt > 1){
            stateEnum = AlertState.OK;
            alertCnt--;
            assertEquals(alertCnt, sut.getAlertCnt());
            assertEquals(false, sut.getTooMany());
            assertEquals(false, sut.getBadDelete());
        }
        else if (alertCnt == 1){
            stateEnum = AlertState.START;
            alertCnt--;
            assertEquals(alertCnt, sut.getAlertCnt());
            assertEquals(false, sut.getTooMany());
            assertEquals(false, sut.getBadDelete());
        }
        else {
            stateEnum = AlertState.BADDELETE;
            alertCnt = 0;
            assertEquals(alertCnt, sut.getAlertCnt());
            assertEquals(false, sut.getTooMany());
            assertEquals(true, sut.getBadDelete());
        }
    }

    public boolean allAlertGuard (){
        return getState().equals(AlertState.START) || getState().equals(AlertState.OK);
    }
    public @Action void allAlert() throws InterruptedException {
        sut.allAlert();
        if(alertCnt == 0){
            stateEnum = AlertState.START;
            assertEquals(alertCnt, sut.getAlertCnt());
        }
        else{
            stateEnum = AlertState.OK;
            assertEquals(alertCnt, sut.getAlertCnt());
        }
    }


    //Test runner
    @Test
    public void BulbOperatorModelRunner() {
        final GreedyTester tester = new GreedyTester(new AlertModelTest()); //Creates a test generator that can generate random walks. A greedy random walk gives preference to transitions that have never been taken before. Once all transitions out of a state have been taken, it behaves the same as a random walk.
        tester.setRandom(new Random()); //Allows for a random path each time the model is run.
        tester.buildGraph(); //Builds a model of our FSM to ensure that the coverage metrics are correct.
        tester.addListener(new StopOnFailureListener()); //This listener forces the test class to stop running as soon as a failure is encountered in the model.
        tester.addListener("verbose"); //This gives you printed statements of the transitions being performed along with the source and destination states.
        tester.addCoverageMetric(new TransitionPairCoverage()); //Records the transition pair coverage i.e. the number of paired transitions traversed during the execution of the test.
        tester.addCoverageMetric(new StateCoverage()); //Records the state coverage i.e. the number of states which have been visited during the execution of the test.
        tester.addCoverageMetric(new ActionCoverage()); //Records the number of @Action methods which have ben executed during the execution of the test.
        tester.generate(500); //Generates 500 transitions
        tester.printCoverage(); //Prints the coverage metrics specified above.
    }
}
