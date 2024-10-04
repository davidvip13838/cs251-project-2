package Items;

import Items.Types.SwordType;
import java.util.Objects;

/**
 * Implements our SwordType class
 */
public class Sword extends SwordType{
    /**
     * Constructs the Sword object by referencing the parent class. Fields are as named.
     */
    public Sword(int totalHealth, int healthLeft, int timeToClean, int length, int DPS, int attackSpeed,
                 String name, String description, String comments, String style) {
        super(totalHealth, healthLeft, timeToClean, length, DPS, attackSpeed, name, description, comments, style);
    }

    // A static helper method to generate a unique value based on certain fields.
    // This method could be used for any internal use where the uniqueness of the sword needs to be determined.
    public static int generateUniqueId(int Health, int DP, int attackSpee, String styl) {
        return Objects.hash(Health, DP, attackSpee, styl);
    }

    /**
     * Returns the hash code of this object, for internal use only.
     *
     * <bold>251 Students: This function will not be tested directly, it is for your convenience only.</bold>
     * @return a hash code of the object
     */
    @Override
    public int hashCode() {
        // Generates a unique hash based on the sword's unique identifiers
        return Objects.hash(totalHealth, DPS, attackSpeed, style);
    }

    /**
     * We should also implement the equals method to ensure two Sword objects
     * with the same unique fields are considered equal.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Sword sword = (Sword) obj;
        return totalHealth == sword.totalHealth &&
                DPS == sword.DPS &&
                attackSpeed == sword.attackSpeed &&
                Objects.equals(style, sword.style);
    }
}

