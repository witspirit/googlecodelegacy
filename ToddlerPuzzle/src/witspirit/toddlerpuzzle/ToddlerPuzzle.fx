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

def puzzleToAreaRatio = 0.6;
def rows = 3;
def columns = 3;
def images = [
	"{__DIR__}testImage.jpg",
	"http://farm3.static.flickr.com/2486/4100325856_c9872ac655_b_d.jpg",
	"http://farm3.static.flickr.com/2666/4240174977_815312779e_b_d.jpg",
	"http://www.cornerstonetech.biz/docs/WTP_Wallpaper_1280x1024.jpg",
	"http://www.4freedesktopwallpaper.com/winnie-the-pooh/wallpaper_winnie_the_pooh_06lg.jpg",
	"http://www.disneywallpaper.net/data/media/7/Winnie_the_Pooh_800.jpg",
	"http://www.bszonnedorp.be/Kleuters/gele%20klas/themabrieven/WinnieThePoohWallpaper2800.jpg",
	"http://www.cartoonwallpapers.in/bulkupload/cartoon2/Others/Winnie%20the%20Pooh.jpg",
	"http://w1.ohoboho.com/walls/a47864f4c33f3e38bf9b807b78d8896e.jpg",
];

// def imageUrl = "{__DIR__}testImage.jpg";
// def imageUrl = "http://farm3.static.flickr.com/2486/4100325856_c9872ac655_b_d.jpg";
// def imageUrl = "http://farm3.static.flickr.com/2666/4240174977_815312779e_b_d.jpg";
// def imageUrl = "http://www.cornerstonetech.biz/docs/WTP_Wallpaper_1280x1024.jpg";
// def imageUrl = "http://www.4freedesktopwallpaper.com/winnie-the-pooh/wallpaper_winnie_the_pooh_06lg.jpg";
// def imageUrl = "http://www.disneywallpaper.net/data/media/7/Winnie_the_Pooh_800.jpg";
// def imageUrl = "http://www.bszonnedorp.be/Kleuters/gele%20klas/themabrieven/WinnieThePoohWallpaper2800.jpg";
// def imageUrl = "http://www.cartoonwallpapers.in/bulkupload/cartoon2/Others/Winnie%20the%20Pooh.jpg";
var imageIndex = 0;

def nextImage = function() : Void {
    if (imageIndex >= (sizeof images)-1) {
	    imageIndex = 0;
	} else {
	    imageIndex++;
	}
};

def imageUrl = bind images[imageIndex];

def puzzleImage = bind Image {
    url: imageUrl;
    preserveRatio : true;
    width : playArea.width * puzzleToAreaRatio;
    height : playArea.height * puzzleToAreaRatio;
}

var startScene : Scene;
var playScene : Scene;
var doneScene : Scene;
var screen : Stage;

def playAgain = function() : Void {
    nextImage();
	screen.scene = startScene;
}

def quit = function() : Void {
    FX.exit();
};

def start = function() : Void {
	screen.scene = playScene;
	puzzle.shuffle();   
};

def complete = function() : Void {
    screen.scene = doneScene;
}

def puzzle = bind Puzzle {
    rows: rows;
    columns: columns;
    image: puzzleImage;
    playArea : playArea;
    onDone: complete;
}

var buttonsDuringPlay = HBox {
    spacing: 10;
    content: [
    	Button {
    	    action: complete;
    	    text : "Complete";
    	},
    	Button {
    	    action: quit;
    	    text: "Quit";
    	}
    ];
}

buttonsDuringPlay.translateX = (fullArea.width-buttonsDuringPlay.width)/2;
buttonsDuringPlay.translateY = fullArea.maxY-buttonsDuringPlay.height;

var buttonsDuringComplete = HBox {
    spacing: 10;
    content: [
    	Button {
    	    action: playAgain;
    	    text: "Play Again"; 
    	}
    	Button {
    	    action: quit;
    	    text: "Quit";
    	}
    ];
}

buttonsDuringComplete.translateX = (fullArea.width-buttonsDuringComplete.width)/2;
buttonsDuringComplete.translateY = fullArea.maxY-buttonsDuringComplete.height;

playScene = Scene {
    content: bind [
    	puzzle,
    	buttonsDuringPlay
    ]
}

doneScene = Scene {
    content: bind [
    	ImageView {
    	    image : puzzleImage;
    	    x : (playArea.width-puzzleImage.width)/2;
    	    y : (playArea.height-puzzleImage.height)/2;
    	    onMousePressed : function(event) {
				playAgain();
    	    };
    	},
    	buttonsDuringComplete
    ]
}

var buttonsDuringStart = HBox {
    spacing: 10;
    content: [
    	Button {
    	    action: start;
    	    text : "Start";
    	},
    	Button {
    	    action: quit;
    	    text: "Quit";
    	}
    ];
}

buttonsDuringStart.translateX = (fullArea.width-buttonsDuringStart.width)/2;
buttonsDuringStart.translateY = fullArea.maxY-buttonsDuringStart.height;

startScene = Scene {
    content: bind [
    	ImageView {
    	    image : puzzleImage;
    	    x : (playArea.width-puzzleImage.width)/2;
    	    y : (playArea.height-puzzleImage.height)/2;
    	    onMousePressed : function(event) {
				nextImage();
    	    };
    	},
    	buttonsDuringStart
    ]
}

screen = Stage {
    title: "Toddler Puzzle";
    x : playArea.minX;
    y : playArea.minY;
    width : playArea.width;
    height : playArea.height;
    scene: startScene;
    fullScreen : true;
}

