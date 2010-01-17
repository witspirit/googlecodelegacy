package witspirit.toddlerpuzzle;

import javafx.stage.Stage;
import javafx.stage.Screen;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

def puzzleImage = Image {
    url: "file:/Users/witspirit/Pictures/iPhoto Library/Originals/2005/0506-02/050602-056.jpg";
    preserveRatio : true;
}




def imagePreview = ImageView {
    image: puzzleImage;
    preserveRatio : true;
    fitWidth : Screen.primary.visualBounds.width * 0.8;
    fitHeight : Screen.primary.visualBounds.height * 0.8;
}

var configurationScene = Scene {
    content: [
    	imagePreview
    ]
}

var screen = Stage {
    title: "Toddler Puzzle";
    x : Screen.primary.visualBounds.minX;
    y : Screen.primary.visualBounds.minY;
    width : Screen.primary.visualBounds.width;
    height : Screen.primary.visualBounds.height;
}

screen.scene = configurationScene;