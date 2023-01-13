package ml.mops.base.structures;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.util.EulerAngle;

public class MopsStructureBlock {
    public boolean enchanted = false;
    public Material block = Material.DIAMOND_BLOCK;
    public EulerAngle angle = new EulerAngle(0, 0, 0);
}
