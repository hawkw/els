package data.scripts

import com.fs.starfarer.api.Global
import com.fs.starfarer.api.campaign.SectorEntityToken
import com.fs.starfarer.api.campaign.econ.MarketAPI

/**
  * Created by hawk on 7/14/16.
  */
package object world {

  implicit class AddMarket(val planet: SectorEntityToken)
  extends AnyVal {
    def addMarket( faction: String, name: String, size: Int, tariff: Float
                 , conditions: Seq[String], submarkets: Seq[String]
                 , connectedEntities: SectorEntityToken* ): MarketAPI = {
      val market
        = Global.getFactory.createMarket(planet.getId + "_market", name, size)

      market.setFactionId(faction)
      market.setPrimaryEntity(planet)
      market.setBaseSmugglingStabilityValue(0)
      market.getTariff.modifyFlat("generator", tariff)

      for (submarket <- submarkets) { market addSubmarket submarket }

      for (condition <- conditions) { market addCondition condition }

      for (entity <- connectedEntities) {
        market.getConnectedEntities add entity
        entity setMarket market
        entity setFaction faction
      }

      Global.getSector.getEconomy addMarket market
      planet setMarket market
      planet setFaction faction

      market
    }
  }

}
