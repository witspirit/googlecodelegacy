package witspirit.toddlerpuzzle;

import javafx.stage.Stage;
import javafx.stage.Screen;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Rectangle2D;

def playArea = Screen.primary.visualBounds;

def puzzleImage = Image {
    url: "{__DIR__}testImage.jpg";
    preserveRatio : true;
    width : playArea.width * 0.8;
    height : playArea.height * 0.8;
}

def puzzle = Puzzle {
    rows: 4;
    columns: 4;
    image: puzzleImage;
    playArea : playArea;
}

var configurationScene = Scene {
    content: bind [
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

