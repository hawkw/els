package data.hullmods

import com.fs.starfarer.api.combat.{BaseHullMod, ShipAPI}
import scala.collection.JavaConversions._

/**
  * Created by hawk on 7/16/16.
  */
class AmInjector
extends BaseHullMod {

  import AmInjector._

  override def applyEffectsAfterShipCreation(ship: ShipAPI, id: String): Unit
    // remove any blocked hullmods
    = for { mod <- ship.getVariant.getHullMods if BlockedMods contains mod }
        ship.getVariant.removeMod(mod)


}

object AmInjector {
  // hullmods that cannot be added if the ship has an antimatter injector
  private val BlockedMods
    = Set( "unstable_injector"
         , "augmentedengines"
         , "safetyoverrides" )
}