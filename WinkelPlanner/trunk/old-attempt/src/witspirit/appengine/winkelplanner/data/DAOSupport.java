package witspirit.appengine.winkelplanner.data;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

public final class DAOSupport {
    private static final PersistenceManagerFactory pmfInstance = JDOHelper.getPersistenceManagerFactory("transactions-optional");

    private DAOSupport() {}

    public static PersistenceManagerFactory getPMF() {
        return pmfInstance;
    }
    
    public static PersistenceManager getPM() {
        return pmfInstance.getPersistenceManager();
    }
    
    public static void perform(DataAction dataAction) {
        PersistenceManager pm = pmfInstance.getPersistenceManager();
        try {
            dataAction.execute(pm);
        } finally {
            pm.close();
        }
    }
}
