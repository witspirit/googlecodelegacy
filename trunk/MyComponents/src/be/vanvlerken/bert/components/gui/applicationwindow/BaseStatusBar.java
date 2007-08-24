/**
 * @author wItspirit
 * 10-apr-2003
 * BaseStatusBar.java
 */package be.vanvlerken.bert.components.gui.applicationwindow;

import java.util.ArrayList;
import java.util.List;

/**
 * Basic implementation of a StatusBar
 */
public class BaseStatusBar implements StatusBar
{
    private StatusBarListener   statusBarListener;
    private List                messageStack;

    public class Message
    {
        private boolean  cleanUp;
        private String    message;
        
        public Message(String message)
        {
            this.message = message;
            cleanUp = false;
        }
        
        public boolean needsCleanUp()
        {
            return cleanUp;
        }
        
        public void setCleanUp(boolean cleanUpRequired)
        {
            cleanUp = cleanUpRequired;
        }
        
        public String getMessage()
        {
            return message;
        }
    }
    
    public BaseStatusBar()
    {
        statusBarListener = null;
        
        messageStack = new ArrayList();
        messageStack.add(new Message(" "));
    }
    
    /**
     * @see be.vanvlerken.bert.components.gui.applicationwindow.StatusBar#addMessage(String)
     */
    public int addMessage(String message)
    {
        messageStack.add(new Message(message));
        fireStatusBarEvent();
        return getLevel();
    }

    /**
     * @see be.vanvlerken.bert.components.gui.applicationwindow.StatusBar#clearMesssage(int)
     */
    public void clearMesssage(int index)
    {
        /* This method requires special care
         * When the index is somewhere inside our list, we set the cleanUp flag, BUT
         * not remove it, in order to preserve the indexes.
         * When the index equals the top, we clean up the top and all subsequent elements
         * with the clean up flag set.
         */
    
        Message clearedMessage = (Message) messageStack.get(index);
        clearedMessage.setCleanUp(true);
        
        Message traversalMessage = (Message) messageStack.get(getLevel());
        while ( traversalMessage.needsCleanUp() )
        {
            messageStack.remove(getLevel());
            traversalMessage = (Message) messageStack.get(getLevel());
        }
        
        fireStatusBarEvent();
    }

    /**
     * @see be.vanvlerken.bert.components.gui.applicationwindow.StatusBar#getLevel()
     */
    public int getLevel()
    {
        return (messageStack.size()-1);
    }

    /**
     * @see be.vanvlerken.bert.components.gui.applicationwindow.StatusBar#setMessage(int, String)
     */
    public int setMessage(int level, String message)
    {
        
        messageStack.set(level, new Message(message));
        fireStatusBarEvent();
        return level;
    }

    /**
     * @see be.vanvlerken.bert.components.gui.applicationwindow.StatusBar#getMessage()
     */
    public String getMessage()
    {
        return getMessage(getLevel());
    }

    /**
     * @see be.vanvlerken.bert.components.gui.applicationwindow.StatusBar#getMessage(int)
     */
    public String getMessage(int level)
    {
        Message message = (Message) messageStack.get(level);
        return message.getMessage();
    }

    /**
     * @see be.vanvlerken.bert.components.gui.applicationwindow.StatusBar#setStatusBarListener(StatusBarListener)
     */
    public void setStatusBarListener(StatusBarListener statusBarListener)
    {
        this.statusBarListener = statusBarListener;
    }
    
    protected void fireStatusBarEvent()
    {
        if ( statusBarListener != null )
        {
            statusBarListener.statusBarUpdated(new StatusBarEvent(this));
        }
    }
}
