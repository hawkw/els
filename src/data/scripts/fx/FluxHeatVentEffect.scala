package data.scripts.fx

import data.util.Animation.map
import com.fs.starfarer.api.combat.{CombatEngineAPI, EveryFrameWeaponEffectPlugin, ShipAPI, WeaponAPI}
import java.awt.Color
import java.util

import com.fs.starfarer.api.combat.WeaponAPI.WeaponType;



/**
  * Created by hawk on 5/4/16.
  */
class FluxHeatVentEffect
extends EveryFrameWeaponEffectPlugin {
  private[this] val color = new Color(255,125,100,25)
  private[this] val deco = util.EnumSet.of(WeaponType.DECORATIVE)

  override def advance( amount: Float
                      , engine: CombatEngineAPI
                      , weapon: WeaponAPI): Unit = {
    val ship = weapon.getShip
    val flux = ship.getFluxTracker

    val brightness = map[Float]( 0, flux.getMaxFlux, 0f, 25f)(flux.getCurrFlux)
    ship.setWeaponGlow(brightness, color, deco)
  }

}
