package fa;

import java.util.HashSet;
import java.util.Set;

public class WaitingSet {
    private Set<State> set;
    private String letter;

    public WaitingSet(Set<State> set, String letter) {
        this.set = set;
        this.letter = letter;
    }

    public Set<State> getSet() {
        return set;
    }

    public void setSet(HashSet<State> set) {
        this.set = set;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }
}
