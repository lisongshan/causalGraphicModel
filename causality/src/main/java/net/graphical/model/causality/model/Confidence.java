package net.graphical.model.causality.model;

/**
 * Created by sli on 2/2/16.
 */
public enum Confidence {
    ONE_HUNDRED_PERCENT() {
        @Override
        public boolean isTrue(double prob) {
            return prob > 0.999;
        }
    },

    NINETY_PERCENT() {
        @Override
        public boolean isTrue(double prob) {
            return prob > 0.899;
        }
    },

    EIGHTY_PERCENT() {
        @Override
        public boolean isTrue(double prob) {
            return prob > 0.799;
        }
    },
    SEVENTY_PERCENT() {
        @Override
        public boolean isTrue(double prob) {
            return prob > 0.699;
        }
    },
    SIXTY_PERCENT(){
        @Override
        public boolean isTrue(double prob) {
            return prob > 0.599;
        }
    },
    FIFTY_PERCENT() {
        @Override
        public boolean isTrue(double prob) {
            return prob > 0.499;
        }
    },
    FORTY_PERCENT(){
        @Override
        public boolean isTrue(double prob) {
            return prob > 0.399;
        }
    };

    Confidence() {
    }


    public abstract boolean isTrue(double prob);
}
