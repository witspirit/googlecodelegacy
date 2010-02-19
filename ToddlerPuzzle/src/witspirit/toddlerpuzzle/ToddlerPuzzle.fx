package witspirit.toddlerpuzzle;

import javafx.stage.Stage;
import javafx.stage.Screen;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

def fullArea = Screen.primary.visualBounds;
def playArea = Rectangle2D {
    minX: fullArea.minX;
    minY: fullArea.minY;
    width: fullArea.width;
    height: fullArea.height-50;
}

def puzzleToAreaRatio = 0.7;
def rows = 3;
def columns = 3;
// def imageUrl = "{__DIR__}testImage.jpg";
// def imageUrl = "http://farm3.static.flickr.com/2486/4100325856_c9872ac655_b_d.jpg";
// def imageUrl = "http://farm3.static.flickr.com/2666/4240174977_815312779e_b_d.jpg";
def imageUrl = "http://www.cornerstonetech.biz/docs/WTP_Wallpaper_1280x1024.jpg";

def puzzleImage = Image {
    url: imageUrl;
    preserveRatio : true;
    width : playArea.width * puzzleToAreaRatio;
    height : playArea.height * puzzleToAreaRatio;
}

def puzzle = Puzzle {
    rows: rows;
    columns: columns;
    image: puzzleImage;
    playArea : playArea;
}

var playScene : Scene;
var doneScene : Scene;
var screen : Stage;


var buttonsDuringPlay = HBox {
    content: [
    	Button {
    	    action: function() {
    	    	screen.scene = doneScene;    
    	    };
    	    text : "Complete";
    	},
    	Button {
    	    action: function() {
    	        FX.exit();
    	    };
    	    text: "Quit";
    	}
    ];
}

buttonsDuringPlay.translateX = (fullArea.width-buttonsDuringPlay.width)/2;
buttonsDuringPlay.translateY = fullArea.maxY-buttonsDuringPlay.height;

var buttonsDuringComplete = HBox {
    content: [
    	Button {
    	    action: function() {
    	        FX.exit();
    	    };
    	    text: "Quit";
    	}
    ];
}

buttonsDuringComplete.translateX = (fullArea.width-buttonsDuringComplete.width)/2;
buttonsDuringComplete.translateY = fullArea.maxY-buttonsDuringComplete.height;

playScene = Scene {
    content: [
    	puzzle,
    	buttonsDuringPlay
    ]
}

doneScene = Scene {
    content: [
    	ImageView {
    	    image : puzzleImage;
    	    x : (playArea.width-puzzleImage.width)/2;
    	    y : (playArea.height-puzzleImage.height)/2;
    	    onMousePressed : function(event) {
				screen.scene = playScene;
    	        puzzle.shuffle();
    	    };
    	},
    	buttonsDuringComplete
    ]
}

puzzle.onDone = function() {
    screen.scene = doneScene;
}

screen = Stage {
    title: "Toddler Puzzle";
    x : playArea.minX;
    y : playArea.minY;
    width : playArea.width;
    height : playArea.height;
    scene: playScene;
    fullScreen : true;
}

