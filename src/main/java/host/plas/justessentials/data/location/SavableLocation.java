package host.plas.justessentials.data.location;

import host.plas.bou.scheduling.TaskManager;
import host.plas.justessentials.utils.WorldUtils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import tv.quaint.thebase.lib.re2j.Matcher;
import tv.quaint.utils.MatcherUtils;

import java.util.List;

@Getter @Setter
public class SavableLocation implements Comparable<SavableLocation> {
    private String world;
    private double x, y, z;
    private float yaw, pitch;

    public SavableLocation(String world, double x, double y, double z, float yaw, float pitch) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public SavableLocation(World world, double x, double y, double z, float yaw, float pitch) {
        this(world.getName(), x, y, z, yaw, pitch);
    }

    public SavableLocation(String world, double x, double y, double z) {
        this(world, x, y, z, 0, 0);
    }

    public SavableLocation(World world, double x, double y, double z) {
        this(world.getName(), x, y, z, 0, 0);
    }

    public SavableLocation(Location location) {
        this(location.getWorld(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

    public SavableLocation(String string) {
        fromString(string);
    }

    public String asString() {
        return "[" + world + "; " + x + "; " + y + "; " + z + "; " + yaw + "; " + pitch + ";]";
    }

    public Location asLocation() {
        return new Location(WorldUtils.getWorld(world), x, y, z, yaw, pitch);
    }

    public void fromString(String string) {
        string = string.replace(" ", ""); // Remove spaces

        Matcher matcher = MatcherUtils.matcherBuilder("\\[(.*?);(.*?);(.*?);(.*?);(.*?);(.*?);\\]", string);
        List<String[]> groups = MatcherUtils.getGroups(matcher, 6);

        for (String[] group : groups) {
            world = group[0];
            x = Double.parseDouble(group[1]);
            y = Double.parseDouble(group[2]);
            z = Double.parseDouble(group[3]);
            yaw = Float.parseFloat(group[4]);
            pitch = Float.parseFloat(group[5]);
        }
    }

    public void teleport(Entity entity) {
        TaskManager.runTask(entity, () -> {
            try {
                TaskManager.teleport(entity, asLocation());
            } catch (Exception e) {
                try {
                    entity.teleportAsync(asLocation());
                } catch (Exception e2) {
                    entity.teleport(asLocation());
                }
            }
        });
    }

    @Override
    public int compareTo(@NotNull SavableLocation o) {
        if (o.world.equals(world)) {
            if (o.x == x) {
                if (o.y == y) {
                    if (o.z == z) {
                        if (o.yaw == yaw) {
                            if (o.pitch == pitch) {
                                return 0;
                            } else {
                                return Float.compare(o.pitch, pitch);
                            }
                        } else {
                            return Float.compare(o.yaw, yaw);
                        }
                    } else {
                        return Double.compare(o.z, z);
                    }
                } else {
                    return Double.compare(o.y, y);
                }
            } else {
                return Double.compare(o.x, x);
            }
        } else {
            return world.compareTo(o.world);
        }
    }
}
