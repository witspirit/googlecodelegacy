package witspirit.ui.layout;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.RadioButton;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Tile;
import javafx.scene.layout.LayoutInfo;

def group = ToggleGroup {};
def choiceText = ["STOP", "READY", "GO"];

def choices = for (label in choiceText) {
    RadioButton {
        toggleGroup : group;
        text: label;
        // layoutInfo: LayoutInfo {
        //     width: 150;
        // }
    }
}

var colors = ["RED", "GOLD", "GREEN"];

var lights = for (color in colors) {
    Circle {
        centerX: 12;
        centerY: 12;
        radius: 12;
        stroke: Color.GRAY;
        fill: bind RadialGradient {
            centerX: 8;
            centerY: 8;
            radius: 12;
            proportional: false;
            stops: [
            	Stop {
            	    offset: 0.0;
            	    color: Color.WHITE;
            	},
            	Stop {
            	    offset: 1.0;
            	    color: if (choices[indexof color].selected) {
            	 		Color.web(color);       
            	    } else {
            	        Color.GRAY;
            	    }
            	}
            ]
        }
    }
}

// Box layouts are simple

def lightBox = HBox {
    spacing: 15;
    content: lights;
    translateY: 25;
}

def selectBox = VBox {
    spacing: 10;
    content: choices;
}

def overallBox = HBox {
    content: [
    	selectBox,
    	lightBox
    ]
}

// Alternatively, instead of using the boxes, we can use the tiles
def tile = Tile {
    columns: 2;
    rows: 3;
    vgap: 5;
    content: for (i in [0..2]) {
        [choices[i], lights[i]] 
    }
}

Stage {
    title: "Layout";
    scene: Scene {
        width: 210;
        height: 90;
        content: [
			tile
        ]
    }
}