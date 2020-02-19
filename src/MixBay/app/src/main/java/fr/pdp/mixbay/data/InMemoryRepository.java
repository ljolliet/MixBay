package fr.pdp.mixbay.data;

import fr.pdp.mixbay.business.dataAccess.RepositoryI;
import fr.pdp.mixbay.business.models.Session;

public class InMemoryRepository implements RepositoryI {

    private Session session;

    @Override
    public Session getSession() {
        return session;
    }

    @Override
    public void setSession(Session session) {
        this.session = session;
    }
}
