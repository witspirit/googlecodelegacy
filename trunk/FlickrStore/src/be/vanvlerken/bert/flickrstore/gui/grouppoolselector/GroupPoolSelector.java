/**
 * @author wItspirit
 * 29-okt-2005
 * UrlSelector.java
 */

package be.vanvlerken.bert.flickrstore.gui.grouppoolselector;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.border.Border;

import be.vanvlerken.bert.components.gui.applicationwindow.StatusBar;
import be.vanvlerken.bert.flickrstore.gui.ActionFactory;
import be.vanvlerken.bert.flickrstore.gui.UrlProvider;

public class GroupPoolSelector extends JPanel implements Observer, UrlProvider
{
    private ActionFactory        actionFactory;
    private JLabel               setIconLabel;
    private JLabel               nameLabel;
    private JLabel               photoCountLabel;
    private JButton              verifyButton;
    private JButton              downloadButton;
    private GroupPoolDescription groupPoolDescription;
    private JTextArea            urlArea;
    private StatusBar            statusBar;
    private JProgressBar         progressBar;
    private JLabel               descriptionLabel;

    public GroupPoolSelector(StatusBar statusBar, JProgressBar progressBar, ActionFactory actionFactory)
    {
        super(new BorderLayout());
        this.statusBar = statusBar;
        this.progressBar = progressBar;
        this.actionFactory = actionFactory;
        generateLayout();
    }

    /**
     * 
     */
    private void generateLayout()
    {
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel urlLabel = new JLabel("Flickr GroupPool URL");
        urlArea = new JTextArea();

        groupPoolDescription = new GroupPoolDescription();
        groupPoolDescription.addObserver(this);

        setIconLabel = new JLabel();
        nameLabel = new JLabel();
        descriptionLabel = new JLabel();
        photoCountLabel = new JLabel();
        updateGroupPoolPreview();

        JPanel descriptionPanel = new JPanel(new BorderLayout());
        descriptionPanel.add(nameLabel, BorderLayout.NORTH);
        descriptionPanel.add(descriptionLabel, BorderLayout.CENTER);
        descriptionPanel.add(photoCountLabel, BorderLayout.SOUTH);

        JPanel verifyPanel = new JPanel(new BorderLayout(10, 0));
        Border etchedBorder = BorderFactory.createEtchedBorder();
        Border titledBorder = BorderFactory.createTitledBorder(etchedBorder, "Selected GroupPool");
        verifyPanel.setBorder(titledBorder);
        verifyPanel.add(setIconLabel, BorderLayout.WEST);
        verifyPanel.add(descriptionPanel, BorderLayout.CENTER);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(urlLabel, BorderLayout.NORTH);
        centerPanel.add(urlArea, BorderLayout.CENTER);
        centerPanel.add(verifyPanel, BorderLayout.SOUTH);

        verifyButton = new JButton(actionFactory.getVerifyGroupPoolAction(statusBar, this, groupPoolDescription));
        downloadButton = new JButton(actionFactory.getDownloadGroupPoolAction(statusBar, progressBar, this));
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        buttonPanel.add(verifyButton);
        buttonPanel.add(downloadButton);

        this.add(centerPanel, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.SOUTH);
    }

    public void update(Observable observable, Object extra)
    {
        if (observable == groupPoolDescription)
        {
            updateGroupPoolPreview();
        }
    }

    /**
     * 
     */
    private void updateGroupPoolPreview()
    {
        setIconLabel.setIcon(groupPoolDescription.getIcon());
        nameLabel.setText(groupPoolDescription.getName());
        if (groupPoolDescription.getDescription() == null)
        {
            descriptionLabel.setText("");
        }
        else
        {
            descriptionLabel.setText("<html>" + groupPoolDescription.getDescription() + "</html>");
        }
        String labelText;
        if (groupPoolDescription.getPhotoCount() == GroupPoolDescription.UNKNOWN)
        {
            labelText = "Unknown number of photos";
        }
        else
        {
            labelText = groupPoolDescription.getPhotoCount() + " photos";
        }
        photoCountLabel.setText(labelText);
    }

    public String getUrl()
    {
        return urlArea.getText();
    }
}
