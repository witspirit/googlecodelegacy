/**
 * @author wItspirit
 * 29-okt-2005
 * UrlSelector.java
 */

package be.vanvlerken.bert.flickrstore.gui.photosetselector;

import be.vanvlerken.bert.components.gui.applicationwindow.StatusBar;
import be.vanvlerken.bert.flickrstore.gui.ActionFactory;
import be.vanvlerken.bert.flickrstore.gui.UrlProvider;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class PhotosetSelector extends JPanel implements Observer, UrlProvider {
    private static final long serialVersionUID = 1L;

    private ActionFactory actionFactory;
    private JLabel setIconLabel;
    private JLabel titleLabel;
    private JLabel photoCountLabel;
    private JButton verifyButton;
    private JButton downloadButton;
    private PhotosetDescription photosetDescription;
    private JTextArea urlArea;
    private JLabel descriptionLabel;
    private StatusBar statusBar;
    private JProgressBar progressBar;

    public PhotosetSelector(StatusBar statusBar, JProgressBar progressBar, ActionFactory actionFactory) {
        super(new BorderLayout());
        this.statusBar = statusBar;
        this.progressBar = progressBar;
        this.actionFactory = actionFactory;
        generateLayout();
    }

    /**
     *
     */
    private void generateLayout() {
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel urlLabel = new JLabel("Flickr PhotoSet URL");
        urlArea = new JTextArea();

        photosetDescription = new PhotosetDescription();
        photosetDescription.addObserver(this);

        setIconLabel = new JLabel();
        titleLabel = new JLabel();
        descriptionLabel = new JLabel();
        photoCountLabel = new JLabel();
        updatePhotosetPreview();

        JPanel descriptionPanel = new JPanel(new BorderLayout());
        descriptionPanel.add(titleLabel, BorderLayout.NORTH);
        descriptionPanel.add(descriptionLabel, BorderLayout.CENTER);
        descriptionPanel.add(photoCountLabel, BorderLayout.SOUTH);

        JPanel verifyPanel = new JPanel(new BorderLayout(10, 0));
        Border etchedBorder = BorderFactory.createEtchedBorder();
        Border titledBorder = BorderFactory.createTitledBorder(etchedBorder, "Selected PhotoSet");
        verifyPanel.setBorder(titledBorder);
        verifyPanel.add(setIconLabel, BorderLayout.WEST);
        verifyPanel.add(descriptionPanel, BorderLayout.CENTER);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(urlLabel, BorderLayout.NORTH);
        centerPanel.add(urlArea, BorderLayout.CENTER);
        centerPanel.add(verifyPanel, BorderLayout.SOUTH);

        verifyButton = new JButton(actionFactory.getVerifyPhotosetAction(statusBar, this, photosetDescription));
        downloadButton = new JButton(actionFactory.getDownloadPhotosetAction(statusBar, progressBar, this));
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        buttonPanel.add(verifyButton);
        buttonPanel.add(downloadButton);

        this.add(centerPanel, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.SOUTH);
    }

    public void update(Observable observable, Object extra) {
        if (observable == photosetDescription) {
            updatePhotosetPreview();
        }
    }

    /**
     *
     */
    private void updatePhotosetPreview() {
        setIconLabel.setIcon(photosetDescription.getIcon());
        titleLabel.setText(photosetDescription.getTitle());
        if (photosetDescription.getDescription() == null) {
            descriptionLabel.setText("");
        } else {
            descriptionLabel.setText("<html>" + photosetDescription.getDescription() + "</html>");
        }
        String labelText;
        if (photosetDescription.getPhotocount() == PhotosetDescription.UNKNOWN) {
            labelText = "Unknown number of photos";
        } else {
            labelText = photosetDescription.getPhotocount() + " photos";
        }
        photoCountLabel.setText(labelText);
    }

    public String getUrl() {
        return urlArea.getText();
    }
}
