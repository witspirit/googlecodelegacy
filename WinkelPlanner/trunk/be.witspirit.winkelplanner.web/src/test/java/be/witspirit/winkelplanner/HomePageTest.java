package be.witspirit.winkelplanner;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;


public class HomePageTest {

    @Test
    public void simpleRender() {
        WicketTester tester = new WicketTester();
        tester.startPage(HomePage.class);
        tester.assertRenderedPage(HomePage.class);
    }
}
