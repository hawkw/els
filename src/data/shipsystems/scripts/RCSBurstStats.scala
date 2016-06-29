package data.shipsystems.scripts

import com.fs.starfarer.api.Global
import com.fs.starfarer.api.combat.{MutableShipStatsAPI, ShipAPI}
import com.fs.starfarer.api.plugins.ShipSystemStatsScript
import com.fs.starfarer.api.plugins.ShipSystemStatsScript.{State, StatusData}
import org.dark.graphics.util.AnamorphicFlare
import org.lwjgl.util.vector.Vector2f

import scala.collection.JavaConversions._

class RCSBurstStats
extends ShipSystemStatsScript {

  override def apply(stats: MutableShipStatsAPI, id: String,
                     state: State, effectLevel: Float): Unit = {

    if (state == State.OUT) stats.getMaxSpeed.unmodify(id)
    else {
      stats.getMaxSpeed.modifyPercent(id, 200f * effectLevel)
      stats.getAcceleration.modifyPercent(id, 300f * effectLevel)
      stats.getMaxTurnRate.modifyPercent(id, 100f * effectLevel)
      stats.getMaxTurnRate.modifyPercent(id, 100f * effectLevel)
    }

//    if (state == State.IN) {
//
//      val ship = stats.getEntity.asInstanceOf[ShipAPI]
//      val combat = Global.getCombatEngine
//      val key = s"${ship.getId}_$id"
//      val test = combat.getCustomData.get(key)

//      if (test == null && effectLevel > 0.4f) {
//        combat.getCustomData.put(key, new Object())
//        for { engine <- ship.getEngineController.getShipEngines
//          if engine.isActive && !engine.isSystemActivated }
//        {
//          val angle = Math.random().asInstanceOf[Float] * 15f - 7.5f
//          val color = engine.getEngineColor
//
//          AnamorphicFlare.createFlare( ship
//                                       , engine.getLocation
//                                       , combat
//                                       , 1f
//                                       , 0.1f
//                                       , angle
//                                       , 5f
//                                       , 1f
//                                       , color
//                                       , color )
//        }
//      } else {
//        combat.getCustomData.remove(key)
//      }
//    }


  }




  override def getStatusData(index: Int,
                             state: State,
                             effectLevel: Float): StatusData
    = if (index == 0) new StatusData("increased maneuverability", false)
      else null

  override def unapply(stats: MutableShipStatsAPI, id: String): Unit = {
    stats.getMaxSpeed.unmodify(id)
    stats.getMaxTurnRate.unmodify(id)
    stats.getTurnAcceleration.unmodify(id)
    stats.getAcceleration.unmodify(id)
  }

}