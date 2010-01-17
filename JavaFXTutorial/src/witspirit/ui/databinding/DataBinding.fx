package witspirit.ui.databinding;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.control.Slider;

var circle = Circle {
    centerX: bind slider.value + 50;
    centerY: 60;
    radius: 50;
    stroke: Color.YELLOW;
    fill: RadialGradient {
        centerX: 50; 
        centerY: 60;
        radius: 50;
        proportional: false;
        focusX:50;
        focusY:30;
        stops: [
        	Stop {
        	    offset: 0.0;
        	    color: Color.RED;
        	},
        	Stop {
        	    offset: 1.0;
        	    color: Color.WHITE;
        	}
        ];
    }
}

var slider = Slider {
    min: 0;
    max: 60;
    value: 0;
    translateX: 10;
    translateY: 110;
}


Stage {
    title: "Data Binding";
    scene: Scene {
        width: 220;
        height: 170;
        content: [ 
        	circle,
        	slider
        ];
    }
}