package com.example.julia.read_a_lot;

import java.io.Serializable;

/**
 * Created by Julia on 14/06/2017.
 */

public class Answer implements Serializable {
    private String answer;
    private int goodOrFault;

    public Answer(String answerInfo, int goodOrFaultInfo){
        answer = answerInfo;
        goodOrFault = goodOrFaultInfo;
    }

    public String getAnswer() {
        return answer;
    }

    public int getGoodOrFault() {
        return goodOrFault;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setGoodOrFault(int goodOrFault) {
        this.goodOrFault = goodOrFault;
    }
}
