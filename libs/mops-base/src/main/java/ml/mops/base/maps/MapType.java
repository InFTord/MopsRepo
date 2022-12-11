package ml.mops.base.maps;

public enum MapType {
    PVP("pvp");

    final String filePath;

    MapType (String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }
}
