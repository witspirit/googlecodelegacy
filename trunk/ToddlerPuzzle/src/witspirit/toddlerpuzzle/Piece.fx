package witspirit.toddlerpuzzle;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Rectangle2D;
import javafx.scene.effect.DropShadow;
import javafx.scene.Group;
import javafx.scene.text.Text;
import javafx.scene.text.TextOrigin;
import javafx.scene.Node;
import javafx.geometry.Bounds;
import javafx.scene.CustomNode;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.effect.Glow;
import javafx.scene.effect.Lighting;
import javafx.scene.effect.light.DistantLight;

public class Piece extends CustomNode {
    public-init var puzzle: Puzzle;
    public-init var image: Image;
    public-init var width : Number;
    public-init var height : Number;
    public-init var row : Integer;
    public-init var column : Integer;
    public var x : Number;
    public var y : Number;
        
    def nearDistance = 30;
    public-read def dropZone: Rectangle2D = Rectangle2D {
    	minX : puzzle.frameX + column*width;
    	minY : puzzle.frameY + row*height;
    	width : width;
    	height : height;
    };
    def imageSection : Rectangle2D = Rectangle2D {
        minX : column*width;
        minY : row*height;
        width : width;
        height : height;
    }
    
    public-read var selected = bind puzzle.selectedPiece == this;
    public-read var isNearDropZone : Boolean = bind nearDropZone(x, y);
    public var isPlaced : Boolean = false;
    
    override function create() : Node {
        Group {
            var viewport : ImageView;
        	content: [
        		Rectangle {
        		    x : dropZone.minX;
        		    y : dropZone.minY;
        		    width : dropZone.width;
        		    height: dropZone.height;
        		    stroke : Color.ORANGE;
        		    strokeWidth : 2.0;
        		    fill : Color.YELLOW;
        		    visible : bind not isPlaced and isNearDropZone;
        		    effect: Glow {
        		    	level: 0.8;
        		    };
        		},
        		viewport = ImageView {
    		 	    image: image;
    		 	    viewport : imageSection;
    		 	    translateX : bind x;
    		 	    translateY : bind y;
    		 	    blocksMouse : bind not isPlaced;
    		 	    
    		 	    effect: bind if(selected) {
    		 	            DropShadow {
 	    	    				radius: 20;
	    		 	    	    offsetX: 10;
	    		 	    	    offsetY: 10;
	    		 	    	};
	    		 	    } else {
	    		 	        null;
	    		 	    };
    		 	    
    		 	    onMousePressed : function(event) {
    		 	        puzzle.piecePressed(this);
    		 	    };
    		 	    
    		 	    onMouseMoved : function(event) {
    		 	      	if (selected) {
    		 	      	    x = event.sceneX - width/2;
    		 	      	    y = event.sceneY - height/2;
    		 	      	}  
    		 	    };
    		 	    
    		 	    onMouseDragged : function(event) {
    		 	        if (selected) {
    		 	            x = event.sceneX - width/2;
    		 	            y = event.sceneY - height/2;
    		 	        }
    		 	    }
    		 	},
        	]
        } 
    }
    
    function nearDropZone(x : Number, y : Number) : Boolean {
        var xDistance : Number = x - dropZone.minX;
        if (xDistance < 0) {
            xDistance = -xDistance;
        }
        var yDistance : Number = y - dropZone.minY;
        if (yDistance < 0) {
            yDistance = -yDistance;
        }
        return xDistance < nearDistance and yDistance < nearDistance;
    }
}
