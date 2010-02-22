package witspirit.toddlerpuzzle;

import javafx.stage.Stage;
import javafx.stage.Screen;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class ToddlerPuzzle {
    public-init var fullArea : Rectangle2D;
    public-init var fullScreen : Boolean;
    public-init var puzzleToAreaRatio : Number;
    public-init var onQuit : function() : Void;
    
    def playArea = Rectangle2D {
        minX: fullArea.minX;
        minY: fullArea.minY;
        width: fullArea.width;
        height: fullArea.height-50;
    };
    
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
    
    var imageIndex = 0;
    var puzzleImage : Image;
    var nextImage : Image;
    var screen : Stage;
    
    init {
        println("init");
    	puzzleImage = loadImage(images[imageIndex], false);
        nextIndex();
        nextImage = loadImage(images[imageIndex], true); 
        
        println("Initializing Stage");
        screen = Stage {
            title: "Toddler Puzzle";
            x : playArea.minX;
            y : playArea.minY;
            width : playArea.width;
            height : playArea.height;
            scene: createStartScene(puzzleImage);
            fullScreen : fullScreen;
        };
        println("Stage initialized");
    }
    
	function nextIndex() : Integer {
        if (imageIndex >= (sizeof images)-1) {
    	    imageIndex = 0;
    	} else {
    	    imageIndex++;
    	}
    };
    
    function loadImage(imageUrl : String, background: Boolean) : Image {
        return Image {
            url: imageUrl;
            preserveRatio: true;
            width : playArea.width * puzzleToAreaRatio;
            height : playArea.height * puzzleToAreaRatio;
            backgroundLoading : background;
        }
    };
    
    function showNextImage() : Image {
        nextIndex();
    	puzzleImage = nextImage;
    	nextImage = loadImage(images[imageIndex], true);
    	return puzzleImage;
    }
    
    
    function quit() : Void {
		screen.close();
        screen = null;
    	if (onQuit != null) {
    	    onQuit();
    	}
    };
    
    function playAgain() : Void {
    	screen.scene = createStartScene(showNextImage());
    };
    
    function start() : Void {
        var puzzle = newPuzzle(puzzleImage);
    	screen.scene = createPlayScene(puzzle);
    	puzzle.shuffle();   
    };
    
    function complete() : Void {
        screen.scene = createDoneScene(puzzleImage);
    };
    
    function newPuzzle(puzzleImage : Image) : Puzzle {
    	return Puzzle {
    		rows: rows;
    	    columns: columns;
            image: puzzleImage;
            playArea : playArea;
    	    onDone: complete;    
    	}    
    };
    
    
    function createPlayScene(puzzle: Puzzle) : Scene {
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
    	};
    	
    	buttonsDuringPlay.translateX = (fullArea.width-buttonsDuringPlay.width)/2;
    	buttonsDuringPlay.translateY = fullArea.maxY-buttonsDuringPlay.height;
        
        return Scene {
            content: [
            	puzzle,
            	buttonsDuringPlay
            ]
        }
    };
    
    function createDoneScene(puzzleImage : Image) : Scene { 
    	var buttonsDuringComplete = HBox {
    	    spacing: 10;
    	    content: [
    	    	Button {
    	    	     action: playAgain;
    	    	    text: "Play Again"; 
    	    	},
    	    	Button {
    	    	    action: quit;
    	    	    text: "Quit";
    	    	}
    	    ];
    	};
    	
    	buttonsDuringComplete.translateX = (fullArea.width-buttonsDuringComplete.width)/2;
    	buttonsDuringComplete.translateY = fullArea.maxY-buttonsDuringComplete.height;
    	
    	return Scene {
        	content: [
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
    };
    
    function createStartScene(puzzleImage: Image) : Scene {
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
    	};
    
    	buttonsDuringStart.translateX = (fullArea.width-buttonsDuringStart.width)/2;
    	buttonsDuringStart.translateY = fullArea.maxY-buttonsDuringStart.height;
    	return Scene {
    	    content: [
    	    	ImageView {
    	    	    image : puzzleImage;
    	    	    x : (playArea.width-puzzleImage.width)/2;
    	    	    y : (playArea.height-puzzleImage.height)/2;
    	    	    onMousePressed : function(event) {
    					playAgain();
    	    	    };
    	    	},
    	    	buttonsDuringStart
    	    ]
    	}    
    };
}

function run() {
	println("Defining puzzle");
	def puzzle = ToddlerPuzzle {
	    fullArea : Screen.primary.visualBounds;
	    fullScreen : false;
	    puzzleToAreaRatio : 0.6;
	    onQuit: function() {
	        println("onQuit");
	        // FX.exit();
	    };
	}
	println("Puzzle defined");
}

