package host.plas.justessentials.holders;

import host.plas.bou.compat.CompatibilityManager;

public class CompatManager extends CompatibilityManager {
    private static CompatManager instance;

    public static CompatManager get() {
        if (instance == null) {
            instance = new CompatManager();
        }

        return instance;
    }

    public void init() {
    }
}
