package com.site21.bittermelon.common.systems.embeddedobject;

public record EmbeddedObject(String modelPart, int cube, float midX, float midY, float midZ) {
    private static float snapToFace(float value) {
        return value > 0.5F ? 1.0F : 0.5F;
    }

//    public static EmbeddedObject createEmbeddedObject(
//            String bodyPart,
//            ModelPart.Cube cube,
//            PlacementStyle placementStyle,
//            RandomSource random
//    ) {
//        float midX = random.nextFloat();
//        float midY = random.nextFloat();
//        float midZ = random.nextFloat();
//        int plane = -1;
//
//        if (placementStyle == PlacementStyle.ON_SURFACE) {
//            plane = random.nextInt(3);
//            switch (plane) {
//                case 0 -> midX = snapToFace(midX);
//                case 1 -> midY = snapToFace(midY);
//                default -> midZ = snapToFace(midZ);
//            }
//        }
//
//        return new EmbeddedObject(bodyPart, midX, midY, midZ, plane, UUID.randomUUID());
//    }

    public static enum PlacementStyle {
        IN_CUBE,
        ON_SURFACE;
    }
}
