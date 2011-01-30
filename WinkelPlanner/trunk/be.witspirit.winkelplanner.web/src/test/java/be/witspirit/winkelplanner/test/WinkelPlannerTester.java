package be.witspirit.winkelplanner.test;

import org.apache.wicket.util.tester.WicketTester;

public class WinkelPlannerTester extends WicketTester {
    
    public WinkelPlannerTester(boolean loggedIn) {
        super(new WinkelPlannerTestApplication(new WinkelPlannerTestModule(loggedIn)));
    }

}
