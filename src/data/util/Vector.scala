package data.util

import org.lwjgl.util.vector.Vector2f

/**
  * Created by hawk on 5/4/16.
  */
object Vector {

  implicit class Vector2fOps(val v: Vector2f) extends AnyVal {

    @inline final def * (u: Vector2f): Float
      = v dot u

    @inline final def dot (u: Vector2f): Float
      = Vector2f dot (v, u)

    @inline final def + (u: Vector2f): Vector2f
      = Vector2f add (v, u, new Vector2f)

    @inline final def  - (u: Vector2f): Vector2f
      = Vector2f sub (v, u, new Vector2f)

    @inline final def ° (u: Vector2f): Float
      = Vector2f angle (v, u)

    @inline final def x: Float = v getX
    @inline final def y: Float = v getY
    @inline final def x_=(x2: Float) = v setX x2
    @inline final def y_=(y2: Float) = v setY y2

  }

}
