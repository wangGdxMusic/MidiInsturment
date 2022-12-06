package com.kw.gdx.view.dialog;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.kw.gdx.view.dialog.base.BaseDialog;
import com.kw.gdx.utils.Layer;

public class DialogManager {
    private Stage stage;
    private Image shadow;
    private Array<BaseDialog> array = new Array<>();

    public DialogManager(Stage stage) {
        this.stage = stage;
    }

    public enum Type{
        closeOldShowCurr,
        hideOldShowCurr,
        NotHideShowCurr
    }

    public void showShadow(float time){
        if (hasShadow)return;
        hasShadow = true;
        shadow = Layer.getShadow();
        shadow.setColor(0,0,0,0.0F);
        shadow.addAction(Actions.alpha(0.25F,time));
        stage.addActor(shadow);
        stage.getRoot().findActor("stg");
        shadow.setName("shadow");
    }

    public void showShadow(boolean isUp,float time){
        if (hasShadow)return;
        hasShadow = true;
        shadow = Layer.getShadow();
        shadow.setColor(0,0,0,0.0F);
        shadow.addAction(Actions.alpha(0.75F,time));
        stage.addActor(shadow);
        Actor stg = stage.getRoot().findActor("stg");
        if (isUp) {
            if (stg != null) {
            stg.toFront();
            }
        }
        shadow.setName("shadow");
    }

    public void showDialog(BaseDialog dialog,float delay){
        stage.addAction(Actions.delay(delay,Actions.run(()->{
            showDialog(dialog);
        })));
    }

    public void showDialog(BaseDialog dialog,float delay,boolean ff){
        if (dialog.isShadow()) {
            showShadow(ff,0.01667F);
        }
        stage.addAction(Actions.delay(delay,Actions.run(()->{
            showDialog(dialog);
        })));
    }

    private boolean hasShadow = false;

    public void showDialog(BaseDialog dialog,boolean isShaUp) {
        if (dialog.isShadow()) {
            showShadow(isShaUp,dialog.getShadowTime());
        }
        showDialog(stage.getRoot(),dialog);
    }

    public void showDialog(BaseDialog dialog) {
        if (dialog.isShadow()) {
            showShadow(dialog.getShadowTime());
        }
        showDialog(stage.getRoot(),dialog);
    }

    public void showDialog(Group parent,BaseDialog dialog){
        if (array.size>0) {
            if (dialog.getType() == Type.closeOldShowCurr) {
                BaseDialog peek = array.pop();
                peek.close();
                parent.addActor(dialog);
                dialog.show();
                array.add(dialog);
            }else if (dialog.getType() == Type.hideOldShowCurr){
                BaseDialog peek = array.peek();
                peek.hide();
                parent.addActor(dialog);
                dialog.show();
                array.add(dialog);
            }else if (dialog.getType() == Type.NotHideShowCurr){
                parent.addActor(dialog);
                dialog.show();
                array.add(dialog);
            }
        }else {
            parent.addActor(dialog);
            array.add(dialog);
        }
    }


    public void closeDialog(BaseDialog dialog,boolean flag) {
        if (shadow!=null){
            shadow.toFront();
        }
        closeDialog(dialog);
    }
    public void closeDialog(BaseDialog dialog){
        int shadowCloseType = dialog.getShadowCloseType();
        dialog.close();
        array.removeValue(dialog,true);
        if (array.size<=0){
            Actor shadow = stage.getRoot().findActor("shadow");
            if (shadow!=null) {
                hasShadow = false;
                if (shadowCloseType==1){
                    shadow.addAction(Actions.sequence(
                            Actions.alpha(1,0.5F),
                            Actions.delay(0.05F),
                            Actions.alpha(0, 0.2F),
                            Actions.removeActor()));
                }else {
                    shadow.addAction(Actions.sequence(
                            Actions.delay(0.1F),
                            Actions.alpha(0, 0.1667F),
                            Actions.removeActor()));
                }
            }
        }else{
            BaseDialog peek = array.peek();
            peek.show();
        }
    }

    public BaseDialog getBack(){
        if (array.size>0) {
            BaseDialog pop = array.peek();
            return pop;
        }else {
            return null;
        }
    }

    public BaseDialog back() {
        if (array.size>0) {
            BaseDialog pop = array.pop();
            pop.other();
            closeDialog(pop);
            if (array.size>0){
                BaseDialog pop1 = array.peek();
                pop1.show();
            }
            return pop;
        }
        return null;
    }
}
