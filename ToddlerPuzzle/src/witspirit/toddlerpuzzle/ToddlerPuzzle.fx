package witspirit.toddlerpuzzle;

import javafx.stage.Stage;
import javafx.stage.Screen;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Rectangle2D;

// def playArea = Screen.primary.visualBounds;
def playArea = Rectangle2D {
    minX: 100;
    minY: 100;
    width: 300;
    height: 300;
}

def puzzleImage = Image {
    url: "{__DIR__}testImage.jpg";
    preserveRatio : true;
    width : playArea.width * 0.7;
    height : playArea.height * 0.7;
}

def puzzle = Puzzle {
    rows: 3;
    columns: 3;
    image: puzzleImage;
    playArea : playArea;
}

var targetArea = puzzle.targetArea;
targetArea.translateX = 10;
targetArea.translateY = 10;

var configurationScene = Scene {
    content: bind [
    	targetArea,
    	for (piece in puzzle.pieces) {
    	    piece.view;
    	}
    ]
}

var screen = Stage {
    title: "Toddler Puzzle";
    x : playArea.minX;
    y : playArea.minY;
    width : playArea.width;
    height : playArea.height;
    scene: configurationScene;
}

