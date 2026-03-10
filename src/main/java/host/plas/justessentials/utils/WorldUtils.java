package host.plas.justessentials.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

public class WorldUtils {
    public static World getWorld(String name) {
        return Bukkit.getWorld(name);
    }

    public static Location getTopLocationMax(Location location) {
        return location.getWorld().getHighestBlockAt(location).getLocation();
    }

    public static Location getTopLocation(Location location) {
        World world = location.getWorld();

        Block block = location.getBlock();
        int y = block.getY();

        Block tempBlock = block;
        int tempY = y;

        while (! block.getType().isAir()) {
            if (! isGreaterThanMinHeight(y, world)) {
                y = tempY;
                block = tempBlock;
                break;
            }

            block = block.getRelative(0, -1, 0);
            y = block.getY();
        }

        if (tempY != y) {
            return tempBlock.getLocation();
        }

        while (block.getType().isAir()) {
            if (! isLessThanMaxHeight(y, world)) {
                return location;
            }

            block = block.getRelative(0, 1, 0);
            y = block.getY();
        }

        Block topBlock = block.getRelative(0, 1, 0);
        while (! topBlock.getType().isAir()) {
            if (! isLessThanMaxHeight(y, world)) {
                return topBlock.getLocation().add(0, 1, 0);
            }

            topBlock = topBlock.getRelative(0, 1, 0);
            y = topBlock.getY();
        }

        return topBlock.getLocation();
    }

    public static boolean isLessThanMaxHeight(double y, World world) {
        return y < world.getMaxHeight();
    }

    public static boolean isGreaterThanMinHeight(double y, World world) {
        return y > world.getMinHeight();
    }

    public static boolean isLessThanMaxHeight(Location location) {
        return isLessThanMaxHeight(location.getY(), location.getWorld());
    }

    public static boolean isGreaterThanMinHeight(Location location) {
        return isGreaterThanMinHeight(location.getY(), location.getWorld());
    }
}
