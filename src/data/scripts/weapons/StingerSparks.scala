package data.scripts.weapons

import java.awt.Color

/**
  * Created by hawk on 5/4/16.
  */
class StingerSparks
extends SparksOnHitEffect{
  override val sparkChance = 0.25f
  override val sparkColor = new Color(255, 175, 100, 255)

  override val particleCount = 1
  override val particleSize = 5f
  override val particleBrightness = 255f
  override val particleDuration = 0.5f

  override val coneAngle = 100f
  override val minVelocity = 0.07f
  override val maxVelocity = 0.175f
}
