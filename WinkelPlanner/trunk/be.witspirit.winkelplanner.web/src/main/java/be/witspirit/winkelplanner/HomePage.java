package be.witspirit.winkelplanner;

import org.apache.wicket.markup.html.basic.Label;

import be.witspirit.winkelplanner.web.common.BasePage;

public class HomePage extends BasePage {

  public HomePage() {
    add(new Label("hello", "Hello World"));
  }

}
