package witspirit.appengine.winkelplanner.data;

import javax.jdo.PersistenceManager;

public interface DataAction {
    
    void execute(PersistenceManager pm);

}
