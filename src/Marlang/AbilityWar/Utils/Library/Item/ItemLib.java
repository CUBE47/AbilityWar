package Marlang.AbilityWar.Utils.Library.Item;

import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import Marlang.AbilityWar.Utils.VersionCompat.ServerVersion;

public class ItemLib {
	
	public static ColouredItem WOOL = new ColouredItem() {
		
		@Override
		public String getName() {
			return "WOOL";
		}
		
	};
	
	abstract public static class ColouredItem {
		
		private ColouredItem() {}
		
		/**
		 * Material �̸�
		 */
		abstract public String getName();
		
		public ItemStack getItemStack(ItemColor color) {
			if(ServerVersion.getVersion() >= 13) {
				Material material = Material.valueOf(color.toString() + "_" + getName());
				return new ItemStack(material);
			} else {
				Material material = Material.valueOf(getName());
				return new ItemStack(material, 1, color.getDamage());
			}
		}

		public boolean compareType(Material material) {
			if(ServerVersion.getVersion() >= 13) {
				String name = material.toString();
				for(ItemColor color : ItemColor.values()) {
					name = name.replaceAll(color.toString() + "_", "");
				}
				
				return name.equalsIgnoreCase(getName());
			} else {
				return material.toString().equalsIgnoreCase(getName());
			}
		}
		
	}

	public static enum ItemColor {

		/**
		 * �Ͼ��
		 */
		WHITE((short) 0),
		/**
		 * ��Ȳ��
		 */
		ORANGE((short) 1),
		/**
		 * ��ȫ��
		 */
		MAGENTA((short) 2),
		/**
		 * �ϴû�
		 */
		LIGHT_BLUE((short) 3),
		/**
		 * �����
		 */
		YELLOW((short) 4),
		/**
		 * ���λ�
		 */
		LIME((short) 5),
		/**
		 * ��ȫ��
		 */
		PINK((short) 6),
		/**
		 * ȸ��
		 */
		GRAY((short) 7),
		/**
		 * ȸ���
		 */
		LIGHT_GRAY((short) 8),
		/**
		 * û�ϻ�
		 */
		CYAN((short) 9),
		/**
		 * �����
		 */
		PURPLE((short) 10),
		/**
		 * �Ķ���
		 */
		BLUE((short) 11),
		/**
		 * ����
		 */
		BROWN((short) 12),
		/**
		 * �ʷϻ�
		 */
		GREEN((short) 13),
		/**
		 * ������
		 */
		RED((short) 14),
		/**
		 * ������
		 */
		BLACK((short) 15),
		/**
		 * ������ ���� ��� ���Ǵ� ���Դϴ�.
		 */
		ERROR((short) -1);

		private short damage;

		private ItemColor(short damage) {
			this.damage = damage;
		}

		public short getDamage() {
			return damage;
		}
		
		/**
		 * Damage�� ItemColor�� �޾ƿɴϴ�.
		 * �ش��ϴ� ItemColor�� ���� ��� ERROR�� ��ȯ�մϴ�.
		 */
		public static ItemColor getByDamage(short damage) {
			for(ItemColor color : ItemColor.values()) {
				if(color.getDamage() == damage) {
					return color;
				}
			}
			
			return ItemColor.ERROR;
		}
		
	}
	
	@SuppressWarnings("deprecation")
	public static SkullMeta setOwner(SkullMeta meta, String Player) {
		if(ServerVersion.getVersion() >= 13) {
			meta.setOwningPlayer(new OfflinePlayer() {
				
				@Override
				public Map<String, Object> serialize() {
					return null;
				}
				
				@Override
				public void setOp(boolean value) {}
				
				@Override
				public boolean isOp() {
					return false;
				}
				
				@Override
				public void setWhitelisted(boolean value) {}
				
				@Override
				public boolean isWhitelisted() {
					return false;
				}
				
				@Override
				public boolean isOnline() {
					return false;
				}
				
				@Override
				public boolean isBanned() {
					return false;
				}
				
				@Override
				public boolean hasPlayedBefore() {
					return false;
				}
				
				@Override
				public UUID getUniqueId() {
					return null;
				}
				
				@Override
				public org.bukkit.entity.Player getPlayer() {
					// TODO Auto-generated method stub
					return null;
				}
				
				@Override
				public String getName() {
					return Player;
				}
				
				@Override
				public long getLastPlayed() {
					return 0;
				}
				
				@Override
				public long getFirstPlayed() {
					return 0;
				}
				
				@Override
				public Location getBedSpawnLocation() {
					return null;
				}
			});
		} else {
			meta.setOwner(Player);
		}
		
		return meta;
	}
	
	public static ItemStack getHead(String owner) {
		ItemStack item = MaterialLib.PLAYER_HEAD.getItem();
		
		SkullMeta meta = (SkullMeta) item.getItemMeta();
		item.setItemMeta(setOwner(meta, owner));
		
		return item;
	}
	
}
