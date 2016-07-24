import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michau on 16.05.2015.
 */
public class LevelList implements Serializable{
    /**
     * Serial Version used for checking compatibility with {@link LevelList} on client's side
     */
    static final long serialVersionUID = 1;

    public List levelList;

    /**
     * Parametrised {@link LevelList} constructor
     * @param list List of levels
     */
    public LevelList(List list) {
        levelList = list;
    }

    /**
     * Default {@link LevelList} constructor
     */
    public LevelList() {
        levelList = new ArrayList<>();
    }
}
