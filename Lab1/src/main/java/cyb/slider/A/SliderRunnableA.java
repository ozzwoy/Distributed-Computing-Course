package cyb.slider.A;

import javax.swing.*;
import java.util.function.UnaryOperator;

public class SliderRunnableA implements Runnable {
    private final JSlider slider;
    private final UnaryOperator<Integer> operator;

    public SliderRunnableA(JSlider slider, UnaryOperator<Integer> operator) {
        this.slider = slider;
        this.operator = operator;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            synchronized (slider) {
                slider.setValue(operator.apply(slider.getValue()));
            }
        }
    }
}
