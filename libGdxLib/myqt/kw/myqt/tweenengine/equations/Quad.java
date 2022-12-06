package kw.myqt.tweenengine.equations;

import kw.myqt.tweenengine.TweenEquation;

public abstract class Quad extends TweenEquation {
    public static final Quad IN = new Quad() {
        @Override
        public final float compute(float t) {
            return t * t;
        }

        @Override
        public String toString() {
            return "Quad.IN";
        }
    };

    public static final Quad OUT = new Quad() {
        @Override
        public final float compute(float t) {
            return -t * (t - 2);
        }

        @Override
        public String toString() {
            return "Quad.OUT";
        }
    };

    public static final Quad INOUT = new Quad() {
        @Override
        public final float compute(float t) {
            if ((t *= 2) < 1) return 0.5f * t * t;
            return -0.5f * ((--t) * (t - 2) - 1);
        }

        @Override
        public String toString() {
            return "Quad.INOUT";
        }
    };
}