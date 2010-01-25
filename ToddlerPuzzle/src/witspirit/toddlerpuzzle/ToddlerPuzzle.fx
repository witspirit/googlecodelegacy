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
    width: 700;
    height: 700;
}
def puzzleToAreaRatio = 0.4;
def rows = 3;
def columns = 3;

def puzzleImage = Image {
    url: "{__DIR__}testImage.jpg";
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

var targetArea = puzzle.targetArea;
targetArea.translateX = 30;
targetArea.translateY = 15;

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

