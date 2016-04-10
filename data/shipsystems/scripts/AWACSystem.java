package data.shipsystems.scripts;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.plugins.ShipSystemStatsScript;
import com.fs.starfarer.api.util.IntervalUtil;
import com.fs.starfarer.api.combat.WeaponAPI.WeaponType;

import java.awt.Color;
import java.util.EnumSet;
import java.util.Set;
import java.util.HashSet;

import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

public class AWACSystem implements ShipSystemStatsScript {

    private CombatEngineAPI engine;
    private ShipAPI host;

    private static final float RANGE = 5000f;
    private static final float ACCURACY_BONUS = 0.75f;
    private static final float RANGE_BONUS = 25f;
    private static final float SENSOR_BONUS = 25f;
    private static final Vector2f ZERO = new Vector2f();
    private static final Color WEAPON_GLOW = new Color(150, 255, 200, 100);

    private final IntervalUtil interval = new IntervalUtil(1.5f, 1.5f);

    private final EnumSet<WeaponType> WEAPONS_AFFECTED
        = EnumSet.of(WeaponType.BALLISTIC, WeaponType.ENERGY);

    //Creates a hashmap that keeps track of what ships are receiving the benefits.
    private final HashSet<ShipAPI> buffed = new HashSet<ShipAPI>();

    private static final String staticID = "adeptAWACS";

    // private Consumer<ShipAPI> unmodify = (ShipAPI ship) -> {
    //     MutableShipStatsAPI stats = ship.getMutableStats();
    //     stats.getAutofireAimAccuracy().unmodify(staticID);
    //     stats.getBallisticWeaponRangeBonus().unmodify(staticID);
    //     stats.getEnergyWeaponRangeBonus().unmodify(staticID);
    //     stats.getSensorStrength().unmodify(staticID);
    //     buffed.remove(ship);
    // };

    @Override
    public void apply(MutableShipStatsAPI my_stats,
                      String id,
                      State state,
                      float effectLevel) {
        if (engine != Global.getCombatEngine()) {
            engine = Global.getCombatEngine();
            buffed.clear();
        }
        ShipAPI this_ship = (ShipAPI) my_stats.getEntity();

        // engine.getShips().stream()
        //       .filter(ship -> ship.isAlive() && ship != this_ship &&
        //                       ship.getOwner() == this_ship.getOwner() &&
        //                       MathUtils.getDistance(ship, this_ship) <= RANGE)
        //       .forEach(ship -> {
        //             MutableShipStatsAPI stats = ship.getMutableStats();
        //             stats.getAutofireAimAccuracy()
        //                  .modifyFlat(staticID, ACCURACY_BONUS);
        //             stats.getBallisticWeaponRangeBonus()
        //                 .modifyPercent(staticID, RANGE_BONUS);
        //             stats.getEnergyWeaponRangeBonus()
        //                 .modifyPercent(staticID, RANGE_BONUS);
        //             buffed.put(ship)
        //         });

        for (ShipAPI ship : engine.getShips()) {
            if (!ship.isAlive() || ship.getOwner() != this_ship.getOwner()) {
                continue;
            }

            MutableShipStatsAPI stats = ship.getMutableStats();
            if (MathUtils.getDistance(ship, this_ship) <= RANGE) {
                stats.getAutofireAimAccuracy()
                     .modifyFlat(staticID, ACCURACY_BONUS);
                stats.getBallisticWeaponRangeBonus()
                    .modifyPercent(staticID, RANGE_BONUS);
                stats.getEnergyWeaponRangeBonus()
                    .modifyPercent(staticID, RANGE_BONUS);
                ship.setWeaponGlow(effectLevel, WEAPON_GLOW, WEAPONS_AFFECTED);
                buffed.add(ship);
            } else if (buffed.contains(ship)) {
                stats.getAutofireAimAccuracy().unmodify(staticID);
                stats.getBallisticWeaponRangeBonus().unmodify(staticID);
                stats.getEnergyWeaponRangeBonus().unmodify(staticID);
                stats.getSensorStrength().unmodify(staticID);
                ship.setWeaponGlow(0f, WEAPON_GLOW, WEAPONS_AFFECTED);
                buffed.remove(ship);
            }

        }

    }

    @Override
    public void unapply(MutableShipStatsAPI my_stats, String id) {
        for (ShipAPI ship : buffed) {
            MutableShipStatsAPI stats = ship.getMutableStats();
            stats.getAutofireAimAccuracy().unmodify(staticID);
            stats.getBallisticWeaponRangeBonus().unmodify(staticID);
            stats.getEnergyWeaponRangeBonus().unmodify(staticID);
            stats.getSensorStrength().unmodify(staticID);
            ship.setWeaponGlow(0f, WEAPON_GLOW, WEAPONS_AFFECTED);
        }
        buffed.clear();
    }

    @Override
    public StatusData getStatusData(int index, State state, float effectLevel) {
        if (index == 0) {
            return new StatusData("improved fire control", false);
        } else if (index == 1) {
            return new StatusData("weapon range +" + (int) (RANGE_BONUS * effectLevel) + "%", false);
        } else if (index == 2) {
            return new StatusData("sensor range + " + (int) (SENSOR_BONUS * effectLevel) + "%", false);
        }
        return null;
    }

    public void init(CombatEngineAPI engine, ShipAPI host) {
        this.engine = engine;
        this.host = host;
    }
}
