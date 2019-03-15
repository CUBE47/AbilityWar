package Marlang.AbilityWar.Game.Script;

public class ScriptException extends Exception {
	
	private static final long serialVersionUID = 1192403591954231786L;
	
	public ScriptException(State state) {
		super(state.getMessage());
	}
	
	protected enum State {
		
		Not_Loaded("��ũ��Ʈ�� �ҷ����� ���� ������ �߻��Ͽ����ϴ�."),
		IllegalFile("��ũ��Ʈ ������ �ƴմϴ�."),
		Not_Found("��ũ��Ʈ�� ã�� �� �����ϴ�.");
		
		String Message;
		
		private State(String msg) {
			this.Message = msg;
		}
		
		public String getMessage() {
			return Message;
		}
		
	}
}
