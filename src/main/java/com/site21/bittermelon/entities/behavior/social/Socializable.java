package com.site21.bittermelon.entities.behavior.social;

import com.site21.bittermelon.character.Character;
import com.site21.bittermelon.character.Species;

import java.util.Map;

public interface Socializable {
    Map<Character, Relationship> getRelationships();

    default Relationship getRelationship(Character character) {
        if (character == null) {
            return null;
        }

        return getRelationships().computeIfAbsent(character, k -> new Relationship());
    }

    default void clearRelationship(Character character) {
        getRelationships().remove(character);
    }

    default void addRelationship(Character character, Relationship relationship) {
        getRelationships().put(character, relationship);
    }

    void modifySocialization(float amount);
    void setSocialization(float amount);
}
