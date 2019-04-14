package DayBreak.AbilityWar.Utils;

import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

import DayBreak.AbilityWar.AbilityWar;
import DayBreak.AbilityWar.Utils.VersionCompat.ServerVersion;

/**
 * FallingBlock�� ���� ���ϰ� ����ϱ� ���� ���� ��ƿ�Դϴ�.
 * @author DayBreak ����
 */
public abstract class FallBlock implements Listener {

	private Object Data = null;
	private Location location;
	private World world;
	private Vector vector = new Vector(0, 0, 0);

	/**
	 * Fallblock�� �⺻ �������Դϴ�.
	 * @param Data		������ FallingBlock�� ����
	 * @param location	������ ��ġ
	 */
	public FallBlock(Material Data, Location location) {
		if(ServerVersion.getVersion() >= 13) {
			try {
				Method method = Material.class.getDeclaredMethod("createBlockData");
				this.Data = method.invoke(Data);
			} catch(Exception ex) {ex.printStackTrace();}
		} else {
			this.Data = new MaterialData(Data);
		}
		this.location = location;
		this.world = location.getWorld();
	}

	/**
	 * Fallblock�� �⺻ �������Դϴ�.
	 * @param Data		������ FallingBlock�� ����
	 * @param location	������ ��ġ
	 * @param vector	������ �� ������ ����
	 */
	public FallBlock(Material Data, Location location, Vector vector) {
		if(ServerVersion.getVersion() >= 13) {
			try {
				Method method = Material.class.getDeclaredMethod("createBlockData");
				this.Data = method.invoke(Data);
			} catch(Exception ex) {}
		} else {
			this.Data = new MaterialData(Data);
		}
		this.location = location;
		this.world = location.getWorld();
		this.vector = vector;
	}
	
	/**
	 * FallinBlock�� �����մϴ�.
	 * @param setBlock 	FallingBlock�� ���� �������� �� ��� ��ġ���� ��ġ���� ����
	 * @return 			������ FallingBlock
	 * 					FallingBlock�� �������� ������ ��� null ��ȯ
	 */
	@SuppressWarnings("deprecation")
	public FallingBlock Spawn(boolean setBlock) {
		if(ServerVersion.getVersion() >= 13) {
			try {
				Class<?> blockDataClass = Class.forName("org.bukkit.block.data.BlockData");
				Method spawnFallingBlock = World.class.getDeclaredMethod("spawnFallingBlock", Location.class, blockDataClass);
				fb = (FallingBlock) spawnFallingBlock.invoke(world, location, blockDataClass.cast(Data));
			} catch(Exception ex) {}
		} else if(ServerVersion.getVersion() >= 11) {
			fb = world.spawnFallingBlock(location, (MaterialData) Data);
		} else {
			fb = world.spawnFallingBlock(location, ((MaterialData) Data).getItemType(), ((MaterialData) Data).getData());
		}
		
		this.setBlock = setBlock;
		Bukkit.getPluginManager().registerEvents(this, AbilityWar.getPlugin());
		
		if(ServerVersion.getVersion() >= 9) {
			fb.setInvulnerable(true);
		}
		
		fb.setDropItem(false);
		
		fb.setVelocity(vector);
		
		return fb;
	}
	
	private boolean setBlock;
	private FallingBlock fb;
	
	/**
	 * FallingBlock ������ ��ƼƼ�� ���� �������� �� ȣ��˴ϴ�.
	 * @param block	FallingBlock ��ƼƼ
	 */
	abstract public void onChangeBlock(FallingBlock block);
	
	/**
	 * FallingBlock�� ���� �������� �� ��� ��ġ ĵ�� �� onChangeBlock() ȣ���� ���� ���˴ϴ�.
	 */
	@EventHandler
	public void onEntityChangeBlock(EntityChangeBlockEvent e) {
		if(fb.equals(e.getEntity())) {
			onChangeBlock(fb);
			if(!setBlock) {
				e.setCancelled(true);
				e.getEntity().remove();
			}
			
			HandlerList.unregisterAll(this);
		}
	}
	
}
