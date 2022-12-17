package ml.mops.base.maps;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapManager {
    static public List<Map> getPlayableMaps() {

        return new ArrayList<>(Arrays.asList(Map.values()));
    }
}
