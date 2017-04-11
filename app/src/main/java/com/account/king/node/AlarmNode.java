package com.account.king.node;

/**
 * @author king
 */
public class AlarmNode {

    //repeat_mode : 0-7  0：每天  1周一 以此类推

    private boolean isRemind;

    private int hour;
    private int minute;
    private int repeat_mode;

    public AlarmNode() {
        isRemind = false;
        hour = 19;
        minute = 0;
        repeat_mode = 0;
    }

    public boolean isRemind() {
        return isRemind;
    }

    public void setRemind(boolean remind) {
        isRemind = remind;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getRepeat_mode() {
        return repeat_mode;
    }

    public void setRepeat_mode(int repeat_mode) {
        this.repeat_mode = repeat_mode;
    }

    public String getTime() {
        String hours = hour < 10 ? "0" + hour : hour + "";
        String minutes = minute < 10 ? "0" + minute : minute + "";
        return hours + ":" + minutes;
    }


    public AlarmNode copy() {
        AlarmNode node = new AlarmNode();
        node.setRemind(this.isRemind());
        node.setHour(this.hour);
        node.setMinute(this.minute);
        node.setRepeat_mode(this.repeat_mode);
        return node;
    }

    public boolean beCompare(AlarmNode node) {
        if ((this.isRemind() && !node.isRemind()) || (!this.isRemind() && node.isRemind())) {
            return false;
        }
        if (this.hour != node.hour) {
            return false;
        }
        if (this.minute != node.minute) {
            return false;
        }
        if (this.repeat_mode != node.repeat_mode) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "AlarmNode{" +
                "isRemind=" + isRemind +
                ", hour=" + hour +
                ", minute=" + minute +
                ", repeat_mode=" + repeat_mode +
                '}';
    }
}
