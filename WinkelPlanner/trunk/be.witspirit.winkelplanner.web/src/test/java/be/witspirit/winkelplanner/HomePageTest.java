package be.witspirit.winkelplanner;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;

import be.witspirit.winkelplanner.test.WinkelPlannerTester;


public class HomePageTest {

    @Test
    public void simpleRender() {
        WicketTester tester = new WinkelPlannerTester(false);
        tester.startPage(HomePage.class);
        tester.assertRenderedPage(HomePage.class);
    }
}
