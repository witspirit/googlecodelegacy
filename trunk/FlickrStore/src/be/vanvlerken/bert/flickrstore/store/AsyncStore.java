/**
 * @author wItspirit
 * 28-okt-2005
 * AsyncStore.java
 */

package be.vanvlerken.bert.flickrstore.store;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import com.aetrion.flickr.photos.Photo;

public class AsyncStore {
    private ExecutorService executor;
    private SyncStore store;

    public AsyncStore(ExecutorService executor, SyncStore store) {
	this.executor = executor;
	this.store = store;
    }

    private class SavePhotos implements Callable<Object> {
	private List<Photo> photos;
	private String targetFolder;
	private StoreProgress progress;
	private OverwriteStrategy overwriteStrategy;

	public SavePhotos(List<Photo> photos, String targetFolder, StoreProgress progress, OverwriteStrategy overwriteStrategy) {
	    this.photos = photos;
	    this.targetFolder = targetFolder;
	    this.progress = progress;
	    this.overwriteStrategy = overwriteStrategy;
	}

	public Object call() throws Exception {
	    store.savePhotos(photos, targetFolder, progress, overwriteStrategy);
	    return null;
	}
    }

    public Future<Object> savePhotos(List<Photo> photos, String targetFolder, StoreProgress progress, OverwriteStrategy overwriteStrategy) {
	return executor.submit(new SavePhotos(photos, targetFolder, progress, overwriteStrategy));
    }

    public Future<Object> savePhoto(Photo photo, String targetFolder, OverwriteStrategy overwriteStrategy) {
	List<Photo> photos = new ArrayList<Photo>();
	photos.add(photo);
	return executor.submit(new SavePhotos(photos, targetFolder, null, overwriteStrategy));
    }

}
