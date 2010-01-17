package witspirit.ui.events;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.animation.Timeline;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;

def play = Image {
	url: "{__DIR__}play_onMouseExited.png";
}
def playPressed = Image {
    url: "{__DIR__}play_onMousePressed.png"; 
}
def playEntered = Image {
    url: "{__DIR__}play_onMouseEntered.png";
}
def stop = Image {
    url: "{__DIR__}stop_onMouseExited.png";
}
def stopPressed = Image {
    url: "{__DIR__}stop_onMousePressed.png";
}
def stopEntered = Image {
    url: "{__DIR__}stop_onMouseEntered.png";
}

var image = play;
var button = ImageView {
    image: bind image;
}


var x : Number;
var y : Number;

var showPlay = true;

button.onMousePressed = function(event) {
    x = event.sceneX - event.node.translateX;
    y = event.sceneY - event.node.translateY;
    image = if (showPlay) {
        playPressed;
    } else {
     	stopPressed;   
    }
}
button.onMouseReleased = function(event) {
    if (showPlay) {
        showPlay = false;
        image = stopEntered;
    } else {
        showPlay = true;
        image = playEntered;
    }
}
button.onMouseDragged = function(event) {
    if (event.sceneX - x < 0) {
        event.node.translateX = 0;
    } else {
        if (event.sceneX - x > 300 - image.width) {
            event.node.translateX = 300 - image.width;
        } else {
            event.node.translateX = event.sceneX - x; 
        }
    }
    if (event.sceneY - y < 0) {
        event.node.translateY = 0;
    } else {
        if (event.sceneY - y > 240 - image.height) {
            event.node.translateY = 240 - image.height;
        } else {
            event.node.translateY = event.sceneY - y; 
        }
    }
}

var tooltip = Text {
    content: bind if (showPlay) { "Play Button" } else { "Stop Button" };
    translateX: bind button.translateX;
    translateY: bind button.translateY+80;
    opacity: 0.0;
    font: Font {
        size: 12;
        name: "Tahoma";
    }
    fill: Color.BLACK;
}

def appear = Timeline {
    keyFrames: [
    	at (0s) { tooltip.opacity => 0.0; },
    	at (1.5s) { tooltip.opacity => 1.0; }
    ]
}

button.onMouseEntered = function(event) {
    image = if (showPlay) {
        playEntered;
    } else {
        stopEntered;
    }
    appear.rate = 1;
    appear.play();
}
button.onMouseExited = function(event) {
    image = if (showPlay) {
        play;
    } else {
        stop;
    }
    appear.rate = -1;
    appear.play();
    tooltip.opacity = 0.0;
}

var group = Group {
    content: [
    	button,
    	tooltip
    ]
}

Stage {
    title: "Events";
    scene: Scene {
        width: 300;
        height: 240;
        content: [
        	group
        ]
    }
}