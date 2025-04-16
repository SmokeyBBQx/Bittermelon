package com.site21.bittermelon.content.medical.compartments;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.site21.bittermelon.content.medical.medicalstats.MedicalStats;
import net.minecraft.core.Holder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static com.site21.bittermelon.init.neoforge.BitterRegistries.*;

public class CompartmentInstance {
    public static final Codec<Holder<Compartment>> COMPARTMENT_NON_EMPTY_CODEC = COMPARTMENT_REGISTRY.holderByNameCodec().validate(DataResult::success);
    private static final StreamCodec<RegistryFriendlyByteBuf, Holder<Compartment>> COMPARTMENT_STREAM_CODEC = ByteBufCodecs.holderRegistry(COMPARTMENT_REGISTRY_KEY);

    public static <E extends Enum<E>> Codec<EnumSet<E>> enumSetCodec(Class<E> enumClass) {
        return Codec.list(
                Codec.STRING.comapFlatMap(
                        s -> {
                            try {
                                return DataResult.success(Enum.valueOf(enumClass, s));
                            } catch (IllegalArgumentException e) {
                                return DataResult.error(() -> "Unknown enum value: " + s);
                            }
                        },
                        Enum::name
                )
        ).xmap(
                list -> {
                    EnumSet<E> enumSet = EnumSet.noneOf(enumClass);
                    enumSet.addAll(list);
                    return enumSet;
                },
                ArrayList::new
        );
    }

    public static final Codec<EnumSet<CompartmentTag>> COMPARTMENT_TAGS_CODEC =
            enumSetCodec(CompartmentTag.class);

    public static final Codec<EnumMap<FunctionType, Float>> ATTRIBUTE_MAP_CODEC =
            Codec.unboundedMap(
                    Codec.STRING.comapFlatMap(
                            s -> {
                                try {
                                    return DataResult.success(FunctionType.valueOf(s));
                                } catch (IllegalArgumentException e) {
                                    return DataResult.error(() -> "Unknown FunctionType: " + s);
                                }
                            },
                            Enum::name
                    ),
                    Codec.FLOAT
            ).xmap(
                    map -> {
                        EnumMap<FunctionType, Float> enumMap = new EnumMap<>(FunctionType.class);
                        enumMap.putAll(map);
                        return enumMap;
                    },
                    HashMap::new
            );

    public static final Codec<CompartmentInstance> CODEC = Codec.lazyInitialized(
            () -> RecordCodecBuilder.create(
                    instance -> instance.group(
                                    COMPARTMENT_NON_EMPTY_CODEC.fieldOf("compartmentID").forGetter(CompartmentInstance::getCompartmentHolder),
                                    UUIDUtil.CODEC.fieldOf("id").forGetter(CompartmentInstance::getUUID),
                                    Codec.FLOAT.fieldOf("health").forGetter(CompartmentInstance::getHealth),
                                    Codec.FLOAT.fieldOf("trueMaxHealth").forGetter(CompartmentInstance::getTrueMaxHealth),
                                    Codec.FLOAT.fieldOf("modifiedMaxHealth").forGetter(CompartmentInstance::getMaxHealth),
                                    Codec.BOOL.fieldOf("isHidden").forGetter(CompartmentInstance::isHidden),
                                    Codec.BOOL.fieldOf("isObscured").forGetter(CompartmentInstance::isObscured),
                                    UUIDUtil.CODEC.lenientOptionalFieldOf("parent").forGetter(CompartmentInstance::getOptionalParentID),
                                    Codec.list(UUIDUtil.CODEC).xmap(HashSet::new, ArrayList::new).fieldOf("children").forGetter(CompartmentInstance::getChildren),
                                    ATTRIBUTE_MAP_CODEC.fieldOf("attributes").forGetter(CompartmentInstance::getAttributes),
                                    COMPARTMENT_TAGS_CODEC.fieldOf("compartmentTags").forGetter(CompartmentInstance::getTags),
                                    ItemStack.OPTIONAL_CODEC.fieldOf("item").forGetter(CompartmentInstance::getItem),
                                    Codec.STRING.fieldOf("displayName").forGetter(CompartmentInstance::getName),
                                    ResourceLocation.CODEC.lenientOptionalFieldOf("icon").forGetter(CompartmentInstance::getOptionalIcon))
                            .apply(instance, CompartmentInstance::new)));

    public static final StreamCodec<RegistryFriendlyByteBuf, CompartmentInstance> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public void encode(@NotNull RegistryFriendlyByteBuf buf, @NotNull CompartmentInstance value) {
            COMPARTMENT_STREAM_CODEC.encode(buf, value.getCompartmentHolder());
            buf.writeUUID(value.getUUID());
            buf.writeFloat(value.getHealth());
            buf.writeFloat(value.getTrueMaxHealth());
            buf.writeFloat(value.getMaxHealth());
            buf.writeBoolean(value.isHidden());
            buf.writeBoolean(value.isObscured());

            boolean hasParent = value.getParentID() != null;
            buf.writeBoolean(hasParent);
            if (hasParent) {
                buf.writeUUID(value.getParentID());
            }

            buf.writeCollection(value.getChildren(), RegistryFriendlyByteBuf::writeUUID);
            buf.writeMap(value.getAttributes(),
                    FriendlyByteBuf::writeEnum,
                    FriendlyByteBuf::writeFloat
            );
            buf.writeEnumSet(value.getTags(), CompartmentTag.class);
            ItemStack.OPTIONAL_STREAM_CODEC.encode(buf, value.getItem());
            buf.writeUtf(value.getName());

            boolean hasIcon = value.getIcon() != null;
            buf.writeBoolean(hasIcon);
            if (hasIcon) {
                buf.writeResourceLocation(value.getIcon());
            }
        }

        @Override
        public @NotNull CompartmentInstance decode(@NotNull RegistryFriendlyByteBuf buf) {
            Holder<Compartment> compartmentHolder = COMPARTMENT_STREAM_CODEC.decode(buf);
            UUID id = buf.readUUID();
            float health = buf.readFloat();
            float trueMaxHealth = buf.readFloat();
            float modifiedMaxHealth = buf.readFloat();
            boolean isHidden = buf.readBoolean();
            boolean isObscured = buf.readBoolean();

            UUID parent = null;
            boolean hasParent = buf.readBoolean();
            if (hasParent) {
                parent = buf.readUUID();
            }
            Optional<UUID> optionalParent = Optional.ofNullable(parent);

            HashSet<UUID> children = new HashSet<>(buf.readCollection(
                    HashSet::new, RegistryFriendlyByteBuf::readUUID)
            );

            EnumMap<FunctionType, Float> attributes = new EnumMap<>(FunctionType.class);
            Map<FunctionType, Float> tempMap = buf.readMap(
                    byteBuf -> byteBuf.readEnum(FunctionType.class),
                    FriendlyByteBuf::readFloat
            );
            attributes.putAll(tempMap);

            EnumSet<CompartmentTag> tags = buf.readEnumSet(CompartmentTag.class);
            ItemStack item = ItemStack.OPTIONAL_STREAM_CODEC.decode(buf);
            String displayName = buf.readUtf();

            ResourceLocation icon = null;
            if (buf.readBoolean()) {
                icon = buf.readResourceLocation();
            }
            Optional<ResourceLocation> optionalIcon = Optional.ofNullable(icon);

            return new CompartmentInstance(
                    compartmentHolder, id, health, trueMaxHealth, modifiedMaxHealth,
                    isHidden, isObscured, optionalParent, children, attributes, tags,
                    item, displayName, optionalIcon
            );
        }
    };

    private final Compartment compartment;
    private final UUID uuid;
    private float health;
    private final float trueMaxHealth;
    private float modifiedMaxHealth;
    private boolean isHidden;
    private boolean isObscured;
    private UUID parent;
    private final HashSet<UUID> children;
    private final EnumMap<FunctionType, Float> attributes;
    private final EnumSet<CompartmentTag> compartmentTags;
    private ItemStack item;
    private String displayName;
    protected ResourceLocation icon;
    private float function = 1.0f;

    /**
     * Codec deserialization constructor
     */
    public CompartmentInstance(@NotNull Holder<Compartment> compartment, UUID id, float health, float trueMaxHealth,
                               float modifiedMaxHealth, boolean isHidden, boolean isObscured, @NotNull Optional<UUID> optionalParent,
                               HashSet<UUID> children, EnumMap<FunctionType, Float> attributes,
                               EnumSet<CompartmentTag> compartmentTags, ItemStack item, String displayName,
                               @NotNull Optional<ResourceLocation> optionalIcon) {
        this.compartment = compartment.value();
        this.uuid = id;
        this.health = health;
        this.trueMaxHealth = trueMaxHealth;
        this.modifiedMaxHealth = modifiedMaxHealth;
        this.isHidden = isHidden;
        this.isObscured = isObscured;
        optionalParent.ifPresent(parent -> this.parent = parent);
        this.children = children;
        this.attributes = attributes;
        this.compartmentTags = compartmentTags;
        this.item = item;
        this.displayName = displayName;
        optionalIcon.ifPresent(icon -> this.icon = icon);
    }

    /**
     * Constructor for new compartment instances
     */
    @Contract(pure = true)
    public CompartmentInstance(@NotNull Compartment compartment, EnumMap<FunctionType, Float> attributes, EnumSet<CompartmentTag> compartmentTags, float maxHealth, String displayName, boolean isHidden) {
        this.compartment = compartment;
        this.attributes = attributes;
        this.compartmentTags = compartmentTags;
        this.uuid = UUID.randomUUID();
        this.health = maxHealth;
        this.trueMaxHealth = maxHealth;
        this.modifiedMaxHealth = maxHealth;
        this.displayName = displayName;
        this.isHidden = isHidden;
        this.item = ItemStack.EMPTY;
        isObscured = false;
        children = new HashSet<>();

        compartment.attributeInitializer.accept(this);
    }

    @Contract(pure = true)
    public CompartmentInstance(@NotNull Compartment compartment, EnumMap<FunctionType, Float> attributes, float maxHealth, String displayName, boolean isHidden) {
        this(compartment, attributes, compartment.getDefaultTags().clone(), maxHealth, displayName, isHidden);
    }

    @Contract(pure = true)
    public CompartmentInstance(@NotNull Compartment compartment, float maxHealth, String displayName, boolean isHidden) {
        this(compartment, new EnumMap<>(FunctionType.class), compartment.getDefaultTags().clone(), maxHealth, displayName, isHidden);
    }

    public void initializeWithParent(@NotNull CompartmentInstance parent) {
        this.parent = parent.uuid;
        parent.addChild(uuid);
    }

    public void tick(MedicalStats medicalStats) {
        compartment.tick(medicalStats, this);
    }

    public boolean hasTag(CompartmentTag tag) {
        return compartmentTags.contains(tag);
    }

    public void updateFunction(float functionMultiplier) {
        function = 1 * getHealth() / trueMaxHealth * functionMultiplier;
    }

    public boolean areChildrenEmpty(MedicalStats medicalStats) {
        return children.isEmpty() || children.stream().allMatch(child -> medicalStats.getCompartment(child).isHidden);
    }

    public float getAttribute(FunctionType type) {
        if (attributes == null) return 0f;

        if (type == FunctionType.FUNCTION) {
            return attributes.getOrDefault(type, 1f) * function;
        }

        return attributes.getOrDefault(type, 0f) * function;
    }

    public Compartment getCompartment() {
        return compartment;
    }

    public Holder<Compartment> getCompartmentHolder() {
        return compartment.builtInRegistryHolder();
    }

    public UUID getUUID() {
        return uuid;
    }

    public float getHealth() {
        return health;
    }

    public float getTrueMaxHealth() {
        return trueMaxHealth;
    }

    public float getMaxHealth() {
        return modifiedMaxHealth;
    }

    public boolean isHidden() {
        return isHidden;
    }

    public boolean isObscured() {
        return isObscured;
    }

    @Nullable
    public UUID getParentID() {
        return parent;
    }

    public Optional<UUID> getOptionalParentID() {
        return Optional.ofNullable(parent);
    }

    public CompartmentInstance getParent(MedicalStats medicalStats) {
        if (parent == null) return null;
        return medicalStats.getCompartment(parent);
    }

    public HashSet<UUID> getChildren() {
        return children;
    }

    public EnumMap<FunctionType, Float> getAttributes() {
        return attributes;
    }

    public EnumSet<CompartmentTag> getTags() {
        return compartmentTags;
    }

    public ItemStack getItem() {
        return item;
    }

    public String getName() {
        return displayName;
    }

    @Nullable
    public ResourceLocation getIcon() {
        return icon;
    }

    public Optional<ResourceLocation> getOptionalIcon() {
        return Optional.ofNullable(icon);
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public void setHidden(boolean hidden) {
        isHidden = hidden;
    }

    public void setObscured(boolean obscured) {
        isObscured = obscured;
    }

    public void setParent(UUID parent) {
        this.parent = parent;
    }

    public void setItem(@NotNull ItemLike item) {
        this.item = item.asItem().getDefaultInstance();
    }

    public void setItem(ItemStack stack) {
        this.item = stack;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setIcon(ResourceLocation icon) {
        this.icon = icon;
    }

    public void modifyHealth(float amount) {
        this.health = Math.max(0, Math.min(modifiedMaxHealth, health + amount));
    }

    public void modifyMaxHealth(float delta) {
        this.modifiedMaxHealth = Math.max(0, Math.min(modifiedMaxHealth + delta, trueMaxHealth));
    }

    public void addChild(UUID childID) {
        children.add(childID);
    }

    public void setAttribute(FunctionType type, float value) {
        attributes.put(type, value);
    }

    public void addTag(CompartmentTag tag) {
        compartmentTags.add(tag);
    }
}
