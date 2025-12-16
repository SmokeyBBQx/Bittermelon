package com.site21.bittermelon.common.systems.medical.compartment;

import java.awt.*;

/**
 * Predicate for extracting/excising compartment.
 * @param layer the layer index
 * @param point the point within the layer
 */
public record ExcisionPredicate(int layer, Point point) {
}
