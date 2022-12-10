package kw.mulitplay.game.screen;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.Comparator;

import javax.sound.midi.Instrument;
import javax.sound.midi.MidiUnavailableException;

import kw.mulitplay.game.constant.Constant;
import kw.mulitplay.game.constant.LevelConfig;
import kw.mulitplay.game.group.InsturmentItem;
import kw.mulitplay.game.group.PianoView;
import kw.mulitplay.game.group.PuziView;
import kw.mulitplay.game.midi.gamemode.ModeController;
import kw.mulitplay.game.midi.handler.Channel;
import kw.mulitplay.game.midi.handler.MidiInstruments;
import kw.mulitplay.game.midi.handler.MidiUtils;
import kw.mulitplay.game.midi.handler.Note;
import kw.mulitplay.game.midi.handler.Sheet;
import kw.mulitplay.game.screen.base.BaseScreen;
import kw.mulitplay.game.time.ActorTimeLine;

public class DIMIDemoScreen extends BaseScreen {
    private Array<Channel> channelArray;
    private PianoView view ;
    private int resolution;
    private float timer = 0;
    private Array<ActorTimeLine> disposeNode;
    private Array<ActorTimeLine> actorTimeLines;

    public DIMIDemoScreen(){
        channelArray = new Array<>();
        disposeNode = new Array<>();
        actorTimeLines = new Array<>();
    }

    @Override
    protected void initView() {
        view = new PianoView();
        view.setMode(0);
        view.showPianoKey();
        stage.addActor(view);
        try {
            Instrument[] instruments = MidiInstruments.getInstruments();
            ScrollPane pane = new ScrollPane(new Table(){{
                    for (Instrument instrument : instruments) {
                        add(new InsturmentItem(instrument));
                        row();
                    }
            }},new ScrollPane.ScrollPaneStyle());
            stage.addActor(pane);
            pane.setSize(Constant.width,Constant.height);

            if (Constant.instrument!=null) {
                MidiInstruments.selectInstrument(Constant.instrument);
            }
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        }
//        FileHandle source = new FileHandle("midi/wind street.mid");
        FileHandle source = LevelConfig.fileHandle;
        try {
            Sheet sheet = MidiUtils.getSheet(source.file());
            Channel[] channels = sheet.getChannels();
            resolution = sheet.getResolution();
            for (Channel channel : channels) {
                channelArray.add(channel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (Channel channel : channelArray) {
            for (Note note : channel.getNotes()) {
                actorTimeLines.add(new ActorTimeLine(note,view,resolution,note.getNum()));
            }
        }
        actorTimeLines.sort(new Comparator<ActorTimeLine>() {
            @Override
            public int compare(ActorTimeLine o1, ActorTimeLine o2) {
                return (int) (o1.getStartTime() - o2.getStartTime());
            }
        });

        Array<Note> array = new Array<>();
        for (Channel channel : channelArray) {
            for (Note note : channel.getNotes()) {
                array.add(note);
            }
        }
        PuziView view = new PuziView(actorTimeLines);
        stage.addActor(view);
    }


    /**
     * 节点时间：
     *ka
     */
//    private static final long TEMPO = 120;
//    private static double computeTick(long tick, Sheet sheet) {
//        return tick * 60000.0 / sheet.getResolution() / TEMPO;
//    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void back() {
        enterScreen(new SongListScreen());
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        timer +=delta;
        for (ActorTimeLine actorTimeLine : actorTimeLines) {
            if (actorTimeLine.getStatus()==2){
                disposeNode.add(actorTimeLine);
            }
            actorTimeLine.act(timer);
        }
        for (ActorTimeLine actorTimeLine : disposeNode) {
            actorTimeLines.removeValue(actorTimeLine,false);
            actorTimeLine.dispose();
        }
        disposeNode.clear();
    }
}
