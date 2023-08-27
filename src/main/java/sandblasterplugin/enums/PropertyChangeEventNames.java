package sandblasterplugin.enums;

public enum PropertyChangeEventNames {
    
    PYTHON2_BIN_PATH_UPDATED("PYTHON2BINPATH_UPDATED"),
    PYTHON3_BIN_PATH_UPDATED("PYTHON3BINPATH_UPDATED"),
    
    IOS_VERSION_VALUE_UPDATED("IOS_VERSION_VALUE_UPDATED"),
    SANDBOX_OPERATIONS_FILE_PATH_UPDATED("SANDBOX_OPERATIONS_FILE_PATH_UPDATED"),
    SANDBOX_PROFILES_FILE_PATH_UPDATED("SANDBOX_PROFILES_FILE_PATH_UPDATED"),
    OUT_DIR_PATH_UPDATED("OUT_DIR_PATH_UPDATED"),
    
    FILE_CONTENT_UPDATED("FILE_CONTENT_UPDATED"),
	TREE_ROOT_NODE_UPDATED("TREE_ROOT_NODE_UPDATED");
    
    private final String eventName;

    PropertyChangeEventNames(String eventName) {
        this.eventName = eventName;
    }

    public String getEventName() {
        return eventName;
    }
}
