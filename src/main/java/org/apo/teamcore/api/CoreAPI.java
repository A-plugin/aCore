package org.apo.teamcore.api;

import java.util.List;

public interface CoreAPI {
    public boolean isEnabled();
    public List<String> getCoreList();
    public boolean CoreAlive();
    public int getCoreHealth();
    public String getCoreMaterial();

    boolean CoreAlive(String name);

    int getCoreHealth(String name);

    String getCoreMaterial(String name);
}
