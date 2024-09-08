package net.smokeybbq.bittermelon.systems.medical.compartments;

public class InjuryCompartment extends Compartment {
    private InjuryType injuryType;
    public InjuryCompartment(String name, float permeability, float volume, float healFactor, InjuryType injuryType, boolean bleeding) {
        super(name, permeability, volume, healFactor);
        this.injuryType = injuryType;
        this.bleeding = bleeding;

        pain = Math.min(100, volume);
        health = 0;
    }

    @Override
    public void update() {
        if (injuryType != InjuryType.SCAR) {
            modifyHealth(healFactor / volume);
            modifyPain(100 - health);
            if (health >= 100) {
                injuryType = InjuryType.SCAR;
            }
        }
    }

    public InjuryType getInjuryType() {return injuryType;}
}
