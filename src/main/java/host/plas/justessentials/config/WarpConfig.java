package host.plas.justessentials.config;

import host.plas.justessentials.JustEssentials;
import host.plas.justessentials.data.location.SavableLocation;
import host.plas.justessentials.data.warps.Warp;
import host.plas.justessentials.data.warps.WarpManager;
import host.plas.justessentials.data.warps.WarpType;
import tv.quaint.storage.resources.flat.simple.SimpleConfiguration;

import java.util.concurrent.ConcurrentSkipListSet;

public class WarpConfig extends SimpleConfiguration {
    public WarpConfig() {
        super("warps.yml", JustEssentials.getInstance(), false);
    }

    @Override
    public void init() {
        reloadConfig();
    }

    public void reloadConfig() {
        WarpManager.flushWarps();
        ConcurrentSkipListSet<Warp> warps = getSavedWarps();
        WarpManager.loadAllWarps(warps);
    }

    public ConcurrentSkipListSet<Warp> getSavedWarps() {
        reloadResource();

        String path = "saved";

        ConcurrentSkipListSet<Warp> warps = new ConcurrentSkipListSet<>();

        singleLayerKeySet(path).forEach(key -> {
            String path2 = path + "." + key;

            String savedLocation = getOrSetDefault(path2 + ".location", "[world; 0; 100; 0; 0; 0;]");
            String type = getOrSetDefault(path2 + ".type", WarpType.ADMIN.name());

            SavableLocation location = null;
            try {
                location = new SavableLocation(savedLocation);
            } catch (Exception e) {
                JustEssentials.getInstance().logWarning("Failed to load warp location: " + key);
                JustEssentials.getInstance().logWarning(e);
                return;
            }

            WarpType warpType = null;
            try {
                warpType = WarpType.valueOf(type);
            } catch (Exception e) {
                JustEssentials.getInstance().logWarning("Failed to load warp type: " + key);
                JustEssentials.getInstance().logWarning(e);
                return;
            }

            Warp warp = new Warp(key, location, warpType);

            warps.add(warp);
        });

        return warps;
    }

    public void saveWarp(Warp warp) {
        String path = "saved." + warp.getIdentifier();

        write(path + ".location", warp.getLocation().asString());
        write(path + ".type", warp.getType().name());
    }

    public void deleteWarp(Warp warp) {
        getResource().remove("saved." + warp.getIdentifier());
    }
}
