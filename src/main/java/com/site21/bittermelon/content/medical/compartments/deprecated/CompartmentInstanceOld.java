package com.site21.bittermelon.content.medical.compartments.deprecated;

public class CompartmentInstanceOld {
//    public static final Codec<Holder<Compartment>> COMPARTMENT_NON_EMPTY_CODEC = COMPARTMENT_REGISTRY.holderByNameCodec().validate(DataResult::success);
//    private static final StreamCodec<RegistryFriendlyByteBuf, Holder<Compartment>> COMPARTMENT_STREAM_CODEC = ByteBufCodecs.holderRegistry(COMPARTMENT_REGISTRY_KEY);
//
//    public static <E extends Enum<E>> Codec<EnumSet<E>> enumSetCodec(Class<E> enumClass) {
//        return Codec.list(
//                Codec.STRING.comapFlatMap(
//                        s -> {
//                            try {
//                                return DataResult.success(Enum.valueOf(enumClass, s));
//                            } catch (IllegalArgumentException e) {
//                                return DataResult.error(() -> "Unknown enum value: " + s);
//                            }
//                        },
//                        Enum::name
//                )
//        ).xmap(
//                list -> {
//                    EnumSet<E> enumSet = EnumSet.noneOf(enumClass);
//                    enumSet.addAll(list);
//                    return enumSet;
//                },
//                ArrayList::new
//        );
//    }
//
//    public static final Codec<EnumSet<CompartmentTag>> COMPARTMENT_TAGS_CODEC =
//            enumSetCodec(CompartmentTag.class);
//
//    public static final Codec<EnumMap<MedicalAttribute, Float>> ATTRIBUTE_MAP_CODEC =
//            Codec.unboundedMap(
//                    Codec.STRING.comapFlatMap(
//                            s -> {
//                                try {
//                                    return DataResult.success(MedicalAttribute.valueOf(s));
//                                } catch (IllegalArgumentException e) {
//                                    return DataResult.error(() -> "Unknown MedicalAttribute: " + s);
//                                }
//                            },
//                            Enum::name
//                    ),
//                    Codec.FLOAT
//            ).xmap(
//                    map -> {
//                        EnumMap<MedicalAttribute, Float> enumMap = new EnumMap<>(MedicalAttribute.class);
//                        enumMap.putAll(map);
//                        return enumMap;
//                    },
//                    HashMap::new
//            );
//
//    public static final Codec<CompartmentInstance> CODEC = Codec.lazyInitialized(
//            () -> RecordCodecBuilder.create(
//                    instance -> instance.group(
//                                    COMPARTMENT_NON_EMPTY_CODEC.fieldOf("compartmentID").forGetter(CompartmentInstance::getCompartmentHolder),
//                                    UUIDUtil.CODEC.fieldOf("id").forGetter(CompartmentInstance::getUUID),
//                                    UUIDUtil.CODEC.fieldOf("parentSpaceID").forGetter(CompartmentInstance::getParentSpaceID),
//                                    CompartmentSpace.CODEC.fieldOf("compartmentSpace").forGetter(CompartmentInstance::getCompartmentSpace),
//                                    VisualData.CODEC.fieldOf("visualData").forGetter(CompartmentInstance::getVisualData),
//                                    Codec.FLOAT.fieldOf("health").forGetter(CompartmentInstance::getHealthRaw),
//                                    Codec.FLOAT.fieldOf("trueMaxHealth").forGetter(CompartmentInstance::getTrueMaxHealth),
//                                    Codec.FLOAT.fieldOf("modifiedMaxHealth").forGetter(CompartmentInstance::getMaxHealth),
//                                    Codec.BOOL.fieldOf("isHidden").forGetter(CompartmentInstance::isHidden),
//                                    Codec.BOOL.fieldOf("isObscured").forGetter(CompartmentInstance::isObscured),
//                                    ATTRIBUTE_MAP_CODEC.fieldOf("attributes").forGetter(CompartmentInstance::getAttributes),
//                                    COMPARTMENT_TAGS_CODEC.fieldOf("compartmentTags").forGetter(CompartmentInstance::getTags),
//                                    ItemStack.OPTIONAL_CODEC.fieldOf("item").forGetter(CompartmentInstance::getItem),
//                                    Codec.STRING.fieldOf("displayName").forGetter(CompartmentInstance::getName),
//                                    ResourceLocation.CODEC.lenientOptionalFieldOf("icon").forGetter(CompartmentInstance::getOptionalIcon))
//                            .apply(instance, CompartmentInstance::new)));
//
//    public static final StreamCodec<RegistryFriendlyByteBuf, CompartmentInstance> STREAM_CODEC = new StreamCodec<>() {
//        @Override
//        public void encode(@NotNull RegistryFriendlyByteBuf buf, @NotNull CompartmentInstance value) {
//            COMPARTMENT_STREAM_CODEC.encode(buf, value.getCompartmentHolder());
//            buf.writeUUID(value.getUUID());
//            buf.writeUUID(value.getUUID());
//            CompartmentSpace.STREAM_CODEC.encode(buf, value.getCompartmentSpace());
//            VisualData.STREAM_CODEC.encode(buf, value.getVisualData());
//            buf.writeFloat(value.getHealthRaw());
//            buf.writeFloat(value.getTrueMaxHealth());
//            buf.writeFloat(value.getMaxHealth());
//            buf.writeBoolean(value.isHidden());
//            buf.writeBoolean(value.isObscured());
//
//            buf.writeMap(value.getAttributes(),
//                    FriendlyByteBuf::writeEnum,
//                    FriendlyByteBuf::writeFloat
//            );
//            buf.writeEnumSet(value.getTags(), CompartmentTag.class);
//            ItemStack.OPTIONAL_STREAM_CODEC.encode(buf, value.getItem());
//            buf.writeUtf(value.getName());
//
//            boolean hasIcon = value.getIcon() != null;
//            buf.writeBoolean(hasIcon);
//            if (hasIcon) {
//                buf.writeResourceLocation(value.getIcon());
//            }
//        }
//
//        @Override
//        public @NotNull CompartmentInstance decode(@NotNull RegistryFriendlyByteBuf buf) {
//            Holder<Compartment> compartmentHolder = COMPARTMENT_STREAM_CODEC.decode(buf);
//            UUID id = buf.readUUID();
//            UUID parentSpaceID = buf.readUUID();
//            CompartmentSpace compartmentSpace = CompartmentSpace.STREAM_CODEC.decode(buf);
//            VisualData visualData = VisualData.STREAM_CODEC.decode(buf);
//            float health = buf.readFloat();
//            float trueMaxHealth = buf.readFloat();
//            float modifiedMaxHealth = buf.readFloat();
//            boolean isHidden = buf.readBoolean();
//            boolean isObscured = buf.readBoolean();
//
//            EnumMap<MedicalAttribute, Float> attributes = new EnumMap<>(MedicalAttribute.class);
//            Map<MedicalAttribute, Float> tempMap = buf.readMap(
//                    byteBuf -> byteBuf.readEnum(MedicalAttribute.class),
//                    FriendlyByteBuf::readFloat
//            );
//            attributes.putAll(tempMap);
//
//            EnumSet<CompartmentTag> tags = buf.readEnumSet(CompartmentTag.class);
//            ItemStack item = ItemStack.OPTIONAL_STREAM_CODEC.decode(buf);
//            String displayName = buf.readUtf();
//
//            ResourceLocation icon = null;
//            if (buf.readBoolean()) {
//                icon = buf.readResourceLocation();
//            }
//            Optional<ResourceLocation> optionalIcon = Optional.ofNullable(icon);
//
//            return new CompartmentInstance(
//                    compartmentHolder, id, parentSpaceID, compartmentSpace, visualData, health, trueMaxHealth, modifiedMaxHealth,
//                    isHidden, isObscured, attributes, tags,
//                    item, displayName, optionalIcon
//            );
//        }
//    };
//
//    private final Compartment compartment;
//    private final UUID uuid;
//    private UUID parentSpaceID;
//    private CompartmentSpace compartmentSpace;
//    private VisualData visualData;
//    private float health;
//    private final float trueMaxHealth;
//    private float modifiedMaxHealth;
//    private boolean isHidden;
//    private boolean isObscured;
//    private final EnumMap<MedicalAttribute, Float> attributes;
//    private final EnumSet<CompartmentTag> compartmentTags;
//    private ItemStack item;
//    private String displayName;
//    protected ResourceLocation icon;
//    private float function = 1.0f;
//
//    /**
//     * Codec deserialization constructor
//     */
//    public CompartmentInstance(@NotNull Holder<Compartment> compartment, UUID id, UUID parentSpaceID, CompartmentSpace compartmentSpace, VisualData visualData, float health, float trueMaxHealth,
//                               float modifiedMaxHealth, boolean isHidden, boolean isObscured, EnumMap<MedicalAttribute, Float> attributes,
//                               EnumSet<CompartmentTag> compartmentTags, ItemStack item, String displayName,
//                               @NotNull Optional<ResourceLocation> optionalIcon) {
//        this.compartment = compartment.value();
//        this.uuid = id;
//        this.parentSpaceID = parentSpaceID;
//        this.compartmentSpace = compartmentSpace;
//        this.visualData = visualData;
//        this.health = health;
//        this.trueMaxHealth = trueMaxHealth;
//        this.modifiedMaxHealth = modifiedMaxHealth;
//        this.isHidden = isHidden;
//        this.isObscured = isObscured;
//        this.attributes = attributes;
//        this.compartmentTags = compartmentTags;
//        this.item = item;
//        this.displayName = displayName;
//        optionalIcon.ifPresent(icon -> this.icon = icon);
//    }
//
//    /**
//     * Constructor for new compartment instances
//     */
//    @Contract(pure = true)
//    public CompartmentInstance(@NotNull Compartment compartment, EnumMap<MedicalAttribute, Float> attributes, EnumSet<CompartmentTag> compartmentTags, VisualData visualData, CompartmentInstance parent, int layer, float maxHealth, String displayName, boolean isHidden) {
//        this.compartment = compartment;
//        this.attributes = attributes;
//        this.visualData = visualData;
//        this.compartmentTags = compartmentTags;
//        this.uuid = UUID.randomUUID();
//        if (parent != null) {
//            this.parentSpaceID = parent.getUUID();
//        } else {
//            this.parentSpaceID = uuid;
//            parent = this;
//        }
//        this.health = maxHealth;
//        this.trueMaxHealth = maxHealth;
//        this.modifiedMaxHealth = maxHealth;
//        this.displayName = displayName;
//        this.isHidden = isHidden;
//        this.item = ItemStack.EMPTY;
//        isObscured = false;
//
//        this.compartmentSpace = new CompartmentSpace();
//        parent.getCompartmentSpace().addToLayer(layer, uuid);
//
//        compartment.attributeInitializer.accept(this);
//    }
//
//    @Contract(pure = true)
//    public CompartmentInstance(@NotNull Compartment compartment, EnumMap<MedicalAttribute, Float> attributes, VisualData visualData, @NotNull CompartmentInstance parent, int layer, float maxHealth, String displayName, boolean isHidden) {
//        this(compartment, attributes, compartment.getDefaultTags().clone(), visualData, parent, layer, maxHealth, displayName, isHidden);
//    }
//
//    @Contract(pure = true)
//    public CompartmentInstance(@NotNull Compartment compartment, VisualData visualData, @NotNull CompartmentInstance parent, int layer, float maxHealth, String displayName, boolean isHidden) {
//        this(compartment, new EnumMap<>(MedicalAttribute.class), compartment.getDefaultTags().clone(), visualData, parent, layer, maxHealth, displayName, isHidden);
//    }
//
//    public CompartmentInstance(@NotNull Compartment compartment, float maxHealth, String displayName, boolean isHidden) {
//        this(compartment, new EnumMap<>(MedicalAttribute.class), compartment.getDefaultTags().clone(), null, null, 0, maxHealth, displayName, isHidden);
//    }
//
//    public void tick(MedicalStats medicalStats) {
//        compartment.tick(medicalStats, this);
//    }
//
//    public boolean hasTag(CompartmentTag tag) {
//        return compartmentTags.contains(tag);
//    }
//
//    public void updateFunction(float functionMultiplier, MedicalStats medicalStats) {
//        function = 1 * getHealth(medicalStats) / trueMaxHealth * functionMultiplier;
//    }
//
//    public float getAttribute(MedicalAttribute type) {
//        if (attributes == null) return 0f;
//
//        if (type == MedicalAttribute.FUNCTION) {
//            return attributes.getOrDefault(type, 1f) * function;
//        }
//
//        return attributes.getOrDefault(type, 0f) * function;
//    }
//
//    public Compartment getCompartment() {
//        return compartment;
//    }
//
//    public Holder<Compartment> getCompartmentHolder() {
//        return compartment.builtInRegistryHolder();
//    }
//
//    public UUID getUUID() {
//        return uuid;
//    }
//
//    @Nullable
//    public UUID getParentSpaceID() {
//        return parentSpaceID;
//    }
//
//    public CompartmentInstance getSpaceParent(@NotNull MedicalStats medicalStats) {
//        return medicalStats.getCompartment(parentSpaceID);
//    }
//
//    public CompartmentSpace getCompartmentSpace() {
//        return compartmentSpace;
//    }
//
//    public VisualData getVisualData() {
//        return visualData;
//    }
//
//    public float getHealth(MedicalStats medicalStats) {
//        float totalHealth = this.health;
////        for (UUID childID : children) {
////            CompartmentInstance child = medicalStats.getCompartment(childID);
////            if (child != null) {
////                totalHealth += child.getAttribute(MedicalAttribute.HEALTH);
////            }
////        }
//        return totalHealth;
//    }
//
//    public float getHealthRaw() {
//        return health;
//    }
//
//    public float getTrueMaxHealth() {
//        return trueMaxHealth;
//    }
//
//    public float getMaxHealth() {
//        return modifiedMaxHealth;
//    }
//
//    public boolean isHidden() {
//        return isHidden;
//    }
//
//    public boolean isObscured() {
//        return isObscured;
//    }
//
//    public EnumMap<MedicalAttribute, Float> getAttributes() {
//        return attributes;
//    }
//
//    public EnumSet<CompartmentTag> getTags() {
//        return compartmentTags;
//    }
//
//    public ItemStack getItem() {
//        return item;
//    }
//
//    public String getName() {
//        return displayName;
//    }
//
//    @Nullable
//    public ResourceLocation getIcon() {
//        return icon;
//    }
//
//    public Optional<ResourceLocation> getOptionalIcon() {
//        return Optional.ofNullable(icon);
//    }
//
//
//    public void setParentSpaceID(UUID parent) {
//        this.parentSpaceID = parent;
//    }
//
//    public void setVisualData(VisualData visualData) {
//        this.visualData = visualData;
//    }
//
//    public void setHealth(float health) {
//        this.health = health;
//    }
//
//    public void setHidden(boolean hidden) {
//        isHidden = hidden;
//    }
//
//    public void setObscured(boolean obscured) {
//        isObscured = obscured;
//    }
//
//    public void setItem(@NotNull ItemLike item) {
//        this.item = item.asItem().getDefaultInstance();
//    }
//
//    public void setItem(ItemStack stack) {
//        this.item = stack;
//    }
//
//    public void setDisplayName(String displayName) {
//        this.displayName = displayName;
//    }
//
//    public void setIcon(ResourceLocation icon) {
//        this.icon = icon;
//    }
//
//    public void modifyHealth(float amount) {
//        this.health = Math.max(0, Math.min(modifiedMaxHealth, health + amount));
//    }
//
//    public void modifyMaxHealth(float delta) {
//        this.modifiedMaxHealth = Math.max(0, Math.min(modifiedMaxHealth + delta, trueMaxHealth));
//    }
//
//    public void setAttribute(MedicalAttribute type, float value) {
//        attributes.put(type, value);
//    }
//
//    public void addTag(CompartmentTag tag) {
//        compartmentTags.add(tag);
//    }
//
//    public CompartmentInstance getParent(MedicalStats medicalStats) {
//        return null;
//    }
//
//    public HashSet<UUID> getChildren() {
//        return null;
//    }
//
//    public void setParent(UUID uuid) {
//
//    }
//
//    public UUID getParentID() {
//        return null;
//    }
//
//    public void initializeWithParent(CompartmentInstance uuid) {
//
//    }
//
//    public boolean areChildrenEmpty(MedicalStats medicalStats) {
//        return false;
//    }
}
