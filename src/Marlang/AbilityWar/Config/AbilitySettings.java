package Marlang.AbilityWar.Config;

import java.util.ArrayList;

import Marlang.AbilityWar.Ability.AbilityBase;
import Marlang.AbilityWar.Ability.AbilityManifest;
import Marlang.AbilityWar.Utils.Data.FileManager;

/**
 * �ɷ� ���� ����
 * @author _Marlang ����
 */
public class AbilitySettings {
	
	//TODO: �ɷ� ���� ���� ������ �����
	
	private static ArrayList<SettingObject<?>> Settings = new ArrayList<SettingObject<?>>();
	
	private static void registerSetting(SettingObject<?> object) {
		if(!Settings.contains(object)) {
			Settings.add(object);
		}
	}
	
	private static CommentedConfiguration Config = new CommentedConfiguration(FileManager.getFile("Ability.yml"));
	
	public static void Setup() {
		CommentedConfiguration newConfig = new CommentedConfiguration(FileManager.getFile("Ability.yml"));
		Config.load();
		
		for(SettingObject<?> Setting : Settings) {
			newConfig.set(Setting.getPath(), Setting.getValue());
			newConfig.addComment(Setting.getPath(), Setting.getComments());
		}
		
		Config = newConfig;
		
		Config.save();
		newConfig = null;
	}
	
	abstract public static class SettingObject<T> {
		
		private final String Path;
		private final T Default;
		private final String[] Comments;
		
		public SettingObject(Class<? extends AbilityBase> abilityClass, String Path, T Default, String... Comments) {
			AbilityManifest manifest = abilityClass.getAnnotation(AbilityManifest.class);
			if(manifest != null) {
				this.Path = "�ɷ�." + manifest.Name() + "." + Path;
			} else {
				throw new IllegalArgumentException(abilityClass.getName() + " Ŭ������ AbilityManifest ������̼��� �������� �ʽ��ϴ�.");
			}
			
			this.Default = Default;
			this.Comments = Comments;

			registerSetting(this);
		}
		
		public String getPath() {
			return Path;
		}
		
		public T getDefault() {
			return Default;
		}
		
		public String[] getComments() {
			return Comments;
		}
		
		abstract public boolean Condition(T value);
		
		@SuppressWarnings("unchecked")
		public T getValue() {
			Object o = Config.get(getPath());
			
			if(o != null && o.getClass().isAssignableFrom(getDefault().getClass())) {
				if(Condition((T) o)) {
					return (T) o;
				} else {
					return getDefault();
				}
			} else {
				return getDefault();
			}
		}
		
	}
	
	public static void Refresh() {
		Config.load();
		Setup();
	}
	
}
