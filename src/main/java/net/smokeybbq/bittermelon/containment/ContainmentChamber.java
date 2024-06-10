package net.smokeybbq.bittermelon.containment;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.character.CharacterManager;

public class ContainmentChamber {
    String name;
    int overallStat, securityStat, researchStat, caretakingStat, integrityStat, protocolStat;
    int patrollingSubStat, securitySystemsSubStat, rotationSubStat;
    int experimentSubStat, publicationSubStat;
    int healthSubStat, moodSubStat, nourishmentSubStat, recreationalSubStat;
    int structuralSubStat, sanitationSubStat, environmentalSubStat;
    int x, y, z, containmentSize;
    int patrolTimeThreshold = 100;

    public ContainmentChamber(int x, int y, int z, int containmentSize) {

    }

    public void addPoints(String stat, int points) {

    }

    private void updateStats() {
        securityStat = patrollingSubStat + securitySystemsSubStat + rotationSubStat;
        researchStat = experimentSubStat + publicationSubStat;
        caretakingStat = healthSubStat + moodSubStat + nourishmentSubStat + recreationalSubStat;
        integrityStat = structuralSubStat + sanitationSubStat + environmentalSubStat;

        overallStat = securityStat + researchStat + caretakingStat + integrityStat + protocolStat;
    }

    @SubscribeEvent
    private void handleSecurityPatrolling(TickEvent.PlayerTickEvent event) {
        int patrolTime = 0;

        if (event.player instanceof ServerPlayer) {
            ServerPlayer player = (ServerPlayer) event.player;
            Character character = CharacterManager.getActiveCharacter(player.getUUID());
            if (compareDistance(player) < containmentSize && character.getAge() > 5) {
                patrolTime++;
            }
        }

        if (patrolTime >= patrolTimeThreshold) {
            patrolTime = 0;
            patrollingSubStat += 1;
        }
    }

    public double compareDistance(ServerPlayer player) {
        double x = Math.abs(this.x - player.getX());
        double y = Math.abs(this.y - player.getY());
        double z = Math.abs(this.z - player.getZ());
        return x + y + z;
    }


}
