package com.uom.cps3230.Log;

import com.uom.cps3230.Alerts.AlertOperator;
import com.uom.cps3230.Alerts.Enum.AlertState;
import com.uom.cps3230.Log.Enum.LogState;
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

public class LogModelTest implements FsmModel {
    LogOperator sut = new LogOperator();
    LogState stateEnum = LogState.START;
    int count = 0;
    boolean logged = false;

    Random rd = new Random();

    @Override
    public LogState getState() {
        return stateEnum;
    }

    @Override
    public void reset(boolean b) {
        if(b) {
            sut = new LogOperator();
        }
        count = 0;
        stateEnum = LogState.START;
    }

    public boolean loginGuard(){
        return getState().equals(LogState.START);
    }
    public @Action void login() throws InterruptedException {
        boolean userId = rd.nextBoolean();
        sut.login(userId);

        if (userId == true){
            stateEnum = LogState.OK;
            logged = true;
            count = 0;

            assertEquals(logged, sut.isLogged());
            assertEquals(count, sut.getCount());

        }else {
            if(count < 2){
                stateEnum = LogState.START;
                logged = false;
                count++;

                assertEquals(logged, sut.isLogged());
                assertEquals(count, sut.getCount());
            }
            else{
                stateEnum = LogState.BADPASSWORD;
                Thread.sleep(2000);

                logged = false;
                count = 0;
                stateEnum = LogState.START;

                assertEquals(logged, sut.isLogged());
                assertEquals(count, sut.getCount());
            }
        }
    }

    public boolean logoutGuard(){
        return getState().equals(LogState.OK);
    }
    public @Action void logout() {
        sut.logout();

        stateEnum = LogState.START;
        logged = false;

        assertEquals(logged, sut.isLogged());
        assertEquals(count, sut.getCount());

    }


    //Test runner
    @Test
    public void BulbOperatorModelRunner() {
        final GreedyTester tester = new GreedyTester(new LogModelTest()); //Creates a test generator that can generate random walks. A greedy random walk gives preference to transitions that have never been taken before. Once all transitions out of a state have been taken, it behaves the same as a random walk.
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
