/**
 * @author wItspirit
 * 8-okt-2005
 * FlickrTest.java
 */

package be.vanvlerken.bert.flickrstore.store;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xml.sax.SAXException;

import be.vanvlerken.bert.flickrstore.FlickrCommunicationException;

import com.aetrion.flickr.FlickrException;
import com.aetrion.flickr.photos.Photo;
import com.aetrion.flickr.tags.Tag;
import com.thoughtworks.xstream.XStream;

/**
 * Small test class to verify the functionality of the Flickr API
 */
public class SyncStore {
    private XStream xstream;

    public SyncStore() {
	xstream = new XStream();
	xstream.alias("PhotoMetadata", PhotoMetadata.class);
	xstream.alias("Tag", Tag.class);
    }

    /**
     * @see be.vanvlerken.bert.flickrstore.store.Store#savePhotos(java.util.List, java.lang.String)
     * @param progress
     *                May be null
     */
    public void savePhotos(List<Photo> photos, String targetFolder, StoreProgress progress, OverwriteStrategy overwriteStrategy)
	    throws FlickrCommunicationException {
	if (progress == null) {
	    progress = new DummyStoreProgress();
	}
	if (overwriteStrategy == null) {
	    overwriteStrategy = new OverwriteAlways();
	}
	CombinedOverwriteStrategy comboOverwrite = new CombinedOverwriteStrategy(overwriteStrategy);
	progress.setMaximum(photos.size());
	int photosStored = 0;
	progress.setCurrent(photosStored);
	File folder = new File(targetFolder);
	folder.mkdir();
	try {
	    for (Photo photo : photos) {
		savePhoto(photo, targetFolder, comboOverwrite);
		saveMetadata(photo, targetFolder, comboOverwrite);
		photosStored++;
		progress.setCurrent(photosStored);
	    }
	} catch (IOException e) {
	    throw new FlickrCommunicationException("Failed to connect to Flickr.", e);
	} catch (SAXException e) {
	    throw new FlickrCommunicationException("Could not understand response from Flickr.", e);
	} catch (FlickrException e) {
	    throw new FlickrCommunicationException("Flickr returned an error: " + e.getErrorMessage(), e);
	}
    }

    private boolean writeRequired(File file, OverwriteStrategy overwriteStrategy) {
	// Only write the file if
	// 1. The file does not exist
	// 2. Or the existing file should be overwritten
	return !file.exists() || overwriteStrategy.overwrite(file);
    }

    /**
     * @see be.vanvlerken.bert.flickrstore.store.Store#savePhoto(com.aetrion.flickr.photos.Photo, java.lang.String)
     */
    public void savePhoto(Photo photo, String targetFolder, OverwriteStrategy overwriteStrategy) throws IOException, SAXException, FlickrException {
	String fileName = buildFileNameBase(photo, targetFolder);
	URL photoUrl = new URL(photo.getOriginalUrl());
	File photoFile = new File(fileName + "." + fileExtension(photoUrl));
	if (writeRequired(photoFile, overwriteStrategy)) {
	    OutputStream fileOutput = null;
	    InputStream photoInput = null;
	    try {
		fileOutput = new BufferedOutputStream(new FileOutputStream(photoFile));
		photoInput = new BufferedInputStream(photoUrl.openStream());		
		int data = photoInput.read();
		while (data != -1) {
		    fileOutput.write(data);
		    data = photoInput.read();
		}
		fileOutput.flush();
	    } finally {
		if (photoInput != null) {
		    photoInput.close();
		}
		if (fileOutput != null) {
		    fileOutput.close();
		}
	    }
	}
    }

    /**
     * @param photo
     * @param targetFolder
     * @return
     */
    private String buildFileNameBase(Photo photo, String targetFolder) {
	// Create a SAFE name. Replace any forbidden characters by _
	String scrambledName = photo.getTitle();
	// Forbidden chars are: \ / : * ? " < > |
	Pattern scramblePattern = Pattern.compile("[\\\\/:\\*\\?\"<>|]");
	Matcher scrambler = scramblePattern.matcher(scrambledName);
	scrambledName = scrambler.replaceAll("_");
	String fileName = targetFolder + "/" + scrambledName + "-" + photo.getId();
	return fileName;
    }

    /**
     * @param photo
     * @param fileName
     * @throws IOException
     */
    public void saveMetadata(Photo photo, String targetFolder, OverwriteStrategy overwriteStrategy) throws IOException {
	String fileName = buildFileNameBase(photo, targetFolder);
	File xmlFile = new File(fileName + ".xml");
	if (writeRequired(xmlFile, overwriteStrategy)) {
	    PhotoMetadata metadata = new PhotoMetadata(photo);
	    FileWriter xmlOutput = new FileWriter(xmlFile);
	    xstream.toXML(metadata, xmlOutput);
	}
    }

    private String fileExtension(URL originalUrl) {
	String fileName = originalUrl.getFile();
	String extension = fileName.substring(fileName.length() - 3, fileName.length());
	return extension;
    }
}
