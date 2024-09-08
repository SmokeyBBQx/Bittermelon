package net.smokeybbq.bittermelon.systems.atmospherics;

public class AtmosManagerV2 {
//    private void equalizeGasses(BlockPos pos, AtmosCell cell) {
//        Map<Substance, Float> totalGasses = new HashMap<>(cell.getGasses());
//        List<AtmosCell> cellsToEqualize = new ArrayList<>();
//        cellsToEqualize.add(cell);
//
//        for (AtmosCell neighbor : getNeighbors(pos)) {
//            if (Math.abs(neighbor.getTotalAmount() - cell.getTotalAmount()) > 0.001f) {
//                cellsToEqualize.add(neighbor);
//                for (Map.Entry<Substance, Float> entry : neighbor.getGasses().entrySet()) {
//                    mergeSubstances(totalGasses, entry.getKey(), entry.getValue());
//                }
//            }
//        }
//
//        if (cellsToEqualize.size() > 1) {
//            Map<Substance, Float> equalizedGasses = new HashMap<>();
//            for (Map.Entry<Substance, Float> entry : totalGasses.entrySet()) {
//                equalizedGasses.put(entry.getKey(), entry.getValue() / cellsToEqualize.size());
//            }
//
//            for (AtmosCell cellToEqualize : cellsToEqualize) {
//                cellToEqualize.setGasses(new HashMap<>(equalizedGasses));
//            }
//        }
//    }
}
