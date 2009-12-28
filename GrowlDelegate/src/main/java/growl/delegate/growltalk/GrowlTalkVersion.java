package growl.delegate.growltalk;

public enum GrowlTalkVersion {
	PLAIN(1),
	AES128(1);

	private int versionId;
	
	private GrowlTalkVersion(int versionId) {
		this.versionId = versionId;
	}
	
	public int getVersionId() {
		return versionId;
	}
}
