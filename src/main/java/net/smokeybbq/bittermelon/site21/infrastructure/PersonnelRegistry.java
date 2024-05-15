package net.smokeybbq.bittermelon.site21.infrastructure;

import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.character.CharacterManager;

import java.util.Map;
import java.util.UUID;

public class PersonnelRegistry {


    private PersonnelRegistry() {

    }
    Map<UUID, Character> CharacterMap = CharacterManager.getInstance().getCharacterMap();


}
