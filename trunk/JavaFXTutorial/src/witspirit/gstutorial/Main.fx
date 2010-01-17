package witspirit.gstutorial;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.text.*;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.animation.Interpolator;


var maxRadius = 120;
var minRadius = 70;

var opacity = 1.0;
var gradius = maxRadius;
var fontSize = 22;

Timeline {
    repeatCount: Timeline.INDEFINITE,
    keyFrames: [
        KeyFrame {
            time: 3s
            canSkip: true
        	values : [
            	opacity => 0.5 tween Interpolator.EASEBOTH,
            	gradius => minRadius tween Interpolator.LINEAR,
            	fontSize => 12 tween Interpolator.EASEBOTH
        	]
        }
        KeyFrame {
            time : 6s
            canSkip: true
            values : [
            	opacity => 1.0 tween Interpolator.EASEBOTH,
            	gradius => maxRadius tween Interpolator.LINEAR,
            	fontSize => 22 tween Interpolator.EASEBOTH
            ]
        }
    ]
}.play();

Stage {
    title : "First JavaFX App"
    scene: Scene {
        width: 250
        height: 250
        content: [ 
        	Circle {
            	centerX: 100
        		centerY: 100
            	radius: bind gradius
            	opacity : bind opacity
            	fill: RadialGradient {
            	    centerX : 75
            	    centerY : 75
            	    radius : gradius
            	    proportional : false
            	    stops : [
            	    	Stop {
            	    	    offset : 0.0
            	    	    color : Color.web("#3B8DED")
            	    	}
            	    	Stop {
            	    	    offset : 1.0
            	    	    color : Color.web("#044EA4")
            	    	}
            	    ]
            	}
        	}
        	Text {
        	    font : bind Font { size: fontSize }
        	    x: 40 y : 90
        	    textAlignment : TextAlignment.CENTER
        	    content : "Welcome to \nJavaFX !"
        	    
        	} 
        ]
    }
}