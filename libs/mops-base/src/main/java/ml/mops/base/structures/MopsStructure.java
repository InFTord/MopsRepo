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
    Location center = null;
    List<MopsStructureBlock> blockList = new ArrayList<>();
    HashMap<MopsStructureBlock, Location> offset = new HashMap<>();
    HashMap<MopsStructureBlock, EulerAngle> angle = new HashMap<>();

    World world = null;

    List<ArmorStand> standList = new ArrayList<>();

    MopsStructure(World thisWorld, Location thisCenter, List<MopsStructureBlock> thisBlockList, HashMap<MopsStructureBlock, Location> thisOffset, HashMap<MopsStructureBlock, EulerAngle> thisAngle) {
        world = thisWorld;
        center = thisCenter;
        blockList = thisBlockList;
        offset = thisOffset;
        angle = thisAngle;
    }

    public void summonStructure() {
        for(MopsStructureBlock block : blockList) {
            ArmorStand stand = (ArmorStand) world.spawnEntity(center.add(offset.get(block)), EntityType.ARMOR_STAND);

            stand.setHelmet(block.getBlock());
            stand.setHeadPose(angle.get(block));

            standList.add(stand);
        }
    }

    public void rotate(int x, int y, int z) {

    }
}
