package net.smokeybbq.bittermelon.medical.simulation.compartments.anatomies;

import net.smokeybbq.bittermelon.medical.simulation.compartments.Compartment;
import net.smokeybbq.bittermelon.medical.simulation.compartments.CompartmentTag;
import net.smokeybbq.bittermelon.medical.simulation.compartments.GroupCompartment;
import net.smokeybbq.bittermelon.medical.simulation.compartments.SingleGroupCompartment;

import java.util.HashMap;
import java.util.Map;

public class HumanFactory {
    public static Map<String, Compartment> createCompartments() {
        Map<String, Compartment> compartments = new HashMap<>();

        GroupCompartment head = new GroupCompartment("head", 1);
        head.addSubCompartment(new SingleGroupCompartment("skull", 0.0001F));
        head.addSubCompartment(new SingleGroupCompartment("brain", 0.01F));
        head.addSubCompartment(new SingleGroupCompartment("left_eye", 0.1F));
        head.addSubCompartment(new SingleGroupCompartment("right_eye", 0.1F));
        head.addSubCompartment(new SingleGroupCompartment("left_ear", 0.1F));
        head.addSubCompartment(new SingleGroupCompartment("right_ear", 0.1F));
        head.addSubCompartment(new SingleGroupCompartment("skin", 0.2F));
        head.addSubCompartment(new SingleGroupCompartment("nose", 0.3F));
        head.addSubCompartment(new SingleGroupCompartment("mouth", 0.5F));
        head.addSubCompartment(new SingleGroupCompartment("tongue", 0.5F));
        head.addSubCompartment(new SingleGroupCompartment("teeth", 0.0001F));

        GroupCompartment chest = new GroupCompartment("chest", 1);
        chest.addSubCompartment(new SingleGroupCompartment("heart", 0.4F));
        chest.addSubCompartment(new SingleGroupCompartment("left_lung", 0.7F));
        chest.addSubCompartment(new SingleGroupCompartment("right_lung", 0.7F));
        SingleGroupCompartment thymus = new SingleGroupCompartment("thymus", 0.3F);
        thymus.addTag(CompartmentTag.IMMUNE);
        chest.addSubCompartment(thymus);
        chest.addSubCompartment(new SingleGroupCompartment("muscles", 0.4F));
        chest.addSubCompartment(new SingleGroupCompartment("ribs", 0.1F));
        chest.addSubCompartment(new SingleGroupCompartment("sternum", 0.1F));
        chest.addSubCompartment(new SingleGroupCompartment("esophagus", 0.5F));
        chest.addSubCompartment(new SingleGroupCompartment("trachea", 0.5F));
        head.addSubCompartment(new SingleGroupCompartment("skin", 0.2F));

        GroupCompartment abdomen = new GroupCompartment("abdomen", 1);

        SingleGroupCompartment stomach = new SingleGroupCompartment("stomach", 0.7F);
        stomach.addTag(CompartmentTag.GASTROINTESTINAL);
        abdomen.addSubCompartment(stomach);

        SingleGroupCompartment smallIntestine = new SingleGroupCompartment("small_intestine", 0.9F);
        smallIntestine.addTag(CompartmentTag.GASTROINTESTINAL);
        abdomen.addSubCompartment(smallIntestine);

        SingleGroupCompartment largeIntestine = new SingleGroupCompartment("large_intestine", 0.8F);
        largeIntestine.addTag(CompartmentTag.GASTROINTESTINAL);
        abdomen.addSubCompartment(largeIntestine);

        SingleGroupCompartment liver = new SingleGroupCompartment("liver", 0.9F);
        liver.addTag(CompartmentTag.METABOLIZING);
        abdomen.addSubCompartment(liver);

        abdomen.addSubCompartment(new SingleGroupCompartment("gallbladder", 0.5F));
        abdomen.addSubCompartment(new SingleGroupCompartment("pancreas", 0.5F));

        SingleGroupCompartment spleen = new SingleGroupCompartment("spleen", 0.4F);
        spleen.addTag(CompartmentTag.IMMUNE);
        abdomen.addSubCompartment(spleen);

        SingleGroupCompartment leftKidney = new SingleGroupCompartment("left_kidney", 0.9F);
        leftKidney.addTag(CompartmentTag.ELIMINATING);
        abdomen.addSubCompartment(leftKidney);

        SingleGroupCompartment rightKidney = new SingleGroupCompartment("right_kidney", 0.9F);
        rightKidney.addTag(CompartmentTag.ELIMINATING);
        abdomen.addSubCompartment(rightKidney);

        abdomen.addSubCompartment(new SingleGroupCompartment("bladder", 0.4F));
        abdomen.addSubCompartment(new SingleGroupCompartment("muscles", 0.4F));
        abdomen.addSubCompartment(new SingleGroupCompartment("spine", 0.1F));
        abdomen.addSubCompartment(new SingleGroupCompartment("pelvis", 0.2F));
        head.addSubCompartment(new SingleGroupCompartment("skin", 0.2F));

        GroupCompartment leftUpperLimb = new GroupCompartment("left_arm", 1);
        leftUpperLimb.addSubCompartment(new SingleGroupCompartment("left_humerus", 0.1F));
        leftUpperLimb.addSubCompartment(new SingleGroupCompartment("left_radius_ulna", 0.1F));
        leftUpperLimb.addSubCompartment(new SingleGroupCompartment("left_hand", 0.3F));
        leftUpperLimb.addSubCompartment(new SingleGroupCompartment("left_wrist", 0.3F));
        leftUpperLimb.addSubCompartment(new SingleGroupCompartment("skin", 0.2F));
        leftUpperLimb.addSubCompartment(new SingleGroupCompartment("muscles", 0.4F));
        leftUpperLimb.addSubCompartment(new SingleGroupCompartment("tendons", 0.2F));

        GroupCompartment rightUpperLimb = new GroupCompartment("right_arm", 1);
        rightUpperLimb.addSubCompartment(new SingleGroupCompartment("right_humerus", 0.1F));
        rightUpperLimb.addSubCompartment(new SingleGroupCompartment("right_radius_ulna", 0.1F));
        rightUpperLimb.addSubCompartment(new SingleGroupCompartment("right_hand", 0.3F));
        rightUpperLimb.addSubCompartment(new SingleGroupCompartment("right_wrist", 0.3F));
        rightUpperLimb.addSubCompartment(new SingleGroupCompartment("skin", 0.2F));
        rightUpperLimb.addSubCompartment(new SingleGroupCompartment("muscles", 0.4F));
        rightUpperLimb.addSubCompartment(new SingleGroupCompartment("tendons", 0.2F));

        GroupCompartment leftLowerLimb = new GroupCompartment("left_leg", 1);
        leftLowerLimb.addSubCompartment(new SingleGroupCompartment("left_femur", 0.1F));
        leftLowerLimb.addSubCompartment(new SingleGroupCompartment("left_tibia_fibula", 0.1F));
        leftLowerLimb.addSubCompartment(new SingleGroupCompartment("left_foot", 0.3F));
        leftLowerLimb.addSubCompartment(new SingleGroupCompartment("left_ankle", 0.3F));
        leftLowerLimb.addSubCompartment(new SingleGroupCompartment("skin", 0.2F));
        leftLowerLimb.addSubCompartment(new SingleGroupCompartment("muscles", 0.4F));
        leftLowerLimb.addSubCompartment(new SingleGroupCompartment("tendons", 0.2F));

        GroupCompartment rightLowerLimb = new GroupCompartment("right_leg", 1);
        rightLowerLimb.addSubCompartment(new SingleGroupCompartment("right_femur", 0.1F));
        rightLowerLimb.addSubCompartment(new SingleGroupCompartment("right_tibia_fibula", 0.1F));
        rightLowerLimb.addSubCompartment(new SingleGroupCompartment("right_foot", 0.3F));
        rightLowerLimb.addSubCompartment(new SingleGroupCompartment("right_ankle", 0.3F));
        rightLowerLimb.addSubCompartment(new SingleGroupCompartment("skin", 0.2F));
        rightLowerLimb.addSubCompartment(new SingleGroupCompartment("muscles", 0.4F));
        rightLowerLimb.addSubCompartment(new SingleGroupCompartment("tendons", 0.2F));

        GroupCompartment back = new GroupCompartment("back", 1);
        back.addSubCompartment(new SingleGroupCompartment("spine", 0.1F));
        back.addSubCompartment(new SingleGroupCompartment("scapula", 0.1F));
        back.addSubCompartment(new SingleGroupCompartment("muscles", 0.4F));
        back.addSubCompartment(new SingleGroupCompartment("skin", 0.2F));

        SingleGroupCompartment circulatory = new SingleGroupCompartment("circulatory_system", 1);

        compartments.put("circulatory_system", circulatory);
        compartments.put("head", head);
        compartments.put("chest", chest);
        compartments.put("abdomen", abdomen);
        compartments.put("left_arm", leftUpperLimb);
        compartments.put("right_arm", rightUpperLimb);
        compartments.put("left_leg", leftLowerLimb);
        compartments.put("right_leg", rightLowerLimb);
        compartments.put("back", back);
        return compartments;
    }
}
