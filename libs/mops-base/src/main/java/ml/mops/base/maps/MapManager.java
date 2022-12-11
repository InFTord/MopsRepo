package ml.mops.base.maps;

import java.util.ArrayList;
import java.util.List;

public class MapManager {

    static public List<Map> getMaps() {
        List<Map> maps = new ArrayList<>();
        maps.add(Map.DESERT);
        maps.add(Map.PLAINS);
        maps.add(Map.AQUA);
        maps.add(Map.TAIGA);
        maps.add(Map.CITY);
        maps.add(Map.MOPSFORT);

        return maps;
    }
}
