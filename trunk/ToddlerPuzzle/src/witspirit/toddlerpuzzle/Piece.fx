package witspirit.toddlerpuzzle;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Rectangle2D;
import javafx.scene.effect.DropShadow;

import java.lang.Math;

public class Piece {
    public-init var puzzle: Puzzle;
    public-init var image: Image;
    public-init var part: Rectangle2D;
    var x : Number;
    var y : Number;
    var selected = bind puzzle.selectedPiece == this;
    var clickedOffsetX : Number;
    var clickedOffsetY : Number ;
    
    public-read var view = ImageView {
 	    image: image;
 	    viewport : part;
 	    x : bind x;
 	    y : bind y;
 	    blocksMouse : true;
 	    
 	    effect: bind if(selected) {
 	    	DropShadow {
 	    	    radius: 20;
 	    	    offsetX: 10;
 	    	    offsetY: 10;
 	    	}  
 	    } else {
 	    	null;
 	    };
 	    
 	    onMouseClicked : function(event) {
 	        puzzle.pieceClicked(this);
 	        clickedOffsetX = event.sceneX-x;
 	        clickedOffsetY = event.sceneY-y;
 	    };
 	    
 	    onMouseMoved : function(event) {
 	      	if (selected) {
 	      	    x = event.sceneX - clickedOffsetX;
 	      	    y = event.sceneY - clickedOffsetY;
 	      	}  
 	    };
 	} 
 	
 	public function scatter(bounds: Rectangle2D) : Void {
 	    x = Math.random() * (bounds.width - part.width) + bounds.minX;
 	    y = Math.random() * (bounds.height - part.height) + bounds.minY;
 	}
}