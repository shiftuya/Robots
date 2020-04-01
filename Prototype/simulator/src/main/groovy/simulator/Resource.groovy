package simulator

interface Resource {
    String getName()

    int getVersion()

    File getFile()

    byte[] getContent()

    void writeFile()

    void setFile(File file)
}