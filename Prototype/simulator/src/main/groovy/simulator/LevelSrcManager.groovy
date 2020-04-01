package simulator

interface LevelSrcManager {

    Class getLevel(String name);

    Resource getLevelSrc(String name);

    boolean saveLevel(Resource source, ArrayList<Resource> dep);

    boolean addResource(String levelName, Resource dep);

    boolean deleteLevel(String levelName)

    List<Resource> listLevels();
}