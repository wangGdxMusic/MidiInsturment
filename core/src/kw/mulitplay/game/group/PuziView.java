package kw.mulitplay.game.group;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.kw.gdx.asset.Asset;

import kw.mulitplay.game.constant.Constant;
import kw.mulitplay.game.midi.handler.Note;
import kw.mulitplay.game.time.ActorTimeLine;

public class PuziView extends Group {
    public PuziView(Array<ActorTimeLine> notes){
        Table panel = new Table(){{
            Group group = null;
            int index = 0;
            int ii = 0;
            for (ActorTimeLine note : notes) {
                if (index % 10 == 0) {
                    ii = 0;
                    group = new Group();
                    add(group);
                    row();
                }
                index++;
                ii ++;
                group.setSize(Constant.width,200);
                Image im = note.getImage();
                if (note.isUp()) {
                    im.setY(50);
                }else {
                    im.setY(-50);
                    im.setColor(Color.RED);
                }
                group.addActor(im);
                im.setX(ii*100);

            }
        }};
        ScrollPane pane = new ScrollPane(panel,new ScrollPane.ScrollPaneStyle());
//        addActor(pane);
        pane.setSize(Constant.width,Constant.height);
    }
}
