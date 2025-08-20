package com.site21.bittermelon.init.custom;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.roles.Role;
import com.site21.bittermelon.content.substance.Substance;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static com.site21.bittermelon.init.neoforge.BitterRegistries.ROLE_REGISTRY_KEY;
import static com.site21.bittermelon.init.neoforge.BitterRegistries.SUBSTANCE_REGISTRY_KEY;

public class Roles {
    public static final DeferredRegister<Role> ROLES = DeferredRegister.create(ROLE_REGISTRY_KEY, Bittermelon.MOD_ID);

    public static final Supplier<Role> CLASS_D = ROLES.register("class_d",
            () -> new Role("Class-D", 0XFFF77C00).defaultRole());

    public static final Supplier<Role> ASSISTANT = ROLES.register("assistant",
            () -> new Role("Assistant", 0XFF755F52).defaultRole());

    
}
