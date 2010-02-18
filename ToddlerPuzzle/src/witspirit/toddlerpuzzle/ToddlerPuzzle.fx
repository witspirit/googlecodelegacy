package witspirit.toddlerpuzzle;

import javafx.stage.Stage;
import javafx.stage.Screen;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Rectangle2D;

def playArea = Screen.primary.visualBounds;
// def playArea = Rectangle2D {
//    minX: 100;
//    minY: 100;
//    width: 700;
//    height: 700;
// }

def puzzleToAreaRatio = 0.7;
def rows = 3;
def columns = 3;
// def imageUrl = "{__DIR__}testImage.jpg";
// def imageUrl = "http://farm3.static.flickr.com/2486/4100325856_c9872ac655_b_d.jpg";
def imageUrl = "http://farm3.static.flickr.com/2666/4240174977_815312779e_b_d.jpg";

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
    frameX : 100;
    frameY : 100;
}
puzzle.onDone = puzzle.shuffle;

var playScene = Scene {
    content: [
    	puzzle
    ]
}

var screen = Stage {
    title: "Toddler Puzzle";
    x : playArea.minX;
    y : playArea.minY;
    width : playArea.width;
    height : playArea.height;
    scene: playScene;
    fullScreen : false;
}

