package kw.mulitplay.game.time;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kw.gdx.asset.Asset;

import kw.mulitplay.game.constant.Constant;
import kw.mulitplay.game.group.PianoView;
import kw.mulitplay.game.midi.handler.MidiInstruments;
import kw.mulitplay.game.midi.handler.Note;

public class ActorTimeLine {
    private float startTime;
    private Note note;
    private float endTime;
    private int status;
    private PianoView view;
    private Image image;
    private int up;

    public ActorTimeLine(Note note, PianoView view, int resolution,int up){
        this.up = up;
        this.note = note;
        this.startTime = note.getTimeStamp() * 60.0f / resolution / 120.0f*2;
        this.endTime = (note.getTimeStamp()+note.getLength())*60.0f / resolution / 120.0f*2;
        this.view = view;
        image = new Image(Asset.getAsset().getTexture("main/white.png"));
    }

    public Image getImage() {
        return image;
    }

    public void act(float timeline){
        if (status == 0) {
            if (startTime <= timeline && endTime >= timeline) {
                view.getHashMap().get((note.getKey())+"").touchDownKey();
                MidiInstruments.noteOn(note.getKey());
                status = 1;
//                image.setColor(Color.BLACK);
            }
        }else {
//            note.getLength())*60.0f / resolution / 120.0f*2
            if (endTime <= timeline) {
                view.getHashMap().get((note.getKey())+"").finishTouchi();
                MidiInstruments.noteOff(note.getKey());
                status = 2;
//                image.setColor(Color.WHITE);
            }
        }
    }

    public boolean isUp(){
        return up == 0;
    }

    public int getStatus() {
        return status;
    }

    public void dispose() {
        view = null;
        note = null;
    }

    public Note getNote() {
        return note;
    }

    @Override
    public String toString() {
        return "ActorTimeLine{" +
                "startTime=" + startTime +
                ", endTime=" + endTime +
                ", status=" + status +
                '}';
    }
}
