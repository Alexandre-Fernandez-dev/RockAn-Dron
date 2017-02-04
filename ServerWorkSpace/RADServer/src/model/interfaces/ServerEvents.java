package model.interfaces;

public interface ServerEvents {
	public void gameUserListChanged(String name);
	public void gameListChanged();
	public void gameFull();
	public void gameEnd();
}
