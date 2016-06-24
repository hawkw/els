package data.util

import com.fs.starfarer.api.combat.MutableShipStatsAPI

/**
  * Created by hawk on 5/23/16.
  */
object RichStats {
  implicit class RichShipStats(val stats: MutableShipStatsAPI) {
    def maxSpeed = stats.getMaxSpeed
    def maxSpeed_= = ???

  }

}
