package com.site21.bittermelon.common.systems.ai.behavior.social;

import java.util.List;

public class Relationship {
    private float opinion = 0;
    private List<Relation> relations;

    public Relationship() {
    }

    public float getOpinion() {
        float finalOpinion = opinion;

        for (Relation relation : relations) {
            finalOpinion += relation.getModifier();
        }

        return finalOpinion;
    }

    public List<Relation> getRelations() {
        return relations;
    }

    public void setOpinion(float opinion) {
        this.opinion = opinion;
    }

    public void modifyOpinion(float amount) {
        opinion = Math.min(100, Math.max(-100, opinion + amount));
    }
}
