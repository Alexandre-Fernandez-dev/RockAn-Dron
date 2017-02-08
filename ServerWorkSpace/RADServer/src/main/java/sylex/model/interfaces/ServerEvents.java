package sylex.model.interfaces;

public interface ServerEvents {
	/*public void gameUserListChanged(String name);
	public void gameListChanged();*/
    public void userListChanged();
	public void gameReady();
	public void gameEnd();
}
