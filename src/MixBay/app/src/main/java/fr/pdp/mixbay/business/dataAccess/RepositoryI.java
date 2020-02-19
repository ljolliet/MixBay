package fr.pdp.mixbay.business.dataAccess;

import fr.pdp.mixbay.business.models.Session;

public interface RepositoryI {

    Session getSession();
    void setSession(Session session);
}
