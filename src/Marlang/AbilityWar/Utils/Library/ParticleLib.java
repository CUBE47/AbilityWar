/** MIT ���̼���
* 
* Copyright �� 2019 _Marlang
* 
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"),
* to deal in the Software without restriction, including without limitation 
* the rights to use, copy, modify, merge, publish, distribute, sublicense, 
* and/or sell copies of the Software, and to permit persons to whom the 
* Software is furnished to do so, subject to the following conditions:
*
* The above copyright notice and this permission notice shall be included 
* in all copies or substantial portions of the Software.
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, 
* EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES 
* OF * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. 
* IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, 
* DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
* ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
* DEALINGS IN THE SOFTWARE.
**/

package Marlang.AbilityWar.Utils.Library;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Particle;

import Marlang.AbilityWar.Config.AbilityWarSettings;
import Marlang.AbilityWar.Utils.Library.Packet.ParticlePacket;
import Marlang.AbilityWar.Utils.VersionCompat.ServerVersion;

/**
 * ��ƼŬ ���̺귯��
 * @author _Marlang ����
 * @since 2019-02-19
 * @version 1.1
 */
public class ParticleLib {
	
	private ParticleLib() {}
	
	public static Particles BARRIER = new Particles("BARRIER", "BARRIER", "BARRIER", "BARRIER", "BARRIER", "BARRIER", "");
	public static Particles BLOCK_CRACK = new Particles("BLOCK_CRACK", "BLOCK_CRACK", "BLOCK_CRACK", "BLOCK_CRACK", "BLOCK_CRACK", "BLOCK_CRACK", "");
	public static Particles BLOCK_DUST = new Particles("BLOCK_DUST", "BLOCK_DUST", "BLOCK_DUST", "BLOCK_DUST", "BLOCK_DUST", "BLOCK_DUST", "");
	public static Particles BUBBLE_COLUMN_UP = new Particles("BUBBLE_COLUMN_UP", "", "", "", "", "", "");
	public static Particles BUBBLE_POP = new Particles("BUBBLE_POP", "", "", "", "", "", "");
	public static Particles CLOUD = new Particles("CLOUD", "CLOUD", "CLOUD", "CLOUD", "CLOUD", "CLOUD", "");
	public static Particles CRIT = new Particles("CRIT", "CRIT", "CRIT", "CRIT", "CRIT", "CRIT", "");
	public static Particles CRIT_MAGIC = new Particles("CRIT_MAGIC", "CRIT_MAGIC", "CRIT_MAGIC", "CRIT_MAGIC", "CRIT_MAGIC", "CRIT_MAGIC", "");
	public static Particles CURRENT_DOWN = new Particles("CURRENT_DOWN", "", "", "", "", "", "");
	public static Particles DAMAGE_INDICATOR = new Particles("DAMAGE_INDICATOR", "DAMAGE_INDICATOR", "DAMAGE_INDICATOR", "DAMAGE_INDICATOR", "DAMAGE_INDICATOR", "", "");
	public static Particles DOLPHIN = new Particles("DOLPHIN", "", "", "", "", "", "");
	public static Particles DRAGON_BREATH = new Particles("DRAGON_BREATH", "DRAGON_BREATH", "DRAGON_BREATH", "DRAGON_BREATH", "DRAGON_BREATH", "", "");
	public static Particles DRIP_LAVA = new Particles("DRIP_LAVA", "DRIP_LAVA", "DRIP_LAVA", "DRIP_LAVA", "DRIP_LAVA", "DRIP_LAVA", "");
	public static Particles DRIP_WATER = new Particles("DRIP_WATER", "DRIP_WATER", "DRIP_WATER", "DRIP_WATER", "DRIP_WATER", "DRIP_WATER", "");
	public static Particles ENCHANTMENT_TABLE = new Particles("ENCHANTMENT_TABLE", "ENCHANTMENT_TABLE", "ENCHANTMENT_TABLE", "ENCHANTMENT_TABLE", "ENCHANTMENT_TABLE", "ENCHANTMENT_TABLE", "");
	public static Particles END_ROD = new Particles("END_ROD", "END_ROD", "END_ROD", "END_ROD", "END_ROD", "", "");
	public static Particles EXPLOSION_HUGE = new Particles("EXPLOSION_HUGE", "EXPLOSION_HUGE", "EXPLOSION_HUGE", "EXPLOSION_HUGE", "EXPLOSION_HUGE", "EXPLOSION_HUGE", "");
	public static Particles EXPLOSION_LARGE = new Particles("EXPLOSION_LARGE", "EXPLOSION_LARGE", "EXPLOSION_LARGE", "EXPLOSION_LARGE", "EXPLOSION_LARGE", "EXPLOSION_LARGE", "");
	public static Particles EXPLOSION_NORMAL = new Particles("EXPLOSION_NORMAL", "EXPLOSION_NORMAL", "EXPLOSION_NORMAL", "EXPLOSION_NORMAL", "EXPLOSION_NORMAL", "EXPLOSION_NORMAL", "");
	public static Particles FALLING_DUST = new Particles("FALLING_DUST", "FALLING_DUST", "FALLING_DUST", "FALLING_DUST", "", "", "");
	public static Particles FIREWORKS_SPARK = new Particles("FIREWORKS_SPARK", "FIREWORKS_SPARK", "FIREWORKS_SPARK", "FIREWORKS_SPARK", "FIREWORKS_SPARK", "FIREWORKS_SPARK", "");
	public static Particles FLAME = new Particles("FLAME", "FLAME", "FLAME", "FLAME", "FLAME", "FLAME", "MOBSPAWNER_FLAMES");
	public static Particles FOOTSTEP = new Particles("", "FOOTSTEP", "FOOTSTEP", "FOOTSTEP", "FOOTSTEP", "FOOTSTEP", "");
	public static Particles HEART = new Particles("HEART", "HEART", "HEART", "HEART", "HEART", "HEART", "");
	public static Particles ITEM_CRACK = new Particles("ITEM_CRACK", "ITEM_CRACK", "ITEM_CRACK", "ITEM_CRACK", "ITEM_CRACK", "ITEM_CRACK", "");
	public static Particles LAVA = new Particles("LAVA", "LAVA", "LAVA", "LAVA", "LAVA", "LAVA", "");
	public static Particles MOB_APPEARANCE = new Particles("MOB_APPEARANCE", "MOB_APPEARANCE", "MOB_APPEARANCE", "MOB_APPEARANCE", "MOB_APPEARANCE", "MOB_APPEARANCE", "");
	public static Particles NAUTILUS = new Particles("NAUTILUS", "", "", "", "", "", "");
	public static Particles NOTE = new Particles("NOTE", "NOTE", "NOTE", "NOTE", "NOTE", "NOTE", "");
	public static Particles PORTAL = new Particles("PORTAL", "PORTAL", "PORTAL", "PORTAL", "PORTAL", "PORTAL", "");
	public static Particles REDSTONE = new Particles("REDSTONE", "REDSTONE", "REDSTONE", "REDSTONE", "REDSTONE", "REDSTONE", "");
	public static Particles SLIME = new Particles("SLIME", "SLIME", "SLIME", "SLIME", "SLIME", "SLIME", "");
	public static Particles SMOKE_LARGE = new Particles("SMOKE_LARGE", "SMOKE_LARGE", "SMOKE_LARGE", "SMOKE_LARGE", "SMOKE_LARGE", "SMOKE_LARGE", "");
	public static Particles SMOKE_NORMAL = new Particles("SMOKE_NORMAL", "SMOKE_NORMAL", "SMOKE_NORMAL", "SMOKE_NORMAL", "SMOKE_NORMAL", "SMOKE_NORMAL", "SMOKE");
	public static Particles SNOWBALL = new Particles("SNOWBALL", "SNOWBALL", "SNOWBALL", "SNOWBALL", "SNOWBALL", "SNOWBALL", "");
	public static Particles SNOW_SHOVEL = new Particles("SNOW_SHOVEL", "SNOW_SHOVEL", "SNOW_SHOVEL", "SNOW_SHOVEL", "SNOW_SHOVEL", "SNOW_SHOVEL", "");
	public static Particles SPELL = new Particles("SPELL", "SPELL", "SPELL", "SPELL", "SPELL", "SPELL", "");
	public static Particles SPELL_INSTANT = new Particles("SPELL_INSTANT", "SPELL_INSTANT", "SPELL_INSTANT", "SPELL_INSTANT", "SPELL_INSTANT", "SPELL_INSTANT", "");
	public static Particles SPELL_MOB = new Particles("SPELL_MOB", "SPELL_MOB", "SPELL_MOB", "SPELL_MOB", "SPELL_MOB", "SPELL_MOB", "");
	public static Particles SPELL_MOB_AMBIENT = new Particles("SPELL_MOB_AMBIENT", "SPELL_MOB_AMBIENT", "SPELL_MOB_AMBIENT", "SPELL_MOB_AMBIENT", "SPELL_MOB_AMBIENT", "SPELL_MOB_AMBIENT", "");
	public static Particles SPELL_WITCH = new Particles("SPELL_WITCH", "SPELL_WITCH", "SPELL_WITCH", "SPELL_WITCH", "SPELL_WITCH", "SPELL_WITCH", "");
	public static Particles SPIT = new Particles("SPIT", "SPIT", "SPIT", "", "", "", "");
	public static Particles SQUID_INK = new Particles("SQUID_INK", "", "", "", "", "", "");
	public static Particles SUSPENDED = new Particles("SUSPENDED", "SUSPENDED", "SUSPENDED", "SUSPENDED", "SUSPENDED", "SUSPENDED", "");
	public static Particles SUSPENDED_DEPTH = new Particles("SUSPENDED_DEPTH", "SUSPENDED_DEPTH", "SUSPENDED_DEPTH", "SUSPENDED_DEPTH", "SUSPENDED_DEPTH", "SUSPENDED_DEPTH", "");
	public static Particles SWEEP_ATTACK = new Particles("SWEEP_ATTACK", "SWEEP_ATTACK", "SWEEP_ATTACK", "SWEEP_ATTACK", "SWEEP_ATTACK", "", "");
	public static Particles TOTEM = new Particles("TOTEM", "TOTEM", "TOTEM", "", "", "", "");
	public static Particles TOWN_AURA = new Particles("TOWN_AURA", "TOWN_AURA", "TOWN_AURA", "TOWN_AURA", "TOWN_AURA", "TOWN_AURA", "");
	public static Particles VILLAGER_ANGRY = new Particles("VILLAGER_ANGRY", "VILLAGER_ANGRY", "VILLAGER_ANGRY", "VILLAGER_ANGRY", "VILLAGER_ANGRY", "VILLAGER_ANGRY", "");
	public static Particles VILLAGER_HAPPY = new Particles("VILLAGER_HAPPY", "VILLAGER_HAPPY", "VILLAGER_HAPPY", "VILLAGER_HAPPY", "VILLAGER_HAPPY", "VILLAGER_HAPPY", "");
	public static Particles WATER_BUBBLE = new Particles("WATER_BUBBLE", "WATER_BUBBLE", "WATER_BUBBLE", "WATER_BUBBLE", "WATER_BUBBLE", "WATER_BUBBLE", "");
	public static Particles WATER_DROP = new Particles("WATER_DROP", "WATER_DROP", "WATER_DROP", "WATER_DROP", "WATER_DROP", "WATER_DROP", "");
	public static Particles WATER_SPLASH = new Particles("WATER_SPLASH", "WATER_SPLASH", "WATER_SPLASH", "WATER_SPLASH", "WATER_SPLASH", "WATER_SPLASH", "");
	public static Particles WATER_WAKE = new Particles("WATER_WAKE", "WATER_WAKE", "WATER_WAKE", "WATER_WAKE", "WATER_WAKE", "WATER_WAKE", "");
	
	public static class Particles {
		
		private String particleName = "";
		private Particle particle = null;
		private Effect effect = null;

		private Particles(String Name13, String Name12, String Name11, String Name10, String Name9, String Name8, String Name7) {
			switch (ServerVersion.getVersion()) {
			case 13:
				particleName = Name13;
				particle = getParticle();
				break;
			case 12:
				particleName = Name12;
				particle = getParticle();
				break;
			case 11:
				particleName = Name11;
				particle = getParticle();
				break;
			case 10:
				particleName = Name10;
				particle = getParticle();
				break;
			case 9:
				particleName = Name9;
				particle = getParticle();
				break;
			case 8:
				particleName = Name8;
				break;
			case 7:
				effect = getEffect();
				particleName = Name7;
				break;
			}
		}

		/**
		 * 1.9���� �̻�
		 */
		private Particle getParticle() {
			Particle particle = null;
			
			for (Particle p : Particle.values()) {
				if (p.toString().equalsIgnoreCase(particleName)) {
					particle = p;
				}
			}
			
			return particle;
		}
		
		/**
		 * 1.7.10���� ����
		 */
		private Effect getEffect() {
			Effect effect = null;
			
			for (Effect e : Effect.values()) {
				if (e.toString().equalsIgnoreCase(particleName)) {
					effect = e;
				}
			}
			
			return effect;
		}
		
		public void spawnParticle(Location l, int Count, double offsetX, double offsetY, double offsetZ) {
			if (AbilityWarSettings.getVisualEffect()) {
				if(ServerVersion.getVersion() >= 9) {
					Particle p = particle;
					if (p != null) {
						l.getWorld().spawnParticle(p, l, Count, offsetX, offsetY, offsetZ);
					}
				} else if (ServerVersion.getVersion() == 8) {
					ParticlePacket packet = new ParticlePacket(particleName, l, (float) offsetX, (float) offsetY, (float) offsetZ, Count);
					packet.Broadcast();
				} else if (ServerVersion.getVersion() <= 7) {
					Effect e = effect;
					if (e != null) {
						l.getWorld().playEffect(l, e, Count);
					}
				}
			}
		}

		public void spawnParticle(Location l, int Count, double offsetX, double offsetY, double offsetZ, double extra) {
			if (AbilityWarSettings.getVisualEffect()) {
				if(ServerVersion.getVersion() >= 9) {
					Particle p = particle;
					if (p != null) {
						l.getWorld().spawnParticle(p, l, Count, offsetX, offsetY, offsetZ, extra);
					}
				} else if (ServerVersion.getVersion() == 8) {
					ParticlePacket packet = new ParticlePacket(particleName, l, (float) offsetX, (float) offsetY, (float) offsetZ, (float) extra, Count);
					packet.Broadcast();
				} else if (ServerVersion.getVersion() <= 7) {
					Effect e = effect;
					if (e != null) {
						l.getWorld().playEffect(l, e, Count);
					}
				}
			}
		}

		public void spawnParticle(Location l, int Count, double offsetX, double offsetY, double offsetZ, Object arg) {
			if (AbilityWarSettings.getVisualEffect()) {
				if(ServerVersion.getVersion() >= 9) {
					Particle p = particle;
					if (p != null) {
						l.getWorld().spawnParticle(p, l, Count, offsetX, offsetY, offsetZ, arg);
					}
				} else if (ServerVersion.getVersion() == 8) {
					ParticlePacket packet = new ParticlePacket(particleName, l, (float) offsetX, (float) offsetY, (float) offsetZ, Count);
					packet.Broadcast();
				} else if (ServerVersion.getVersion() <= 7) {
					Effect e = effect;
					if (e != null) {
						l.getWorld().playEffect(l, e, Count);
					}
				}
			}
		}

	}

}
