package kw.myqt.tweenengine.path;

import kw.myqt.tweenengine.TweenEquation;
import kw.myqt.tweenengine.TweenPath;

public abstract class Linear implements TweenPath {

    @Override
    public float compute ( float t, float[] points, int pointsCnt){
        int segment = (int) Math.floor((pointsCnt - 1) * t);
        segment = Math.max(segment, 0);
        segment = Math.min(segment, pointsCnt - 2);

        t = t * (pointsCnt - 1) - segment;

        return points[segment] + t * (points[segment + 1] - points[segment]);
    }
}