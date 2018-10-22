package Model;

import java.io.Serializable;

public class Program implements Serializable {

    public Program(){}

    public Program(String name, int work_time, int rest_time, int is_checked) {
        this.name = name;
        this.work_time = work_time;
        this.rest_time = rest_time;
        setIs_checked(is_checked);
        isNotOpened = 0;
    }

    private String name;
    private int work_time;
    private int rest_time;
    private boolean is_checked;
    private int isNotOpened;

    public void setIs_checked(boolean is_checked) {
        this.is_checked = is_checked;
    }

    public int getIsNotOpened() {
        return isNotOpened;
    }

    public void setIsNotOpened(int isNotOpened) {
        this.isNotOpened = isNotOpened;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWork_time() {
        return work_time;
    }

    public void setWork_time(int work_time) {
        this.work_time = work_time;
    }

    public int getRest_time() {
        return rest_time;
    }

    public void setRest_time(int rest_time) {
        this.rest_time = rest_time;
    }

    public void setIs_checked(int is_checked) {
        this.is_checked = true;
        if (is_checked == 0)
            this.is_checked = false;
    }

    public int getIs_checked(){
        if (is_checked)
            return 1;
        return 0;
    }
}
