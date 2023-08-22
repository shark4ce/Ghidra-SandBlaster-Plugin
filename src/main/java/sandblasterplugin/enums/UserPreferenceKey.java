package sandblasterplugin.enums;

public enum UserPreferenceKey {
	
    PYTHON2_BIN_PATH("PYTHON2_BIN_PATH"),
    PYTHON3_BIN_PATH("PYTHON3_BIN_PATH"),
    OUT_DIR_PATH("OUT_DIR_PATH");
    
    private final String keyName;

    UserPreferenceKey(String keyName) {
        this.keyName = keyName;
    }

    public String getKeyName() {
        return keyName;
    }
}
