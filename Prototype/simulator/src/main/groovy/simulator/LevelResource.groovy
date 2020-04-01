package simulator

class LevelResource implements Resource {
    private String name
    private String hash
    private byte[] content
    private File sourceFile
    private int version

    LevelResource(String _name, int _version, byte[] _content) {
        name = _name
        content = _content
        version = _version
        sourceFile = null
    }

    LevelResource(String _name, int _version, File srcFile) {
        name = _name
        sourceFile = srcFile
        version = _version
        content = srcFile.readBytes()
    }

    @Override
    String getName() {
        return name
    }

    @Override
    int getVersion() {
        return version
    }

    @Override
    File getFile() {
        return sourceFile
    }

    @Override
    void setFile(File file) {
        sourceFile = file
    }

    @Override
    void writeFile() {
        if (file != null) {
            file.write()
        }
    }

    @Override
    byte[] getContent() {
        return content
    }
}
