package com.site21.bittermelon.content.roles;

import com.site21.bittermelon.content.character.Character;
import com.site21.bittermelon.content.personnel.registry.PersonnelEntry;
import com.site21.bittermelon.content.personnel.registry.PersonnelRegistry;
import net.minecraft.world.entity.player.Player;

public class FoundationRole extends Role {
    public final String department;
    public final String position;

    public FoundationRole(String name, int color, String quote, String description, String department, String position) {
        super(name, color, quote, description);
        this.department = department;
        this.position = position;
    }

    @Override
    public void onRoleAdded(Player player, Character character) {
        super.onRoleAdded(player, character);

        PersonnelEntry personnelEntry = new PersonnelEntry(player, character, department, position);
        PersonnelRegistry.get(player.level()).addEntry(personnelEntry);
    }
}
