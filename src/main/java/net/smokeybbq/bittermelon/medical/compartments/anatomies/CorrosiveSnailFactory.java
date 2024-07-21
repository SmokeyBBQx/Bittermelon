package net.smokeybbq.bittermelon.medical.compartments.anatomies;

import net.smokeybbq.bittermelon.medical.compartments.Compartment;
import net.smokeybbq.bittermelon.medical.compartments.GroupCompartment;
import net.smokeybbq.bittermelon.medical.compartments.SingleGroupCompartment;

import java.util.HashMap;
import java.util.Map;

public class CorrosiveSnailFactory {
    public static Map<String, Compartment> createCompartments() {
        Map<String, Compartment> compartments = new HashMap<>();

        GroupCompartment visceralHump = new GroupCompartment("visceral_hump", 1F);
        visceralHump.addSubCompartment(new SingleGroupCompartment("shell", 0F));
        visceralHump.addSubCompartment(new SingleGroupCompartment("lung", 0.7F));
        GroupCompartment ganglia = new GroupCompartment("ganglia", 0.2F);
        ganglia.addSubCompartment(new SingleGroupCompartment("cerebral_ganglia", 0.2F));
        ganglia.addSubCompartment(new SingleGroupCompartment("pedal_ganglia", 0.2F));
        ganglia.addSubCompartment(new SingleGroupCompartment("pleural_ganglia", 0.2F));
        ganglia.addSubCompartment(new SingleGroupCompartment("buccal_ganglia", 0.2F));
        visceralHump.addSubCompartment(ganglia);
        visceralHump.addSubCompartment(new SingleGroupCompartment("stomach", 0.7F));
        visceralHump.addSubCompartment(new SingleGroupCompartment("intestine", 0.8F));
        visceralHump.addSubCompartment(new SingleGroupCompartment("kidney", 0.9F));
        visceralHump.addSubCompartment(new SingleGroupCompartment("liver", 0.9F));
        visceralHump.addSubCompartment(new SingleGroupCompartment("heart", 0.4F));
        visceralHump.addSubCompartment(new SingleGroupCompartment("alkali_glands", 0.5F));

        GroupCompartment foot = new GroupCompartment("foot", 1F);
        foot.addSubCompartment(new SingleGroupCompartment("underside_skin", 0.05F));
        foot.addSubCompartment(new SingleGroupCompartment("topside_skin", 0.01F));
        foot.addSubCompartment(new SingleGroupCompartment("muscles", 0.4F));
        foot.addSubCompartment(new SingleGroupCompartment("excretory_pores", 0.5F));
        foot.addSubCompartment(new SingleGroupCompartment("mouth", 0.5F));
        foot.addSubCompartment(new SingleGroupCompartment("first_digit", 0.4F));
        foot.addSubCompartment(new SingleGroupCompartment("second_digit", 0.4F));
        foot.addSubCompartment(new SingleGroupCompartment("third_digit", 0.4F));
        foot.addSubCompartment(new SingleGroupCompartment("fourth_digit", 0.4F));
        foot.addSubCompartment(new SingleGroupCompartment("fifth_digit", 0.4F));
        foot.addSubCompartment(new SingleGroupCompartment("sixth_digit", 0.4F));

        compartments.put("visceral_hump", visceralHump);
        compartments.put("foot", foot);

        return compartments;
    }

}
