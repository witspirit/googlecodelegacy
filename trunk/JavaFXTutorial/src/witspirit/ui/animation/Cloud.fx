package witspirit.ui.animation;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Path;
import javafx.scene.shape.ArcTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.animation.Interpolator;
import javafx.scene.effect.Lighting;
import javafx.scene.effect.light.DistantLight;

def background = ImageView {
    image: Image {
        url: "{__DIR__}sun.jpg";
    }
}

var x : Number;
var y : Number;

def cloud = Path {
    translateX: bind x;
    translateY: bind y;
    stroke: Color.DODGERBLUE;
    fill: LinearGradient {
        startX: 60;
        startY: 10;
        endX: 10;
        endY: 80;
        proportional: false;
        stops: [
        	Stop {
        	    offset: 0.0;
        	    color: Color.DODGERBLUE;
        	},
        	Stop {
        	    offset: 0.5;
        	    color: Color.LIGHTSKYBLUE;
        	},
        	Stop {
        	    offset: 1.0;
        	    color: Color.WHITE;
        	}
        ];
    };
    elements: [
    	MoveTo {x: 15; y: 15; },
    	ArcTo {x: 50; y: 10; radiusX: 20; radiusY: 20; sweepFlag: true; },
    	ArcTo {x: 70; y: 20; radiusX: 20; radiusY: 20; sweepFlag: true; },
    	ArcTo {x: 50; y: 60; radiusX: 20; radiusY: 20; sweepFlag: true; },
    	ArcTo {x: 20; y: 50; radiusX: 10; radiusY:  5; sweepFlag: true; },
    	ArcTo {x: 15; y: 15; radiusX: 10; radiusY: 10; sweepFlag: true; },
    ];
    effect: Lighting {
        light: DistantLight {
            azimuth : 90;
        }
    }
}


def horizontalTimeline = Timeline {
    repeatCount: Timeline.INDEFINITE;
    autoReverse: true;
    keyFrames : [
    	// KeyFrame {
    	//    time: 0s;
    	//    values: x => 0.0;
    	//},
    	//KeyFrame {
    	//    time: 4s;
    	//    values: x => 158.0 tween Interpolator.LINEAR;
    	//}
    	// Or the short form...
    	at (0s) { x=> 0.0 },
    	at (4s) { x=> 158.0 tween Interpolator.LINEAR }
    ];
};
horizontalTimeline.play();

def verticalTimeline = Timeline {
    repeatCount: Timeline.INDEFINITE;
    autoReverse: true;
    keyFrames : [
    	at(0s) {y => 0.0},
    	at(7s) {y => 258.0 tween Interpolator.EASEBOTH}
    ]
};
verticalTimeline.play();

Stage {
    title: "Cloud";
    scene: Scene {
        fill: Color.WHITE;
        content: [
        	background,
        	cloud
        ];
    };
}