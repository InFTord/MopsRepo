package ml.mops.base.structures;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.util.EulerAngle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MopsStructure {
    Location center;
    List<MopsStructureBlock> blockList;
    HashMap<MopsStructureBlock, Location> offset;
    HashMap<MopsStructureBlock, EulerAngle> angle;

    World world;

    List<ArmorStand> standList = new ArrayList<>();

    MopsStructure(World thisWorld, Location thisCenter, List<MopsStructureBlock> thisBlockList, HashMap<MopsStructureBlock, Location> thisOffset, HashMap<MopsStructureBlock, EulerAngle> thisAngle) {
        world = thisWorld;
        center = thisCenter;
        blockList = thisBlockList;
        offset = thisOffset;
        angle = thisAngle;
    }

    public void summonStructure() {
        for (MopsStructureBlock block : blockList) {
            ArmorStand stand = (ArmorStand) world.spawnEntity(center.add(offset.get(block)), EntityType.ARMOR_STAND);

            stand.getEquipment().setHelmet(block.getBlock());
            stand.setHeadPose(angle.get(block));

            standList.add(stand);
        }
    }

    public void rotate(int x, int y, int z) {

    }
}
