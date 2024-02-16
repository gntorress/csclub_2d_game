package World;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

public class Map {
    public TileType[] tileTypes;
    public Tile[][] layout;

    public Map(){
        tileTypes = null;
        layout = null;
    }

    public boolean isValid(){
        return Arrays.stream(layout).flatMap(Arrays::stream).allMatch(Objects::nonNull);
        /*
        for(Tile[] row : layout){
            for(Tile tile : row){
                if(tile == null) return false;
            }
        }
        return true;
        */
    }
}