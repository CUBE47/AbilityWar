package Marlang.AbilityWar.Game.Script;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;

import Marlang.AbilityWar.Game.Games.AbstractGame;
import Marlang.AbilityWar.Game.Script.Objects.AbstractScript;
import Marlang.AbilityWar.Game.Script.Objects.Setter.Setter;
import Marlang.AbilityWar.Game.Script.ScriptException.State;
import Marlang.AbilityWar.Utils.Messager;
import Marlang.AbilityWar.Utils.Data.FileManager;
import Marlang.AbilityWar.Utils.Thread.AbilityWarThread;

/**
 * ��ũ��Ʈ
 * @author _Marlang ����
 */
abstract public class Script {
	
	private static ArrayList<AbstractScript> Scripts = new ArrayList<AbstractScript>();
	
	/**
	 * ��� ��ũ��Ʈ�� ���۽�ŵ�ϴ�.
	 */
	public static void RunAll(AbstractGame game) {
		if(AbilityWarThread.isGameTaskRunning()) {
			for(AbstractScript script : Scripts) {
				script.Start(game);
			}
		}
	}
	
	/**
	 * ��ũ��Ʈ�� �߰��մϴ�.
	 */
	public static void AddScript(AbstractScript script) {
		if(!Scripts.contains(script)) {
			Scripts.add(script);
		}
	}
	
	/**
	 * ��ũ��Ʈ ���� �ȿ� �ִ� ��� ��ũ��Ʈ�� �ҷ��ɴϴ�.
	 */
	public static void LoadAll() {
		Scripts.clear();
		
		for(File file : FileManager.getFolder("Script").listFiles()) {
			try {
				AbstractScript script = Load(file);
				Scripts.add(script);
			} catch (ScriptException e) {}
		}
	}
	
	private static ArrayList<ScriptRegisteration> ScriptTypes = new ArrayList<ScriptRegisteration>();
	
	/**
	 * ��ũ��Ʈ ���
	 * @throws IllegalArgumentException ����Ϸ��� ��ũ��Ʈ Ŭ������ �̸��� �ٸ� ��ũ��Ʈ Ŭ������
	 *                                  �̹� ����ϰ� �ִ� �̸��� ���,
	 *                                  �̹� ��ϵ� ��ũ��Ʈ Ŭ������ ���
	 */
	public static void registerScript(Class<? extends AbstractScript> clazz, RequiredData<?>... requiredDatas) throws IllegalArgumentException {
		for(ScriptRegisteration check : ScriptTypes) {
			if(check.getClazz().getSimpleName().equalsIgnoreCase(clazz.getSimpleName())) {
				throw new IllegalArgumentException("�̹� ������� ��ũ��Ʈ �̸��Դϴ�.");
			}
		}
		
		if(!isRegistered(clazz)) {
			ScriptTypes.add(new ScriptRegisteration(clazz, requiredDatas));
		} else {
			throw new IllegalArgumentException("�̹� ��ϵ� ��ũ��Ʈ�Դϴ�.");
		}
	}
	
	public static ScriptRegisteration getRegisteration(Class<? extends AbstractScript> clazz) throws IllegalArgumentException, ScriptException {
		if(isRegistered(clazz)) {
			for(ScriptRegisteration sr : ScriptTypes) {
				if(sr.getClazz().equals(clazz)) {
					return sr;
				}
			}
			
			throw new ScriptException(State.Not_Found);
		} else {
			throw new IllegalArgumentException("��ϵ��� ���� ��ũ��Ʈ�Դϴ�.");
		}
	}
	
	public static Class<? extends AbstractScript> getScriptClass(String className) throws ClassNotFoundException {
		for(ScriptRegisteration reg : ScriptTypes) {
			if(reg.getClazz().getSimpleName().equalsIgnoreCase(className)) {
				return reg.getClazz();
			}
		}
		
		throw new ClassNotFoundException();
	}
	
	public static List<String> getRegisteredScripts() {
		List<String> list = new ArrayList<String>();
		for(ScriptRegisteration reg : ScriptTypes) {
			list.add(reg.getClazz().getSimpleName());
		}
		
		return list;
	}
	
	public static boolean isRegistered(Class<? extends AbstractScript> clazz) {
		for(ScriptRegisteration check : ScriptTypes) {
			if(check.getClazz().equals(clazz)) {
				return true;
			}
		}
		
		return false;
	}
	
	public static class ScriptRegisteration {
		
		private final Class<? extends AbstractScript> clazz;
		private final RequiredData<?>[] requiredDatas;
		
		public ScriptRegisteration(Class<? extends AbstractScript> clazz, RequiredData<?>... requiredDatas) {
			this.clazz = clazz;
			this.requiredDatas = requiredDatas;
		}
		
		public Class<? extends AbstractScript> getClazz() {
			return clazz;
		}
		
		public RequiredData<?>[] getRequiredDatas() {
			return requiredDatas;
		}
		
	}
	
	public static class RequiredData<T> {
		
		private final String Key;
		private final Class<T> clazz;
		private final T Default;
		private final Class<? extends Setter<T>> setterClass;
		
		public RequiredData(String Key, Class<T> clazz, T Default) {
			this.Key = Key;
			this.clazz = clazz;
			this.Default = Default;
			this.setterClass = null;
		}

		public RequiredData(String Key, Class<T> clazz) {
			this.Key = Key;
			this.clazz = clazz;
			this.Default = null;
			this.setterClass = null;
		}

		public RequiredData(String Key, Class<T> clazz, Class<? extends Setter<T>> setterClass) {
			this.Key = Key;
			this.clazz = clazz;
			this.Default = null;
			this.setterClass = setterClass;
		}

		public RequiredData(String Key, Class<T> clazz, T Default, Class<? extends Setter<T>> setterClass) {
			this.Key = Key;
			this.clazz = clazz;
			this.Default = Default;
			this.setterClass = setterClass;
		}
		
		public String getKey() {
			return Key;
		}
		
		public Class<T> getClazz() {
			return clazz;
		}
		
		public T getDefault() {
			return Default;
		}

		public Class<? extends Setter<T>> getSetterClass() {
			return setterClass;
		}
		
	}
	
	/**
	 * ��ũ��Ʈ ����
	 */
	public static void Save(AbstractScript script) {
		try {
			if(isRegistered(script.getClass())) {
				FileManager.getFolder("Script");
				File f = FileManager.getFile("Script/" + script.getScriptName() + ".yml");
				ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(f));
				output.writeObject(script);
				output.flush();
				output.close();
			} else {
				throw new ClassNotFoundException();
			}
		} catch (IOException ioException) {
			Messager.sendErrorMessage("��ũ��Ʈ�� �����ϴ� ���� ������ �߻��Ͽ����ϴ�.");
		} catch (ClassNotFoundException e) {
			Messager.sendErrorMessage("��ϵ��� ���� ��ũ��Ʈ�Դϴ�.");
		}
	}
	
	/**
	 * ��ũ��Ʈ �ҷ�����
	 */
	public static AbstractScript Load(File file) throws ScriptException {
		try {
			if(file.exists()) {
				ObjectInputStream input = new ObjectInputStream(new FileInputStream(file));
				Object obj = input.readObject();
				input.close();
				
				if(obj != null) {
					if(obj instanceof AbstractScript) {
						return (AbstractScript) obj;
					} else {
						throw new ScriptException(State.IllegalFile);
					}
				} else {
					throw new NullPointerException();
				}
			} else {
				throw new IOException();
			}
		} catch (IOException | NullPointerException | ClassNotFoundException Exception) {
			Messager.sendErrorMessage(ChatColor.translateAlternateColorCodes('&', "&e" + file.getName() + " &f��ũ��Ʈ�� �ҷ����� ���� ������ �߻��Ͽ����ϴ�."));
			throw new ScriptException(State.Not_Loaded);
		}
	}
	
}
