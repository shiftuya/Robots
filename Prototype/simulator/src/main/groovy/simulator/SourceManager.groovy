package simulator

import groovy.transform.CompileStatic

import java.util.stream.Collectors

@CompileStatic
class SourceManager implements LevelSrcManager {
    private HashMap<String, Resource> levels
    private String path
    private GroovyClassLoader gcl

    SourceManager(String _path) {
        path = _path
        gcl = new GroovyClassLoader()
        levels = new HashMap<>()
        File dir = new File(path)
        File[] contents = dir.listFiles()
        for (File cont : contents) {
            File src = new File(path + "/" + cont.getName() + "/" + cont.getName() + "_lvl.groovy")
            try {
                Resource lvlSrc = new LevelResource(cont.getName(), 1, src)
                levels.put(cont.getName(), lvlSrc)
            } catch (IOException e) {
                System.err.println(e.toString())
            }
        }
    }

    @Override
    synchronized Class getLevel(String name) {
        Resource lvlSrc = levels.get(name)
        if (lvlSrc == null)
            return null
        def levelClass = gcl.parseClass(lvlSrc.file)
        return levelClass
    }

    @Override
    synchronized Resource getLevelSrc(String name) {
        return levels.get(name)
    }

    private boolean writeFile(Resource res, String name, String lvlName) {
        File storage = new File(path + "/" + lvlName + "/" + name)
        try {
            storage.getParentFile().mkdirs()
            storage.createNewFile()
            FileOutputStream fos = new FileOutputStream(storage)
            fos.write(res.getContent())
            fos.close()
            res.setFile(storage)
        } catch (Exception ignore) {
            return false
        }
        return true
    }

    private boolean writeFile(Resource res, String lvlName) {
        return writeFile(res, res.getName(), lvlName)
    }


    @Override
    synchronized boolean saveLevel(Resource source, ArrayList<Resource> dep) {
        Resource prev = levels.get(source.getName())
        if ((prev == null || prev.getVersion() <= source.getVersion()) && writeFile(source, source.getName() + "_lvl.groovy", source.getName())) {
            if (dep != null) {
                for (Resource res : dep) {
                    if (!writeFile(res, source.getName())) {
                        return false
                    }
                }
            }
            levels.put(source.getName(), source)
            return true
        }
        return false
    }

    @Override
    synchronized boolean addResource(String levelName, Resource dep) {
        if (levels.get(levelName) == null)
            return false
        return writeFile(dep, levelName)
    }

    @Override
    synchronized boolean deleteLevel(String levelName) {
        levels.remove(levelName)
        return deleteDirectory(new File(path + "/" + levelName))
    }

    @Override
    synchronized List<Resource> listLevels() {
        return levels.entrySet().stream().map({ x -> x.getValue() }).collect(Collectors.toList())
    }

    private boolean deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles()
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file)
            }
        }
        return directoryToBeDeleted.delete()
    }
}
