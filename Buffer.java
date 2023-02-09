public class Buffer {
    // Lots of stuff goes here
    private String filespec;
    private boolean dirty;
    DLList<String> lists;

    public Buffer() {
        lists = new DLList<String>();
        dirty = false;
        filespec = "";
    }

    public Buffer(DLList<String> lists) {
        this.lists = lists;
    }

    public String getFilespec() {
        return filespec;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    public void setFilespec(String filespec) {
        this.filespec = filespec;
    }

    public boolean isEmpty() {
        return lists.isEmpty();
    }
}