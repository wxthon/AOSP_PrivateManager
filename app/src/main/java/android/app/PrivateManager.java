package android.app;

import java.util.List;

public class PrivateManager {

    public List<String> getBlackList() {
        return null;
    }

    public void setAutoStartPackage(String packageName, boolean disable) {}

    public boolean allowAutoStart(String packageName) {
        return false;
    }

    public void smartClean() {}

}