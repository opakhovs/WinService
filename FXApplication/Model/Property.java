package Model;

import java.io.Serializable;

public class Property implements Serializable {

    public Property(){}

    public Property(String option, int value){
        setOption(option);
        setValue(value);
    }

    private String option;
    private int value;

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
