package witspirit.ui.declarative;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

def sceneWidth = 300;
def sceneHeight = 250;

def rectangleWidth = 250;
def rectangleHeight = 80;

Stage {
    title: "Introduction to the declarative style of JavaFx";
    scene : Scene {
        width : sceneWidth;
        height : sceneHeight;
        content : [
        	Circle {
        	    centerX : sceneWidth / 2;
        	    centerY : sceneHeight / 2;
        	    radius : sceneWidth / 4;
        	    fill : Color.MAROON;
        	    stroke : Color.INDIANRED;
        	    strokeWidth : 10.0;
        	},
        	Rectangle {
        	    x : (sceneWidth-rectangleWidth)/2;
        	    y : (sceneHeight - rectangleHeight)/2;
        	    width : rectangleWidth;
        	    height : rectangleHeight;
        	    arcWidth : 20;
        	    arcHeight : 20;
        	    fill: Color.web("#6699ff")
        	    stroke: Color.web("#003399")
        	    strokeWidth: 5.0
        	    opacity: 0.5
        	}
        ];
    }
};
