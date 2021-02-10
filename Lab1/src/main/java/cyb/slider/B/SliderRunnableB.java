package cyb.slider.B;

import javax.swing.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.UnaryOperator;

public class SliderRunnableB implements Runnable {
    private final JSlider slider;
    private final AtomicInteger semaphore;
    private final UnaryOperator<Integer> operator;

    public SliderRunnableB(JSlider slider, AtomicInteger semaphore, UnaryOperator<Integer> operator) {
        this.slider = slider;
        this.semaphore = semaphore;
        this.operator = operator;
    }

    @Override
    public void run() {
        if (!Thread.interrupted() && semaphore.compareAndSet(0, 1)) {
            slider.setValue(operator.apply(slider.getValue()));
        }
    }
}
