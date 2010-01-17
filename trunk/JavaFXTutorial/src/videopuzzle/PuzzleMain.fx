/* 
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER
 * Copyright 2009 Sun Microsystems, Inc. All rights reserved. Use is subject to license terms. 
 * 
 * This file is available and licensed under the following license:
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 *
 *   * Redistributions of source code must retain the above copyright notice, 
 *     this list of conditions and the following disclaimer.
 *
 *   * Redistributions in binary form must reproduce the above copyright notice,
 *     this list of conditions and the following disclaimer in the documentation
 *     and/or other materials provided with the distribution.
 *
 *   * Neither the name of Sun Microsystems nor the names of its contributors 
 *     may be used to endorse or promote products derived from this software 
 *     without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package videopuzzle;

import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.shape.*;
import javafx.scene.media.*;
import javafx.scene.text.*;
import javafx.geometry.*;
import javafx.scene.transform.*;
import javafx.stage.*;
import javafx.lang.FX;

var uri = "http://sun.edgeboss.net/download/sun/media/1460825906/1460825906_2957282001_elephantsdream-640x360.flv";
if (FX.getArgument("mediaURL") != null) {
    uri = FX.getArgument("mediaURL").toString();
}
var player = MediaPlayer {
    media: Media { source: uri }
    autoPlay: true
    repeatCount: MediaPlayer.REPEAT_FOREVER
    volume: 0.5
};

var ui = VideoPuzzleUI { };

var main = Main { };
main.puz = Puzzle {
    width: 512
    height: 288
    scatterBounds: Rectangle2D {
        minX: -100
        minY: -30
        width: 512+200
        height: 288+60
    }
    video: player
    dragTargetImage: (ui.target_drag as ImageView).image
    ui: ui
};

var phase1 = ui.getGroup("phase1");
phase1.visible = true;
var phase2 = ui.getNode("phase2");
ui.phase2.visible = false;
var puzzlePieces = Group {
    translateX: (700-main.puz.width)/2
    translateY: (430-main.puz.height)/2-16
    content: bind [
        Rectangle { 
            width: main.puz.width
            height: main.puz.height
            fill: null
            stroke: Color.BLACK
            strokeWidth: 2
        },
        main.puz.pieces
    ]
};
puzzlePieces.visible = false;


// the preview used in phase 1
var phase1Preview = MediaView {
    transforms: [Transform.translate(18,50), Transform.scale(0.8,0.8)]
    mediaPlayer: player
};

// the phase 1 start button
ui.Start_Button.onMouseClicked = function(e:MouseEvent):Void {
    main.puz.scatter();
    main.startGame();
    ui.phase1.visible = false;
    phase1Preview.visible = false;
    ui.phase2.visible = true;
    puzzlePieces.visible = true;
}

// the difficulty slider
var slider_startx = ui.slider.translateX;
ui.slider_bkg.onMouseDragged = function(e:MouseEvent):Void {
    ui.slider.translateX = slider_startx + e.dragX;
    if(ui.slider.translateX < ui.slider_bkg.translateX) {
        ui.slider.translateX = ui.slider_bkg.translateX;
    }
    if(ui.slider.translateX > ui.slider_bkg.translateX + 110) {
        ui.slider.translateX = ui.slider_bkg.translateX + 110;
    }
    main.difficulty = 2 + ((ui.slider.translateX - ui.slider_bkg.translateX)/30) as Integer;
}

var difficulty_text = Text { 
    x: 549
    y: 315
    fill: Color.BLACK
    font: Font { size: 10 }
    content: bind "{main.difficulty}x{main.difficulty} - {main.difficulty*main.difficulty} pieces selected"
};
insert difficulty_text into (phase1 as Group).content;

// the volume control
ui.volume.visible = true;
ui.volume_rollover.visible = true;
RolloverBehavior { target: ui.volume_rollover };
ui.volume_rollover.onMouseClicked = function(e:MouseEvent):Void {
    ui.volume.visible = false;
    ui.volume_rollover.visible = false;
    ui.volume_stop.visible = true;
    ui.volume_stop_rollover.visible = true;
    player.volume = 0.0;
};
ui.volume_stop.visible = false;
ui.volume_stop_rollover.visible = false;
RolloverBehavior { target: ui.volume_stop_rollover };
ui.volume_stop_rollover.onMouseClicked = function(e:MouseEvent):Void {
    ui.volume.visible = true;
    ui.volume_rollover.visible = true;
    ui.volume_stop.visible = false;
    ui.volume_stop_rollover.visible = false;
    player.volume = 1.0;
};

// the play / pause control
RolloverBehavior{ target: ui.play_rollover };
ui.play_rollover.onMouseClicked = function(e:MouseEvent):Void {
    ui.play.visible = false;
    ui.play_rollover.visible = false;
    ui.pause.visible = true;
    ui.pause_rollover.visible = true;
    player.play();
};

RolloverBehavior { target: ui.pause_rollover };
ui.pause_rollover.onMouseClicked = function(e:MouseEvent):Void {
    ui.play.visible = true;
    ui.play_rollover.visible = true;
    ui.pause.visible = false;
    ui.pause_rollover.visible = false;
    player.pause();
};
ui.pause.translateX -= 25;
ui.pause_rollover.translateX -= 25;
ui.pause.visible = true;
ui.pause_rollover.visible = true;
ui.play.visible = false;
ui.play_rollover.visible = false;
ui.load.visible = false;
ui.load_rollover.visible = false;


ui.framework.visible = true;


var previewSpotX = 12; var previewSpotY = 50;
var overlaySpotX = 95; var overlaySpotY = 55;
var previewDimOverlay = Group {
    visible: false
    content:[
        Rectangle { 
            x: 7 y: 25 width: 685 height: 332 arcWidth: 10 arcHeight: 10
            fill: Color.rgb(0,0,0,0.4)  
            blocksMouse: true
        }
    ]
}
var previewGridOverlay = Group {
    translateX: overlaySpotX
    translateY: overlaySpotY
    visible: false
    content: bind for(i in [0..main.puz.pieceRows-1],j in [0..main.puz.pieceCols-1]) {
        Rectangle {
            x: i*main.puz.pieceWidth
            y: j*main.puz.pieceHeight
            width: main.puz.pieceWidth
            height: main.puz.pieceHeight
            fill: null
            stroke: Color.WHITE
        }
    }
};

// the phase 2 buttons
ui.hint_button_active.visible = true;
RolloverBehavior { target: ui.hint_button_active };
ui.hint_button_active.onMouseClicked = function(e:MouseEvent):Void {
    main.puz.toggleHint();
};

ui.shuffle_button_active.visible = true;
RolloverBehavior { target: ui.shuffle_button_active };
ui.shuffle_button_active.onMouseClicked = function(e:MouseEvent):Void {
    main.puz.scatter();
};
ui.preview_button_active.visible = true;
RolloverBehavior { target: ui.preview_button_active };
ui.preview_button_active.onMouseClicked = function(e:MouseEvent):Void {
    phase1Preview.transforms = [Transform.translate(overlaySpotX,overlaySpotY), Transform.scale(0.8,0.8)];
    phase1Preview.visible = not phase1Preview.visible;
    previewDimOverlay.visible = phase1Preview.visible;
    previewGridOverlay.visible = phase1Preview.visible;
}
ui.stop_button_active.visible = true;
RolloverBehavior { target: ui.stop_button_active };
ui.stop_button_active.onMouseClicked = function(e:MouseEvent):Void {
    ui.phase1.visible = true;
    phase1Preview.visible = true;
    ui.phase2.visible = false;
    puzzlePieces.visible = false;
    phase1Preview.transforms = [Transform.translate(previewSpotX,previewSpotY), Transform.scale(0.8,0.8)];
    previewDimOverlay.visible = false;
    previewGridOverlay.visible = false;
}


// drag and close controls

var dragArea = Rectangle { x: 0 y: 0 width: 700 height: 30 fill: Color.rgb(0,0,0,0.0) };
var isApplet = (FX.getArgument("isApplet")!=null);

ui.drag.visible = false;
ui.drag_text.visible = isApplet and AppletStageExtension.appletDragSupported;
var outOfBrowser:Boolean = false on replace {
    ui.drag.visible = outOfBrowser;
    ui.drag_text.visible = not outOfBrowser and AppletStageExtension.appletDragSupported;
}

ui.close_94.visible = not isApplet;
ui.close_94.onMousePressed = function(e:MouseEvent) {
    stage.close();
}
ui.window_icons.visible = true;

phase1.blocksMouse = true;
phase2.blocksMouse = true;

var stage:Stage = Stage {
    visible: true
    title: "Video Puzzle"
    style: StageStyle.UNDECORATED
    scene: Scene {
        fill: null
        content: [
            dragArea,
            ui,
            puzzlePieces,
            previewDimOverlay,
            phase1Preview,
            previewGridOverlay,
        ]
    }
    extensions: [
        AppletStageExtension {
            shouldDragStart: function(e: MouseEvent): Boolean {
                return e.primaryButtonDown and not outOfBrowser and dragArea.hover;
            }
            
            onDragStarted: function(): Void {
                outOfBrowser = true;
            }
            onAppletRestored: function(): Void {
                outOfBrowser = false;
            }
            
            useDefaultClose: false
        }
    ]}

var stageDragInitialX:Number;
var stageDragInitialY:Number;

ui.framework.onMousePressed = function(e:MouseEvent) {
   stageDragInitialX = e.screenX - stage.x;
   stageDragInitialY = e.screenY - stage.y;
}

ui.framework.onMouseDragged = function(e:MouseEvent) {
   stage.x = e.screenX - stageDragInitialX;
   stage.y = e.screenY - stageDragInitialY;
};
