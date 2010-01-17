package witspirit.toddlerpuzzle;

import javafx.stage.Stage;
import javafx.stage.Screen;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Rectangle2D;

def puzzleImage = Image {
    url: "{__DIR__}testImage.jpg";
    preserveRatio : true;
    width : Screen.primary.visualBounds.width * 0.8;
    height : Screen.primary.visualBounds.height * 0.8;
}

def puzzle = Puzzle {
    rows: 4;
    columns: 4;
    image: puzzleImage;
}

var configurationScene = Scene {
    content: [
    	for (piece in puzzle.pieces) {
    	    piece.view;
    	}
    ]
}

var screen = Stage {
    title: "Toddler Puzzle";
    x : Screen.primary.visualBounds.minX;
    y : Screen.primary.visualBounds.minY;
    width : Screen.primary.visualBounds.width;
    height : Screen.primary.visualBounds.height;
    scene: configurationScene;
}

