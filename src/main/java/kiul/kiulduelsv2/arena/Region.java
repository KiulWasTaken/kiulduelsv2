package kiul.kiulduelsv2.arena;

import org.bukkit.Location;
import org.bukkit.util.Vector;


public class Region
{
    private final Vector minimum;
    private final Vector maximum;

    private final Vector center;

    public Region(Vector firstPoint, Vector secondPoint)
    {
        this.minimum = Vector.getMinimum(firstPoint, secondPoint);
        this.maximum = Vector.getMaximum(firstPoint, secondPoint);
        this.center = minimum.getMidpoint(maximum);
    }

    public Vector getCenter()
    {
        return center;
    }

    public boolean contains(Location location)
    {
        return location.toVector().isInAABB(minimum, maximum);

    }
}
