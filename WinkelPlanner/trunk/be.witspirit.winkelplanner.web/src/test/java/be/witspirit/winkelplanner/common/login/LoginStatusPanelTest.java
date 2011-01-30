package be.witspirit.winkelplanner.common.login;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.util.tester.TestPanelSource;
import org.junit.Before;
import org.junit.Test;

import be.witspirit.winkelplanner.test.WinkelPlannerTester;
import be.witspirit.winkelplanner.web.common.login.LoginStatusPanel;

public class LoginStatusPanelTest {

    private TestPanelSource panelSource;

    @Before
    public void setUp() {
        panelSource = new TestPanelSource() {
            @Override
            public Panel getTestPanel(String id) {
                return new LoginStatusPanel(id);
            }
        };
    }

    @Test
    public void notLoggedIn() {
        WinkelPlannerTester tester = new WinkelPlannerTester(false);

        tester.startPanel(panelSource);
        tester.assertComponent("panel:status", LoginStatusPanel.NotLoggedIn.class);
    }
    
    @Test
    public void loggedIn() {
        WinkelPlannerTester tester = new WinkelPlannerTester(true);

        tester.startPanel(panelSource);
        tester.assertComponent("panel:status", LoginStatusPanel.LoggedIn.class);
    }
}
