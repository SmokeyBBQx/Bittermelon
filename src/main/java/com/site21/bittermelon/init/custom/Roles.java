package com.site21.bittermelon.init.custom;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.systems.roles.FoundationRole;
import com.site21.bittermelon.systems.roles.Role;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static com.site21.bittermelon.init.neoforge.BitterRegistries.ROLE_REGISTRY_KEY;

public class Roles {
    public static final DeferredRegister<Role> ROLES = DeferredRegister.create(ROLE_REGISTRY_KEY, Bittermelon.MOD_ID);

    public static final Supplier<Role> CLASS_D = ROLES.register("class_d",
            () -> new FoundationRole("Class-D", 0XFFF77C00, "Our most expensive asset", "Waste the Foundation's resources spent on training you by dying in under a minute.",
                    "Auxiliary and Expendable Resources Department", "Class-D")
                    .defaultRole()
                    .skinLocation(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "textures/gui/sprites/character/skins/class_d.png")));

    public static final Supplier<Role> ASSISTANT = ROLES.register("assistant",
            () -> new FoundationRole("Assistant", 0XFF755F52, "Glorified water boys", "", "N/A", "Assistant")
                    .defaultRole()
                    .skinLocation(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "textures/gui/sprites/character/skins/assistant.png"))
    );

    public static final Supplier<Role> SECURITY_TRAINEE = ROLES.register("security_trainee",
            () -> new FoundationRole(
                    "Security Trainee",
                    0XFF4A6085,
                    "Gun safety examples",
                    "Security Trainee, the lowest rank of S&C, is usually held by new recruits while at the Security Induction Program (SIP), but the rank is occasionally also assigned to personnel after completion of the SIP.",
                    "Security Department",
                    "Security Trainee")
                    .addMessage(Component.literal("Welcome to the Security Department. Make sure to read the wiki for info on how to get started."))
                    .skinLocation(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "textures/gui/sprites/character/skins/security.png"))
    );

    public static final Supplier<Role> SECURITY_GUARD = ROLES.register("security_guard",
            () -> new FoundationRole(
                    "Security Guard",
                    0XFF4A6085,
                    "Shoot at anything that moves",
                    "Security Guard is the first promotion new personnel can earn after the SIP. The Security Guard's job is to apply the skills and knowledge gained from the induction program while continuing to learn how to follow orders given by higher-ranking supervisors.",
                    "Security Department",
                    "Security Guard")
                    .addMessage(Component.literal("Welcome to the Security Department. Make sure to read the wiki for info on how to get started."))
                    .skinLocation(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "textures/gui/sprites/character/skins/security.png"))
    );

    public static final Supplier<Role> RESEARCH_INTERN = ROLES.register("research_intern",
            () -> new FoundationRole(
                    "Research Intern",
                    0XFF35B5CC,
                    "Days Without Incident: 0",
                    "Research and Experimentation. The backbone of the Scientific Department. If there is something that doesn't fit in, we deal with it. The R&E division is the broadest of all divisions, exploring all fundamental sciences of the world with the spin of the anomalies. Being a researcher in R&E doesn't just mean \"mad scientist throwing Class-Ds at anomalies.\" No, the work is precise and beneficial to the Foundation. The countless unpredictable anomalies we have in containment and the numerous number of SCPs that get discovered every year lead to an inconsistent set of rules. Therefore we can not have rules, and that's what we are about. Being able to work with anything, secure it, contain it, and protect it, no matter how unconventional and strange it is.",
                    "Scientific Department",
                    "Research Intern")
                    .addMessage(Component.literal("Welcome to the Scientific Department. Make sure to read the wiki for info on how to get started."))
                    .skinLocation(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "textures/gui/sprites/character/skins/science.png"))
    );
}
