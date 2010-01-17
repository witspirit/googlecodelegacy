package witspirit.ui.playbutton;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.Group;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Reflection;

def buttonOffsetX = 48;
def buttonOffsetY = 45;
def buttonSize = 150;

def LIGHTBLUE = Color.web("#99ddff");
def DARKBLUE = Color.web("#337799");
def LIGHTERBLUE = Color.web("#aaeeff");
def MEDIUMBLUE = Color.web("#66aacc");

def outline = Rectangle {
    x: buttonOffsetX;
    y: buttonOffsetY;
    width: buttonSize;
    height: buttonSize;
    arcWidth: 15;
    arcHeight: 15;
    stroke: DARKBLUE;
    fill: null;
    strokeWidth: 5;
}

def top = Rectangle {
    x: buttonOffsetX;
    y: buttonOffsetY;
    width: buttonSize;
    height: 60;
    arcWidth: 0;
    arcHeight: 0; 
    stroke: Color.GREEN;
    fill: LinearGradient {
        startX: 0.0;
        endX: 0.0;
        startY: 0.0;
        endY: 1.0;
        proportional: true;
        stops: [
            Stop {
                offset: 0.0;
                color: MEDIUMBLUE;
            },
            Stop {
                offset: 1.0;
                color: LIGHTERBLUE;
            }
        ] 
    }
}

def middle = Rectangle {
	x: buttonOffsetX;
	y: buttonOffsetY+55;
	width: buttonSize;
	height: 70;
	fill: LinearGradient {
	    startX: 0.0;
	    endX: 0.0;
	    startY: 0.0;
	    endY: 1.0;
	    proportional: true;
	    stops: [
	        Stop {
	            offset: 0.0;
	            color: DARKBLUE;
	        },
	        Stop {
	            offset: 1.0;
	           	color: LIGHTBLUE;
	        }
	    ] 
	}
}

def bottom = Rectangle {
    x: buttonOffsetX;
    y: buttonOffsetY+123;
    width: buttonSize;
    height: 25;
    fill : LinearGradient {
        startX: 0.0;
        endX: 0.0;
        startY: 0.0;
        endY: 1.0;
        proportional: true;
        stops: [
        	Stop{
        	    offset: 0.0;
        	    color: LIGHTBLUE;
        	},
        	Stop {
        	    offset: 1.0;
        	    color: DARKBLUE;
        	}
        ]
    }
}

def circle = Circle {
    centerX: buttonOffsetX+(buttonSize/2);
    centerY: buttonOffsetY+(buttonSize/2) + 3;
    radius: buttonSize/2/2; // We want half the size of the button (r=d/2; d=buttonSize/2)
    fill : LinearGradient {
        startX: 0.0;
        endX: 0.0;
        startY: 0.0;
        endY: 1.0;
        stops : [
        	Stop {
        	    offset: 0.0;
        	    color: LIGHTBLUE;
        	},
        	Stop {
        	    offset: 1.0;
        	    color: MEDIUMBLUE;
        	}
        ]
    }
    stroke: MEDIUMBLUE;
    strokeWidth: 2.0;
}

def SHADOWCOLOR = Color.web("#337799");

def shadowTriangle = Polygon {
    points: [142.0,  126.0, 113.0, 108.0, 111.0, 143.0]; // Seems unmanageable
    fill: SHADOWCOLOR;
}

def playTriangle = Polygon {
    points: [142.0,  123.0, 110.0, 105.0, 110.0, 140.0]; // Seems unmanageable
    fill: Color.WHITE;
    effect: DropShadow {
        color: SHADOWCOLOR;
        offsetX: 2;
        offsetY: 5;
    }
}

def substrate = Group {
    content: [
    	middle,
    	bottom,
    	top,
        outline
    ]
}

def playIcon = Group {
    content: [
    	circle,
    	// shadowTriangle, // Made obsolete by drop shadow effect
    	playTriangle
    ]
}

def button = Group {
    content: [
    	substrate,
    	playIcon
    ]
    effect: Reflection {
        fraction: 0.5;
        topOpacity: 0.5;
        topOffset: 0.5;
    }
}

Stage {
    title: "Play Button";
    scene: Scene {
        width: 250;
        height: 350;
        fill: Color.WHITE;
        content: [
        	button
        ]
    }
}