package data.shipsystems.scripts

import com.fs.starfarer.api.combat.MutableShipStatsAPI
import com.fs.starfarer.api.plugins.ShipSystemStatsScript
import com.fs.starfarer.api.plugins.ShipSystemStatsScript.{State, StatusData}

class RCSBurstStats
extends ShipSystemStatsScript {

  override def apply(stats: MutableShipStatsAPI, id: String,
                     state: State, effectLevel: Float): Unit
    = if (state == State.OUT) stats.getMaxSpeed.unmodify(id)
      else {
        stats.getMaxSpeed.modifyPercent(id, 200f * effectLevel)
        stats.getAcceleration.modifyPercent(id, 300f * effectLevel)
        stats.getMaxTurnRate.modifyPercent(id, 100f * effectLevel)
        stats.getMaxTurnRate.modifyPercent(id, 100f * effectLevel)
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