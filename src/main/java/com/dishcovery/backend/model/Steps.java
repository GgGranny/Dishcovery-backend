package com.dishcovery.backend.model;


import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Steps {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long stepsId;

    @ElementCollection
    private List<String>  steps;

    public Steps() {
    }

    public Steps(List<String> steps) {
        this.steps = steps;
    }

    public long getStepsId() {
        return stepsId;
    }

    public void setStepsId(long stepsId) {
        this.stepsId = stepsId;
    }

    public List<String> getSteps() {
        return steps;
    }

    public void setSteps(List<String> steps) {
        this.steps = steps;
    }

    @Override
    public String toString() {
        return "Steps{" +
                "stepsId=" + stepsId +
                ", steps=" + steps +
                '}';
    }
}
