package World;

public class Map {
    public TileType[] tileTypes;
    public Tile[][] layout;

    public Map(){
        tileTypes = null;
        layout = null;
    }

    public boolean isValid(){
        for(Tile[] row : layout){
            for(Tile tile : row){
                if(tile == null) return false;
            }
        }
        return true;
    }
}
