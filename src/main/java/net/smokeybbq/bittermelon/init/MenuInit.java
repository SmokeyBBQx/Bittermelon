package net.smokeybbq.bittermelon.init;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.smokeybbq.bittermelon.Bittermelon;
import net.smokeybbq.bittermelon.items.cigarettes.CigarettePackMenu;
import net.smokeybbq.bittermelon.items.handlabeler.HandLabelerMenu;
import net.smokeybbq.bittermelon.items.toolbox.ToolBoxMenu;

public class MenuInit {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, Bittermelon.MODID);

    public static final RegistryObject<MenuType<HandLabelerMenu>> HAND_LABELER_MENU = MENUS.register("hand_labeler", () -> IForgeMenuType.create(((windowId, inv, data) -> new HandLabelerMenu(windowId, inv))));

    public static final RegistryObject<MenuType<ToolBoxMenu>> TOOLBOX_MENU = MENUS.register("toolbox_menu", () -> IForgeMenuType.create((windowId, inv, data) -> new ToolBoxMenu(windowId, inv, data.readItem())));

    public static final RegistryObject<MenuType<CigarettePackMenu>> CIGARETTE_PACK_MENU = MENUS.register("cigarette_pack", () -> IForgeMenuType.create((windowId, inv, data) -> new CigarettePackMenu(windowId, inv, data.readItem())));
}
