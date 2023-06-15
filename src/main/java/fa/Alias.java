package fa;

public class Alias {
    private String name;
    private boolean enabled;

    public Alias() {
        this.name = new String();
        this.enabled = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if(this.enabled == true)
        this.name = name;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
