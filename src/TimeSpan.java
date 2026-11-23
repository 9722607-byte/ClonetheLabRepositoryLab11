import exceptions.IllegalInputTimeSpanException;
import exceptions.IllegalScaleFactorException;
import exceptions.NegativeTimeSpanException;

import java.util.Objects;

public class TimeSpan {

    private int hours;
    private int minutes;

    public TimeSpan() {
        this.hours = 0;
        this.minutes = 0;
    }

    public TimeSpan(int minutes) {
        if (minutes < 0) {
            throw new IllegalInputTimeSpanException("Кількість хвилин не може бути від'ємною.");
        }
        this.hours = minutes / 60;
        this.minutes = minutes % 60;
    }

    public TimeSpan(int hours, int minutes) {
        if (hours < 0 || minutes < 0 || minutes > 59) {
            throw new IllegalInputTimeSpanException(
                    "Некоректні вхідні значення: години >= 0, хвилини в межах 0..59."
            );
        }
        this.hours = hours;
        this.minutes = minutes;
    }

    public TimeSpan(TimeSpan other) {
        Objects.requireNonNull(other, "Інтервал часу не може бути null.");
        this.hours = other.hours;
        this.minutes = other.minutes;
    }

    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

    private int toTotalMinutes() {
        return hours * 60 + minutes;
    }

    private void setFromTotalMinutes(int totalMinutes) {
        if (totalMinutes < 0) {
            throw new NegativeTimeSpanException("Інтервал часу не може бути від'ємним.");
        }
        this.hours = totalMinutes / 60;
        this.minutes = totalMinutes % 60;
    }

    public void add(int hours, int minutes) {
        if (hours < 0 || minutes < 0 || minutes > 59) {
            throw new IllegalInputTimeSpanException(
                    "Некоректні вхідні значення: години >= 0, хвилини в межах 0..59."
            );
        }
        setFromTotalMinutes(toTotalMinutes() + hours * 60 + minutes);
    }

    public void add(TimeSpan span) {
        Objects.requireNonNull(span, "Інтервал часу не може бути null.");
        setFromTotalMinutes(toTotalMinutes() + span.toTotalMinutes());
    }

    public void subtract(TimeSpan span) {
        Objects.requireNonNull(span, "Інтервал часу не може бути null.");
        int diff = toTotalMinutes() - span.toTotalMinutes();
        if (diff < 0) {
            throw new NegativeTimeSpanException("Результат віднімання не може бути від'ємним.");
        }
        setFromTotalMinutes(diff);
    }

    public void scale(int factor) {
        if (factor <= 0) {
            throw new IllegalScaleFactorException("Множник масштабування повинен бути більшим за 0.");
        }
        setFromTotalMinutes(toTotalMinutes() * factor);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TimeSpan timeSpan)) return false;
        return hours == timeSpan.hours && minutes == timeSpan.minutes;
    }

    @Override
    public int hashCode() {
        return Objects.hash(hours, minutes);
    }

    @Override
    public String toString() {
        return hours + "h " + minutes + "m";
    }
}