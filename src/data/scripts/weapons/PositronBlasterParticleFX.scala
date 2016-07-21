package data.scripts.weapons

import com.fs.starfarer.api.combat.{EveryFrameWeaponEffectPlugin, CombatEngineAPI => CombatEngine, DamagingProjectileAPI => Projectile, WeaponAPI => Weapon}

import data.util.Vector._
import org.lazywizard.lazylib.MathUtils.getRandomNumberInRange
import org.lwjgl.util.vector.Vector2f

import scala.collection.JavaConverters._
import me.hawkweisman.elizalib.scalaAPIs.graphics.ColorUtil._

import java.awt.Color

/**
  * Created by hawk on 6/29/16.
  */
class PositronBlasterParticleFX
extends EveryFrameWeaponEffectPlugin {
  import PositronBlasterParticleFX._

  override def advance(amount: Float,
                       engine: CombatEngine,
                       weapon: Weapon): Unit
    = if (Math.random().asInstanceOf[Float] > 0.15 + amount)
      for { projectile: Projectile <- engine.getProjectiles.asScala
        if !projectile.isFading && !projectile.didDamage &&
           projectile.getProjectileSpecId == "els_positron_bolt" &&
           projectile.getSource == weapon.getShip }
      {
        val angle = getRandomNumberInRange(-AngleMax, AngleMax)
        val velocity = projectile.getVelocity
                                 .negate(new Vector2f)
                                 .scale(ParticleVelocityScale)
                                 .asInstanceOf[Vector2f]
                                 .rotate(angle)
        val size = getRandomNumberInRange(8, 14)
        engine.addHitParticle( projectile.getLocation
                             , velocity
                             , size
                             , 1
                             , ParticleDuration
                             , ParticleColor )
      }

}
object PositronBlasterParticleFX {

  private val AngleMax = 30f
  private val ParticleDuration = 0.275f
  private val ParticleColor
    = new Color(255,100,235,120) // pinkish "electron" style colors
  private val ParticleColor2
    = new Color(155,100,255,120) // purplier AM Blaster stylecolor
  private val ParticleVelocityScale = 0.25f

}